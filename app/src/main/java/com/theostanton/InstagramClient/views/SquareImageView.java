package com.theostanton.InstagramClient.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.theostanton.InstagramClient.helpers.ViewHelper;

/**
 * Created by theo on 27/12/14.
 */
public class SquareImageView extends ImageView {

    private static final String TAG = "SquareImageView";


    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // TODO possibly being called more than necessary

        float displayWidth = getResources().getDisplayMetrics().widthPixels;

        Drawable drawable = getDrawable();

        if (drawable == null) {
            Log.e(TAG, "onMeasure drawable==null");
            setMeasuredDimension((int) displayWidth, (int) displayWidth);
            return;
        }
//        Rect bounds = drawable.getBounds();
        float width = drawable.getIntrinsicWidth();
        float height = drawable.getIntrinsicHeight();
//        Log.d(TAG,"drawable w = "  + width + " h " + height);

        float ratio = width / height;


        int newHeight = (int) (displayWidth / ratio);

//        Log.d(TAG,"ratio " + ratio + " displayWidth " + displayWidth + " newHeight " + newHeight);


        setMeasuredDimension((int) displayWidth, newHeight);


        setOutlineProvider(ViewHelper.getRectOutline());
    }

//    @Override
//    public void setImageBitmap(Bitmap bm) {
//        super.setImageBitmap(bm);
//        Log.d(TAG, "setImageBitmap");
//    }
}
