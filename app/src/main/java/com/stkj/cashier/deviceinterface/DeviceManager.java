package com.stkj.cashier.deviceinterface;

import android.content.Context;


/**
 * 默认设备接口实现
 */
public class DeviceManager extends DeviceInterface {


    private static final DeviceManager INSTANCE = new DeviceManager();

    private DeviceManager() {}

    public static DeviceManager getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isSupportMobileSignal() {
        return true;
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public String getDeviceName() {
        return "默认设备";
    }

    @Override
    public String getMachineNumber() {
        return "143D002006400040";
    }

    @Override
    public boolean isSupportReadICCard() {
        return false;
    }

    @Override
    public boolean isSupportScanQrCode() {
        return false;
    }

    @Override
    public boolean isSupportReadWeight() {
        return false;
    }

    @Override
    public boolean isSupportPrint() {
        return false;
    }

    @Override
    public boolean isSupportMoneyBox() {
        return false;
    }

    @Override
    public boolean isSupportDualCamera() {
        return false;
    }

}
