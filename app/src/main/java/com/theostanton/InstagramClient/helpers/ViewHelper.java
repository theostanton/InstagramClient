package com.theostanton.InstagramClient.helpers;

import android.graphics.Outline;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by theo on 02/01/15.
 */
public class ViewHelper {

    private static final String TAG = "ViewHelper";

    private static float density = 3.0f;

    public static void setDensity(float newDensity) {
        Log.d(TAG, "density set to " + newDensity);
        density = newDensity;
    }

    public static ViewOutlineProvider getCicularOutline(final int width, final int height) {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int size = Math.min(width, height);
                int xOff = (width - size) / 2;
                int yOff = (height - size) / 2;
//                Log.d(TAG,"w " + width + " h " + height + " x " + xOff + " y " + yOff);
                outline.setOval(xOff, yOff, size, size);
            }
        };
    }

    public static ViewOutlineProvider getCicularOutline() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int width = view.getWidth();
                int height = view.getHeight();
                int size = Math.min(width, height);
                int xOff = (width - size) / 2;
                int yOff = (height - size) / 2;
//                Log.d(TAG,"w " + width + " h " + height + " x " + xOff + " y " + yOff);
                outline.setOval(xOff, yOff, size, size);
            }
        };
    }

    public static ViewOutlineProvider getRectOutline(final Rect rect) {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRect(rect);
            }
        };
    }

    public static ViewOutlineProvider getRectOutline() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRect(0, 0, view.getMeasuredWidth() / 2, view.getMeasuredHeight());
            }
        };
    }

    // from SO http://stackoverflow.com/questions/15039913/android-how-to-measure-total-height-of-listview
    public static int getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();
        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        return totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
    }

    public static int getDp(float px) {
        return (int) (density * px);
    }

    public static int getDp(int px) {
        return (int) (density * px);
    }

    public static int calculateTextViewHeight(TextView textView) {
//        if(textView.getVisibility()==View.GONE) return 0;
        int height = textView.getMeasuredHeight();
        if (textView.getText() == null || textView.getText().length() == 0) {
            height = 0;
        }
        Log.d("calculateTextViewHeight", "text=*" + textView.getText() + "* return " + height);
        return height;
    }

}
