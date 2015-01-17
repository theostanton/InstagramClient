package com.theostanton.InstagramClient.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.theostanton.InstragramClient.R;

import java.util.ArrayList;

/**
 * Created by theo on 15/01/15.
 */
public class ButtonHeaderBackground extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener {

    private static final String TAG = "ButtonHeaderBackground";
    private static final int NUM_FOOTERS = 4;
    private static final int TOP = NUM_FOOTERS;

    private int footerwidth;
    private int footerHeight;

    private ArrayList<View> footers;
    private float[] startAlphas = {1.0f, 1.0f, 1.0f, 1.0f};
    private View top;
    private int maxHeightTop = 0;

    private int nextFooter = 0;

    private ValueAnimator animator;

    public ButtonHeaderBackground(Context context) {
        super(context);
        init(context);
    }

    public ButtonHeaderBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ButtonHeaderBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ButtonHeaderBackground(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        footerHeight = getResources().getDimensionPixelSize(R.dimen.footer_height);
        footerwidth = displayWidth / 4;

        animator = new ValueAnimator();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        footers = new ArrayList<>();
        footers.add(findViewById(R.id.footer_one));
        footers.add(findViewById(R.id.footer_two));
        footers.add(findViewById(R.id.footer_three));
        footers.add(findViewById(R.id.footer_four));
        top = findViewById(R.id.top);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = getWidth();
        int h = getMeasuredHeight();

        int topHeight = h - footerHeight;
//        Log.d(TAG,"h=" + h + " topheight=" + topHeight + " max=" + maxHeightTop);


        if (topHeight >= maxHeightTop) {
            maxHeightTop = topHeight;
            top.getLayoutParams().height = topHeight;
            Log.d(TAG, "topheight=" + topHeight);
        }

        for (View v : footers) {
            v.getLayoutParams().width = footerwidth;
            v.getLayoutParams().height = footerHeight + 25;

        }

        setFooter(0);


    }

    public void setFooter(int newFooterPos) {
        Log.d(TAG, "animatTo " + newFooterPos);

        if (newFooterPos < 0) return;

        nextFooter = newFooterPos;


        if (animator.isRunning()) animator.cancel();

        for (int i = 0; i < NUM_FOOTERS; i++) {
            startAlphas[i] = footers.get(i).getAlpha();
        }

        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.addUpdateListener(this);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(250L);
        animator.start();

//        for(int i=0; i<NUM_FOOTERS; i++){
//            footers.get(i).setVisibility(i==newFooterPos ? INVISIBLE : VISIBLE);
//        }
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float val = animation.getAnimatedFraction();

        for (int i = 0; i < NUM_FOOTERS; i++) {
            float newAlpha = startAlphas[i] - val * (nextFooter == i ? -1 : 1);
//            Log.d(TAG,i + " newAlph " + newAlpha);
            if (newAlpha >= 0.0f && newAlpha <= 1.0f) {
                footers.get(i).setAlpha(newAlpha);
            }
        }
    }
}
