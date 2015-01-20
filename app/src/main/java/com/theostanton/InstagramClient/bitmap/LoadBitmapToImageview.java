package com.theostanton.InstagramClient.bitmap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.theostanton.InstagramClient.helpers.HTTPHandler;

public class LoadBitmapToImageview extends AsyncTask<ImageView, Void, Bitmap> {

    private static final String TAG = "LoadBitmapToImageview";

    private ImageView imageView;
    private BitmapHandler bitmapHandler;
    private boolean animate = true;
    private boolean borderless = true;

    private float elevation;

    public LoadBitmapToImageview() {

    }

    public void setAnimation(boolean animate) {
        this.animate = animate;
    }

    public void setBorderless(boolean borderless) {
        this.borderless = borderless;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bitmapHandler = BitmapHandler.getInstance();
    }

    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        imageView = imageViews[0];
        elevation = imageView.getElevation();
        if (elevation > 0.0f) imageView.setElevation(0.0f);
        String url = (String) imageView.getTag();

//        Log.d(TAG, "url = " + url);
        Bitmap bitmap = bitmapHandler.getFromCache(url);
        if (bitmap != null) {
//            animate = false;
            return bitmap;
        }

        bitmap = HTTPHandler.downloadBitmap(url);
        bitmapHandler.putInCache(url, bitmap);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (animate) imageView.setAlpha(0.0f);

        if (borderless && bitmapHandler.cropBorderless())
            imageView.setImageBitmap(BitmapHelper.getBorderless(bitmap));
        else imageView.setImageBitmap(bitmap);
        if (animate) imageView.animate().alpha(1.0f).setDuration(300L).start();

        if (elevation > 0.0f) imageView.setElevation(elevation);

//        Log.d(TAG, "Finished");
    }
}
