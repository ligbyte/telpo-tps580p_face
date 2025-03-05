package com.stkj.cashier.deviceinterface.callback;

public interface OnPrintListener {

    void onPrintSuccess();

    void onPrintError(String message);
}
