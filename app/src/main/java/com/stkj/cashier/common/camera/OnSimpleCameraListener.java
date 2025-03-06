package com.stkj.cashier.common.camera;

public interface OnSimpleCameraListener {

    void onTakePicture(String path);

    default void onCaptureVideo(String path) {

    }

    default void onCaptureVideoError(String msg) {

    }

    default void onTakePictureError(String msg) {

    }
}
