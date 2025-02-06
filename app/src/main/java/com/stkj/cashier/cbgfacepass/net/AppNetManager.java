package com.stkj.cashier.cbgfacepass.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.stkj.cashier.cbgfacepass.callback.AppNetCallback;
import com.stkj.cashier.cbgfacepass.net.retrofit.RetrofitManager;

import java.util.HashSet;
import java.util.Set;

/**
 * app 网络管理类
 */
public enum AppNetManager {
    INSTANCE;
    public static final String API_TEST_URL = "http://101.43.252.67:9003";
    public static final String API_OFFICIAL_URL = "http://101.42.54.44:9003";
    private AppOkhttpIntercept appOkhttpIntercept;
    private AppRetrofitJsonConvertListener retrofitJsonConvertListener;
    private boolean isRequestingDeviceDomain;
    private Set<AppNetCallback> netCallbackSet = new HashSet<>();

    public AppOkhttpIntercept getAppOkhttpHttpIntercept() {
        if (appOkhttpIntercept == null) {
            appOkhttpIntercept = new AppOkhttpIntercept();
        }
        return appOkhttpIntercept;
    }

    public AppRetrofitJsonConvertListener getRetrofitJsonConvertListener() {
        if (retrofitJsonConvertListener == null) {
            retrofitJsonConvertListener = new AppRetrofitJsonConvertListener();
        }
        return retrofitJsonConvertListener;
    }



    public void clearAppNetCache() {
        isRequestingDeviceDomain = false;
        RetrofitManager.INSTANCE.removeAllRetrofit();
    }

    public boolean isRequestingDeviceDomain() {
        return isRequestingDeviceDomain;
    }

    public void addNetCallback(@NonNull AppNetCallback appNetCallback) {
        netCallbackSet.add(appNetCallback);
    }

    public void removeNetCallback(@NonNull AppNetCallback appNetCallback) {
        netCallbackSet.remove(appNetCallback);
    }
}