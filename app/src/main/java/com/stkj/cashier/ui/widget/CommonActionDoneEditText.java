package com.stkj.cashier.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 通用监听editText
 */
public class CommonActionDoneEditText extends AppCompatEditText {
    private OnEditWatchListener onEditWatchListener;

    public CommonActionDoneEditText(Context context) {
        super(context);
        init(context);
    }

    public CommonActionDoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonActionDoneEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (onEditWatchListener != null) {
                    onEditWatchListener.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onEditWatchListener != null) {
                    onEditWatchListener.onAfterTextChanged(s.toString());
                }
            }
        });
        setSingleLine(true);
        setImeOptions(EditorInfo.IME_ACTION_DONE);
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (onEditWatchListener != null) {
                        onEditWatchListener.onActionDone();
                    }
                    return true;
                }
                return false;
            }
        });
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onEditWatchListener != null) {
                    onEditWatchListener.onFocusChange(hasFocus);
                }
            }
        });
    }

    public void setOnEditWatchListener(OnEditWatchListener onEditWatchListener) {
        this.onEditWatchListener = onEditWatchListener;
    }

    public interface OnEditWatchListener {
        default void onAfterTextChanged(String s) {
        }

        default void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        default void onActionDone() {
        }

        default void onFocusChange(boolean hasFocus) {
        }
    }

}
