package com.theostanton.InstagramClient.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by theo on 03/01/15.
 */
public class ViewDimensionAnimation extends Animation {

    public static final boolean WIDTH_ONLY = true;
    public static final boolean HEIGHT_ONLY = true;
    protected static final String TAG = "ViewWidthAnimation";
    protected View view;
    protected int width;
    protected int height;
    protected int initWidth;
    protected int initHeight;
    private boolean adjustHeight = false;
    private boolean adjustWidth = false;

    public ViewDimensionAnimation(View view, int newSide, boolean adjustWidth){

        this.view = view;
        setDuration(300L);

        if(adjustWidth){
            this.adjustWidth=true;
            width = newSide;
            initWidth = view.getWidth();
        }
        else{
            adjustHeight=true;
            height = newSide;
            initHeight = view.getHeight();
        }
    }

    public ViewDimensionAnimation(View view, int newWidth, int newHeight){

        this.view = view;
        setDuration(300L);

        if(newWidth!=view.getWidth()){
            adjustWidth=true;
            width = newWidth;
            initWidth = view.getWidth();
        }

        if(newHeight!=view.getHeight()){
            adjustHeight=true;
            height = newHeight;
            initHeight = view.getHeight();
        }


    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        if(adjustWidth){
            view.getLayoutParams().width = initWidth + (int) ((width - initWidth) * interpolatedTime);
        }

        if(adjustHeight){
            view.getLayoutParams().height = initHeight + (int) ((height - initHeight) * interpolatedTime);
        }

        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
