package com.stkj.cashier.app.main.callback;


import com.stkj.cashier.cbgfacepass.model.FacePassPeopleInfo;

public interface ConsumerController {

    /**
     * 设置人脸识别预览
     */
    void setFacePreview(boolean preview);

    /**
     * 设置消费者页人脸下面提示
     */
    void setConsumerTips(String tips);

    /**
     * 设置消费者页人脸下面提示
     */
    void setConsumerTips(String tips, int consumerPro);

    /**
     * 设置消费者身份认证提示语
     */
    void setConsumerAuthTips(String tips);

    boolean isConsumerAuthTips();

    /**
     * 人脸识别确认
     */
    void setConsumerConfirmFaceInfo(FacePassPeopleInfo facePassPeopleInfo, boolean needConfirm, int consumerType);

    /**
     * 刷卡识别确认
     */
    void setConsumerConfirmCardInfo(String cardNumber, boolean needConfirm);

    /**
     * 扫码识别确认
     */
    void setConsumerConfirmScanInfo(String scanData, boolean needConfirm);

    /**
     * 取餐模式
     */
    void setConsumerTakeMealWay();

    /**
     * 重置消费者默认布局
     */
    void resetFaceConsumerLayout();

    /**
     * 设置消费页面默认状态
     */
    void setNormalConsumeStatus();

    /**
     * 设置消费页面支付状态
     */
    void setPayConsumeStatus();

    /**
     * 设置支付金额
     */
    void setPayPrice(String payPrice, boolean canCancelPay);

    /**
     * 设置隐藏显示取消按钮
     */
    void setCanCancelPay(boolean showCancelPay);

}
