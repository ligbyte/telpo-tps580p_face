package com.stkj.cashier.app.main.callback;

import android.view.SurfaceView;



public interface ConsumerListener {

    default void onCreateFacePreviewView(SurfaceView previewView, SurfaceView irPreviewView) {

    }

    default void onCreateTitleLayout(String homeTitleLayout) {

    }

    default void onConsumerDismiss() {

    }

    default void onConsumerChanged() {

    }
}
