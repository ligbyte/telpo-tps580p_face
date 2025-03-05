package com.stkj.cashier.common.ui.widget.shapeselectlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.stkj.cashier.R;


public class ShapeSelectLayoutHelper {

    private GradientDrawable normalGradientDrawable = new GradientDrawable();
    private int normalCircleRadius;
    private int normalRadius;
    private int normalBottomLeftRadius;
    private int normalBottomRightRadius;
    private int normalTopLeftRadius;
    private int normalTopRightRadius;
    private int normalSolidColor;
    private int normalStrokeColor;
    private int normalStrokeWidth;
    private int normalDashWidth;
    private int normalDashGap;

    //选中状态下
    private GradientDrawable selectGradientDrawable;
    private int selectCircleRadius;
    private int selectRadius;
    private int selectBottomLeftRadius;
    private int selectBottomRightRadius;
    private int selectTopLeftRadius;
    private int selectTopRightRadius;
    private int selectSolidColor;
    private int selectStrokeColor;
    private int selectStrokeWidth;
    private int selectDashWidth;
    private int selectDashGap;
    private boolean shapeSelect = false;
    private int shapeNormalColor;
    private int shapeSelectColor;

    public void init(Context context, AttributeSet attributeSet, View view) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeSelectLayoutHelper);
        normalCircleRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalCircleRadiusT, 0);
        normalRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalRadiusT, 0);
        normalBottomLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalBottomLeftRadiusT, 0);
        normalBottomRightRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalBottomRightRadiusT, 0);
        normalTopLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalTopLeftRadiusT, 0);
        normalTopRightRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalTopRightRadiusT, 0);
        normalSolidColor = a.getColor(R.styleable.ShapeSelectLayoutHelper_normalSolidColorT, Color.TRANSPARENT);
        normalStrokeColor = a.getColor(R.styleable.ShapeSelectLayoutHelper_normalStrokeColorT, Color.TRANSPARENT);
        normalStrokeWidth = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalStrokeWidthT, 0);
        normalDashWidth = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalDashWidthT, 0);
        normalDashGap = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_normalDashGapT, 0);
        //选中状态
        selectCircleRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectCircleRadiusT, 0);
        selectRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectRadiusT, 0);
        selectBottomLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectBottomLeftRadiusT, 0);
        selectBottomRightRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectBottomRightRadiusT, 0);
        selectTopLeftRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectTopLeftRadiusT, 0);
        selectTopRightRadius = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectTopRightRadiusT, 0);
        selectSolidColor = a.getColor(R.styleable.ShapeSelectLayoutHelper_selectSolidColorT, Color.TRANSPARENT);
        selectStrokeColor = a.getColor(R.styleable.ShapeSelectLayoutHelper_selectStrokeColorT, Color.TRANSPARENT);
        selectStrokeWidth = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectStrokeWidthT, 0);
        selectDashWidth = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectDashWidthT, 0);
        selectDashGap = a.getDimensionPixelSize(R.styleable.ShapeSelectLayoutHelper_selectDashGapT, 0);
        shapeSelect = a.getBoolean(R.styleable.ShapeSelectLayoutHelper_shapeSelect, false);
        shapeNormalColor = a.getColor(R.styleable.ShapeSelectLayoutHelper_shapeNormalColor, Color.TRANSPARENT);
        shapeSelectColor = a.getColor(R.styleable.ShapeSelectLayoutHelper_shapeSelectColor, Color.TRANSPARENT);
        a.recycle();
        view.setWillNotDraw(false);
    }


    public int getShapeNormalColor() {
        return shapeNormalColor;
    }

    public void setShapeNormalColor(int shapeNormalColor) {
        this.shapeNormalColor = shapeNormalColor;
    }

    public int getShapeSelectColor() {
        return shapeSelectColor;
    }

    public void setShapeSelectColor(int shapeSelectColor) {
        this.shapeSelectColor = shapeSelectColor;
    }

    public boolean isShapeSelect() {
        return shapeSelect;
    }

    public void setNormalRadius(int normalRadius) {
        this.normalRadius = normalRadius;
    }

    public void setNormalRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        this.normalTopLeftRadius = topLeftRadius;
        this.normalTopRightRadius = topRightRadius;
        this.normalBottomLeftRadius = bottomLeftRadius;
        this.normalBottomRightRadius = bottomRightRadius;
    }

    public void setNormalStrokeColor(int color) {
        this.normalStrokeColor = color;
    }

    public void setNormalStrokeWidth(int width) {
        this.normalStrokeWidth = width;
    }

    public void setNormalSolidColor(int normalSolidColor) {
        this.normalSolidColor = normalSolidColor;
    }

    public void setNormalDashGap(int normalDashGap) {
        this.normalDashGap = normalDashGap;
    }

    public void setNormalDashWidth(int normalDashWidth) {
        this.normalDashWidth = normalDashWidth;
    }

    public void setSelectRadius(int radius) {
        this.selectRadius = radius;
    }

    public void setSelectRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        this.selectTopLeftRadius = topLeftRadius;
        this.selectTopRightRadius = topRightRadius;
        this.selectBottomLeftRadius = bottomLeftRadius;
        this.selectBottomRightRadius = bottomRightRadius;
    }

    public void setSelectStrokeColor(int color) {
        this.selectStrokeColor = color;
    }

    public void setSelectStrokeWidth(int width) {
        this.selectStrokeWidth = width;
    }

    public void setSelectSolidColor(int solidColor) {
        this.selectSolidColor = solidColor;
    }

    public void setSelectDashGap(int dashGap) {
        this.selectDashGap = dashGap;
    }

    public void setSelectDashWidth(int dashWidth) {
        this.selectDashWidth = dashWidth;
    }

    public void setShapeSelect(boolean shapeSelect) {
        this.shapeSelect = shapeSelect;
    }

    public void draw(Canvas canvas, View view) {
        if (canvas == null || view == null) {
            return;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (shapeSelect) {
            if (selectGradientDrawable == null) {
                selectGradientDrawable = new GradientDrawable();
            }
            if (selectCircleRadius > 0) {
                selectGradientDrawable.setShape(GradientDrawable.OVAL);
                int circleSize = selectCircleRadius * 2;
                selectGradientDrawable.setSize(circleSize, circleSize);
                selectGradientDrawable.setBounds(0, 0, circleSize, circleSize);
            } else {
                selectGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                if (selectRadius != 0) {
                    selectGradientDrawable.setCornerRadius(selectRadius);
                } else {
                    selectGradientDrawable.setCornerRadii(new float[]{selectTopLeftRadius, selectTopLeftRadius, selectTopRightRadius, selectTopRightRadius, selectBottomRightRadius, selectBottomRightRadius, selectBottomLeftRadius, selectBottomLeftRadius});
                }
                selectGradientDrawable.setBounds(0, 0, view.getWidth(), view.getHeight());
            }
            selectGradientDrawable.setColor(selectSolidColor);
            selectGradientDrawable.setStroke(selectStrokeWidth, selectStrokeColor, selectDashWidth, selectDashGap);
            selectGradientDrawable.draw(canvas);
        } else {
            if (normalCircleRadius > 0) {
                normalGradientDrawable.setShape(GradientDrawable.OVAL);
                int circleSize = normalCircleRadius * 2;
                normalGradientDrawable.setSize(circleSize, circleSize);
                normalGradientDrawable.setBounds(0, 0, circleSize, circleSize);
            } else {
                normalGradientDrawable.setShape(GradientDrawable.RECTANGLE);
                if (normalRadius != 0) {
                    normalGradientDrawable.setCornerRadius(normalRadius);
                } else {
                    normalGradientDrawable.setCornerRadii(new float[]{normalTopLeftRadius, normalTopLeftRadius, normalTopRightRadius, normalTopRightRadius, normalBottomRightRadius, normalBottomRightRadius, normalBottomLeftRadius, normalBottomLeftRadius});
                }
                normalGradientDrawable.setBounds(0, 0, view.getWidth(), view.getHeight());
            }
            normalGradientDrawable.setColor(normalSolidColor);
            normalGradientDrawable.setStroke(normalStrokeWidth, normalStrokeColor, normalDashWidth, normalDashGap);
            normalGradientDrawable.draw(canvas);
        }
    }

    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;

    public void measuredDimension(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        if (normalCircleRadius > 0) {
            int newMeasureSpec = View.MeasureSpec.makeMeasureSpec(normalCircleRadius * 2, View.MeasureSpec.EXACTLY);
            mWidthMeasureSpec = newMeasureSpec;
            mHeightMeasureSpec = newMeasureSpec;
        }
    }

    public int getWidthMeasureSpec() {
        return mWidthMeasureSpec;
    }

    public int getHeightMeasureSpec() {
        return mHeightMeasureSpec;
    }

}
