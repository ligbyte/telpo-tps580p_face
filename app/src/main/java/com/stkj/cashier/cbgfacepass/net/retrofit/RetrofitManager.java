package com.stkj.cashier.cbgfacepass.net.retrofit;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stkj.cashier.cbgfacepass.net.callback.RetrofitConvertJsonListener;
import com.stkj.cashier.cbgfacepass.net.okhttp.OkHttpManager;
import com.stkj.cashier.cbgfacepass.net.retrofit.jackson.JacksonConverterFactory;
import com.stkj.cashier.util.rxjava.RxJava3CallAdapterFactory;
import com.stkj.cashier.util.rxjava.ScalarsConverterFactory;

import java.util.HashMap;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;

public enum RetrofitManager {

    INSTANCE;

    private HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();
    private String defaultBaseUrl;
    private RetrofitConvertJsonListener convertJsonListener;

    public Retrofit getRetrofit(String baseUrl) {
        Retrofit retrofit = retrofitHashMap.get(baseUrl);
        if (retrofit == null) {
            retrofit = createRetrofit(baseUrl);
            retrofitHashMap.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    public void setConvertJsonListener(RetrofitConvertJsonListener convertJsonListener) {
        this.convertJsonListener = convertJsonListener;
    }

    public RetrofitConvertJsonListener getConvertJsonListener() {
        return convertJsonListener;
    }

    public void removeDefaultRetrofit(String baseUrl) {
        retrofitHashMap.remove(baseUrl);
    }

    public void removeAllRetrofit() {
        retrofitHashMap.clear();
    }

    public void setDefaultBaseUrl(String defaultBaseUrl) {
        this.defaultBaseUrl = defaultBaseUrl;
    }

    public Retrofit getDefaultRetrofit() {
        return getRetrofit(defaultBaseUrl);
    }

    private Retrofit createRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        HttpUrl httpUrl = null;
        try {
            httpUrl = HttpUrl.parse(baseUrl);
            if (httpUrl == null) {
                httpUrl = HttpUrl.parse(defaultBaseUrl);
            }
        } catch (Throwable e) {
            Log.d("RetrofitManager", "createRetrofit fail baseUrl error");
        }
        builder.baseUrl(httpUrl == null ? HttpUrl.get("http://failbaseurl") : httpUrl)
                .client(OkHttpManager.INSTANCE.getAppHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(getJacksonConfig()))
                .build();
        return builder.build();
    }

    private ObjectMapper getJacksonConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}