package com.theostanton.InstagramClient.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by theo on 17/01/15.
 */
public class HeaderUserImageView extends UserImageView {

    protected static final String TAG = "HeaderUserImageView";

    public HeaderUserImageView(Context context) {
        super(context);
    }

    public HeaderUserImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderUserImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeaderUserImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


//        int width = getMeasuredWidth(); // - getPaddingLeft() - getPaddingRight();
//        int height = getMeasuredHeight(); // - getPaddingTop() - getPaddingBottom();
//        int size = Math.max(width,height);
//
//        Log.d(TAG, "w=" + width + " h=" + height + " size=" + size);
////        int size = width < height ? width : height;
////        size = Math.min(ViewHelper.getDp(150),size);
////        Log.d(TAG,"onMeasure width=" +width + " onMeasure height " + height);
//        setMeasuredDimension(width, height);

    }
}
