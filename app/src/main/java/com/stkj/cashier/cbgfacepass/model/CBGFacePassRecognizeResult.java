package com.stkj.cashier.cbgfacepass.model;

/**
 * 人脸识别结果
 */
public class CBGFacePassRecognizeResult {

    private String faceToken;
    private boolean isFacePassSuccess;
    private int recognitionState;

    public CBGFacePassRecognizeResult(String faceToken, boolean isFacePassSuccess, int recognitionState) {
        this.faceToken = faceToken;
        this.isFacePassSuccess = isFacePassSuccess;
        this.recognitionState = recognitionState;
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public boolean isFacePassSuccess() {
        return isFacePassSuccess;
    }

    public void setFacePassSuccess(boolean facePassSuccess) {
        isFacePassSuccess = facePassSuccess;
    }

    public int getRecognitionState() {
        return recognitionState;
    }

    public void setRecognitionState(int recognitionState) {
        this.recognitionState = recognitionState;
    }

    @Override
    public String toString() {
        return "CBGFacePassRecognizeResult{" +
                "faceToken='" + faceToken + '\'' +
                ", isFacePassSuccess=" + isFacePassSuccess +
                ", recognitionState=" + recognitionState +
                '}';
    }
}
