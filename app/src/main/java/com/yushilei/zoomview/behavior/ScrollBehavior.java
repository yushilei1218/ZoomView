package com.yushilei.zoomview.behavior;

import android.content.Context;
import android.graphics.Matrix;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yushilei.zoomview.R;

/**
 * Created by Administrator on 2017/6/23.
 */
public class ScrollBehavior extends CoordinatorLayout.Behavior {
    private ImageView backImg;
    Matrix matrix;

    public ScrollBehavior() {
    }

    public ScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    String TAG = "ScrollBehavior";

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.i(TAG, "onStartNestedScroll");
        if (backImg == null)
            backImg = ((ImageView) coordinatorLayout.findViewById(R.id.background_img));
        //当滑动开始的时候，我们只关注垂直的方向
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        //当垂直方向发生滑动时 这里的Child就是Header, target就是产生滑动的View
        int height = child.getHeight();
        float headerY = child.getY();
        float newHeaderY = headerY - dy;
        if (dy > 0) {
            //向上滑动
            if (Math.abs(newHeaderY) >= height) {
                child.setY(-height);
            } else {
                child.setY(newHeaderY);
                consumed[1] = dy;
            }
        } else {
            //向下滑动
            if (headerY < 0) {
                if (newHeaderY > 0) {
                    child.setY(0);
                } else {
                    child.setY(newHeaderY);
                }
                consumed[1] = dy;
            } else {
                child.setY(headerY + dy / 2f);
                consumed[1] = dy;
                Matrix matrix = new Matrix(backImg.getMatrix());
                float sx = 1f + child.getY() / 100f;
                matrix.setScale(sx, sx, backImg.getWidth() / 2, backImg.getHeight() / 2);
                backImg.setImageMatrix(matrix);
            }

        }
    }
}
