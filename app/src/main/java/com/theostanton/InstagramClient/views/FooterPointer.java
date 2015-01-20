package com.theostanton.InstagramClient.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 05/01/15.
 */
public class FooterPointer extends View {

    private static final String TAG = "FooterPointer";

    private Paint paint;

    public FooterPointer(Context context) {
        super(context);
        init();
    }

    public FooterPointer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterPointer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FooterPointer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        int colour = getResources().getColor(R.color.footer_pointer);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colour);
        paint.setStyle(Paint.Style.FILL);
        setBackground(null);
//        Canvas canvas = surfaceHolder.lockCanvas();
//        canvas.drawColor(Color.BLACK);
//        canvas.drawCircle(event.getX(), event.getY(), 50, paint);
//        surfaceHolder.unlockCanvasAndPost(canvas);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(canvas.getWidth(), canvas.getHeight()) / 2;

//        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, radius, paint);
    }
}
