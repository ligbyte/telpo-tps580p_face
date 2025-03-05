package com.stkj.cashier.common.ui.widget.zoomimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stkj.cashier.R;


/**
 * touchImage width loading
 */
public class ZoomTouchLoadingLayout extends FrameLayout implements ZoomTouchImage.OnLoadImageListener {

    private ZoomTouchImage mTouchIv;
    private ProgressBar mMaterialPB;

    public ZoomTouchLoadingLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ZoomTouchLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomTouchLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.include_zoom_touch_image, this);
        mTouchIv = (ZoomTouchImage) findViewById(R.id.touch_iv);
        mMaterialPB = (ProgressBar) findViewById(R.id.loading_pb);
    }

    /**
     * 加载图片
     *
     * @param imageUrl 图片url
     */
    public void loadImage(String imageUrl) {
        if (mTouchIv != null) {
            mTouchIv.loadImage(imageUrl, this);
        }
    }

    @Override
    public void onLoadedImage() {
        mMaterialPB.setVisibility(GONE);
    }

    @Override
    public void onLoadFail() {
        mMaterialPB.setVisibility(GONE);
    }

    @Override
    public void onLoadStart() {
        mMaterialPB.setVisibility(VISIBLE);
    }
}
