package com.stkj.cashier.deviceinterface;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.annotation.NonNull;


import com.stkj.cashier.common.core.ActivityWeakRefHolder;
import com.stkj.cashier.deviceinterface.callback.UsbDeviceListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * usb设备监听
 */
public class UsbDeviceHelper extends ActivityWeakRefHolder {

    private HashMap<String, UsbDevice> usbDeviceMap = new HashMap<>();
    private UsbBroadCastReceiver mUsbBroadCastReceiver;
    private Set<UsbDeviceListener> deviceListenerSet = new HashSet<>();

    public UsbDeviceHelper(@NonNull Activity activity) {
        super(activity);
        //注册usb设备监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mUsbBroadCastReceiver = new UsbBroadCastReceiver();
        activity.registerReceiver(mUsbBroadCastReceiver, intentFilter);
        //获取当前已经连接的usb设备
        UsbManager mUsbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDeviceList = mUsbManager.getDeviceList();
        usbDeviceMap.putAll(usbDeviceList);
    }

    public void addUsbListener(UsbDeviceListener usbDeviceListener) {
        deviceListenerSet.add(usbDeviceListener);
    }

    public void removeUsbListener(UsbDeviceListener usbDeviceListener) {
        deviceListenerSet.remove(usbDeviceListener);
    }

    public HashMap<String, UsbDevice> getUsbDeviceMap() {
        return usbDeviceMap;
    }

    public UsbDevice getUsbDevice(String usbName) {
        return usbDeviceMap.get(usbName);
    }

    @Override
    public void onClear() {
        deviceListenerSet.clear();
        Activity holderActivity = getHolderActivity();
        if (holderActivity != null) {
            holderActivity.unregisterReceiver(mUsbBroadCastReceiver);
        }
    }

    //usb 监听 由于权限问题而且sdk内部有超时时间 ,适用情况应该是系统默认usb权限开放或者root 板则使用UsbNativeApi
    private class UsbBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction != null) {
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (usbDevice != null) {
                    if (intentAction.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                        usbDeviceMap.put(usbDevice.getDeviceName(), usbDevice);
                        for (UsbDeviceListener deviceListener : deviceListenerSet) {
                            deviceListener.onAttachDevice(usbDevice, usbDeviceMap);
                        }
                    } else if (intentAction.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                        usbDeviceMap.remove(usbDevice.getDeviceName());
                        for (UsbDeviceListener deviceListener : deviceListenerSet) {
                            deviceListener.onDetachDevice(usbDevice, usbDeviceMap);
                        }
                    }
                }
            }
        }
    }
}
