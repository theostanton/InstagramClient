package com.theostanton.InstagramClient.instagram;

import android.util.Log;

import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.helpers.HTTPHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Instagram {

    private static final String TAG = "Instagram";

    private static Instagram instance;

    private static HashMap<String, JSONObject> objects;
    private static HashMap<String, Post> posts;
    private static HashMap<Integer, User> users;

    private static ArrayList<Post> myFeed;
    private static ArrayList<Post> popularPosts;
    private static ArrayList<Post> myLikes;
    private static ArrayList<User> iFollowList;
    private static ArrayList<User> followedBy;

    private static ArrayList<Post> currPosts;

    private Instagram(String accToken) {
        InstaURL.setAccToken(accToken);
        if (posts == null) {
            posts = new HashMap<String, Post>();
            users = new HashMap<Integer, User>();
            objects = new HashMap<String, JSONObject>(); //TODO lrucache instead
        }
        myFeed = null;
        popularPosts = null;
        myLikes = null;
        followedBy = null;
        iFollowList = null;

    }

    public static Instagram getInstance(String accToken) {
        instance = new Instagram(accToken);
        return instance;
    }

    public static Instagram getInstance() {
        if (instance == null) {
            try {
                throw new Exception("Instagram getInstance before instantiation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    private static void putUsers(ArrayList<User> users) {
        for (User user : users) {
            addUser(user);
        }
    }

    private static void addUser(User user) {
        if (user.id <= 0) {
            Log.e(TAG, "addUser user.id = " + user.id);
        }
        if (users.containsKey(user.id)) {
            if (!users.get(user.id).isComplete() && user.isComplete()) {
                users.put(user.id, user);
//                Log.d(TAG,"put " + user.fullName);
            }
            return;
        }
//        Log.d(TAG,"put " + user.fullName);
        users.put(user.id, user);
    }

    private static void cachePosts(ArrayList<Post> newPosts) {
        currPosts = newPosts;
        for (Post post : newPosts) {
            posts.put(post.getId(), post);
            addUser(post.getUser());
        }
        Log.d(TAG, "posts.size = " + posts.size());
    }

    // Post

    public static Post getPost(String postId) {
        // Shouldn't ever need to fetch a post here, if the post is on screen then it's been cached. Unless cache has been emptied..
        // TODO posts as lrucache and add fetch potion if not cached
        return posts.get(postId);
    }

    public static ArrayList<Post> getPopular(boolean fresh) {
        if (fresh || popularPosts == null) {
            String url = InstaURL.getPopular();
            JSONObject object = getObject(url, fresh);
            popularPosts = InstaJSON.getPostsArrayList(object);
        }
        cachePosts(popularPosts);
        return popularPosts;
    }

    public static ArrayList<Post> getMyFeed(boolean fresh) {
        if (fresh || myFeed == null) {
            Log.d(TAG, "fresh myFeed");
            String url = InstaURL.getMyFeed();
            JSONObject object = getObject(url, fresh);
            myFeed = InstaJSON.getPostsArrayList(object);
        }
        cachePosts(myFeed);
        return myFeed;

    }

    public static ArrayList<Post> getMyLikes(boolean fresh) {
        if (fresh || myLikes == null) {
            String url = InstaURL.getMyLikes();
            JSONObject object = getObject(url, fresh);
            myLikes = InstaJSON.getPostsArrayList(object);
        }
        cachePosts(myLikes);
        return myLikes;
    }

    public static ArrayList<Post> getLikes(int userId, boolean fresh) {
        String url = InstaURL.getLikes(userId);
        JSONObject object = getObject(url, fresh);
        Log.d(TAG, "getLikes object " + object.toString());
        ArrayList<Post> posts = InstaJSON.getPostsArrayList(object);
        cachePosts(posts);
        return posts;
    }

    public static ArrayList<Post> getFeed(int userId, boolean fresh) {
        String url = InstaURL.getFeed(userId);
        JSONObject object = getObject(url, fresh);
        ArrayList<Post> feed = InstaJSON.getPostsArrayList(object);
        cachePosts(feed);
        return feed;
    }

    public static String getPreviousPostId(String postId) {

        for (int i = 0; i < currPosts.size(); i++) {
            Post post = currPosts.get(i);
            if (post.getId().equals(postId)) {
                if (i > 0) return currPosts.get(i - 1).getId();
                else return currPosts.get(currPosts.size() - 1).getId();
            }
        }
        Log.e(TAG, "postId not in currPosts, currposts size=" + currPosts.size());
        return null;
    }

    public static String getNextPostId(String postId) {

        for (int i = 0; i < currPosts.size(); i++) {
            Post post = currPosts.get(i);
            if (post.getId().equals(postId)) {
                if (i < currPosts.size() - 1) return currPosts.get(i + 1).getId();
                else return currPosts.get(0).getId();
            }
        }
        Log.e(TAG, "postId not in currPosts, currposts size=" + currPosts.size());
        return null;
    }

    // User

    public static User getUser(int userId) {

        // gets complete User data
        User user = users.get(userId);
        if (user == null) {
            Log.e(TAG, "no user in users of userId " + userId);
        } else if (user.isComplete()) {
            return user;
        }

        String url = InstaURL.getUser(userId);
        JSONObject object = getObject(url, false);
        user = new User(InstaJSON.getDataObject(object));
        addUser(user);

        return user;
    }

    public static User getCachedUser(int userId) {
//        Log.d(TAG,"getCachedUser userId = " + userId);
        User user = users.get(userId);
        if (user == null) {
            Log.e(TAG, "getCachedUser user==null");
        } else if (user.id < 0) {
            Log.e(TAG, "user.id = " + user.id);
        }
        return user;
    }

    public static ArrayList<User> getFollowsList(int userId, boolean fresh) {

        if (!fresh && userId < 0 && iFollowList != null) return iFollowList;

        String url = InstaURL.getFollowsList(userId);
        JSONObject object = getObject(url, fresh);
        ArrayList<User> followsList = InstaJSON.getUserArraylist(object);

        if (userId < 0) iFollowList = followsList;

        putUsers(followsList);
        return followsList;
    }

    public static ArrayList<User> getFollowewdByList(int userId, boolean fresh) {
        if (!fresh && userId < 0 && followedBy != null) return followedBy;
        String url = InstaURL.getFollowedByList(userId);
        JSONObject object = getObject(url, fresh);
        ArrayList<User> newFollowedBy = InstaJSON.getUserArraylist(object);
        if (userId < 0) followedBy = newFollowedBy;
        putUsers(newFollowedBy);
        return newFollowedBy;
    }

    private static JSONObject getObject(String url, boolean fresh) {
        if (!fresh && objects.containsKey(url)) {
            Log.d(TAG, "got objects(url) from cache");
            return objects.get(url);
        }
        JSONObject object = HTTPHandler.downloadObject(url);
        if (object != null) objects.put(url, object);
        return object;
    }
}
