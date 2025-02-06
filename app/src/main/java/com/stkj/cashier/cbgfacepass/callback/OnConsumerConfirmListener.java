package com.stkj.cashier.cbgfacepass.callback;


import com.stkj.cashier.cbgfacepass.model.FacePassPeopleInfo;

public interface OnConsumerConfirmListener {

    /**
     * 刷脸支付
     */
    void onConfirmFacePass(FacePassPeopleInfo passPeopleInfo);

    void onCancelFacePass(FacePassPeopleInfo passPeopleInfo);

    /**
     * 卡支付
     */
    void onConfirmCardNumber(String cardNumber);

    void onCancelCardNumber(String cardNumber);

    /**
     * 扫码支付
     */
    default void onConfirmScanData(String scanData) {

    }

    default void onCancelScanData(String scanData) {

    }

    /**
     * 副屏取消支付
     */
    default void onConsumerCancelPay() {

    }

    /**
     * 确认取餐码和手机号
     */
    default void onConfirmPhone(String phone) {

    }

    default void onConfirmTakeMealCode(String takeCode) {

    }

    /**
     * 数字输入框显示、隐藏
     */
    default void onShowSimpleInputNumber(boolean show) {

    }
}
