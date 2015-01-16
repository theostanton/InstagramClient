package com.theostanton.InstagramClient.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 15/01/15.
 */
public class HeaderBackground extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener {

    public static final String TAG = "HeaderBackground";

    private Paint paint;

    private View top;
    private View left;
    private View right;

    private int footerWidth;

    private int maxTopHeight = 0;
    private int contractedHeader;
    private int footerHeight;
    private int w = 0;
    private int h = 0;

    private float translation = 0.0f;
    private ValueAnimator slideAnimation;

    public HeaderBackground(Context context) {
        super(context);
        init(context);
    }

    public HeaderBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HeaderBackground(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        slideAnimation = new ValueAnimator();
        slideAnimation.addUpdateListener(this);

        contractedHeader = getResources().getDimensionPixelSize(R.dimen.header_contracted);
        footerHeight = getResources().getDimensionPixelSize(R.dimen.footer_height);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        left = getChildAt(0);
        right = getChildAt(1);
        top = getChildAt(2);

    }

    private void setTopHeight(int h) {
        top.getLayoutParams().height = h;
    }

    private void setLeftWidth(int w) {
        left.getLayoutParams().width = w;
        left.requestLayout();
    }

    private void setRightWidth(int w) {
        right.getLayoutParams().width = w;
        right.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.d(TAG, "w=" + widthMeasureSpec + " h=" + heightMeasureSpec);

        w = getMeasuredWidth();
        footerWidth = w / 4;
        h = getMeasuredHeight();

//        setLeftWidth(w/3);
//        setRightWidth(w/3);

        int topHeight = h - footerHeight;
        if (topHeight > maxTopHeight) {
            maxTopHeight = topHeight;
            setTopHeight(topHeight);
        }
    }

    public void setFooter(final int newFooterPos) {
        if (newFooterPos < 0) {
            setTranslation(-footerWidth);
            return;
        }
        animateTo(newFooterPos);
    }

    private void animateTo(final int newFooterPos) {
        Log.d(TAG, "animeatefooter to " + newFooterPos);

        if (slideAnimation.isRunning()) slideAnimation.cancel();

        float newTranslation = (float) newFooterPos * footerWidth;
//        Log.d(TAG,"animate from " + currExpandFraction + " to " + newFraction);
        slideAnimation = ValueAnimator.ofFloat(translation, newTranslation);
        slideAnimation.addUpdateListener(this);

//        float duration = 1000.0f * Math.abs( footerCurrTranslation - newTranslation );


        slideAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        slideAnimation.setDuration(300L);
        slideAnimation.start();
    }

    private void setTranslation(float newTranslation) {

        translation = newTranslation;

        setLeftWidth((int) translation);

        int rightWidth = (int) (getWidth() - footerWidth - translation);
        setRightWidth(rightWidth);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float val = (Float) valueAnimator.getAnimatedValue();
        Log.d(TAG, "val= " + val);
        setTranslation(val);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        paint.setColor(Color.RED);
//        canvas.drawRect(topRect, paint);
//        paint.setColor(Color.GREEN);
//        canvas.drawRect(botLeftRect, paint);
//        paint.setColor(Color.BLUE);
//        canvas.drawRect(botRightRect,paint);
//    }

//    public ViewOutlineProvider getOutline(){
//
//        final Path path = new Path();
//
//        path.moveTo(0,0);
//        path.lineTo(w,0);
//        path.lineTo(w, h);
//
//        path.lineTo(botRightRect.left, h);
//        path.lineTo(botRightRect.left,h/2);
//
//        path.lineTo(botLeftRect.right,h/2);
//        path.lineTo(botLeftRect.right,h);
//
//        path.lineTo(0,h);
//        path.close();
//
//        return new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                outline.setConvexPath(path);
//            }
//        };
//    }
}
