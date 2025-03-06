package com.stkj.cashier.cbgfacepass;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.stkj.cashier.App;
import com.stkj.cashier.cbgfacepass.model.CBGFacePassConfig;
import com.stkj.cashier.cbgfacepass.model.CBGFacePassRecognizeResult;
import com.stkj.cashier.cbgfacepass.model.RecognizeData;
import com.stkj.cashier.common.core.ActivityWeakRefHolder;
import com.stkj.cashier.common.storage.StorageHelper;
import com.stkj.cashier.util.util.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.schedulers.Schedulers;
import mcv.facepass.FacePassException;
import mcv.facepass.FacePassHandler;
import mcv.facepass.auth.AuthApi.AuthApi;
import mcv.facepass.auth.AuthApi.AuthApplyResponse;
import mcv.facepass.auth.AuthApi.ErrorCodeConfig;
import mcv.facepass.types.FacePassAddFaceDetectionResult;
import mcv.facepass.types.FacePassAddFaceResult;
import mcv.facepass.types.FacePassConfig;
import mcv.facepass.types.FacePassDetectFacesResult;
import mcv.facepass.types.FacePassDetectionResult;
import mcv.facepass.types.FacePassImage;
import mcv.facepass.types.FacePassModel;
import mcv.facepass.types.FacePassPose;
import mcv.facepass.types.FacePassRCAttribute;
import mcv.facepass.types.FacePassRecognitionResult;
import mcv.facepass.types.FacePassRecognitionState;
import mcv.facepass.types.FacePassTrackOptions;

/**
 * 旷视人脸识别帮助类
 */
public class CBGFacePassHandlerHelper extends ActivityWeakRefHolder {

    public final static String TAG = "CBGFacePassHandlerHelper";
    private static final String CERT_FILE_NAME = "CBG_Android_Face_Reco---36500-Formal-two-stage.cert";
    private static final String ACTIVE_FILE_NAME = "CBG_Android_Face_Reco---36500-Formal--1-active.txt";
    private static final String DEFAULT_FACE_PASS_GROUP = "facepass";

    private CBGFacePassConfig mCbgFacePassConfig;
    private FacePassHandler mFacePassHandler;
    private OnInitFacePassListener onInitFacePassListener;
    private OnDetectFaceListener onDetectFaceListener;
    private static boolean hasFacePassSDKAuth;

    public CBGFacePassHandlerHelper(@androidx.annotation.NonNull Activity activity) {
        super(activity);
    }

    /**
     * sdk是否授权成功
     */
    public static boolean hasFacePassSDKAuth() {
        return hasFacePassSDKAuth;
    }

    public void setOnInitFacePassListener(OnInitFacePassListener onInitFacePassListener) {
        this.onInitFacePassListener = onInitFacePassListener;
    }

    public void setOnDetectFaceListener(OnDetectFaceListener onDetectFaceListener) {
        this.onDetectFaceListener = onDetectFaceListener;
    }

    public int getAllFaceCount() {
        if (mFacePassHandler != null) {
            try {
                return mFacePassHandler.getLocalGroupFaceNum(DEFAULT_FACE_PASS_GROUP);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public FacePassAddFaceResult addFace(Bitmap bitmap) throws FacePassException {
        if (mFacePassHandler != null && bitmap != null) {
            return mFacePassHandler.addFace(bitmap);
        }
        return null;
    }

    public FacePassAddFaceDetectionResult addFaceDetect(Bitmap bitmap) throws FacePassException {
        return addFaceDetect(bitmap, 0);
    }

    /**
     * 0："FEEDFRAME_OK_CODE"，feedFrame过程中没有出现错误；
     * 1："FEEDFRAME_ROLL_ERROR_CODE"，feedFrame过程中人脸质量检测的roll值大于阈值；
     * 2："FEEDFRAME_PITCH_ERROR_CODE"，feedFrame过程中人脸质量检测的pitch值大于阈值；
     * 3："FEEDFRAME_YAW_ERROR_CODE"，feedFrame过程中人脸质量检测的yaw值大于阈值；
     * 4："FEEDFRAME_BLUR_ERROR_CODE"，feedFrame过程中人脸质量检测的blur值大于阈值；
     * 5："FEEDFRAME_MINFACE_ERROR_CODE"，feedFrame过程中人脸质量检测的minface值小于阈值；
     * 6："FEEDFRAME_HALFFACE_ERROR_CODE"，feedFrame过程中人脸质量检测的halfface值小于阈值；
     * 7："FEEDFRAME_OCCLUSION_ERROR_CODE"，feedFrame过程中人脸质量检测被遮挡的部分大于阈值；
     * 8："FEEDFRAME_BRIGHTNESS_ERROR_CODE"，feedFrame过程中人脸质量检测的brightness值没有处于阈值范围；
     * 9："FEEDFRAME_UNKNOWN_ERROR_CODE"，feedFrame过程中出现未知错误。
     */
    public FacePassAddFaceDetectionResult addFaceDetect(Bitmap bitmap, int rotation) throws FacePassException {
        if (mFacePassHandler != null && bitmap != null) {
            return mFacePassHandler.addFaceDetect(bitmap, rotation);
        }
        return null;
    }

    public FacePassDetectFacesResult detectFace(Bitmap bitmap) throws FacePassException {
        return detectFace(bitmap, 0);
    }

    public FacePassDetectFacesResult detectFace(Bitmap bitmap, int rotation) throws FacePassException {
        if (mFacePassHandler != null && bitmap != null) {
            return mFacePassHandler.detectFaces(bitmap, rotation);
        }
        return null;
    }

    private int mFaceCameraType = CBGFacePassConfig.FACE_SINGLE_CAMERA;

    /**
     * 是否是双目识别
     */
    public boolean isFaceDualCamera() {
        return mFaceCameraType == CBGFacePassConfig.FACE_DUAL_CAMERA;
    }

    //检测人脸帧 start
    //单目人脸帧
    private final ArrayBlockingQueue<FacePassImage> mFeedFrameQueue = new ArrayBlockingQueue<FacePassImage>(1);

    public void addFeedFrame(FacePassImage facePassImage) {
        mFeedFrameQueue.offer(facePassImage);
    }

    public FacePassDetectionResult feedFrame() throws Throwable {
        if (mFacePassHandler != null) {
            FacePassImage passImage = mFeedFrameQueue.take();
            return mFacePassHandler.feedFrame(passImage);
        }
        return null;
    }
    //单目人脸帧

    //双目人脸帧
    private final ArrayBlockingQueue<Pair<FacePassImage, FacePassImage>> mComplexFrameQueue
            = new ArrayBlockingQueue<>(2);

    private FacePassImage rgbFrameBuffer = null;
    private FacePassImage irFrameBuffer = null;

    public void addRgbFrame(FacePassImage rgbFrame) {
        synchronized (CBGFacePassHandlerHelper.class) {
            if (rgbFrameBuffer == null) {
                rgbFrameBuffer = rgbFrame;
            }
            makeComplexFrame();
        }
    }

    public void addIRFrame(FacePassImage infraFrame) {
        synchronized (CBGFacePassHandlerHelper.class) {
            if (irFrameBuffer == null) {
                irFrameBuffer = infraFrame;
            }
            makeComplexFrame();
        }
    }

    private void makeComplexFrame() {
        if ((rgbFrameBuffer != null) && (irFrameBuffer != null)) {
            if (mComplexFrameQueue.remainingCapacity() > 0) {
                mComplexFrameQueue.offer(new Pair<>(rgbFrameBuffer, irFrameBuffer));
            }
            rgbFrameBuffer = null;
            irFrameBuffer = null;
        }
    }

    /**
     * 获取rbg ir帧
     */
    public Pair<FacePassImage, FacePassImage> takeComplexFrame() throws InterruptedException {
        return mComplexFrameQueue.take();
    }
    //双目人脸帧

    private final AtomicBoolean frameDetectTask = new AtomicBoolean(false);

    public boolean isStartFrameDetectTask() {
        return frameDetectTask.get();
    }

    public void startFeedFrameDetectTask() {
        if (mFacePassHandler != null) {
            if (frameDetectTask.get()) {
                return;
            }
            frameDetectTask.set(true);
            Schedulers.io().scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"-CBGFacePassHandlerHelper--startFeedFrameDetectTask--start-");
                    while (frameDetectTask.get()) {
                        try {
                            /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
                            FacePassDetectionResult detectionResult = null;
                            if (isFaceDualCamera()) {
                                //双目人脸检测
                                Pair<FacePassImage, FacePassImage> framePair;
                                try {
                                    framePair = takeComplexFrame();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    continue;
                                }
                                if (frameDetectTask.get()) {
                                    detectionResult = mFacePassHandler.feedFrameRGBIR(framePair.first, framePair.second);
                                }
                            } else {
                                //单目人脸检测
                                detectionResult = feedFrame();
                            }
                            if (detectionResult != null && detectionResult.faceList != null && detectionResult.faceList.length > 0) {
                                Log.d(TAG,"--CBGFacePassHandlerHelper--detectionResult--faceList >  " + detectionResult.faceList.length);
                                /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
                                if (detectionResult.message.length != 0) {
                                    Log.d(TAG,"-CBGFacePassHandlerHelper--startFeedFrameDetectTask--detectionResult-RecognizeDataQueue.offer");
                                    /*送识别的人脸框的属性信息*/
                                    FacePassTrackOptions[] trackOpts = new FacePassTrackOptions[detectionResult.images.length];
                                    //获取默认人脸识别参数
                                    float searchThreshold = 75f;
                                    float livenessThreshold = 80f; // -1.0f will not change the liveness threshold
                                    float livenessGaThreshold = 85f;
                                    if (mCbgFacePassConfig != null) {
                                        searchThreshold = mCbgFacePassConfig.getSearchThreshold();
                                        livenessThreshold = mCbgFacePassConfig.getLivenessThreshold();
                                        livenessGaThreshold = mCbgFacePassConfig.getLivenessGaThreshold();
                                    }
                                    for (int i = 0; i < detectionResult.images.length; ++i) {
                                        if (detectionResult.images[i].rcAttr.respiratorType != FacePassRCAttribute.FacePassRespiratorType.INVALID
                                                && detectionResult.images[i].rcAttr.respiratorType != FacePassRCAttribute.FacePassRespiratorType.NO_RESPIRATOR) {
                                            trackOpts[i] = new FacePassTrackOptions(detectionResult.images[i].trackId, searchThreshold, livenessThreshold, livenessGaThreshold, -1.0f);
                                        }
                                    }
                                    RecognizeData mRecData = new RecognizeData(detectionResult.message, trackOpts);
                                    mRecognizeDataQueue.offer(mRecData);
                                    if (onDetectFaceListener != null) {
                                        int faceCount = detectionResult.faceList.length;
                                        runUIThreadWithCheck(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (onDetectFaceListener != null) {
                                                    Log.d(TAG,"--CBGFacePassHandlerHelper-onDetectFace--count: " + faceCount);
                                                    onDetectFaceListener.onDetectFace(faceCount);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                            Log.d(TAG,"-CBGFacePassHandlerHelper--startFeedFrameDetectTask--error-" + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    public void stopFeedFrameDetectTask() {
        rgbFrameBuffer = null;
        irFrameBuffer = null;
        mComplexFrameQueue.clear();
        mFeedFrameQueue.clear();
        frameDetectTask.set(false);
    }
    //检测人脸帧 end

    //识别人脸帧 start
    private final ArrayBlockingQueue<RecognizeData> mRecognizeDataQueue = new ArrayBlockingQueue<>(5);

    private final AtomicBoolean recognizeFrameTask = new AtomicBoolean(false);

    public void startRecognizeFrameTask() {
        if (mFacePassHandler != null) {
            if (recognizeFrameTask.get()) {
                return;
            }
            recognizeFrameTask.set(true);
            Schedulers.io().scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"-CBGFacePassHandlerHelper--startRecognizeFrameTask--start-");
                    while (recognizeFrameTask.get()) {
                        try {
                            RecognizeData recognizeData = mRecognizeDataQueue.take();
                            if (recognizeData != null) {
                                FacePassRecognitionResult[] recognizeResultArray = null;
                                if (recognizeFrameTask.get()) {
                                    recognizeResultArray = mFacePassHandler.recognize(DEFAULT_FACE_PASS_GROUP, recognizeData.message, recognizeData.trackOpt);
                                }
                                Log.d(TAG,"--CBGFacePassHandlerHelper-startRecognizeFrameTask--recognizeResultArray = " + (recognizeResultArray == null ? "null" : recognizeResultArray.length));
                                if (recognizeResultArray != null && recognizeResultArray.length > 0) {
                                    List<CBGFacePassRecognizeResult> faceTokenList = new ArrayList<>();
                                    for (FacePassRecognitionResult recognizeResult : recognizeResultArray) {
                                        if (recognizeResult != null) {
                                            int resultSearchScoreThreshold = 75;
                                            if (mCbgFacePassConfig != null) {
                                                resultSearchScoreThreshold = mCbgFacePassConfig.getResultSearchScoreThreshold();
                                            }
                                            //判断人脸相似程度 >=75表示成功 否侧失败处理
                                            if (recognizeResult.detail != null && recognizeResult.detail.searchScore < resultSearchScoreThreshold) {
                                                continue;
                                            }
                                            String faceToken = new String(recognizeResult.faceToken, StandardCharsets.ISO_8859_1);
                                            if (!TextUtils.isEmpty(faceToken)) {
                                                boolean passSuccess = FacePassRecognitionState.RECOGNITION_PASS == recognizeResult.recognitionState;
                                                CBGFacePassRecognizeResult facePassRecognizeResult = new CBGFacePassRecognizeResult(faceToken, passSuccess, recognizeResult.recognitionState);
                                                faceTokenList.add(facePassRecognizeResult);
                                            }
                                            Log.d(TAG,"--CBGFacePassHandlerHelper-startRecognizeFrameTask--recognizeResult faceToken = " + faceToken + " recognitionState=" + recognizeResult.recognitionState);
                                        }
                                    }
                                    if (!faceTokenList.isEmpty()) {
                                        Log.d(TAG,"--CBGFacePassHandlerHelper-stopFacePass-faceTokenList:" + faceTokenList.size());
                                        runUIThreadWithCheck(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (onDetectFaceListener != null) {
                                                    onDetectFaceListener.onDetectFaceToken(faceTokenList);
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(TAG,"--CBGFacePassHandlerHelper-startRecognizeFrameTask--recognizeResult onNoDetectFaceToken");
                                    runUIThreadWithCheck(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (onDetectFaceListener != null) {
                                                onDetectFaceListener.onNoDetectFaceToken();
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                            Log.d(TAG,"-CBGFacePassHandlerHelper--startRecognizeFrameTask--error-" + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    public void stopRecognizeFrameTask() {
        mRecognizeDataQueue.clear();
        recognizeFrameTask.set(false);
    }
    //识别人脸帧 end

    /**
     * 重置人脸识别handler
     */
    public void resetHandler() {
        if (mFacePassHandler != null) {
            mFacePassHandler.reset();
        }
    }

    public boolean bindFaceGroup(byte[] faceToken) throws FacePassException {
        if (mFacePassHandler != null) {
            return mFacePassHandler.bindGroup(DEFAULT_FACE_PASS_GROUP, faceToken);
        }
        return false;
    }

    public void deleteFace(byte[] faceToken) throws FacePassException {
        if (mFacePassHandler != null) {
            mFacePassHandler.deleteFace(faceToken);
        }
    }

    public void deleteAllFace() throws FacePassException {
        if (mFacePassHandler != null) {
            mFacePassHandler.clearAllGroupsAndFaces();
            createFacePassGroup(DEFAULT_FACE_PASS_GROUP);
        }
    }

    private boolean readAssetCert;

    public void setReadAssetCert(boolean readAssetCert) {
        this.readAssetCert = readAssetCert;
    }

    /**
     * 初始化sdk
     */
    public void initAndAuthSdk(CBGFacePassConfig cbgFacePassConfig) {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        Log.d(TAG,"CBGFacePassHelper " + "---initSDK---");
//        Log.d(TAG,"CBGFacePassHelper " + "---initSDK-start--");
//        FacePassHandler.initSDK(activityWithCheck);
//        // 金雅拓授权接口
//        boolean auth_status = FacePassHandler.authCheck();
//        if (!auth_status) {
//            Log.d(TAG,"CBGFacePassHelper " + "---initSDK-authDevice--");
//            FacePassHandler.authDevice(activityWithCheck, readCertContent(CERT_FILE_NAME).trim(), "", new AuthApi.AuthDeviceCallBack() {
//                @Override
//                public void GetAuthDeviceResult(AuthApplyResponse authApplyResponse) {
//                    if (authApplyResponse.errorCode == ErrorCodeConfig.AUTH_SUCCESS) {
//                        Log.d(TAG,"CBGFacePassHelper " + "---initSDK-success--");
//                        initFacePassHandler(cbgFacePassConfig);
//                    } else {
//                        runUIThreadWithCheck(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (onInitFacePassListener != null) {
//                                    onInitFacePassListener.onInitError("人脸识别设备授权失败");
//                                }
//                            }
//                        });
//                    }
//                }
//            });
//        } else {
//            Log.d(TAG,"CBGFacePassHelper " + "---initSDK-success--");
//            initFacePassHandler(cbgFacePassConfig);
//        }
        Schedulers.io().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG,"CBGFacePassHelper " + "---initSDK-start--");
                    FacePassHandler.initSDK(activityWithCheck);
                    // 金雅拓授权接口
                    boolean auth_status = FacePassHandler.authCheck();
                    if (!auth_status) {
                        hasFacePassSDKAuth = false;
                        Log.d(TAG,"CBGFacePassHelper " + "---initSDK-authDevice--");
                        String certContent = readCertContent(CERT_FILE_NAME).trim();
                        String codeContent = readCertContent(ACTIVE_FILE_NAME).trim();
                        FacePassHandler.authDevice(activityWithCheck, certContent, codeContent, new AuthApi.AuthDeviceCallBack() {
                            @Override
                            public void GetAuthDeviceResult(AuthApplyResponse authApplyResponse) {
                                if (authApplyResponse.errorCode == ErrorCodeConfig.AUTH_SUCCESS) {
                                    hasFacePassSDKAuth = true;
                                    Log.d(TAG,"CBGFacePassHelper " + "---authDevice--active--success--");
                                    Log.d(TAG,"CBGFacePassHelper " + "---initSDK-success--");
                                    ToastUtils.showLong("人脸识别SDK已激活");
                                    initFacePassHandler(cbgFacePassConfig);
                                } else {
                                    runUIThreadWithCheck(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (onInitFacePassListener != null) {
                                                onInitFacePassListener.onInitError("人脸识别设备授权失败");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        hasFacePassSDKAuth = true;
                        Log.d(TAG,"CBGFacePassHelper " + "---initSDK-success--");
                        initFacePassHandler(cbgFacePassConfig);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    runUIThreadWithCheck(new Runnable() {
                        @Override
                        public void run() {
                            if (onInitFacePassListener != null) {
                                onInitFacePassListener.onInitError("人脸识别功能初始化失败: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    private void initFacePassHandler(CBGFacePassConfig cbgFacePassConfig) {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        Schedulers.io().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                try {
                    AssetManager assets = activityWithCheck.getAssets();
                    Log.d(TAG,"CBGFacePassHelper " + "start to build FacePassHandler");
                    FacePassConfig config = new FacePassConfig();
                    config.poseBlurModel = FacePassModel.initModel(assets, "attr.pose_blur.arm.190630.bin");

                    config.livenessModel = FacePassModel.initModel(assets, "liveness.CPU.rgb.G.bin");
                    mFaceCameraType = cbgFacePassConfig.getCameraType();
                    if (mFaceCameraType == CBGFacePassConfig.FACE_DUAL_CAMERA) {
                        config.rgbIrLivenessModel = FacePassModel.initModel(assets, "liveness.CPU.rgbir.I.bin");
                        // 真假人同屏模型
                        config.rgbIrGaLivenessModel = FacePassModel.initModel(assets, "liveness.CPU.rgbir.ga_case.A.bin");
                        // 若需要使用GPU模型则加载以下模型文件
                        config.livenessGPUCache = FacePassModel.initModel(assets, "liveness.GPU.rgbir.I.cache");
                        config.rgbIrLivenessGpuModel = FacePassModel.initModel(assets, "liveness.GPU.rgbir.I.bin");
                        config.rgbIrGaLivenessGpuModel = FacePassModel.initModel(assets, "liveness.GPU.rgbir.ga_case.A.bin");
                    }

                    config.searchModel = FacePassModel.initModel(assets, "feat2.arm.K.v1.0_1core.bin");

                    config.detectModel = FacePassModel.initModel(assets, "detector.arm.G.bin");
                    config.detectRectModel = FacePassModel.initModel(assets, "detector_rect.arm.G.bin");
                    config.landmarkModel = FacePassModel.initModel(assets, "pf.lmk.arm.E.bin");

                    config.rcAttributeModel = FacePassModel.initModel(assets, "attr.RC.arm.G.bin");
                    config.occlusionFilterModel = FacePassModel.initModel(assets, "attr.occlusion.arm.20201209.bin");
                    //config.smileModel = FacePassModel.initModel(assets, "attr.RC.arm.200815.bin");
                    //config.ageGenderModel = FacePassModel.initModel(assets, "attr.age_gender.arm.190630.bin");

                    /* 识别阈值参数 */
                    config.rcAttributeAndOcclusionMode = cbgFacePassConfig.isOcclusionMode() ? 1 : 0;
                    config.searchThreshold = cbgFacePassConfig.getSearchThreshold();
                    config.livenessThreshold = cbgFacePassConfig.getLivenessThreshold();
                    config.livenessGaThreshold = cbgFacePassConfig.getLivenessGaThreshold();
                    if (mFaceCameraType == CBGFacePassConfig.FACE_DUAL_CAMERA) {
                        config.livenessEnabled = false;
                        config.rgbIrLivenessEnabled = true;      // 启用双目活体功能(默认CPU)
                        config.rgbIrLivenessGpuEnabled = true;   // 启用双目活体GPU功能
                        config.rgbIrGaLivenessEnabled = true;    // 启用真假人同屏功能(默认CPU)
                        config.rgbIrGaLivenessGpuEnabled = true; // 启用真假人同屏GPU功能
                        Log.d(TAG,"CBGFacePassHelper " + "mFaceCameraType: dual camera");
                    } else {
                        config.livenessEnabled = cbgFacePassConfig.isLivenessEnabled();
                        config.rgbIrLivenessEnabled = false;
                        Log.d(TAG,"CBGFacePassHelper " + "mFaceCameraType: single camera");
                    }

                    int poseThreshold = cbgFacePassConfig.getPoseThreshold();
                    config.poseThreshold = new FacePassPose(poseThreshold, poseThreshold, poseThreshold);
                    config.blurThreshold = 0.8f;
                    config.lowBrightnessThreshold = 30f;
                    config.highBrightnessThreshold = 210f;
                    config.brightnessSTDThreshold = 80f;
                    config.faceMinThreshold = cbgFacePassConfig.getDetectFaceMinThreshold();
                    config.retryCount = 10;
                    config.smileEnabled = false;
                    config.maxFaceEnabled = true;
                    config.fileRootPath = StorageHelper.getExternalCustomDirPath("cbgFacePass");

                    /* 创建SDK实例 */
                    mFacePassHandler = new FacePassHandler(config);

                    /* 入库阈值参数 */
                    FacePassConfig addFaceConfig = mFacePassHandler.getAddFaceConfig();
                    addFaceConfig.poseThreshold.pitch = 35f;
                    addFaceConfig.poseThreshold.roll = 35f;
                    addFaceConfig.poseThreshold.yaw = 35f;
                    addFaceConfig.blurThreshold = 0.7f;
                    addFaceConfig.lowBrightnessThreshold = 70f;
                    addFaceConfig.highBrightnessThreshold = 220f;
                    addFaceConfig.brightnessSTDThresholdLow = 14.14f;
                    addFaceConfig.brightnessSTDThreshold = 63.25f;
                    addFaceConfig.faceMinThreshold = cbgFacePassConfig.getAddFaceMinThreshold();
                    addFaceConfig.rcAttributeAndOcclusionMode = 2;
                    mFacePassHandler.setAddFaceConfig(addFaceConfig);
                    Log.d(TAG,"CBGFacePassHelper " + "end to build FacePassHandler");
                    //创建默认人脸底层库
                    createFacePassGroup(DEFAULT_FACE_PASS_GROUP);
                    runUIThreadWithCheck(new Runnable() {
                        @Override
                        public void run() {
                            if (onInitFacePassListener != null) {
                                onInitFacePassListener.onInitSuccess();
                            }
                            Log.d(TAG,"CBGFacePassHelper " + "initFaceHandler success");
                        }
                    });
                    //识别参数模式赋值
                    mCbgFacePassConfig = cbgFacePassConfig;
                } catch (Throwable e) {
                    e.printStackTrace();
                    runUIThreadWithCheck(new Runnable() {
                        @Override
                        public void run() {
                            if (onInitFacePassListener != null) {
                                onInitFacePassListener.onInitError("人脸识别功能初始化失败: " + e.getMessage());
                            }
                        }
                    });
                    Log.d(TAG,"CBGFacePassHelper " + "initFaceHandler error " + e.getMessage());
                }
            }
        });
    }

    /**
     * 设置是人脸入库配置
     */
    public void setCBGAddFacePassConfig(CBGFacePassConfig cbgFacePassConfig) {
        try {
            if (mFacePassHandler != null && cbgFacePassConfig != null) {
                //入库阈值
                FacePassConfig addFaceConfig = mFacePassHandler.getAddFaceConfig();
                addFaceConfig.faceMinThreshold = cbgFacePassConfig.getAddFaceMinThreshold();
                mFacePassHandler.setAddFaceConfig(addFaceConfig);
                //识别参数模式赋值
                mCbgFacePassConfig = cbgFacePassConfig;
                Log.d(TAG,"CBGFacePassHelper " + "setCBGAddFacePassConfig success faceMinThreshold: " + cbgFacePassConfig.getAddFaceMinThreshold());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d(TAG,"CBGFacePassHelper " + "setCBGAddFacePassConfig error " + e.getMessage());
        }
    }

    /**
     * 设置是人脸识别配置
     */
    public void setCBGFacePassConfig(CBGFacePassConfig cbgFacePassConfig) {
        try {
            if (mFacePassHandler != null && cbgFacePassConfig != null) {
                //停止人脸检测
                stopFeedFrameDetectTask();
                stopRecognizeFrameTask();
                resetHandler();
                //识别阈值
                FacePassConfig facePassConfig = mFacePassHandler.getConfig();
                facePassConfig.rcAttributeAndOcclusionMode = cbgFacePassConfig.isOcclusionMode() ? 1 : 0;
                facePassConfig.searchThreshold = cbgFacePassConfig.getSearchThreshold();
                facePassConfig.livenessThreshold = cbgFacePassConfig.getLivenessThreshold();
                facePassConfig.livenessGaThreshold = cbgFacePassConfig.getLivenessGaThreshold();
                if (!isFaceDualCamera()) {
                    facePassConfig.livenessEnabled = cbgFacePassConfig.isLivenessEnabled();
                }
                int poseThreshold = cbgFacePassConfig.getPoseThreshold();
                facePassConfig.poseThreshold = new FacePassPose(poseThreshold, poseThreshold, poseThreshold);
                facePassConfig.faceMinThreshold = cbgFacePassConfig.getDetectFaceMinThreshold();
                mFacePassHandler.setConfig(facePassConfig);
                //识别参数模式赋值
                mCbgFacePassConfig = cbgFacePassConfig;
                Log.d(TAG,"CBGFacePassHelper " + "setCBGFacePassConfig success faceMinThreshold: " + cbgFacePassConfig.getDetectFaceMinThreshold());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d(TAG,"CBGFacePassHelper " + "setCBGFacePassConfig error " + e.getMessage());
        }
    }

    /**
     * 创建人脸底库
     */
    public void createFacePassGroup(String groupName) {
        if (mFacePassHandler != null) {
            try {
                Log.d(TAG,"CBGFacePassHelper " + "createFacePassGroup groupName: " + groupName);
                String[] localGroups = mFacePassHandler.getLocalGroups();
                if (localGroups != null) {
                    for (String group : localGroups) {
                        if (TextUtils.equals(group, groupName)) {
                            runUIThreadWithCheck(new Runnable() {
                                @Override
                                public void run() {
                                    if (onInitFacePassListener != null) {
                                        onInitFacePassListener.onCreateFaceGroup(groupName);
                                    }
                                    Log.d(TAG,"CBGFacePassHelper " + "createFacePassGroup success exits groupName: " + groupName);
                                }
                            });
                            return;
                        }
                    }
                }
                mFacePassHandler.createLocalGroup(groupName);
                runUIThreadWithCheck(new Runnable() {
                    @Override
                    public void run() {
                        if (onInitFacePassListener != null) {
                            onInitFacePassListener.onCreateFaceGroup(groupName);
                        }
                        Log.d(TAG,"CBGFacePassHelper " + "createFacePassGroup success new groupName:" + groupName);
                    }
                });

            } catch (Throwable e) {
                e.printStackTrace();
                runUIThreadWithCheck(new Runnable() {
                    @Override
                    public void run() {
                        if (onInitFacePassListener != null) {
                            onInitFacePassListener.onCreateFaceGroupError(groupName, e.getMessage());
                        }
                        Log.d(TAG,"CBGFacePassHelper " + "createFacePassGroup error groupName:" + groupName);
                    }
                });
            }
        }
    }

    /**
     * 读取download下面证书
     */
    private String readCertContent(String filename) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        if (readAssetCert) {
            try {
                inputStream = App.instance.getApplicationContext().getAssets().open(filename);
            } catch (IOException e) {
                return "";
            }
        } else {
            String downloadDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator;
            File file = new File(downloadDirPath + filename);
            if (!file.exists()) {
                return "";
            }
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                return "";
            }
        }
        Log.d(TAG,"CBGFacePassHelper " + "---initSDK-readCertContent--" + filename);
        try {
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            while (len > 0) {
                sb.append(new String(buffer, 0, len));
                len = inputStream.read(buffer);
            }
            inputStream.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public FacePassConfig getFaceConfig() {
        if (mFacePassHandler != null) {
            try {
                return mFacePassHandler.getConfig();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public FacePassConfig getFaceAddConfig() {
        if (mFacePassHandler != null) {
            try {
                return mFacePassHandler.getAddFaceConfig();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onClear() {
        stopFeedFrameDetectTask();
        stopRecognizeFrameTask();
        if (mFacePassHandler != null) {
            mFacePassHandler.release();
            mFacePassHandler = null;
        }
    }

    public interface OnInitFacePassListener {
        void onInitSuccess();

        void onInitError(String msg);

        default void onCreateFaceGroup(String groupName) {
        }

        default void onCreateFaceGroupError(String groupName, String msg) {
        }
    }

    public interface OnDetectFaceListener {
        void onDetectFaceToken(List<CBGFacePassRecognizeResult> faceTokenList);

        default void onNoDetectFaceToken() {

        }

        default void onDetectFace(int faceCount) {
        }
    }

}