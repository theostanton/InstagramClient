package com.theostanton.InstagramClient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by theo on 05/01/15.
 */
public class TextViewGetHeight extends TextView {

    public TextViewGetHeight(Context context) {
        super(context);
    }

    public TextViewGetHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewGetHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextViewGetHeight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getCalculatedHeight(){
        return 0;
    }
}
