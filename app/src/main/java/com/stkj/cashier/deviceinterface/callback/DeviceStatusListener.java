package com.stkj.cashier.deviceinterface.callback;


import com.stkj.cashier.deviceinterface.model.DeviceHardwareInfo;

public interface DeviceStatusListener {
    void onAttachDevice(DeviceHardwareInfo deviceHardwareInfo);
    void onDetachDevice(DeviceHardwareInfo deviceHardwareInfo);
}
