package com.theostanton.InstagramClient.instagram;

import android.util.Log;

import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by theo on 27/12/14.
 */
public class InstaJSON {

    public static final String DATA = "data";
    public static final String ID = "id";
    public static final String TAGS = "tags";

    // POST
    public static final String TYPE = "type";
    public static final String LOCATION = "location";
    public static final String COMMENTS = "comments";
    public static final String FILTER = "filter";
    public static final String CREATED_TIME = "created_time";
    public static final String LINK = "link";
    public static final String LIKES = "likes";
    public static final String IMAGES = "images";
    public static final String USERS_IN_PHOTO = "users_in_photo";
    public static final String CAPTION = "caption";
    public static final String USER_HAS_LIKED = "user_has_liked";
    public static final String USER = "user";
    public static final String USERNAME = "username";
    // also ID

    // USER
    public static final String WEBSITE = "website";
    public static final String PROFILE_PICTURE = "profile_picture";
    public static final String FULL_NAME = "full_name";
    public static final String BIO = "bio";
    // also ID
    public static final String COUNTS = "counts";
    public static final String MEDIA = "media";
    public static final String FOLLOWED_BY = "followed_by";
    public static final String FOLLOWS = "follows";
    // IMAGES
    public static final String LOW_RES = "low_resolution";
    public static final String THUMB = "thumbnail";
    public static final String STD_RES = "standard_resolution";
    public static final String URL = "url";
    // LIKES
    public static final String COUNT = "count";
    // COMMENT
    public static final String TEXT = "text";
    // also data of users
    public static final String FROM = "from";
    private static final String TAG = "InstaJSON";
    // also userid and created time

    public static JSONArray getDataArray(JSONObject object) {
        if (object == null) return null;
        try {
            return object.getJSONArray(DATA);
        } catch (JSONException e) {
            Log.e(TAG, "getDataArray object = " + object.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataObject(JSONObject object) {
        if (object == null) return null;
        try {
            return object.getJSONObject(DATA);
        } catch (JSONException e) {
            Log.e(TAG, "getDataObject object = " + object.toString());
            e.printStackTrace();
        }
        return null;
    }


    public static ArrayList<Post> getPostsArrayList(JSONObject object) {
        JSONArray array = getDataArray(object);
        if (array == null) return new ArrayList<Post>();
        Log.d(TAG, "getPostsArralist array length = " + array.length() + " object = " + object.toString());
        ArrayList<Post> posts = new ArrayList<Post>(array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                posts.add(new Post(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.e(TAG, "getPostsArrayList object  = " + object.toString());
            e.printStackTrace();
        }
        return posts;
    }

    public static ArrayList<User> getUserArraylist(JSONObject object) {
        JSONArray array = getDataArray(object);
        if (array == null) return new ArrayList<User>();

        Log.d(TAG, "getUsersArraylist array length = " + array.length() + " object = " + object.toString());

        ArrayList<User> users = new ArrayList<User>(array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                users.add(new User(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }
}
