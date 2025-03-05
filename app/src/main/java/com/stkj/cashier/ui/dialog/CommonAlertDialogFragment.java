package com.stkj.cashier.ui.dialog;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.stkj.cashier.R;
import com.stkj.cashier.common.ui.fragment.BaseDialogFragment;
import com.stkj.cashier.common.ui.widget.shapelayout.ShapeTextView;

/**
 * 公用弹窗fragment
 */
public class CommonAlertDialogFragment extends BaseDialogFragment {

    private TextView tvTitle;
    private TextView tvAlertContent;
    private ShapeTextView stvLeftBt;
    private ShapeTextView stvRightBt;
    private boolean needHandleDismiss;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_common_alert;
    }

    @Override
    protected void initViews(View rootView) {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(alertTitleTxt)) {
            tvTitle.setText(alertTitleTxt);
        }
        tvAlertContent = (TextView) findViewById(R.id.tv_alert_content);
        tvAlertContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (!TextUtils.isEmpty(alertContentTxt)) {
            tvAlertContent.setText(alertContentTxt);
        }
        stvLeftBt = (ShapeTextView) findViewById(R.id.stv_left_bt);
        stvRightBt = (ShapeTextView) findViewById(R.id.stv_right_bt);
        if (!TextUtils.isEmpty(leftNavTxt)) {
            stvLeftBt.setText(leftNavTxt);
        }
        stvLeftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!needHandleDismiss) {
                    dismiss();
                }
                if (mLeftNavClickListener != null) {
                    mLeftNavClickListener.onClick(CommonAlertDialogFragment.this);
                }
            }
        });
        if (!TextUtils.isEmpty(rightNavTxt)) {
            stvRightBt.setVisibility(View.VISIBLE);
            stvRightBt.setText(rightNavTxt);
        } else {
            stvRightBt.setVisibility(View.GONE);
        }
        stvRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!needHandleDismiss) {
                    dismiss();
                }
                if (mRightNavClickListener != null) {
                    mRightNavClickListener.onClick(CommonAlertDialogFragment.this);
                }
            }
        });
    }

    public CommonAlertDialogFragment setNeedHandleDismiss(boolean needHandleDismiss) {
        this.needHandleDismiss = needHandleDismiss;
        return this;
    }

    private OnSweetClickListener mRightNavClickListener;
    private OnSweetClickListener mLeftNavClickListener;

    private String leftNavTxt;

    /**
     * 设置左侧按钮文案
     */
    public CommonAlertDialogFragment setLeftNavTxt(String leftNavTxt) {
        this.leftNavTxt = leftNavTxt;
        if (stvLeftBt != null) {
            stvLeftBt.setText(leftNavTxt);
        }
        return this;
    }

    private String rightNavTxt;

    /**
     * 设置右侧按钮文案
     */
    public CommonAlertDialogFragment setRightNavTxt(String rightNavTxt) {
        this.rightNavTxt = rightNavTxt;
        if (stvRightBt != null) {
            if (!TextUtils.isEmpty(rightNavTxt)) {
                stvRightBt.setVisibility(View.VISIBLE);
                stvRightBt.setText(rightNavTxt);
            } else {
                stvRightBt.setVisibility(View.GONE);
            }
        }
        return this;
    }

    private String alertTitleTxt;

    /**
     * 设置弹窗标题
     */
    public CommonAlertDialogFragment setAlertTitleTxt(String alertTitle) {
        this.alertTitleTxt = alertTitle;
        if (tvTitle != null) {
            tvTitle.setText(alertTitle);
        }
        return this;
    }

    private String alertContentTxt;

    /**
     * 设置弹窗内容
     */
    public CommonAlertDialogFragment setAlertContentTxt(String alertContent) {
        this.alertContentTxt = alertContent;
        if (tvAlertContent != null) {
            if (!TextUtils.isEmpty(alertContent)) {
                tvAlertContent.setVisibility(View.VISIBLE);
                tvAlertContent.setText(alertContent);
            } else {
                tvAlertContent.setVisibility(View.GONE);
            }
        }
        return this;
    }

    /**
     * 设置右侧按钮点击事件
     */
    public CommonAlertDialogFragment setRightNavClickListener(OnSweetClickListener listener) {
        mRightNavClickListener = listener;
        return this;
    }

    /**
     * 设置左侧按钮点击事件
     */
    public CommonAlertDialogFragment setLeftNavClickListener(OnSweetClickListener listener) {
        mLeftNavClickListener = listener;
        return this;
    }

    public interface OnSweetClickListener {
        void onClick(CommonAlertDialogFragment alertDialogFragment);
    }

    public static CommonAlertDialogFragment build() {
        return new CommonAlertDialogFragment();
    }
}
