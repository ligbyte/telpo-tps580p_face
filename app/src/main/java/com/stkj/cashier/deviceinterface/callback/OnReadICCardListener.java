package com.stkj.cashier.deviceinterface.callback;

public interface OnReadICCardListener {

    void onReadCardData(String data);

    void onReadCardError(String message);
}
