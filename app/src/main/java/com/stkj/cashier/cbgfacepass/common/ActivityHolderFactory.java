package com.stkj.cashier.cbgfacepass.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stkj.cashier.app.base.BaseActivity;


public class ActivityHolderFactory {

    public static @Nullable
    <T extends ActivityWeakRefHolder> T get(@NonNull Class<T> tClass, @NonNull Context context) {
        if (context instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) context;
            return (T) baseActivity.getWeakRefHolder(tClass);
        }
        return null;
    }

}
