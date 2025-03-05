package com.stkj.cashier.common.ui.fragment;

import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.stkj.cashier.R;


/**
 * 应用警告统一弹窗
 */
public class AppAlertDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private TextView mTitleTextView;
    private TextView mContentTextView;
    private TextView mConfirmButton;
    private TextView mCancelButton;
    private View mBottomDivider;

    private boolean mDialogCancelable;
    private String mTitleText;
    private CharSequence mContentText;
    private String mCancelText;
    private String mConfirmText;
    private MovementMethod mContentMovementMethod;

    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;

    public interface OnSweetClickListener {
        void onClick(AppAlertDialogFragment appAlertDialog);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_app_alert;
    }

    @Override
    protected void initViews(View rootView) {
        if (mDialogCancelable) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        mTitleTextView = (TextView) rootView.findViewById(R.id.title_text);
        mContentTextView = (TextView) rootView.findViewById(R.id.content_text);
        mContentTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mConfirmButton = (TextView) rootView.findViewById(R.id.confirm_button);
        mCancelButton = (TextView) rootView.findViewById(R.id.cancel_button);
        mBottomDivider = rootView.findViewById(R.id.bottom_divider);
        initData();
    }

    public AppAlertDialogFragment setDialogCancelable(boolean cancelable) {
        mDialogCancelable = cancelable;
        return this;
    }

    private void initData() {
        setTitleText(mTitleText);
        setContentText(mContentText);
        setContentMovementMethod(mContentMovementMethod);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        mCancelButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    public AppAlertDialogFragment setTitleText(String text) {
        mTitleText = text;
        if (mTitleTextView == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mTitleTextView.setVisibility(View.VISIBLE);
            mTitleTextView.setText(text);
        } else {
            mTitleTextView.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialogFragment setContentText(CharSequence text) {
        mContentText = text;
        if (mContentTextView == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mContentTextView.setVisibility(View.VISIBLE);
            mContentTextView.setText(text);
        } else {
            mContentTextView.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialogFragment setContentMovementMethod(MovementMethod movementMethod) {
        if (movementMethod == null) {
            return this;
        }
        this.mContentMovementMethod = movementMethod;
        if (mContentTextView == null) {
            return this;
        }
        mContentTextView.setMovementMethod(movementMethod);
        return this;
    }

    public AppAlertDialogFragment setCancelText(String text) {
        mCancelText = text;
        if (mCancelButton == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mCancelButton.setVisibility(View.VISIBLE);
            mCancelButton.setText(text);
        } else {
            mCancelButton.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mConfirmText)) {
            mBottomDivider.setVisibility(View.VISIBLE);
        } else {
            mBottomDivider.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialogFragment setConfirmText(String text) {
        mConfirmText = text;
        if (mConfirmButton == null) {
            return this;
        }
        if (!TextUtils.isEmpty(text)) {
            mConfirmButton.setVisibility(View.VISIBLE);
            mConfirmButton.setText(text);
        } else {
            mConfirmButton.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mCancelText)) {
            mBottomDivider.setVisibility(View.VISIBLE);
        } else {
            mBottomDivider.setVisibility(View.GONE);
        }
        return this;
    }

    public AppAlertDialogFragment setCancelClickListener(OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public AppAlertDialogFragment setConfirmClickListener(OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.cancel_button) {
            dismiss();
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(AppAlertDialogFragment.this);
            }
        } else if (viewId == R.id.confirm_button) {
            dismiss();
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(AppAlertDialogFragment.this);
            }
        }
    }

    public static AppAlertDialogFragment build() {
        AppAlertDialogFragment appAlertDialog = new AppAlertDialogFragment();
        return appAlertDialog;
    }
}
