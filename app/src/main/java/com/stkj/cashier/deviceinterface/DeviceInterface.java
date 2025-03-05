package com.stkj.cashier.deviceinterface;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.view.KeyEvent;


import com.stkj.cashier.app.base.BaseActivity;
import com.stkj.cashier.common.core.ActivityWeakRefHolder;
import com.stkj.cashier.common.core.AppManager;
import com.stkj.cashier.common.core.MainThreadHolder;
import com.stkj.cashier.common.utils.ActivityUtils;
import com.stkj.cashier.common.utils.AndroidUtils;
import com.stkj.cashier.deviceinterface.callback.DeviceStatusListener;
import com.stkj.cashier.deviceinterface.callback.OnMoneyBoxListener;
import com.stkj.cashier.deviceinterface.callback.OnPrintListener;
import com.stkj.cashier.deviceinterface.callback.OnReadICCardListener;
import com.stkj.cashier.deviceinterface.callback.OnReadWeightListener;
import com.stkj.cashier.deviceinterface.callback.OnScanQRCodeListener;
import com.stkj.cashier.deviceinterface.model.DeviceHardwareInfo;
import com.stkj.cashier.deviceinterface.model.PrinterData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 设备接口
 */
public abstract class DeviceInterface {

    private OnMoneyBoxListener mMoneyBoxListener;
    private OnPrintListener mPrintListener;
    private OnReadICCardListener mReadICCardListener;
    private OnReadWeightListener mReadWeightListener;
    private OnScanQRCodeListener mScanQRCodeListener;
    private DeviceStatusListener mDeviceStatusListener;

    /**
     * 释放
     */
    public void release() {
        mMoneyBoxListener = null;
        mPrintListener = null;
        mReadICCardListener = null;
        mReadWeightListener = null;
        mScanQRCodeListener = null;
        mDeviceStatusListener = null;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public abstract void init(Context context);

    /**
     * 设备名称
     */
    public abstract String getDeviceName();

    /**
     * 获取设备唯一id
     */
    public abstract String getMachineNumber();

    /**
     * 读取ic card
     */
    public void readICCard(OnReadICCardListener readCardListener) {

    }

    /**
     * 注册读卡回调
     */
    public void registerICCardListener(OnReadICCardListener readCardListener) {
        mReadICCardListener = readCardListener;
    }

    public void notifyOnReadCardData(String data) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mReadICCardListener != null) {
                    mReadICCardListener.onReadCardData(data);
                }
            }
        });
    }

    public void notifyOnReadCardError(String msg) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mReadICCardListener != null) {
                    mReadICCardListener.onReadCardError(msg);
                }
            }
        });
    }

    public void unRegisterICCardListener(OnReadICCardListener readICCardListener) {
        if (mReadICCardListener == readICCardListener) {
            mReadICCardListener = null;
        }
    }

    /**
     * 设备是否支持读卡
     */
    public abstract boolean isSupportReadICCard();

    /**
     * 扫条形码
     */
    public void scanQrCode(OnScanQRCodeListener onScanQRCodeListener) {

    }

    /**
     * 注册扫码回调
     */
    public void registerScanQRCodeListener(OnScanQRCodeListener scanQRCodeListener) {
        mScanQRCodeListener = scanQRCodeListener;
    }

    public void notifyOnScanQrCode(String data) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mScanQRCodeListener != null) {
                    mScanQRCodeListener.onScanQrCode(data);
                }
            }
        });
    }

    public void notifyOnScanQRCodeError(String msg) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mScanQRCodeListener != null) {
                    mScanQRCodeListener.onScanQRCodeError(msg);
                }
            }
        });
    }

    public void unRegisterScanQRCodeListener(OnScanQRCodeListener scanQRCodeListener) {
        if (mScanQRCodeListener == scanQRCodeListener) {
            mScanQRCodeListener = null;
        }
    }

    /**
     * 是否支持扫码
     */
    public abstract boolean isSupportScanQrCode();

    /**
     * 读取称重信息
     */
    public void readWeight(OnReadWeightListener onReadWeightListener) {

    }

    /**
     * 注册称重回调
     */
    public void registerReadWeightListener(OnReadWeightListener readWeightListener) {
        mReadWeightListener = readWeightListener;
    }

    public void notifyOnReadWeightData(String data, String unit) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mReadWeightListener != null) {
                    mReadWeightListener.onReadWeightData(data, unit);
                }
            }
        });
    }

    public void notifyOnReadWeightError(String msg) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mReadWeightListener != null) {
                    mReadWeightListener.onReadWeightError(msg);
                }
            }
        });
    }

    public void unRegisterReadWeightListener(OnReadWeightListener readWeightListener) {
        if (mReadWeightListener == readWeightListener) {
            mReadWeightListener = null;
        }
    }


    /**
     * 是否支持称重
     */
    public abstract boolean isSupportReadWeight();

    /**
     * 打印数据
     */
    public void print(List<PrinterData> printerDataList, OnPrintListener onPrintListener) {

    }

    /**
     * 获取一行最大打印字数
     */
    public int getPrintLineMaxLength() {
        return 30;
    }

    /**
     * 注册打印回调
     */
    public void registerPrintListener(OnPrintListener printListener) {
        mPrintListener = printListener;
    }

    public void notifyOnPrintSuccess() {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mPrintListener != null) {
                    mPrintListener.onPrintSuccess();
                }
            }
        });
    }

    public void notifyOnPrintError(String msg) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mPrintListener != null) {
                    mPrintListener.onPrintError(msg);
                }
            }
        });
    }

    public void unRegisterPrintListener(OnPrintListener printListener) {
        if (mPrintListener == printListener) {
            mPrintListener = null;
        }
    }

    /**
     * 是否支持打印
     */
    public abstract boolean isSupportPrint();

    /**
     * 打开关闭钱箱
     */
    public void openMoneyBox(OnMoneyBoxListener onMoneyBoxListener) {

    }

    /**
     * 注册钱箱回调
     */
    public void registerMoneyBoxListener(OnMoneyBoxListener moneyBoxListener) {
        mMoneyBoxListener = moneyBoxListener;
    }

    public void notifyOnBoxOpenSuccess() {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mMoneyBoxListener != null) {
                    mMoneyBoxListener.onBoxOpenSuccess();
                }
            }
        });
    }

    public void notifyOnBoxOpenError(String msg) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mMoneyBoxListener != null) {
                    mMoneyBoxListener.onBoxOpenError(msg);
                }
            }
        });
    }

    public void unRegisterMoneyBoxListener(OnMoneyBoxListener moneyBoxListener) {
        if (mMoneyBoxListener == moneyBoxListener) {
            mMoneyBoxListener = null;
        }
    }

    /**
     * 是否支持钱箱
     */
    public abstract boolean isSupportMoneyBox();

    /**
     * 重启设备
     */
    public boolean rebootDevice() {
        return false;
    }

    /**
     * 关闭设备
     */
    public boolean shutDownDevice() {
        return false;
    }

    /**
     * 显示隐藏系统状态栏
     */
    public boolean showOrHideSysStatusBar(boolean showOrHide) {
        return false;
    }

    /**
     * 显示隐藏系统导航栏
     */
    public boolean showOrHideSysNavBar(boolean showOrHide) {
        return false;
    }

    /**
     * 是否支持双目识别
     */
    public abstract boolean isSupportDualCamera();

    /**
     * 是否支持移动信号
     */
    public abstract boolean isSupportMobileSignal();

    /**
     * 静默安装Apk
     */
    public void silenceInstallApk(String apkPath) {
        if (apkPath.startsWith("content:")) {
            AndroidUtils.installApk(Uri.parse(apkPath));
        } else {
            AndroidUtils.installApk(new File(apkPath));
        }
    }

    public ComponentName getLaunchActivity() {
        return new ComponentName("com.stkj.cashier2", "com.stkj.cashier.home.ui.activity.MainActivity");
    }

    public int getConsumeLayRes() {
        return 0;
    }

    public int getBackCameraId() {
        return -1;
    }

    public int getFrontCameraId() {
        return -1;
    }

    public int getIRCameraId() {
        return -1;
    }

    public int getCameraDisplayOrientation() {
        return -1;
    }

    public boolean needUseCameraPreviewOrientation() {
        return false;
    }

    public int getIRCameraDisplayOrientation() {
        return -1;
    }

    public boolean needUseIRCameraPreviewOrientation() {
        return false;
    }

    //默认识别距离阈值
    public int getDefaultDetectFaceMinThreshold() {
        return 512 - get50cmDetectFaceMinThreshold();
    }

    /**
     * 0.5米人脸识别阈值
     */
    public int get50cmDetectFaceMinThreshold() {
        return 362;
    }

    /**
     * 0.8米人脸识别阈值
     */
    public int get80cmDetectFaceMinThreshold() {
        return 387;
    }

    /**
     * 1米人脸识别阈值
     */
    public int get100cmDetectFaceMinThreshold() {
        return 412;
    }

    //默认人脸入库阈值
    public int getDefaultAddFaceMinThreshold() {
        return 100;
    }

    //默认人脸角度阈值
    public int getDefaultPoseThreshold() {
        return 20;
    }

    /**
     * 初始化usb设备
     */
    public void initUsbDevices(HashMap<String, UsbDevice> usbDeviceMap) {

    }

    /**
     * 添加usb设备
     */
    public void attachUsbDevice(UsbDevice usbDevice) {

    }

    /**
     * 删除usb设备
     */
    public void detachUsbDevice(UsbDevice usbDevice) {

    }

    /**
     * 是否支持usb设备
     */
    public boolean isSupportUSBDevice() {
        return false;
    }

    public ActivityWeakRefHolder getMainWeakRefHolder(Class<? extends ActivityWeakRefHolder> weakRefHolder) {
        Activity mainActivity = AppManager.INSTANCE.getMainActivity();
        if (mainActivity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) mainActivity;
            if (!ActivityUtils.isActivityFinished(baseActivity)) {
                return baseActivity.getWeakRefHolder(weakRefHolder);
            }
        }
        return null;
    }

    public void registerDeviceStatusListener(DeviceStatusListener mDeviceStatusListener) {
        this.mDeviceStatusListener = mDeviceStatusListener;
    }

    public void unRegisterDeviceStatusListener(DeviceStatusListener deviceStatusListener) {
        if (mDeviceStatusListener == deviceStatusListener) {
            mDeviceStatusListener = null;
        }
    }

    public void notifyAttachDevice(DeviceHardwareInfo deviceHardwareInfo) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mDeviceStatusListener != null) {
                    mDeviceStatusListener.onAttachDevice(deviceHardwareInfo);
                }
            }
        });
    }

    public void notifyDetachDevice(DeviceHardwareInfo deviceHardwareInfo) {
        MainThreadHolder.post(new Runnable() {
            @Override
            public void run() {
                if (mDeviceStatusListener != null) {
                    mDeviceStatusListener.onDetachDevice(deviceHardwareInfo);
                }
            }
        });
    }

    /**
     * 分发键盘事件
     *
     * @param event
     */
    public void dispatchKeyEvent(KeyEvent event) {

    }

    public boolean isCanDispatchKeyEvent() {
        return false;
    }

    public boolean isFinishDispatchKeyEvent() {
        return true;
    }

    /**
     * 获取机器默认的设备硬件信息
     */
    public List<DeviceHardwareInfo> getUSBDeviceHardwareInfoList() {
        return new ArrayList<>();
    }

}
