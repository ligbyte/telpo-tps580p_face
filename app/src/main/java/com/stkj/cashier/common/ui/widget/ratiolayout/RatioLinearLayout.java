package com.stkj.cashier.common.ui.widget.ratiolayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RatioLinearLayout extends LinearLayout {

    private RatioLayoutHelper layoutHelper;

    public RatioLinearLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RatioLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        layoutHelper = new RatioLayoutHelper();
        layoutHelper.readRatioAttr(context, attributeSet);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutHelper.measuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(layoutHelper.getWidthMeasureSpec(), layoutHelper.getHeightMeasureSpec());
    }

    public void setRatioWH(float mRatioWH) {
        layoutHelper.setRatioWH(mRatioWH);
        requestLayout();
    }
}
