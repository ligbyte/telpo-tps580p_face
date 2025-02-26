package com.stkj.cashier.cbgfacepass.net.okhttp;


import android.util.Log;

import com.stkj.cashier.cbgfacepass.net.custom.LogInterceptorWrapper;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * okHttp client 管理类
 */

public enum OkHttpManager {

    INSTANCE;

    private final int MAX_TIME_OUT = 10;
    private OkHttpClient mDefHttpClient;
    private OkHttpClient mCacheHttpClient;
    private Interceptor mDefIntercept;
    private boolean httpLogEnable = true;
    private boolean logSwitch;

    /**
     * ！！！在 mDefHttpClient 初始化之前赋值 第一次调用getAppHttpClient()之前
     *
     * @param mDefIntercept
     */
    public void setDefIntercept(Interceptor mDefIntercept) {
        this.mDefIntercept = mDefIntercept;
    }

    public void setHttpLogEnable(boolean httpLogEnable) {
        this.httpLogEnable = httpLogEnable;
    }

    public void setLogSwitch(boolean logSwitch) {
        if (httpLogEnable) {
            this.logSwitch = logSwitch;
        }
    }

    public boolean isLogSwitch() {
        return logSwitch;
    }

    /**
     * 获取默认App client
     */
    public OkHttpClient getAppHttpClient() {
        if (mDefHttpClient == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            if (mDefIntercept != null) {
                httpClientBuilder.addInterceptor(mDefIntercept);
            }
            //log
            if (httpLogEnable) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        //打印retrofit日志
                        Log.d("RetrofitLog","limeRetrofitLog = "+message);
                    }
                });
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                httpClientBuilder.addInterceptor(loggingInterceptor);
            }
            httpClientBuilder.connectTimeout(MAX_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(MAX_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(MAX_TIME_OUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
//            HandshakeCertificates clientCertificates = new HandshakeCertificates.Builder()
//                    .addPlatformTrustedCertificates()
//                    .build();
//            httpClientBuilder.sslSocketFactory(clientCertificates.sslSocketFactory(), clientCertificates.trustManager());
            httpClientBuilder.hostnameVerifier(OkHostnameVerifier.INSTANCE);
            mDefHttpClient = httpClientBuilder.build();
        }
        return mDefHttpClient;
    }

    public OkHttpClient getCacheHttpClient() {
        if (mCacheHttpClient == null) {
            mCacheHttpClient = createNewOkHttpClient(MAX_TIME_OUT, true);
        }
        return mCacheHttpClient;
    }

    public void clearCacheHttpClient() {
        mCacheHttpClient = null;
    }

    /**
     * 创建新的ok http client
     */
    public OkHttpClient createNewOkHttpClient(int timeOut, boolean retry) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .retryOnConnectionFailure(retry);
        return httpClientBuilder.build();
    }

}