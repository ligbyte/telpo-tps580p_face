package com.stkj.cashier.app.main.callback;

public interface OnInputNumberListener {

    void onConfirmNumber(String number);

    void onClickBack();

    default void onConfirmError(boolean hasInputNumber) {

    }
}
