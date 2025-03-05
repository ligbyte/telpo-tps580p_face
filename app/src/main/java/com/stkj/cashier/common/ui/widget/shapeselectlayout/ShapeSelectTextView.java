package com.stkj.cashier.common.ui.widget.shapeselectlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 自定义的可以设置背景
 */
public class ShapeSelectTextView extends AppCompatTextView {

    private ShapeSelectLayoutHelper layoutHelper;

    public ShapeSelectTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ShapeSelectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeSelectTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        layoutHelper = new ShapeSelectLayoutHelper();
        layoutHelper.init(context, attributeSet, this);
        boolean shapeSelect = layoutHelper.isShapeSelect();
        refreshTextColor(shapeSelect);
    }

    private void refreshTextColor(boolean shapeSelect) {
        if (shapeSelect) {
            int shapeSelectColor = layoutHelper.getShapeSelectColor();
            if (shapeSelectColor != 0) {
                setTextColor(shapeSelectColor);
            }
        } else {
            int shapeNormalColor = layoutHelper.getShapeNormalColor();
            if (shapeNormalColor != 0) {
                setTextColor(shapeNormalColor);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        layoutHelper.measuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(layoutHelper.getWidthMeasureSpec(), layoutHelper.getHeightMeasureSpec());
    }

    public void setNormalRadius(int radius) {
        layoutHelper.setNormalRadius(radius);
        postInvalidate();
    }

    public void setNormalRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        layoutHelper.setNormalRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        postInvalidate();
    }

    public void setStrokeColor(int color) {
        layoutHelper.setNormalStrokeColor(color);
        postInvalidate();
    }

    public void setStrokeWidth(int width) {
        layoutHelper.setNormalStrokeWidth(width);
        postInvalidate();
    }

    public void setSolidColor(int solidColor) {
        layoutHelper.setNormalSolidColor(solidColor);
        postInvalidate();
    }

    public void setDashGap(int dashGap) {
        layoutHelper.setNormalDashGap(dashGap);
        postInvalidate();
    }

    public void setDashWidth(int dashWidth) {
        layoutHelper.setNormalDashWidth(dashWidth);
        postInvalidate();
    }


    public void setSelectRadius(int radius) {
        layoutHelper.setSelectRadius(radius);
        postInvalidate();
    }

    public void setSelectRadius(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        layoutHelper.setSelectRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        postInvalidate();
    }

    public void setSelectStrokeColor(int color) {
        layoutHelper.setSelectStrokeColor(color);
        postInvalidate();
    }

    public void setSelectStrokeWidth(int width) {
        layoutHelper.setSelectStrokeWidth(width);
        postInvalidate();
    }

    public void setSelectSolidColor(int solidColor) {
        layoutHelper.setSelectSolidColor(solidColor);
        postInvalidate();
    }

    public void setSelectDashGap(int dashGap) {
        layoutHelper.setSelectDashGap(dashGap);
        postInvalidate();
    }

    public void setSelectDashWidth(int dashWidth) {
        layoutHelper.setSelectDashWidth(dashWidth);
        postInvalidate();
    }

    public void setShapeSelect(boolean shapeSelect) {
        refreshTextColor(shapeSelect);
        layoutHelper.setShapeSelect(shapeSelect);
        postInvalidate();
    }

    public boolean isShapeSelect(){
        return layoutHelper.isShapeSelect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        layoutHelper.draw(canvas, this);
        super.onDraw(canvas);
    }
}
