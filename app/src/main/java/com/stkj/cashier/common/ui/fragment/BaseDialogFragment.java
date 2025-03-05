package com.stkj.cashier.common.ui.fragment;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.stkj.cashier.app.base.BaseActivity;
import com.stkj.cashier.common.utils.FragmentUtils;


public abstract class BaseDialogFragment extends BaseRecyclerFragment {

    public final static String TAG = "BaseDialogFragment";
    private OnDismissListener onDismissListener;

    public void dismiss() {
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
        FragmentManager parentFragmentManager = null;
        try {
            parentFragmentManager = getParentFragmentManager();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (parentFragmentManager == null) {
            Log.d(TAG,"BaseDialogFragment--dismiss--getParentFragmentManager()" + this.getClass().getName());
            return;
        }
        Log.d(TAG,"BaseDialogFragment--dismiss--" + this.getClass().getName());
        FragmentUtils.safeRemoveFragment(getParentFragmentManager(), this);
    }

    public void show(Context context) {

        if (context instanceof BaseActivity) {
            BaseActivity commonActivity = (BaseActivity) context;
            commonActivity.addContentPlaceHolderFragment(this);
            Log.d(TAG,"BaseDialogFragment--show--addContentPlaceHolderFragment()" + this.getClass().getName());
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

}
