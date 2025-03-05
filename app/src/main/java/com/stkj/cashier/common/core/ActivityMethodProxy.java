package com.stkj.cashier.common.core;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface ActivityMethodProxy {
    default void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }
}
