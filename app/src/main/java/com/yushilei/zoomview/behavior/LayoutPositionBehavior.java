package com.yushilei.zoomview.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yushilei.zoomview.R;

/**
 * 位置LayoutPositionBehavior 作用在RecyclerView 上
 * RecyclerView在布局中的位置依赖于Header的位置
 */
public class LayoutPositionBehavior extends CoordinatorLayout.Behavior<RecyclerView> {
    public LayoutPositionBehavior() {
    }

    public LayoutPositionBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    String TAG = "LayoutPositionBehavior";

    /**
     * 这里决定了RecyclerView 和监听那个View的信息变化
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        Log.i(TAG, "layoutDependsOn" + child + " " + dependency);
        //即RecyclerView layoutDependsOn  id=R.id.header 的View
        return dependency.getId() == R.id.header;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        //这个方法回调传入的dependency 就是layoutDependsOn 确定下来的View
        Log.i(TAG, "onDependentViewChanged" + child + " " + dependency);
        //Child 的位置在这里被设定下来，依据dependency的位置
        child.setY(dependency.getY() + dependency.getHeight());
        return true;
    }
}
