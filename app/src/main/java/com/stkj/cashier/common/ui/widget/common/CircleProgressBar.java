package com.stkj.cashier.common.ui.widget.common;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stkj.cashier.R;


/**
 * 圆形进度
 */
public class CircleProgressBar extends View {

    private int mBgColor;
    private int mProColor;
    private int mProWidth;
    private int mCurrentCirclePro;
    private RectF mCircleRectF;
    private Paint mPaint;

    public CircleProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mBgColor = a.getColor(R.styleable.CircleProgressBar_cbp_bg_color, 0xbbaaaaaa);
        mProColor = a.getColor(R.styleable.CircleProgressBar_cbp_pro_color, 0xffffffff);
        mProWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_cbp_pro_width, 0);
        mCurrentCirclePro = a.getInteger(R.styleable.CircleProgressBar_cbp_current_pro, 0);
        a.recycle();
        Resources resources = getResources();
        if (mProWidth == 0) {
            mProWidth = resources.getDimensionPixelSize(R.dimen.dp_2);
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProWidth);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight <= 0 || measuredWidth <= 0) {
            return;
        }
        int minCircleSize = Math.min(measuredWidth, measuredHeight);
        if (mCircleRectF == null) {
            mCircleRectF = new RectF(mProWidth, mProWidth, minCircleSize - mProWidth, minCircleSize - mProWidth);
        }
        //draw background
        mPaint.setColor(mBgColor);
        canvas.drawCircle(mCircleRectF.centerX(), mCircleRectF.centerY(), mCircleRectF.width() / 2, mPaint);
        //draw progress
        mPaint.setColor(mProColor);
        canvas.drawArc(mCircleRectF, 270, mCurrentCirclePro, false, mPaint);
    }

    public void setProgress(int progress) {
        this.mCurrentCirclePro = progress;
        postInvalidate();
    }

}