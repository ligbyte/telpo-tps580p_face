package com.stkj.cashier.ui.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stkj.cashier.R;
import com.stkj.cashier.adapter.CommonSelectItemViewHolder;
import com.stkj.cashier.common.ui.adapter.CommonRecyclerAdapter;
import com.stkj.cashier.common.ui.fragment.BaseDialogFragment;
import com.stkj.cashier.common.ui.widget.shapelayout.ShapeTextView;
import com.stkj.cashier.model.CommonSelectItem;
import com.stkj.cashier.util.util.ToastUtils;

import java.util.List;

/**
 * 通用选择item弹窗
 */
public class CommonSelectDialogFragment extends BaseDialogFragment {

    private TextView tvTitle;
    private RecyclerView rvSelectList;
    private ShapeTextView stvLeftBt;
    private ShapeTextView stvRightBt;
    private String alertTitleTxt;
    private OnSelectListener onSelectListener;
    private CommonRecyclerAdapter mSelectItemAdapter;
    private List<? extends CommonSelectItem> mDataList;

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_common_select;
    }

    @Override
    protected void initViews(View rootView) {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(alertTitleTxt)) {
            tvTitle.setText(alertTitleTxt);
        }
        rvSelectList = (RecyclerView) findViewById(R.id.rv_select_list);
        stvLeftBt = (ShapeTextView) findViewById(R.id.stv_left_bt);
        stvRightBt = (ShapeTextView) findViewById(R.id.stv_right_bt);
        mSelectItemAdapter = new CommonRecyclerAdapter(false);
        mSelectItemAdapter.addViewHolderFactory(new CommonSelectItemViewHolder.Factory());
        mSelectItemAdapter.addItemEventListener(new CommonRecyclerAdapter.OnItemEventListener() {
            @Override
            public void onClickItemView(View view, Object obj) {
                CommonSelectItem selectItem = (CommonSelectItem) obj;
                List<Object> dataList = mSelectItemAdapter.getDataList();
                for (int i = 0; i < dataList.size(); i++) {
                    CommonSelectItem item = (CommonSelectItem) dataList.get(i);
                    item.setSelect(item == selectItem);
                }
                mSelectItemAdapter.notifyDataSetChanged();
            }
        });
        rvSelectList.setAdapter(mSelectItemAdapter);
        stvLeftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonSelectItem commonSelectItem = null;
                List<Object> dataList = mSelectItemAdapter.getDataList();
                for (int i = 0; i < dataList.size(); i++) {
                    CommonSelectItem item = (CommonSelectItem) dataList.get(i);
                    if (item.isSelect()) {
                        commonSelectItem = item;
                    }
                }
                if (commonSelectItem != null) {
                    onSelectListener.onConfirmSelectItem(commonSelectItem);
                    dismiss();
                } else {
                    ToastUtils.showLong("当前未选择");
                }
            }
        });
        stvRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onSelectListener != null) {
                    onSelectListener.onDismiss();
                }
            }
        });
        if (mDataList != null) {
            mSelectItemAdapter.addDataList(mDataList);
        }
    }

    public CommonSelectDialogFragment setSelectListData(List<? extends CommonSelectItem> listData) {
        if (listData != null) {
            mDataList = listData;
            if (mSelectItemAdapter != null) {
                mSelectItemAdapter.removeAllData();
                mSelectItemAdapter.addDataList(listData);
            }
        }
        return this;
    }

    /**
     * 设置弹窗标题
     */
    public CommonSelectDialogFragment setTitle(String alertTitle) {
        this.alertTitleTxt = alertTitle;
        if (tvTitle != null) {
            tvTitle.setText(alertTitle);
        }
        return this;
    }

    public CommonSelectDialogFragment setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
        return this;
    }

    public static CommonSelectDialogFragment build() {
        return new CommonSelectDialogFragment();
    }

    public interface OnSelectListener {
        void onConfirmSelectItem(CommonSelectItem commonSelectItem);

        default void onDismiss() {
        }
    }

}
