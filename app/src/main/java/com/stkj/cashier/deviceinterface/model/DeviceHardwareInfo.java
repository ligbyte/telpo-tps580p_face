package com.stkj.cashier.deviceinterface.model;

import android.hardware.usb.UsbDevice;

import java.util.Objects;

/**
 * 设备硬件信息
 */
public class DeviceHardwareInfo {

    public static final int TYPE_SCAN_GUN_KEYBOARD = 1;
    public static final int TYPE_PRINTER = 2;
    public static final int TYPE_MONEY_BOX = 3;
    public static final int TYPE_SCAN_GUN_SERIAL_PORT = 4;

    private int type;
    private int vendorId;
    private int productId;
    private String deviceName;
    private UsbDevice usbDevice;

    public DeviceHardwareInfo() {
    }

    public DeviceHardwareInfo(int vendorId, int productId, String deviceName, int type) {
        this.vendorId = vendorId;
        this.productId = productId;
        this.deviceName = deviceName;
        this.type = type;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceHardwareInfo that = (DeviceHardwareInfo) o;
        return vendorId == that.vendorId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorId, productId);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public void setUsbDevice(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }

    public boolean isConnected() {
        return usbDevice != null;
    }
}
