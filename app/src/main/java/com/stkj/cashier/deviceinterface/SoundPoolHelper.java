package com.stkj.cashier.deviceinterface;

import android.media.SoundPool;

import com.stkj.cashier.R;
import com.stkj.cashier.common.core.AppManager;


public enum SoundPoolHelper {
    INSTANCE;
    private SoundPool soundPool;
    private int sound1Id;

    public void playSound1() {
        try {
            if (soundPool == null) {
                soundPool = new SoundPool(10, 3, 3);
                sound1Id = soundPool.load(AppManager.INSTANCE.getApplication(), R.raw.beep, 1);
            }
            soundPool.play(sound1Id, 1.0f, 1.0f, 0, 0, 1.0f);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}