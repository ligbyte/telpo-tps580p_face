package com.stkj.cashier.cbgfacepass.net.custom;

import androidx.annotation.NonNull;


import com.stkj.cashier.cbgfacepass.net.okhttp.OkHttpManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * okhttp log包装类
 */
public class LogInterceptorWrapper implements Interceptor {
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (OkHttpManager.INSTANCE.isLogSwitch()) {
            return httpLoggingInterceptor.intercept(chain);
        }
        return chain.proceed(chain.request());
    }



}
