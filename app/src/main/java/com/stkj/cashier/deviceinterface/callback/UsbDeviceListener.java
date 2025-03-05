package com.stkj.cashier.deviceinterface.callback;

import android.hardware.usb.UsbDevice;

import java.util.HashMap;

public interface UsbDeviceListener {

    void onAttachDevice(UsbDevice device, HashMap<String, UsbDevice> allDevices);

    void onDetachDevice(UsbDevice device, HashMap<String, UsbDevice> allDevices);

}
