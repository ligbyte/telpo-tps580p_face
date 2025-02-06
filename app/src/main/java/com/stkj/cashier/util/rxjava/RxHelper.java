package com.stkj.cashier.util.rxjava;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;

//import com.jakewharton.rxbinding4.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import kotlin.Unit;

public class RxHelper {

    public static void init() {
        try {
            RxJavaPlugins.setErrorHandler(Functions.emptyConsumer());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击防抖动
     */
    public static void clickThrottle(View view, long milliSeconds, LifecycleOwner lifecycleOwner, DefaultObserver<Unit> defaultObserver) {
//        RxView.clicks(view)
//                .throttleFirst(milliSeconds, TimeUnit.MILLISECONDS)
//                .to(AutoDisposeUtils.onDestroyDispose(lifecycleOwner))
//                .subscribe(defaultObserver);
    }


    public static void clickThrottle(View view, LifecycleOwner lifecycleOwner, DefaultObserver<Unit> defaultObserver) {
        clickThrottle(view, 500, lifecycleOwner, defaultObserver);
    }

}
