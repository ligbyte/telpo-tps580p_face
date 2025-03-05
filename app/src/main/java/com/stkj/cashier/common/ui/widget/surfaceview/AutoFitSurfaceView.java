/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stkj.cashier.common.ui.widget.surfaceview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * A {@link SurfaceView} that can be adjusted to a specified aspect ratio and
 * performs center-crop transformation of input frames.
 */
public class AutoFitSurfaceView extends SurfaceView {

    private static final String TAG = AutoFitSurfaceView.class.getSimpleName();

    private float aspectRatio = 0f;

    public AutoFitSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoFitSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitSurfaceView(Context context) {
        super(context);
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be
     * measured based on the ratio calculated from the parameters. Note that
     * the actual sizes of parameters don't matter, that is, calling
     * setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        Log.d(TAG, "setAspectRatio width: " + width + " height " + height);
        aspectRatio = (float) width / height;
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (aspectRatio == 0f) {
            setMeasuredDimension(width, height);
        } else {
            // Performs center-crop transformation of the camera frames
            int newWidth;
            int newHeight;
            float actualRatio = (width > height) ? aspectRatio : 1f / aspectRatio;
            if (width < height * actualRatio) {
                newHeight = height;
                newWidth = Math.round(height * actualRatio);
            } else {
                newWidth = width;
                newHeight = Math.round(width / actualRatio);
            }

            Log.d(TAG, "Measured dimensions set: " + newWidth + " x " + newHeight);
            setMeasuredDimension(newWidth, newHeight);
            if (autoFitSurfaceListener != null) {
                autoFitSurfaceListener.onMeasuredDimension(newWidth, newHeight);
            }
        }
    }

    private OnAutoFitSurfaceListener autoFitSurfaceListener;

    public void setAutoFitSurfaceListener(OnAutoFitSurfaceListener autoFitSurfaceListener) {
        this.autoFitSurfaceListener = autoFitSurfaceListener;
    }

    public interface OnAutoFitSurfaceListener {
        void onMeasuredDimension(int width, int height);
    }
}