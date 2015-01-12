package com.theostanton.InstagramClient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollessScrollView extends ScrollView {

    private static final String TAG = "ScrollessScrollView";
    private boolean scrollingEnabled = true;

    public ScrollessScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollessScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollessScrollView(Context context) {
        super(context);
    }

    public boolean isScrollingEnabled() {
        return scrollingEnabled;
    }

    public void setScrollingEnabled(boolean enabled) {
        scrollingEnabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isScrollingEnabled()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            super.onInterceptTouchEvent(ev);
            Log.d(TAG, "onInterceptTouchEvent() return true");
            return true;
        }
    }

    @Override
    public boolean callOnClick() {
        Log.d(TAG, "callOnClick()");
        return callOnClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        super.onInterceptTouchEvent(ev);

        if (!isScrollingEnabled()) {

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(TAG, "onTouchEvent() ACTION_DOWN return false");
                return false;
            } else {
                Log.d(TAG, "onTouchEvent() return true");
                return false;
            }
        }
        return true;

    }
}