package com.stkj.cashier.common.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.stkj.cashier.R;


/**
 * 应用统一loading弹窗
 */
public class AppLoadingDialogFragment extends BaseDialogFragment {

    private int styleId;
    private TextView tvLoading;
    private String mLoadingText;

    @Override
    protected int getLayoutResId() {
        if (styleId == 1) {
            return R.layout.dialog_app_loading_s1;
        }
        return R.layout.dialog_app_loading;
    }

    @Override
    protected void initViews(View rootView) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        if (TextUtils.isEmpty(mLoadingText)) {
            tvLoading.setText("");
            tvLoading.setVisibility(View.GONE);
        } else {
            tvLoading.setText(mLoadingText);
            tvLoading.setVisibility(View.VISIBLE);
        }
    }

    public AppLoadingDialogFragment setLoadingText(String loadingText) {
        mLoadingText = loadingText;
        if (tvLoading != null) {
            if (TextUtils.isEmpty(mLoadingText)) {
                tvLoading.setText("");
                tvLoading.setVisibility(View.GONE);
            } else {
                tvLoading.setText(mLoadingText);
                tvLoading.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public static AppLoadingDialogFragment build(int styleId) {
        AppLoadingDialogFragment appLoadingDialogFragment = new AppLoadingDialogFragment();
        appLoadingDialogFragment.styleId = styleId;
        return appLoadingDialogFragment;
    }

}
