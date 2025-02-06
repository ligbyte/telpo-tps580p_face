package com.stkj.cashier.cbgfacepass.permission;

import android.Manifest;

import com.stkj.cashier.cbgfacepass.common.BasePermissionRequest;


/**
 * cbgfacepass请求权限
 */
public class CBGPermissionRequest extends BasePermissionRequest {

    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public String getRequestExplain() {
        return "请求允许使用你的相机以及存储功能";
    }

    @Override
    public String getRationaleReason() {
        return "你已禁止使用相机以及存储功能，如需开启，请到该应用的系统设置页面打开。";
    }

    @Override
    public String getRequestTitle() {
        return "人脸识别功能请求使用相机以及存储功能";
    }

    @Override
    public String getAgainRequestExplain() {
        return "你已禁止使用相机以及存储功能，如需使用请允许。";
    }
}
