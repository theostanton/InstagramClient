package com.theostanton.InstagramClient.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.theostanton.InstagramClient.views.FrameWithFractionLayout;
import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 06/01/15.
 */
public class FabFragment extends BaseFragment {

    private static final String TAG = "FabFragment";
    private static final float SHOW = 0.0f;
    private static final float HIDE = 200.0f;
    private ValueAnimator animator = new ValueAnimator();
    private FrameWithFractionLayout layout;
    private float currTranslation = 0.0f;
    private float newTranslation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fabs_fragment, container, false);
        layout = (FrameWithFractionLayout) view.findViewById(R.id.fabs_layout);
        setTranslation(HIDE);
        return view;
    }

    public void hide() {
        animateTo(HIDE);
    }

    public void show() {
        animateTo(SHOW);
    }

    public void toggle() {
        if (newTranslation == SHOW) {
            animateTo(-1.0f);
        } else {
            animateTo(1.0f);
        }
    }

    private void setTranslation(float translationY) {
//        Log.d(TAG,"translationY " + translationY);
        currTranslation = translationY;
        layout.setTranslationY(translationY);
    }

    public void animateTo(float newTranslation) {
        Log.d(TAG, "animate to " + newTranslation);
        this.newTranslation = newTranslation;
        if (animator.isRunning()) animator.cancel();
        animator = ValueAnimator.ofFloat(currTranslation, newTranslation);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (Float) valueAnimator.getAnimatedValue();
                setTranslation(val);
            }
        });
        long duration = 300L; // * Math.abs( newTranslation - currTranslation );


        animator.setDuration(duration);
        if (newTranslation == SHOW) {
            animator.setStartDelay(2 * duration);
            animator.setInterpolator(new OvershootInterpolator(2.0f));
        } else {
            animator.setInterpolator(new AnticipateInterpolator());
        }
        animator.start();
    }
}
