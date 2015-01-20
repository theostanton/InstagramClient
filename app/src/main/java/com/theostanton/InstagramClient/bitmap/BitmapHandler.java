package com.theostanton.InstagramClient.bitmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.LruCache;

import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 26/12/14.
 */
public class BitmapHandler {

    private static final String TAG = "Bitmaphandler";

    private static int gridDimension;
    private static int fullDimension;

    /*
    Instagram dimension
    low_res 306x306
    thumb 150x150
    std_res 640x640

    hd dimension
    1776x1080
    grid dimension 1080/3 = 360*360 use low_res
    //TODO
    full dimension 1080*1080 use std_res
     */

    private static BitmapHandler instance;

    private boolean cropBorderless = true;
    private boolean cacheImagesDisk = true;
    private boolean cacheImagesMemory = true;

    private BitmapCache diskCache;
    private LruCache<String, Bitmap> memoryCache;

    private BitmapHandler(Context context) {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        cacheImagesDisk = prefs.getBoolean(context.getResources().getString(R.string.cache_to_memory), true);
        cacheImagesMemory = prefs.getBoolean(context.getResources().getString(R.string.cache_to_memory), true);
        cropBorderless = prefs.getBoolean(context.getResources().getString(R.string.crop_borders_key), true);


        if (cacheImagesMemory) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            memoryCache = new LruCache<String, Bitmap>(cacheSize) {

                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }

//        if(cacheImagesDisk) // instantiate anyway, for clearing etc
        diskCache = BitmapCache.getInstance(context);

        int fullDimension = context.getResources().getDisplayMetrics().widthPixels;
        int gridDimension = fullDimension / 3;

        Log.d(TAG, "fullDimension = " + fullDimension);
        Log.d(TAG, "gridDimension = " + gridDimension);
    }

    public static BitmapHandler getInstance() {
        if (instance == null) {
            try {
                throw new Exception("BitmapHandler getInstance() without context before instantiation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static BitmapHandler getInstance(Context context) {
        if (instance == null) {
            instance = new BitmapHandler(context);
        }
        return instance;
    }

    public void putInCache(String url, Bitmap bitmap) {
        if (cacheImagesMemory && memoryCache.get(url) == null) {
            memoryCache.put(url, bitmap);
        }
        if (cacheImagesDisk) diskCache.save(bitmap, url);
    }

    public Bitmap getFromCache(String url) {

        Bitmap bitmap = null;

        if (cacheImagesMemory) {
            bitmap = memoryCache.get(url);
            if (bitmap != null) {
                return bitmap;
            }
        }


        if (cacheImagesDisk) bitmap = diskCache.get(url);
        return bitmap;
    }

    public void clearAll() {
        diskCache.clearAll();
        if (cacheImagesMemory) memoryCache.evictAll();
        Log.d(TAG, "Image cache cleared");
    }

    public String getDiskCacheSize() {
        int size = diskCache.getSize();
        if (size < 1024) {
            return size + "KB";
        }
        size /= 1024;
        if (size < 1024) {
            return size + "MB";
        }
        size /= 1024;
        return size + "GB";

    }

    public int getFileCount() {
        return diskCache.getFileCount();
    }

    public void setCropBorderless(boolean state) {
        Log.d(TAG, "setCropBorders= " + state);
        cropBorderless = state;
    }

    public boolean cropBorderless() {
        return cropBorderless;
    }
}
