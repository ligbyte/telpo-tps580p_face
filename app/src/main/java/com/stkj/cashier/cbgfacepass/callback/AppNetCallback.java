package com.stkj.cashier.cbgfacepass.callback;

public interface AppNetCallback {
    default void onNetInitSuccess() {
    }

    default void onNetInitError(String message) {
    }
}
