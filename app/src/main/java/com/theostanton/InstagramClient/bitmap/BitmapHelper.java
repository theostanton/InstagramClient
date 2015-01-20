package com.theostanton.InstagramClient.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by theo on 27/12/14.
 */
public class BitmapHelper {


    private static final String TAG = "BitmapHelper";

    // From SO http://stackoverflow.com/questions/11932805/cropping-circular-area-from-bitmap-in-android
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static Bitmap getBorderless(Bitmap bitmap) {

        final int MAX_DIFF = 10;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int colour = bitmap.getPixel(0, 0);

        boolean end = false;
        int left = -1;
        for (int x = 0; x < w / 2; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = bitmap.getPixel(x, y);
                if (pixel != colour) {
                    end = true;
                }
            }
            if (end) break;
            left = x + 1;
        }

        int top = -1;

        if (left < 0) {
            end = false;
            for (int y = 0; y < h / 2; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = bitmap.getPixel(x, y);
                    if (pixel != colour) {
                        if (Math.abs(pixel - colour) > MAX_DIFF) end = true;
                    }
                }
                if (end) break;
                top = y + 1;
            }
        }

        Log.d(TAG, "left = " + left + " top " + top);

        if (left < 0 && top < 0) return bitmap;
        if (left > 0 && top > 0) return bitmap;


        left = left > 0 ? left : 0;
        top = top > 0 ? top + 5 : 0;
        // TODO always a little border vertically

        return Bitmap.createBitmap(bitmap, left, top, w - 2 * left, h - 2 * top);
    }


}
