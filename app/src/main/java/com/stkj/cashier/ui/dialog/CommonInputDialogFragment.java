package com.stkj.cashier.ui.dialog;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.stkj.cashier.R;
import com.stkj.cashier.common.ui.fragment.BaseDialogFragment;
import com.stkj.cashier.common.ui.widget.shapelayout.ShapeTextView;
import com.stkj.cashier.common.utils.KeyBoardUtils;
import com.stkj.cashier.ui.widget.CommonActionDoneEditText;
import com.stkj.cashier.ui.widget.DoubleLimitInputFilter;

/**
 * 公用输入弹窗fragment
 */
public class CommonInputDialogFragment extends BaseDialogFragment {

    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_NUMBER_DECIMAL = 2;

    private TextView tvTitle;
    private CommonActionDoneEditText etInput;
    private String alertTitleTxt;
    private String mInputContent;
    private int mInputType;
    private int mMaxInputNumber = 9999;
    private int mMinInputNumber = 0;
    private boolean needLimitNumber;
    private OnInputListener onInputListener;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_common_input;
    }

    @Override
    protected void initViews(View rootView) {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(alertTitleTxt)) {
            tvTitle.setText(alertTitleTxt);
        }
        etInput = (CommonActionDoneEditText) findViewById(R.id.et_input);
        if (mInputType == INPUT_TYPE_NUMBER) {
            etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_NORMAL);
        } else if (mInputType == INPUT_TYPE_NUMBER_DECIMAL) {
            etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (!TextUtils.isEmpty(mInputContent)) {
            etInput.setText(mInputContent);
            etInput.setSelection(mInputContent.length());
            mInputContent = null;
        }
        etInput.requestFocus();
        KeyBoardUtils.showSoftKeyboard(mActivity, etInput);
        if (needLimitNumber && mInputType != 0) {
            etInput.setFilters(new InputFilter[]{new DoubleLimitInputFilter(mMinInputNumber, mMaxInputNumber)});
        }
        ShapeTextView stvLeftBt = (ShapeTextView) findViewById(R.id.stv_left_bt);
        ShapeTextView stvRightBt = (ShapeTextView) findViewById(R.id.stv_right_bt);
        stvLeftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEndInput();
            }
        });
        stvRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onInputListener != null) {
                    onInputListener.onDismiss();
                }
            }
        });
        etInput.setOnEditWatchListener(new CommonActionDoneEditText.OnEditWatchListener() {
            @Override
            public void onAfterTextChanged(String s) {
                if (onInputListener != null) {
                    onInputListener.onAfterInput(s);
                }
            }

            @Override
            public void onActionDone() {
                onEndInput();
            }
        });
    }

    @Override
    public void dismiss() {
        KeyBoardUtils.hideSoftKeyboard(mActivity, etInput);
        super.dismiss();
    }

    private void onEndInput() {
        dismiss();
        if (onInputListener != null) {
            String endInput = etInput.getText().toString();
            onInputListener.onInputEnd(endInput);
        }
    }

    /**
     * 设置弹窗标题
     */
    public CommonInputDialogFragment setTitle(String alertTitle) {
        this.alertTitleTxt = alertTitle;
        if (tvTitle != null) {
            tvTitle.setText(alertTitle);
        }
        return this;
    }

    /**
     * 设置弹窗内容
     */
    public CommonInputDialogFragment setInputContent(String alertContent) {
        this.mInputContent = alertContent;
        if (etInput != null) {
            if (!TextUtils.isEmpty(alertContent)) {
                etInput.setText(alertContent);
                mInputContent = null;
            }
        }
        return this;
    }

    public CommonInputDialogFragment setInputType(int mInputType) {
        this.mInputType = mInputType;
        if (etInput != null) {
            if (mInputType == INPUT_TYPE_NUMBER) {
                etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_NORMAL);
            } else if (mInputType == INPUT_TYPE_NUMBER_DECIMAL) {
                etInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            }
        }
        return this;
    }

    public CommonInputDialogFragment setInputNumberRange(int mMinInputNumber, int mMaxInputNumber) {
        this.mMinInputNumber = mMinInputNumber;
        this.mMaxInputNumber = mMaxInputNumber;
        return this;
    }

    public CommonInputDialogFragment setNeedLimitNumber(boolean needLimitNumber) {
        this.needLimitNumber = needLimitNumber;
        return this;
    }

    public static CommonInputDialogFragment build() {
        return new CommonInputDialogFragment();
    }

    public CommonInputDialogFragment setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
        return this;
    }

    public interface OnInputListener {
        void onInputEnd(String input);

        default void onAfterInput(String input) {
        }

        default void onDismiss() {
        }
    }

}
