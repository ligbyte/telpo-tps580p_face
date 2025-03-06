package com.stkj.cashier.common.utils;


import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {

    public static void registerEventBus(Object object) {
        try {
            if (!EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().register(object);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void unRegisterEventBus(Object object) {
        try {
            EventBus.getDefault().unregister(object);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
