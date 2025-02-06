package com.stkj.cashier.cbgfacepass.model;


import com.stkj.cashier.cbgfacepass.data.CBGFacePassConfigMMKV;

public class CBGFacePassConfig {

    public static final int FACE_SINGLE_CAMERA = 0;
    public static final int FACE_DUAL_CAMERA = 1;

    private int cameraType;
    //活体开关
    private boolean livenessEnabled;

    /**
     * 遮挡和属性模式，默认使用模式0。
     * 一共有5种模式可以选择，{0，1，2，3，4}，五选一，默认是模式0
     * 其中入库的模式只有0和2两种，即0：遮挡允许入库，2：遮挡不允许入库！
     */
    private boolean occlusionMode;
    //相似度开关
    private boolean livenessGaThresholdEnabled;
    //识别阈值 分布区间[0, 100]，float型，传入未限制位数。
    private float searchThreshold = 75f;
    //活体阈值 分布区间[0, 100]，float型，传入未限制位数。
    private float livenessThreshold = 80f;
    //相似度阈值 分布区间[0, 100]，float型，传入未限制位数。
    private float livenessGaThreshold = 85f;
    /**
     * 最小人脸尺寸。(识别距离)
     * 表示允许进行识别的最小人脸尺寸。100 1m | 125 0.8m | 150 0.5m
     */
    /**
     * 最小人脸尺寸。
     * 表示允许进行识别的最小人脸尺寸。
     * Int值，[0, 512]，理论上两端0和512可取，但是实际使用中强烈不建议选择这么极端的数值。
     * FaceMin是在检测到人脸后，进行大小比较的，同时对长和宽生效，也就是说需要同时满足：
     * width >= faceMin
     * height >= faceMin
     * 如果不满足，这一帧会被直接丢弃，然后等待下一帧。
     * FaceMin直接的影响识别距离，faceMin越小，脸离摄像头越远，但是对算法识别和活体的准确
     * 度越差；faceMin越大，越适合算法，但是就得凑近脸。
     * 这块算法效果不太好量化出来，所以我们给了一个综合的策略：
     * ”建议客户保证faceMin大于旷视的建议、以保障最基本效果，如果需要降低faceMin来提升距
     * 离体验，则需要自行保障整体识别体验（比如降低底库大小、提升底库质量等）“。
     * 目前旷视建议的值是150，基本可以保障识别效果。
     * 附faceMin和识别距离的关系（基于主流720p摄像头的正常调校）：
     */
    private int detectFaceMinThreshold = CBGFacePassConfigMMKV.getDefDetectFaceMinThreshold();

    private int addFaceMinThreshold = CBGFacePassConfigMMKV.getDefAddFaceMinThreshold();
    private int poseThreshold = CBGFacePassConfigMMKV.getDefPoseThreshold();

    //人脸识别分数阈值
    private int resultSearchScoreThreshold = 75;

    public int getResultSearchScoreThreshold() {
        return resultSearchScoreThreshold;
    }

    public void setResultSearchScoreThreshold(int resultSearchScoreThreshold) {
        this.resultSearchScoreThreshold = resultSearchScoreThreshold;
    }

    public int getPoseThreshold() {
        return poseThreshold;
    }

    public void setPoseThreshold(int poseThreshold) {
        this.poseThreshold = poseThreshold;
    }

    public CBGFacePassConfig() {
    }

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }

    public boolean isLivenessEnabled() {
        return livenessEnabled;
    }

    public void setLivenessEnabled(boolean livenessEnabled) {
        this.livenessEnabled = livenessEnabled;
    }

    public boolean isOcclusionMode() {
        return occlusionMode;
    }

    public void setOcclusionMode(boolean occlusionMode) {
        this.occlusionMode = occlusionMode;
    }

    public boolean isLivenessGaThresholdEnabled() {
        return livenessGaThresholdEnabled;
    }

    public void setLivenessGaThresholdEnabled(boolean livenessGaThresholdEnabled) {
        this.livenessGaThresholdEnabled = livenessGaThresholdEnabled;
    }

    public float getSearchThreshold() {
        return searchThreshold;
    }

    public void setSearchThreshold(float searchThreshold) {
        this.searchThreshold = searchThreshold;
    }

    public float getLivenessThreshold() {
        return livenessThreshold;
    }

    public void setLivenessThreshold(float livenessThreshold) {
        this.livenessThreshold = livenessThreshold;
    }

    public float getLivenessGaThreshold() {
        return livenessGaThreshold;
    }

    public void setLivenessGaThreshold(float livenessGaThreshold) {
        this.livenessGaThreshold = livenessGaThreshold;
    }

    public int getDetectFaceMinThreshold() {
        return detectFaceMinThreshold;
    }

    public void setDetectFaceMinThreshold(int detectFaceMinThreshold) {
        this.detectFaceMinThreshold = detectFaceMinThreshold;
    }

    public int getAddFaceMinThreshold() {
        return addFaceMinThreshold;
    }

    public void setAddFaceMinThreshold(int addFaceMinThreshold) {
        this.addFaceMinThreshold = addFaceMinThreshold;
    }
}
