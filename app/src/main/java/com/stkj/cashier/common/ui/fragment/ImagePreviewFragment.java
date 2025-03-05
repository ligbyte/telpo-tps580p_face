package com.stkj.cashier.common.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.stkj.cashier.R;
import com.stkj.cashier.common.ui.widget.zoomimageview.ZoomTouchLoadingLayout;


/**
 * 图片预览页
 */
public class ImagePreviewFragment extends BaseDialogFragment {

    private ZoomTouchLoadingLayout ztllImage;
    private ImageView ivClose;
    private String imageUrl;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_image_preview;
    }

    @Override
    protected void initViews(View rootView) {
        ztllImage = (ZoomTouchLoadingLayout) findViewById(R.id.ztll_image);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(imageUrl)) {
            ztllImage.loadImage(imageUrl);
        }
    }

    public static ImagePreviewFragment build(String imageUrl) {
        ImagePreviewFragment imagePreviewFragment = new ImagePreviewFragment();
        imagePreviewFragment.imageUrl = imageUrl;
        return imagePreviewFragment;
    }

}
