package com.stkj.cashier.common.camera;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.exifinterface.media.ExifInterface;


import com.stkj.cashier.common.core.ActivityWeakRefHolder;
import com.stkj.cashier.common.storage.StorageHelper;
import com.stkj.cashier.common.ui.widget.surfaceview.AutoFitSurfaceView;
import com.stkj.cashier.util.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * camera1帮助类
 */
public class CameraHelper extends ActivityWeakRefHolder implements SurfaceHolder.Callback {

    private static final String TAG = "CameraHelper";

    private boolean isPrepared;
    private boolean isCaptureStarted;
    private boolean isFrontCamera;
    private SurfaceView mSurfaceView;

    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private OnCameraHelperCallback onCameraHelperCallback;

    private String mCameraOutputPath;
    private boolean isFirstInit = true;
    private int displayOrientation = -1;
    private int previewOrientation = 0;
    private boolean needPreviewCallBack;
    private int mCameraId = -1;
    private boolean flipMirrorH;
    private boolean flipMirrorV;

    public CameraHelper(@NonNull Activity activity) {
        super(activity);
    }

    public void setNeedPreviewCallBack(boolean needPreviewCallBack) {
        this.needPreviewCallBack = needPreviewCallBack;
    }

    public void setFlipMirrorH(boolean flipMirrorH) {
        this.flipMirrorH = flipMirrorH;
    }

    public void setFlipMirrorV(boolean flipMirrorV) {
        this.flipMirrorV = flipMirrorV;
    }

    public void setDisplayOrientation(int displayOrientation) {
        this.displayOrientation = displayOrientation;
    }

    public void setCameraId(int cameraId) {
        this.mCameraId = cameraId;
    }

    @Override
    protected void onActivityPause() {
        isPrepared = false;
        releaseMediaRecorder();
        releaseCamera();
        Log.i(TAG, "onActivityPause");
    }

    @Override
    protected void onActivityResume() {
        if (!isFirstInit && !isPrepared && hasPreviewView()) {
            Log.i(TAG, "onActivityResume");
            prepare(mSurfaceView, isFrontCamera);
        }
    }

    public boolean hasPreviewView() {
        return mSurfaceView != null;
    }

    public void prepare(SurfaceView surfaceView) {
        prepare(surfaceView, false);
    }

    public void prepare(SurfaceView surfaceView, boolean isFrontCamera) {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        if (mMediaRecorder != null) {
            releaseMediaRecorder();
        }
        int cameraId = mCameraId;
        if (mCameraId != -1) {
            mCamera = openCameraById(mCameraId);
        } else {
            this.isFrontCamera = isFrontCamera;
            if (isFrontCamera) {
                mCamera = openFrontCamera();
                cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            } else {
                cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                mCamera = openBackCamera();
            }
        }
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activityWithCheck.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Log.i(TAG, "prepare degrees: " + degrees);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        //预览相机原始方向
        previewOrientation = result;
        //设置相机显示的方向
        if (displayOrientation == -1) {
            displayOrientation = result;
        } else {
            result = displayOrientation;
        }
        Log.i(TAG, "prepare displayOrientation: " + displayOrientation);
        mCamera.setDisplayOrientation(result);
        mSurfaceView = surfaceView;
        this.isFrontCamera = isFrontCamera;
        SurfaceHolder viewHolder = surfaceView.getHolder();
        viewHolder.addCallback(this);
        try {
            if (!viewHolder.isCreating()) {
                mCamera.setPreviewDisplay(viewHolder);
                setCameraInitParams(surfaceView.getWidth(), surfaceView.getHeight());
                mCamera.startPreview();
                handlePreviewCallback();
                isPrepared = true;
                isFirstInit = false;
                Log.i(TAG, "prepare viewHolder created");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            ToastUtils.showLong("相机预览失败!");
        }
    }

    private void setCameraInitParams(int width, int height) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            int[] bestPreviewSize = getBestPreviewSize(width, height);
            parameters.setPreviewSize(bestPreviewSize[0], bestPreviewSize[1]);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            //  设置预览照片的大小
            List<Camera.Size> supportedPictureSizes =
                    parameters.getSupportedPictureSizes();
            if (!supportedPictureSizes.isEmpty()) {
                // 获取支持保存图片的尺寸
                Camera.Size pictureSize = supportedPictureSizes.get(0);
                // 从List取出Size
                parameters.setPictureSize(pictureSize.width, pictureSize.height);//
            }
            mCamera.setParameters(parameters);
            if (mSurfaceView instanceof AutoFitSurfaceView) {
                ((AutoFitSurfaceView) mSurfaceView).setAspectRatio(bestPreviewSize[1], bestPreviewSize[0]);
            }
        }
    }

    public int getPreviewOrientation() {
        return previewOrientation;
    }

    public void setCameraHelperCallback(OnCameraHelperCallback onCameraHelperCallback) {
        this.onCameraHelperCallback = onCameraHelperCallback;
    }

    public String getCameraOutputPath() {
        return mCameraOutputPath;
    }

    public void takePicture() {
        if (!isPrepared) {
            ToastUtils.showLong("未准备就绪");
            return;
        }
        try {
            if (mCamera != null) {
                mCameraOutputPath = null;
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            File cacheFile = StorageHelper.createCacheFile("takePicture_cache_" + System.currentTimeMillis() + ".jpg");
                            mCameraOutputPath = cacheFile.getAbsolutePath();
                            FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
                            fileOutputStream.write(data);
                            fileOutputStream.close();
                            //fix pic orientation
                            ExifInterface exifInterface = new ExifInterface(mCameraOutputPath);
                            if (flipMirrorH) {
                                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_HORIZONTAL));
                            } else if (flipMirrorV) {
                                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_FLIP_VERTICAL));
                            } else {
                                String picOrientation;
                                if (displayOrientation != -1) {
                                    picOrientation = String.valueOf(displayOrientation);
                                } else {
                                    // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
                                    // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
                                    if (isFrontCamera) {
                                        picOrientation = String.valueOf(ExifInterface.ORIENTATION_ROTATE_270);
                                    } else {
                                        picOrientation = String.valueOf(ExifInterface.ORIENTATION_ROTATE_90);
                                    }
                                }
                                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, picOrientation);
                            }
                            exifInterface.saveAttributes();
                            if (onCameraHelperCallback != null) {
                                onCameraHelperCallback.onTakePictureSuccess(mCameraOutputPath);
                            }
                            Log.i(TAG, "takePicture cache file: " + mCameraOutputPath);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            if (onCameraHelperCallback != null) {
                                onCameraHelperCallback.onTakePictureError(e.getMessage());
                            }
                            Log.i(TAG, "takePicture write output error: " + e.getMessage());
                        }
                    }
                });
            }
        } catch (Throwable e) {
            e.printStackTrace();
            if (onCameraHelperCallback != null) {
                onCameraHelperCallback.onTakePictureError(e.getMessage());
            }
            Log.i(TAG, "takePicture error: " + e.getMessage());
        }
    }

    public void startCaptureVideo() {
        if (!isPrepared) {
            ToastUtils.showLong("未准备就绪");
            return;
        }
        if (isCaptureStarted) {
            ToastUtils.showLong("正在录制中");
            return;
        }
        try {
            if (mCamera != null) {
                mCameraOutputPath = null;
                //output cache file
                File captureVideoFile = StorageHelper.createCacheFile("captureVideo_cache_" + System.currentTimeMillis() + ".mp4");
                mCameraOutputPath = captureVideoFile.getAbsolutePath();
                if (mMediaRecorder == null) {
                    mMediaRecorder = new MediaRecorder();
                }

                // Step 1: Unlock and set camera to MediaRecorder
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);

                if (isFrontCamera) {
                    mMediaRecorder.setOrientationHint(270);
                } else {
                    mMediaRecorder.setOrientationHint(90);
                }

                // Step 2: Set sources
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
                mMediaRecorder.setProfile(getBestCamcorderProfile());

                // Step 4: Set output file
                mMediaRecorder.setOutputFile(captureVideoFile.getAbsolutePath());

                // Step 5: Set the preview output
                mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());

                // Step 6: Prepare configured MediaRecorder
                mMediaRecorder.prepare();

                mMediaRecorder.start();

                isCaptureStarted = true;
                Log.i(TAG, "startCaptureVideo MediaRecorder.start");
                if (onCameraHelperCallback != null) {
                    onCameraHelperCallback.onCaptureVideoStart();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            releaseMediaRecorder();
            try {
                if (mCamera != null) {
                    mCamera.lock();
                    Log.i(TAG, "startCaptureVideo error lock camera");
                }
            } catch (Throwable error) {
                error.printStackTrace();
            }
            isCaptureStarted = false;
            Log.i(TAG, "startCaptureVideo error: " + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void pauseCaptureVideo() {
        if (!isPrepared) {
            ToastUtils.showLong("未准备就绪");
            return;
        }
        if (!isCaptureStarted) {
            ToastUtils.showLong("拍摄未开始");
            return;
        }
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.pause();
                Log.i(TAG, "pauseCaptureVideo MediaRecorder pause");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "pauseCaptureVideo error: " + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void resumeCaptureVideo() {
        if (!isPrepared) {
            ToastUtils.showLong("未准备就绪");
            return;
        }
        if (!isCaptureStarted) {
            ToastUtils.showLong("拍摄未开始");
            return;
        }
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.resume();
                Log.i(TAG, "resumeCaptureVideo MediaRecorder resume");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "resumeCaptureVideo error: " + e.getMessage());
        }
    }

    public void stopCaptureVideo() {
        if (!isPrepared) {
            ToastUtils.showLong("未准备就绪");
            return;
        }
        if (!isCaptureStarted) {
            ToastUtils.showLong("拍摄未开始");
            return;
        }
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                if (onCameraHelperCallback != null) {
                    onCameraHelperCallback.onCaptureVideoSuccess(mCameraOutputPath);
                }
                Log.i(TAG, "stopCaptureVideo MediaRecorder stop and reset");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            if (onCameraHelperCallback != null) {
                onCameraHelperCallback.onCaptureVideoError(e.getMessage());
            }
            Log.i(TAG, "stopCaptureVideo error: " + e.getMessage());
        }
        isCaptureStarted = false;
    }

    public void switchCamera() {
        if (!isPrepared) {
            ToastUtils.showLong("未准备就绪");
            return;
        }
        if (isCaptureStarted) {
            ToastUtils.showLong("正在录制中,请先停止拍摄!");
            return;
        }
        prepare(mSurfaceView, !isFrontCamera);
    }

    public boolean isCaptureStarted() {
        return isCaptureStarted;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void stopPreview() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                Log.i(TAG, "stopPreview");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "stopPreview error: " + e.getMessage());
        }
        isPrepared = false;
    }

    public void startPreview() {
        if (!isPrepared && !hasPreviewView()) {
            ToastUtils.showLong("未准备就绪");
            return;
        }
        try {
            if (mCamera != null) {
                mCamera.startPreview();
                handlePreviewCallback();
                Log.i(TAG, "startPreview");
                isPrepared = true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "startPreview error: " + e.getMessage());
        }
    }

    private void handlePreviewCallback() {
        if (needPreviewCallBack) {
            Log.i(TAG, "startPreview handlePreviewCallback" + this);
            Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
            mCamera.addCallbackBuffer(new byte[((previewSize.width * previewSize.height) *
                    ImageFormat.getBitsPerPixel(ImageFormat.NV21)) / 8]);
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    camera.addCallbackBuffer(data);
                    if (onCameraHelperCallback != null) {
                        onCameraHelperCallback.onPreviewFrame(data, camera, displayOrientation, previewOrientation);
                    }
                }
            });
        }
    }

    private void releaseMediaRecorder() {
        try {
            if (isCaptureStarted) {
                if (onCameraHelperCallback != null) {
                    onCameraHelperCallback.onCaptureVideoError("releaseMediaRecorder");
                }
            }
            if (mMediaRecorder != null) {
                mMediaRecorder.reset();   // clear recorder configuration
                mMediaRecorder.release(); // release the recorder object
                mMediaRecorder = null;
                Log.i(TAG, "releaseMediaRecorder");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "releaseMediaRecorder error: " + e.getMessage());
        }
        mMediaRecorder = null;
        isCaptureStarted = false;
    }

    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
                Log.i(TAG, "releaseCamera");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "releaseCamera error: " + e.getMessage());
        }
        mCamera = null;
    }

    @Override
    public void onClear() {
        onCameraHelperCallback = null;
        releaseCamera();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            handlePreviewCallback();
            isPrepared = true;
            Log.i(TAG, "surfaceCreated prepared");
        } catch (Throwable e) {
            e.printStackTrace();
            ToastUtils.showLong("相机预览失败!");
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            // preview surface does not exist
            Log.i(TAG, "surfaceChanged surface does not exist");
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "surfaceChanged mCamera.stopPreview error: " + e.getMessage());
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(holder);
            setCameraInitParams(width, height);
            startPreview();
            isPrepared = true;
            isFirstInit = false;
            Log.i(TAG, "surfaceChanged prepared");
        } catch (Throwable e) {
            e.printStackTrace();
            Log.i(TAG, "surfaceChanged mCamera.setPreviewDisplay error:" + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
    }

    private CamcorderProfile getBestCamcorderProfile() {
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF);
        if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        }
        return profile;
    }

    private int[] getBestPreviewSize(int width, int height) {
        Log.i(TAG, "getBestPreviewSize  init width: " + width + " height: " + height);
        int[] result = new int[]{width, height};
        if (mCamera != null) {
            final Camera.Parameters p = mCamera.getParameters();
            //特别注意此处需要规定rate的比是大的比小的，不然有可能出现rate = height/width，但是后面遍历的时候，current_rate = width/height,所以我们限定都为大的比小的。
            float rate = (float) Math.max(width, height) / (float) Math.min(width, height);
            float tmp_diff;
            float min_diff = -1f;
            for (Camera.Size size : p.getSupportedPreviewSizes()) {
                float current_rate = (float) Math.max(size.width, size.height) / (float) Math.min(size.width, size.height);
                tmp_diff = Math.abs(current_rate - rate);
                if (min_diff < 0) {
                    min_diff = tmp_diff;
                    result[0] = size.width;
                    result[1] = size.height;
                }
                if (tmp_diff < min_diff) {
                    min_diff = tmp_diff;
                    result[0] = size.width;
                    result[1] = size.height;
                }
            }
        }
        Log.i(TAG, "getBestPreviewSize width: " + width + " height: " + height + " result: " + Arrays.toString(result));
        return result;
    }

    public static Camera openCameraById(int cameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(cameraId); // attempt to get a Camera instance
        } catch (Throwable e) {
            e.printStackTrace();
            ToastUtils.showLong("打开相机失败");
        }
        return camera;
    }

    public static Camera openBackCamera() {
        Camera camera = null;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance
        } catch (Throwable e) {
            e.printStackTrace();
           ToastUtils.showLong("打开相机失败");
        }
        return camera;
    }

    public static Camera openFrontCamera() {
        Camera camera = null;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // attempt to get a Camera instance
        } catch (Throwable e) {
            e.printStackTrace();
             ToastUtils.showLong("打开相机失败");
        }
        return camera;
    }

    public interface OnCameraHelperCallback {
        default void onTakePictureSuccess(String picPath) {
        }

        default void onTakePictureError(String message) {
        }

        default void onCaptureVideoSuccess(String videoPath) {
        }

        default void onCaptureVideoError(String message) {
        }

        default void onCaptureVideoStart() {
        }

        default void onPreviewFrame(byte[] data, Camera camera, int displayRotation, int previewRotation) {

        }
    }
}
