package com.theostanton.InstagramClient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by theo on 03/01/15.
 */
public class FrameWithFractionLayout extends FrameLayout {

    private static final String TAG = "FrameWithFractionlayout";

    public FrameWithFractionLayout(Context context) {
        super(context);
    }

    public FrameWithFractionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameWithFractionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FrameWithFractionLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public float getXFraction() {
//        Log.d(TAG,"getXFraction getX()="  + getX() + " w " + (getWidth()));
        if(getWidth()==0.0f) return 0.0f;
        return getX() / getWidth();
    }

    public void setXFraction(float xFraction) {
//        Log.d(TAG, "setXFraction " + xFraction);
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }

    public float getYFraction() {
//        Log.d(TAG,"getXFraction getX()="  + getX() + " w " + (getWidth()));
        if(getHeight()==0.0f) return 0.0f;
        return getY() / getHeight();
    }

    public void setYFraction(float yFraction) {
        Log.d(TAG, "setYFraction " + yFraction);
        final int height = getHeight();
        setY((height > 0) ? (yFraction * height) : -9999);
    }

}
