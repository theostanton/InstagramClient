package com.theostanton.InstagramClient.instagram;

import android.util.Log;

public class InstaURL {

    private static final String TAG = "InstaURL";
    private static final String BASE = "https://api.instagram.com/v1/";
    private static final String POPULAR = "media/popular";
    private static final String LIKED = "users/self/media/liked";
    private static final String LIKES = "/likes";
    private static final String MYFEED = "users/self/feed";
    private static final String COUNT = "count=50";
    private static final String USERS = "users/";
    private static final String RECENT = "recent";
    private static final String MEDIA = "media";
    private static final String FOLLOWS = "follows";
    private static final String FOLLOWED_BY = "followed-by";
    private static final String SELF = "self";
    // PUT ACCESS TOKEN HERE
    private static String accToken = "?access_token=1571747679.1fb234f.788bb9da95994e85995f8ba9ed6a925d";

    public static void setAccToken(String newAccToken) {
        if (newAccToken != null) {
            accToken = "?access_token=";
            accToken += newAccToken;
        }
    }

    public static String getPopular() {
        String url = "";
        url += BASE;
        url += POPULAR;
        url += accToken;
        return url;
    }

    public static String getMyFeed() {
        String url = "";
        url += BASE;
        url += MYFEED;
        url += accToken;
        url += "&";
        url += COUNT;
        Log.d(TAG, "getMyFeed url =  " + url);
        return url;
    }

    public static String getMyLikes() {
        String url = "";
        url += BASE;
        url += LIKED;
        url += accToken;
        url += "&";
        url += COUNT;
        Log.d(TAG, "getMyLikes url =  " + url);
        return url;
    }

    public static String getFeed(int userId) {
        String url = "";
        url += BASE;
        url += USERS;
        url += String.valueOf(userId);
        url += "/";
        url += MEDIA;
        url += "/";
        url += RECENT;
        url += accToken;
        url += "&";
        url += COUNT;
        Log.d(TAG, "feed url = " + url);
        return url;
    }

    public static String getUser(int userId) {
        StringBuilder sb = new StringBuilder(BASE);
        sb.append(USERS);
        sb.append(userId);
        sb.append(accToken);
        String url = sb.toString();
        Log.d(TAG, "getUser url= " + url);
        return url;
    }

    public static String getFollowsList(int userId) {
        StringBuilder sb = new StringBuilder(BASE);
        sb.append(USERS);
        if (userId == -1) sb.append(SELF);
        else sb.append(userId);
        sb.append("/");
        sb.append(FOLLOWS);
        sb.append(accToken);
        return sb.toString();
    }

    public static String getFollowedByList(int userId) {
        StringBuilder sb = new StringBuilder(BASE);
        sb.append(USERS);
        if (userId == -1) sb.append(SELF);
        else sb.append(userId);
        sb.append("/");
        sb.append(FOLLOWED_BY);
        sb.append(accToken);
        return sb.toString();
    }


//    https://api.instagram.com/v1/media/111111/likes

    public static String getLikes(int userId) {
        StringBuilder sb = new StringBuilder(BASE);
        sb.append(MEDIA);
        sb.append("/");
        sb.append(userId);
        sb.append("/likes");
        sb.append(accToken);
        String url = sb.toString();
        Log.d(TAG, "getLikes url =  " + url);
        return url;

    }
}
