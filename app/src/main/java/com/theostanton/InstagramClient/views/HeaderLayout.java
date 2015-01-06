package com.theostanton.InstagramClient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by theo on 04/01/15.
 */
public class HeaderLayout extends RelativeLayout {

    private static final String TAG = "HeaderLayout";

    private float expansion = 150.0f;
    private float minHeight = 150.0f;
    private float maxHeight = 300.0f;

    public HeaderLayout(Context context) {
        super(context);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public float getExpandFraction(){
        float expandFraction = (maxHeight - minHeight) / (getHeight() - minHeight);
        Log.d(TAG, "expandFraction " + expandFraction);
        return expandFraction;
    }

    public void setExpandFraction(float expandFraction){
        getLayoutParams().height = (int)( minHeight + expandFraction * expansion );
    }
}
