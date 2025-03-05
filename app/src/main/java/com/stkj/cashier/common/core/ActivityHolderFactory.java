package com.stkj.cashier.common.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stkj.cashier.app.base.BaseActivity;


public class ActivityHolderFactory {

    public static @Nullable
    <T extends ActivityWeakRefHolder> ActivityWeakRefHolder get(@NonNull Class<T> tClass, @NonNull Context context) {
        if (context instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) context;
            return baseActivity.getWeakRefHolder(tClass);
        }
        return null;
    }

}
