package com.stkj.cashier.common.camera;

import android.Manifest;

import com.stkj.cashier.R;
import com.stkj.cashier.common.core.AppManager;
import com.stkj.cashier.common.permissions.base.BasePermissionRequest;


public class SimpleCameraPermission extends BasePermissionRequest {

    private boolean enableCapture;

    public SimpleCameraPermission() {
    }

    public void setEnableCapture(boolean enableCapture) {
        this.enableCapture = enableCapture;
    }

    @Override
    public String[] getPermissions() {
        if (!enableCapture) {
            return new String[]{Manifest.permission.CAMERA};
        }
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    }

    @Override
    public String getRequestExplain() {
        return "请求允许使用你的相机和录音功能";
    }

    @Override
    public String getRationaleReason() {
        return "你已禁止使用相机和录音功能，如需开启，请到该应用的系统设置页面打开。";
    }

    @Override
    public String getRequestTitle() {
        return AppManager.INSTANCE.getApplication().getString(R.string.app_name) + "请求使用相机和录音功能";
    }

    @Override
    public String getAgainRequestExplain() {
        return "你已禁止使用相机和录音功能，如需使用请允许。";
    }

}
