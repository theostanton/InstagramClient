package com.theostanton.InstagramClient.bitmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by theo on 11/12/14.
 */
public class BitmapCache {

    private static final String TAG = "BitmapCache";
    private static final boolean LOG = false;

    private static final String PNG_FOLDER = "/PNGS/";
    private static final String PNG = ".png";
    private static BitmapCache instance;

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;

    private BitmapCache(Context context) {
        if (context == null) Log.e(TAG, "init BitmapCacheHandler with context==null");
        this.context = context;
        sharedPreferences = context.getSharedPreferences("cachedData", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        File folder = new File(context.getFilesDir() + PNG_FOLDER);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                if (LOG) Log.d(TAG, "BitmapCache(), Made dir " + folder.getAbsolutePath());
            } else {
                Log.e(TAG, "BitmapCache(), Couldn't make dir " + folder.getAbsolutePath());
            }
        } else {
            if (LOG) Log.d(TAG, "BitmapCache(), PNG folder exists");
        }
    }

    public static BitmapCache getInstance(Context context) {

        if (instance == null) {
            if (context == null) {
                Log.e(TAG, "getInstance with null context");
            }
            instance = new BitmapCache(context);
        }
        return instance;
    }


    public String getFileName(String url) {
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return context.getFilesDir() + PNG_FOLDER + url;
    }

    public boolean save(Bitmap bitmap, String url) {
        String fileName = getFileName(url);
        File file = new File(fileName);

        if (file.exists()) {
            Log.e(TAG, "Trying to save a bitmap that already exists");
            return true;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        try {
            FileOutputStream fileOutStream = new FileOutputStream(file);
            fileOutStream.write(byteArray);
            fileOutStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Bitmap get(String url) {
        String fileName = getFileName(url);
        File file = new File(fileName);
        if (file.exists()) {
//            Log.d(TAG,"get from url = " + url);
            return BitmapFactory.decodeFile(fileName);
        }
        return null;
    }

    public void clearAll() {

        File dir = new File(context.getFilesDir() + PNG_FOLDER);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                new File(dir, child).delete();
            }
        }
    }


    public int getSize() {
        File dir = new File(context.getFilesDir() + PNG_FOLDER);
        long size = 0L;
        int fileCount;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            fileCount = children.length;
            for (String child : children) {
                try {
                    File file = new File(dir, child);
                    long length = file.length();
                    length = length / 1024;
//                    Log.d(TAG,file.getName() + " " + length + "KB");
                    size += length;
                } catch (Exception e) {
                    System.out.println("File not found : " + e.getMessage() + e);
                }
            }
        } else {
            return -1;
        }

        Log.d(TAG, fileCount + " files " + size + "KB avg size= " + (fileCount > 0 ? size / fileCount : 0) + "KB");
        return (int) size;
    }

    public int getFileCount() {
        File dir = new File(context.getFilesDir() + PNG_FOLDER);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            return children.length;
        }
        return -1;
    }

}




















