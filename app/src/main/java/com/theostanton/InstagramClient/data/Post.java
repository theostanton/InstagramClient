package com.theostanton.InstagramClient.data;

import android.util.Log;

import com.theostanton.InstagramClient.Theo;
import com.theostanton.InstagramClient.helpers.TimeHelper;
import com.theostanton.InstagramClient.instagram.InstaJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by theo on 26/12/14.
 */
public class Post {

    private static final String TAG = "Post";

    private String id = "NA";
    private String stdUrl = "NA";
    private User user;
    private int likesNames;
    private long createdTime;
    private JSONObject object;

    public Post() {
    }

    public Post(JSONObject object) {

        this.object = object;

        try {
            id = object.getString(InstaJSON.ID);
            stdUrl = object.getJSONObject(InstaJSON.IMAGES).getJSONObject(InstaJSON.THUMB).getString(InstaJSON.URL);
            user = new User(object.getJSONObject(InstaJSON.USER));
            createdTime = object.getLong(InstaJSON.CREATED_TIME);
//            Log.d(TAG,"stdUrl " + stdUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTimeAgo() {
        return TimeHelper.getTimeAgo(createdTime);
    }

    public String getImageUrl() {
        return stdUrl;
    }

    public String getStdResUrl() {
        try {
            return object.getJSONObject(InstaJSON.IMAGES).getJSONObject(InstaJSON.STD_RES).getString(InstaJSON.URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getLikesNames() {
        try {
            int numLikes = object.getJSONObject(InstaJSON.LIKES).getInt(InstaJSON.COUNT);
            return Theo.abreviate(numLikes) + " likes";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    public ArrayList<Comment> getComments() {
        JSONArray array;
        try {
            array = object.getJSONObject(InstaJSON.COMMENTS).getJSONArray(InstaJSON.DATA);
        } catch (JSONException e) {
            Log.e(TAG, object.toString());
            e.printStackTrace();
            return null;
        }
        ArrayList<Comment> comments = new ArrayList<Comment>(array.length());
        try {
            comments.add(new Comment(object.getJSONObject(InstaJSON.CAPTION)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                comments.add(new Comment(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return comments;
    }
}
