package com.stkj.cashier.deviceinterface.callback;

public interface OnMoneyBoxListener {
    void onBoxOpenSuccess();

    void onBoxOpenError(String message);
}
