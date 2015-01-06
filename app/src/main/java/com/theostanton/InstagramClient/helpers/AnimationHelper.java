package com.theostanton.InstagramClient.helpers;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;

public class AnimationHelper {

    public static void circularReveal(View view){
        int cx = (view.getLeft() + view.getRight() ) / 2;
        int cy = (view.getTop()  + view.getBottom()) / 2;

        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        try {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0.0f, finalRadius);
            view.setVisibility(View.VISIBLE);
            anim.start();
        }
        catch (Exception e){
            // TODO IllegalStateException
//            e.printStackTrace();
        }
    }

}