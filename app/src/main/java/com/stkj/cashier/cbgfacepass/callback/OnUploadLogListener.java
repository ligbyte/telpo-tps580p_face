package com.stkj.cashier.cbgfacepass.callback;

public interface OnUploadLogListener {
    default void onUploadStart() {

    }

    void onUploadLogSuccess();

    void onUploadLogError(String msg);
}
