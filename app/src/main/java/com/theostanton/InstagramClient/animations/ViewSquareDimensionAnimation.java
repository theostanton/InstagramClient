package com.theostanton.InstagramClient.animations;

import android.view.View;
import android.view.animation.Transformation;

/**
 * Created by theo on 04/01/15.
 */
public class ViewSquareDimensionAnimation extends ViewDimensionAnimation {

    protected static final String TAG = "ViewSquareDimensionAnimation";


    public ViewSquareDimensionAnimation(View view, int newSide){
        super(view, newSide, newSide);
        this.view = view;
    }

    public ViewSquareDimensionAnimation(View view, int newWidth, int newHeight) {
        super(view, newWidth, newHeight);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int nextSide = initWidth + (int) ((width - initWidth) * interpolatedTime);
        view.getLayoutParams().width = nextSide;
        view.getLayoutParams().height = nextSide;
        view.requestLayout();
    }
}
