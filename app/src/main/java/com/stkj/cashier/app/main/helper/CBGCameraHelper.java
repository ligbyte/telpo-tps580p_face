package com.stkj.cashier.app.main.helper;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;


import com.stkj.cashier.app.main.model.PauseFacePassDetect;
import com.stkj.cashier.app.main.model.ResumeFacePassDetect;
import com.stkj.cashier.cbgfacepass.CBGFacePassHandlerHelper;
import com.stkj.cashier.common.camera.CameraHelper;
import com.stkj.cashier.common.core.ActivityHolderFactory;
import com.stkj.cashier.common.core.ActivityWeakRefHolder;
import com.stkj.cashier.common.permissions.callback.PermissionCallback;
import com.stkj.cashier.common.permissions.request.CameraPermissionRequest;
import com.stkj.cashier.common.utils.EventBusUtils;
import com.stkj.cashier.deviceinterface.DeviceManager;
import com.stkj.cashier.permission.AppPermissionHelper;
import com.stkj.cashier.util.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import mcv.facepass.types.FacePassImage;
import mcv.facepass.types.FacePassImageType;

/**
 * 旷视人脸检测帮助类
 */
public class CBGCameraHelper extends ActivityWeakRefHolder {

    private SurfaceView irPreview;
    private SurfaceView preview;
    private CameraHelper cameraHelper;
    private CameraHelper irCameraHelper;
    private CBGFacePassHandlerHelper facePassHandlerHelper;
    private CBGFacePassHandlerHelper.OnDetectFaceListener onDetectFaceListener;
    private boolean isFaceDualCamera;

    public CBGCameraHelper(@NonNull Activity activity) {
        super(activity);
        EventBusUtils.registerEventBus(this);
    }

    public void setOnDetectFaceListener(CBGFacePassHandlerHelper.OnDetectFaceListener onDetectFaceListener) {
        this.onDetectFaceListener = onDetectFaceListener;
    }

    public void setPreviewView(SurfaceView surfaceView, SurfaceView irPreview, boolean isFaceDualCamera) {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        this.preview = surfaceView;
        this.irPreview = irPreview;
        this.isFaceDualCamera = isFaceDualCamera;
        AppPermissionHelper.with((FragmentActivity) activityWithCheck)
                .requestPermission(new CameraPermissionRequest(), new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        facePassHandlerHelper = (CBGFacePassHandlerHelper) ActivityHolderFactory.get(CBGFacePassHandlerHelper.class, activityWithCheck);
                    }
                });
    }

    /**
     * 开始人脸检测
     */
    public void prepareFacePassDetect() {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        if (this.preview == null) {
            return;
        }
        int cameraDisplayOrientation = DeviceManager.getInstance().getCameraDisplayOrientation();
        if (cameraHelper == null) {
            cameraHelper = new CameraHelper(activityWithCheck);
            int cameraId = DeviceManager.getInstance().getBackCameraId();
            cameraHelper.setCameraId(cameraId);
            cameraHelper.setDisplayOrientation(cameraDisplayOrientation);
        }
        int irCameraDisplayOrientation = DeviceManager.getInstance().getIRCameraDisplayOrientation();
        if (irCameraHelper == null && isFaceDualCamera) {
            irCameraHelper = new CameraHelper(activityWithCheck);
            int irCameraId = DeviceManager.getInstance().getIRCameraId();
            cameraHelper.setCameraId(irCameraId);
            irCameraHelper.setDisplayOrientation(irCameraDisplayOrientation);
        }
        if (cameraHelper != null) {
            if (cameraHelper.hasPreviewView()) {
//            cameraHelper.startPreview();
            } else {
                cameraHelper.setNeedPreviewCallBack(true);
                cameraHelper.setCameraHelperCallback(new CameraHelper.OnCameraHelperCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera, int displayOrientation, int previewOrientation) {
                        try {
                            if (!facePassHandlerHelper.isStartFrameDetectTask()) {
                                return;
                            }
                            Camera.Parameters parameters = camera.getParameters();
                            int width = parameters.getPreviewSize().width;
                            int height = parameters.getPreviewSize().height;
                            int orientation = DeviceManager.getInstance().needUseCameraPreviewOrientation() ? previewOrientation : displayOrientation;
                            FacePassImage facePassImage = new FacePassImage(data, width, height, orientation, FacePassImageType.NV21);
                            runUIThreadWithCheck(new Runnable() {
                                @Override
                                public void run() {
                                    if (isFaceDualCamera) {
                                        facePassHandlerHelper.addRgbFrame(facePassImage);
                                    } else {
                                        facePassHandlerHelper.addFeedFrame(facePassImage);
                                    }
                                }
                            });
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                cameraHelper.prepare(preview);
            }
        }
        //双目识别
        if (irPreview != null && irCameraHelper != null) {
            if (irCameraHelper.hasPreviewView()) {
//                irCameraHelper.startPreview();
            } else {
                irCameraHelper.setNeedPreviewCallBack(true);
                irCameraHelper.setCameraHelperCallback(new CameraHelper.OnCameraHelperCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera, int displayOrientation, int previewOrientation) {
                        try {
                            if (!facePassHandlerHelper.isStartFrameDetectTask()) {
                                return;
                            }
                            Camera.Parameters parameters = camera.getParameters();
                            int width = parameters.getPreviewSize().width;
                            int height = parameters.getPreviewSize().height;
                            int orientation = DeviceManager.getInstance().needUseIRCameraPreviewOrientation() ? previewOrientation : displayOrientation;
                            FacePassImage facePassImage = new FacePassImage(data, width, height, orientation, FacePassImageType.NV21);
                            runUIThreadWithCheck(new Runnable() {
                                @Override
                                public void run() {
                                    facePassHandlerHelper.addIRFrame(facePassImage);
                                }
                            });
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                irCameraHelper.prepare(irPreview, true);
            }
        }
        //人脸识别回调
        if (facePassHandlerHelper != null) {
            facePassHandlerHelper.setOnDetectFaceListener(onDetectFaceListener);
        }
    }

    private boolean needResumeFacePassDetect;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPauseFacePassDetect(PauseFacePassDetect eventBus) {
        if (facePassHandlerHelper != null && facePassHandlerHelper.isStartFrameDetectTask()) {
            needResumeFacePassDetect = true;
            stopFacePassDetect();
            ToastUtils.showLong("人脸检测功能已停止");
        } else {
            needResumeFacePassDetect = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResumeFacePassDetect(ResumeFacePassDetect eventBus) {
        if (needResumeFacePassDetect) {
            startFacePassDetect();
            ToastUtils.showLong("人脸检测功能已恢复");
        }
        needResumeFacePassDetect = false;
    }

    /**
     * 开启人脸检测
     */
    public void startFacePassDetect() {
        if (facePassHandlerHelper != null) {
            facePassHandlerHelper.startFeedFrameDetectTask();
            facePassHandlerHelper.startRecognizeFrameTask();
        }
    }

    /**
     * 停止人脸检测
     */
    public void stopFacePassDetect() {
        if (facePassHandlerHelper != null) {
            facePassHandlerHelper.stopFeedFrameDetectTask();
            facePassHandlerHelper.stopRecognizeFrameTask();
            facePassHandlerHelper.resetHandler();
        }
    }

    @Override
    public void onClear() {
        EventBusUtils.unRegisterEventBus(this);
        stopFacePassDetect();
    }

    /**
     * 释放相机
     */
    public void releaseCameraHelper() {
        stopFacePassDetect();
        //人脸识别回调
        if (facePassHandlerHelper != null) {
            facePassHandlerHelper.setOnDetectFaceListener(null);
        }
        if (cameraHelper != null) {
            cameraHelper.onClear();
            cameraHelper = null;
        }
        if (irCameraHelper != null) {
            irCameraHelper.onClear();
            irCameraHelper = null;
        }
    }
}
