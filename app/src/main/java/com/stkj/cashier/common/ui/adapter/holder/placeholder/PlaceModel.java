package com.stkj.cashier.common.ui.adapter.holder.placeholder;

import androidx.annotation.LayoutRes;

/**
 * 占位数据信息
 */
public class PlaceModel {

    private @LayoutRes
    int layoutResId;
    private boolean needClickItemEvent;

    public PlaceModel(@LayoutRes int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public PlaceModel(int layoutResId, boolean needClickItemEvent) {
        this.layoutResId = layoutResId;
        this.needClickItemEvent = needClickItemEvent;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public boolean isNeedClickItemEvent() {
        return needClickItemEvent;
    }

    public void setNeedClickItemEvent(boolean needClickItemEvent) {
        this.needClickItemEvent = needClickItemEvent;
    }
}
