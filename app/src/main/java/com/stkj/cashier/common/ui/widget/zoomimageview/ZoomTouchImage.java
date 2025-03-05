package com.stkj.cashier.common.ui.widget.zoomimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.stkj.cashier.glide.GlideApp;

/**
 * 缩放ImageView
 */
public class ZoomTouchImage extends TouchImageView {

    private OnLoadImageListener mLoadingListener;

    public ZoomTouchImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomTouchImage(Context context) {
        super(context);
    }

    /**
     * 加载图片
     *
     * @param imgUrl 图片地址
     */
    public void loadImage(String imgUrl, OnLoadImageListener loadingListener) {
        this.mLoadingListener = loadingListener;
        GlideApp.with(getContext()).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                setImageBitmap(resource);
                if (mLoadingListener != null) {
                    mLoadingListener.onLoadedImage();
                }
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                if (mLoadingListener != null) {
                    mLoadingListener.onLoadStart();
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (mLoadingListener != null) {
                    mLoadingListener.onLoadFail();
                }
            }
        });
    }

    public interface OnLoadImageListener {
        void onLoadedImage();

        void onLoadFail();

        void onLoadStart();
    }

}
