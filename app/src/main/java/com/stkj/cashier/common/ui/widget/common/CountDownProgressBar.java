package com.stkj.cashier.common.ui.widget.common;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;


import com.stkj.cashier.R;
import com.stkj.cashier.util.rxjava.DefaultDisposeObserver;
import com.stkj.cashier.util.rxjava.RxTransformerUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

/**
 * 圆形倒计时进度
 */
public class CountDownProgressBar extends View {

    public final static String TAG = "CountDownProgressBar";
    private String mSkipTxt;
    private boolean mShowCountDownTime;
    private int mTotalCountDownTime;
    private int mCurrentCountDownTime;
    private int mBgColor;
    private int mProColor;
    private int mProWidth;
    private int mCurrentPro;
    private int mCurrentCirclePro;
    private RectF mCircleRectF;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private DefaultDisposeObserver<Long> mCountDownCallback;
    private int measureWidthHeight;
    private OnCountDownListener mCountDownListener;

    public CountDownProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CountDownProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownProgressBar);
        mBgColor = a.getColor(R.styleable.CountDownProgressBar_cdpb_bg_color, 0xbbaaaaaa);
        mProColor = a.getColor(R.styleable.CountDownProgressBar_cdpb_pro_color, 0xffffffff);
        mProWidth = a.getDimensionPixelSize(R.styleable.CountDownProgressBar_cdpb_pro_width, 0);
        mSkipTxt = a.getString(R.styleable.CountDownProgressBar_cdpb_skip_text);
        int textSize = a.getDimensionPixelSize(R.styleable.CountDownProgressBar_cdpb_count_down_text_size, 0);
        int textColor = a.getColor(R.styleable.CountDownProgressBar_cdpb_count_down_text_color, 0xffffffff);
        mShowCountDownTime = a.getBoolean(R.styleable.CountDownProgressBar_cdpb_show_count_down_time, false);
        mTotalCountDownTime = a.getInt(R.styleable.CountDownProgressBar_cdpb_total_count_down_time, 0);
        a.recycle();
        Resources resources = getResources();
        if (textSize == 0) {
            textSize = resources.getDimensionPixelSize(R.dimen.sp_12);
        }
        if (mProWidth == 0) {
            mProWidth = resources.getDimensionPixelSize(R.dimen.dp_2);
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mProWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        if (TextUtils.isEmpty(mSkipTxt)) {
            mSkipTxt = "跳过";
        }
        measureWidthHeight = (int) (mTextPaint.measureText(mSkipTxt) + resources.getDimensionPixelSize(R.dimen.dp_15) * 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureSpec = MeasureSpec.makeMeasureSpec(measureWidthHeight, MeasureSpec.EXACTLY);
        super.onMeasure(measureSpec, measureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (measureWidthHeight <= 0) {
            return;
        }
        if (mCircleRectF == null) {
            mCircleRectF = new RectF(mProWidth, mProWidth, measureWidthHeight - mProWidth, measureWidthHeight - mProWidth);
        }
        //draw background
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mBgColor);
        canvas.drawCircle(mCircleRectF.centerX(), mCircleRectF.centerY(), mCircleRectF.width() / 2, mPaint);
        //draw progress
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mProColor);
        canvas.drawArc(mCircleRectF, 270, mCurrentCirclePro, false, mPaint);
        //draw text
        float textY = mCircleRectF.centerY() - (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        canvas.drawText(mShowCountDownTime ? String.valueOf(mCurrentCountDownTime) : mSkipTxt, mCircleRectF.centerX(), textY, mTextPaint);
    }

    public void setTotalCountDownTime(int countDownTime) {
        mTotalCountDownTime = countDownTime;
    }

    public void setShowCountDownTime(boolean mShowCountDownTime) {
        this.mShowCountDownTime = mShowCountDownTime;
    }

    public void setCountDownListener(OnCountDownListener mCountDownListener) {
        this.mCountDownListener = mCountDownListener;
    }

    public void startCountDown() {
        stopCountDown();
        if (mTotalCountDownTime <= 0) {
            return;
        }
        mCurrentPro = 0;
        mCurrentCountDownTime = mTotalCountDownTime;
        int mAnimDuration = mTotalCountDownTime * 1000;
        //20ms刷新一次
        int intervalTime = 20;
        //总共需要刷新的次数
        int totalInterval = mAnimDuration / intervalTime;
        mCountDownCallback = new DefaultDisposeObserver<Long>() {
            @Override
            protected void onSuccess(@NonNull Long aLong) {
                mCurrentPro += 1;
                float percent = mCurrentPro * 1.0f / totalInterval;
                mCurrentCirclePro = (int) (360 * (1 - percent));
                mCurrentCountDownTime = (int) Math.ceil(mTotalCountDownTime * (1 - percent));
                Log.d(TAG, "percent = " + percent
                        + " mCurrentPro = " + mCurrentPro +
                        " mCurrentCirclePro = " + mCurrentCirclePro
                        + " mCurrentCountDownTime = " + mCurrentCountDownTime);
                if (mCurrentPro == totalInterval) {
                    if (mCountDownListener != null) {
                        mCountDownListener.onFinish();
                    }
                }
                invalidate();
            }
        };
        Observable.intervalRange(0, totalInterval, 0, intervalTime, TimeUnit.MILLISECONDS)
                .compose(RxTransformerUtils.mainSchedulers())
                .subscribe(mCountDownCallback);
    }

    public void stopCountDown() {
        if (mCountDownCallback != null) {
            mCountDownCallback.dispose();
            mCountDownCallback = null;
            mCountDownListener = null;
        }
    }

    public interface OnCountDownListener {
        default void onFinish() {
        }
    }

}