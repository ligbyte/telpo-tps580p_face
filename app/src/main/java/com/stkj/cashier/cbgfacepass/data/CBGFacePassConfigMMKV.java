package com.stkj.cashier.cbgfacepass.data;

import com.stkj.cashier.cbgfacepass.model.CBGFacePassConfig;
import com.tencent.mmkv.MMKV;

/**
 * 人脸识别信息配置
 */
public class CBGFacePassConfigMMKV {

    public static int DEFAULT_DETECT_FACE_LEVEL50 = 362;
    public static int DEFAULT_DETECT_FACE_LEVEL80 = 387;
    public static int DEFAULT_DETECT_FACE_LEVEL100 = 412;

    public static final String MMKV_NAME = "cbg_facepass";

    //活体开关 livenessEnabled;
    public static boolean getLivenessEnabled() {
        return getFacePassMMKV().getBoolean("livenessEnabled", true);
    }

    public static void putLivenessEnabled(boolean enable) {
        getFacePassMMKV().putBoolean("livenessEnabled", enable);
    }


    /**
     * occlusionMode
     * 遮挡和属性模式，默认使用模式0。
     * 一共有5种模式可以选择，{0，1，2，3，4}，五选一，默认是模式0
     * 其中入库的模式只有0和2两种，即0：遮挡允许入库，2：遮挡不允许入库！
     */
    public static boolean getOcclusionMode() {
        return getFacePassMMKV().getBoolean("occlusionMode", true);
    }

    public static void putOcclusionMode(boolean enable) {
        getFacePassMMKV().putBoolean("occlusionMode", enable);
    }

    //相似度开关 livenessGaThresholdEnabled;
    public static boolean getGaThresholdEnabled() {
        return getFacePassMMKV().getBoolean("livenessGaThresholdEnabled", true);
    }

    public static void putGaThresholdEnabled(boolean enable) {
        getFacePassMMKV().putBoolean("livenessGaThresholdEnabled", enable);
    }

    //识别阈值 分布区间[0, 100]，float型，传入未限制位数。
    // searchThreshold = 65f;
    public static float getSearchThreshold() {
        return getFacePassMMKV().getFloat("searchThreshold", 75f);
    }

    public static void putSearchThreshold(float searchThreshold) {
        getFacePassMMKV().putFloat("searchThreshold", searchThreshold);
    }

    //活体阈值 分布区间[0, 100]，float型，传入未限制位数。
    // livenessThreshold = 80f;
    public static float getLivenessThreshold() {
        return getFacePassMMKV().getFloat("livenessThreshold", 80f);
    }

    public static void putLivenessThreshold(float livenessThreshold) {
        getFacePassMMKV().putFloat("livenessThreshold", livenessThreshold);
    }

    //相似度阈值 分布区间[0, 100]，float型，传入未限制位数。
    //livenessGaThreshold = 85f;
    public static float getGaThreshold() {
        return getFacePassMMKV().getFloat("livenessGaThreshold", 85f);
    }

    public static void putGaThreshold(float livenessGaThreshold) {
        getFacePassMMKV().putFloat("livenessGaThreshold", livenessGaThreshold);
    }

    private static int defPoseThreshold = 20;

    public static void setDefPoseThreshold(int defPoseThreshold) {
        CBGFacePassConfigMMKV.defPoseThreshold = defPoseThreshold;
    }

    public static int getDefPoseThreshold() {
        return defPoseThreshold;
    }

    //角度阈值 分布区间[0, 30]，float型，传入未限制位数。
    // poseThreshold = 10f;
    public static int getPoseThreshold() {
        return getFacePassMMKV().getInt("poseThreshold", defPoseThreshold);
    }

    public static void putPoseThreshold(int poseThreshold) {
        getFacePassMMKV().putInt("poseThreshold", poseThreshold);
    }

    //是否开启双目检测
    public static boolean isOpenDualCamera() {
        return getFacePassMMKV().getBoolean("openDualCamera", false);
    }

    public static void setOpenDualCamera(boolean openDualCamera) {
        getFacePassMMKV().putBoolean("openDualCamera", openDualCamera);
    }

    /**
     * 最小人脸尺寸。(识别距离) [0-512] 值越大识别距离越短
     * 表示允许进行识别的最小人脸尺寸。100 1m | 125 0.8m | 150 0.5m
     */
    private static int defDetectFaceMinThreshold = 362;

    public static void setDefDetectFaceMinThreshold(int defDetectFaceMinThreshold) {
        CBGFacePassConfigMMKV.defDetectFaceMinThreshold = defDetectFaceMinThreshold;
    }

    public static int getDefDetectFaceMinThreshold() {
        return defDetectFaceMinThreshold;
    }

    public static int getDetectFaceMinThreshold() {
        return getFacePassMMKV().getInt("detectFaceMinThreshold", defDetectFaceMinThreshold);
    }

    public static void putDetectFaceMinThreshold(int detectFaceMinThreshold) {
        getFacePassMMKV().putInt("detectFaceMinThreshold", detectFaceMinThreshold);
    }

    /**
     * 1、 0.5m || 2、 0.8m || 3、 1m
     */

    public static final int DETECT_FACE_LEVEL50 = 1;
    public static final int DETECT_FACE_LEVEL80 = 2;
    public static final int DETECT_FACE_LEVEL100 = 3;

    public static int getDetectFaceLevel() {
        return getFacePassMMKV().getInt("detectFaceLevel", DETECT_FACE_LEVEL50);
    }

    public static void putDetectFaceLevel(int detectFaceLevel) {
        getFacePassMMKV().putInt("detectFaceLevel", detectFaceLevel);
    }


    /**
     * 人脸入库最小人脸尺寸。(识别距离)
     * 表示允许进行识别的最小人脸尺寸。100 1m | 125 0.8m | 150 0.5m
     */
    private static int defAddFaceMinThreshold = 100;

    public static void setDefAddFaceMinThreshold(int defAddFaceMinThreshold) {
        CBGFacePassConfigMMKV.defAddFaceMinThreshold = defAddFaceMinThreshold;
    }

    public static int getDefAddFaceMinThreshold() {
        return defAddFaceMinThreshold;
    }

    public static int getAddFaceMinThreshold() {
        return getFacePassMMKV().getInt("addFaceMinThreshold", defAddFaceMinThreshold);
    }

    public static void putAddFaceMinThreshold(int faceMinThreshold) {
        getFacePassMMKV().putInt("addFaceMinThreshold", faceMinThreshold);
    }

    /**
     * 人脸识别结果分数阈值
     */
    public static int getResultSearchScoreThreshold() {
        return getFacePassMMKV().getInt("resultSearchScoreThreshold", 75);
    }

    public static void putResultSearchScoreThreshold(int resultSearchScoreThreshold) {
        getFacePassMMKV().putInt("resultSearchScoreThreshold", resultSearchScoreThreshold);
    }

    public static CBGFacePassConfig getFacePassConfig(boolean isDeviceSupportDualCamera) {
        CBGFacePassConfig cbgFacePassConfig = new CBGFacePassConfig();
        if (isDeviceSupportDualCamera && isOpenDualCamera()) {
            //双目摄像头
            cbgFacePassConfig.setCameraType(CBGFacePassConfig.FACE_DUAL_CAMERA);
            //活体开关
            cbgFacePassConfig.setLivenessEnabled(false);
        } else {
            cbgFacePassConfig.setCameraType(CBGFacePassConfig.FACE_SINGLE_CAMERA);
            //活体开关
            cbgFacePassConfig.setLivenessEnabled(getLivenessEnabled());
        }
        /**
         * 遮挡和属性模式，默认使用模式0。
         * 一共有5种模式可以选择，{0，1，2，3，4}，五选一，默认是模式0
         * 其中入库的模式只有0和2两种，即0：遮挡允许入库，2：遮挡不允许入库！
         */
        cbgFacePassConfig.setOcclusionMode(getOcclusionMode());
        //相似度开关
        cbgFacePassConfig.setLivenessGaThresholdEnabled(getGaThresholdEnabled());
        //识别阈值 分布区间[0, 100]，float型，传入未限制位数。
        cbgFacePassConfig.setSearchThreshold(getSearchThreshold());
        //活体阈值 分布区间[0, 100]，float型，传入未限制位数。
        cbgFacePassConfig.setLivenessThreshold(getLivenessThreshold());
        //相似度阈值 分布区间[0, 100]，float型，传入未限制位数。
        cbgFacePassConfig.setLivenessGaThreshold(getGaThreshold());
        /**
         * 最小人脸尺寸。(识别距离)
         * 表示允许进行识别的最小人脸尺寸。100 1m | 125 0.8m | 150 0.5m
         */
        cbgFacePassConfig.setDetectFaceMinThreshold(getDetectFaceMinThreshold());
        cbgFacePassConfig.setAddFaceMinThreshold(getAddFaceMinThreshold());
        cbgFacePassConfig.setPoseThreshold(getPoseThreshold());
        //识别结果分数
        cbgFacePassConfig.setResultSearchScoreThreshold(getResultSearchScoreThreshold());
        return cbgFacePassConfig;
    }

    public static MMKV getFacePassMMKV() {
        return MMKV.mmkvWithID(MMKV_NAME);
    }
}
