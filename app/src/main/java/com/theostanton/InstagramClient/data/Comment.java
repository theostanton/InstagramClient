package com.theostanton.InstagramClient.data;

import android.util.Log;

import com.theostanton.InstagramClient.instagram.InstaJSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by theo on 27/12/14.
 */
public class Comment {

    private static final String TAG = "Comment";

    public String text;
    public long id;
    public User user;
    public long createdTime;

    public Comment(JSONObject object) {
        try {
            createdTime = object.getLong(InstaJSON.CREATED_TIME);
            id = object.getLong(InstaJSON.ID);
            user = new User(object.getJSONObject(InstaJSON.FROM));
            text = object.getString(InstaJSON.TEXT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return user.userName;
    }

    public int getUserId() {
        Log.d(TAG, "getUserId " + user.id + " " + user.userName);
        return user.id;
    }

}
