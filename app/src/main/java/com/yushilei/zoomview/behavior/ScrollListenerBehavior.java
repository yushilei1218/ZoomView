package com.yushilei.zoomview.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yushilei.zoomview.R;

/**
 * Created by Administrator on 2017/6/23.
 */
public class ScrollListenerBehavior extends CoordinatorLayout.Behavior {
    /**
     * 需要被缩放的ImageView
     */
    private ImageView backImg;
    /**
     * 缩放Matrix
     */
    Matrix mZoomMatrix;
    /**
     * Header下拉的高度/ mScreenHeight+1f 为缩放的系数，即缩放率
     */
    private int mScreenHeight;
    /**
     * 下拉Header时 滑动系数
     */
    private static final float SCROLL_RATIO = 0.5F;

    public ScrollListenerBehavior() {
        this(null, null);
    }

    public ScrollListenerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);

        mScreenHeight = metrics.heightPixels;
    }

    String TAG = "ScrollListenerBehavior";

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

        int height = child.getHeight();//Header高度
        float headerY = child.getY();//Header当前的Y值
        float newHeaderY = headerY - dy;
        if (dy > 0) {
            //向上滑动 dy正数 ,
            //1：Header y>0, Header完全露出 2：Header 部分隐藏 3：Header完全隐藏
            if (headerY > 0) {
                //1：HeaderY完全露出
                consumed[1] = dy;
                if (newHeaderY < 0) {
                    child.setY(0);//当Header Y处于0 继续向上滑动则触发else
                } else {
                    child.setY(newHeaderY);
                }
                zoomBackGroundImg(computeZoomRatio(newHeaderY));
            } else {
                if (headerY > -height) {
                    //2：Header y=0 ,或者Header部分隐藏
                    consumed[1] = dy;
                    if (newHeaderY <= -height) {
                        child.setY(-height);
                    } else {
                        child.setY(newHeaderY);
                    }
                }
                //3：Header完全隐藏就不需要再拦截事件了，什么都不做
            }
        } else {
            //是否需要消化滑动事件dy
            boolean needConsumed = false;
            RecyclerView.LayoutManager manager = ((RecyclerView) target).getLayoutManager();
            int childCount = manager.getChildCount();
            View childAt = manager.getChildAt(0);
            int position = manager.getPosition(childAt);
            //当Recycler item为0 或者第一个item处于顶端则需要拦截事件dy
            needConsumed = childCount == 0 || (position == 0 && childAt.getTop() >= 0);

            if (!needConsumed) {
                return;
            }

            //向下滑动 dy=负数 分2种 1：header Y<0 ;2: header Y>=0 即被下拉
            if (headerY < 0) {
                if (newHeaderY > 0) {
                    child.setY(0);//Header等于Y=0 当继续下拉则触发else
                } else {
                    child.setY(newHeaderY);
                }
                consumed[1] = dy;
            } else {
                //Header完全露出并被下拉 需要放大背景图片
                consumed[1] = dy;//消化事件
                newHeaderY = headerY - SCROLL_RATIO * dy;
                child.setY(newHeaderY);//Header进一步被拉出
                zoomBackGroundImg(computeZoomRatio(newHeaderY));
            }
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        if (child.getY() > 0) {
            //弹性滑动
            ObjectAnimator animator = ObjectAnimator.ofFloat(new HeaderWrapper(child), "Y", child.getY(), 0f);
            animator.setDuration(300);
            animator.start();
        }
    }

    /**
     * 计算缩放系数
     */
    private float computeZoomRatio(float newHeaderY) {
        return 1f + newHeaderY / (float) mScreenHeight;
    }

    /**
     * 根据缩放系数 来缩放背景图片
     */
    private void zoomBackGroundImg(float scaleRatio) {
        if (mZoomMatrix == null) {
            mZoomMatrix = new Matrix(backImg.getMatrix());
        }
        mZoomMatrix.reset();
        mZoomMatrix.setScale(scaleRatio, scaleRatio, backImg.getWidth() / 2, 0f);
        backImg.setImageMatrix(mZoomMatrix);
    }

    /**
     * 包装一下Header,当更改Header Y时，同时进行缩放背景图片
     */
    private final class HeaderWrapper {
        View header;

        public HeaderWrapper(View header) {
            this.header = header;
        }

        public void setY(float newHeaderY) {
            zoomBackGroundImg(computeZoomRatio(newHeaderY));
            header.setY(newHeaderY);
        }
    }
}
