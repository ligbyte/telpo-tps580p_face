package com.stkj.cashier.common.core;

import android.app.Activity;

import androidx.annotation.NonNull;


import com.stkj.cashier.util.rxjava.DefaultDisposeObserver;
import com.stkj.cashier.util.rxjava.RxTransformerUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

/**
 * 间隔时间回调
 */
public class CountDownHelper extends ActivityWeakRefHolder {

    //默认一秒钟
    private long intervalTime = 1000;
    private Set<OnCountDownListener> onCountDownListenerSet = new HashSet<>();
    private DefaultDisposeObserver<Long> mCountDownObserver;

    public CountDownHelper(@NonNull Activity activity) {
        super(activity);
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void startCountDown() {
        Activity activityWithCheck = getHolderActivityWithCheck();
        if (activityWithCheck == null) {
            return;
        }
        if (mCountDownObserver == null) {
            mCountDownObserver = new DefaultDisposeObserver<Long>() {
                @Override
                protected void onSuccess(Long aLong) {
                    for (OnCountDownListener countDownListener : onCountDownListenerSet) {
                        countDownListener.onCountDown();
                    }
                }
            };
            Observable.interval(0, intervalTime, TimeUnit.MILLISECONDS)
                    .compose(RxTransformerUtils.mainSchedulers())
                    .subscribe(mCountDownObserver);
        }
    }

    public void addCountDownListener(OnCountDownListener countDownListener) {
        onCountDownListenerSet.add(countDownListener);
    }

    public void removeCountDownListener(OnCountDownListener countDownListener) {
        onCountDownListenerSet.remove(countDownListener);
    }

    public void stopCountDown() {
        if (mCountDownObserver != null) {
            mCountDownObserver.dispose();
            mCountDownObserver = null;
        }
    }

    @Override
    public void onClear() {
        stopCountDown();
        onCountDownListenerSet.clear();
    }

    public interface OnCountDownListener {
        void onCountDown();
    }
}
