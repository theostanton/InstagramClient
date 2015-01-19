package com.theostanton.InstagramClient.fragments.header;

import android.util.Log;

import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.instagram.Instagram;

/**
 * Created by theo on 17/01/15.
 */
public class HeaderStateHandler {

    private static final String TAG = "HeaderStateHandler";

    protected HeaderFragment header;
    protected UpperHeaderFragment upper;
    protected LowerHeaderFragment lower;

    private int TITLE = 0;
    private int POST = 1;
    private int USER = 2;

    private String title = null;
    private Post post = null;
    private User user = null;

    public HeaderStateHandler(HeaderFragment header, UpperHeaderFragment upper, LowerHeaderFragment lower) {
        this.header = header;
        this.upper = upper;
        this.lower = lower;
    }

    public void freshPost(String postId) {
        Log.d(TAG, "freshPost() postId=" + postId);

        if (post != null && postId.equals(post.getId())) {
            Log.d(TAG, "same post and same id, not sure this is possible");
            return;
        }

        // Shouldn't ever need to download
        Post newPost = Instagram.getPost(postId);
        upper.setPost(newPost);
        User postUser = newPost.getUser();
        if (postUser.isComplete()) return;
        else {
            // prefetches and sets postUser data but doesn't assign to this.user
            fetchCompleteUserAndPopulate(postUser.id, true);
        }
    }

    public void freshUser(int userId) {
        Log.d(TAG, "freshUser userId=" + userId);

        setState(USER);

        // same ids
        if (user != null && userId == user.id) {
            if (user.isComplete()) {
                Log.d(TAG, "same ids and already complete");
                return;
            }
            fetchCompleteUserAndPopulate();
            return;

        }

        // user==null or different user
        User newUser = Instagram.getCachedUser(userId);
        if (newUser == null) {
            Log.e(TAG, "cachedUser shouldn't ever be null");
            fetchCompleteUserAndPopulate(userId, false);
        } else if (newUser.isComplete()) {
            user = newUser;
            upper.setUser(user);
            lower.setValues(user);
        } else {
            user = newUser;
            upper.setUser(user);
            fetchCompleteUserAndPopulate();
        }
    }

    private void fetchCompleteUserAndPopulate() {
        // Only necessary if cacheduser == null whcih shouldn't happen
        fetchCompleteUserAndPopulate(-2, false);
    }

    private void fetchCompleteUserAndPopulate(int userId, final boolean postUser) {


        // Only necessary if cacheduser == null whcih shouldn't happen
        final int finalUserId;
        if (userId == -2) finalUserId = user.id;
        else finalUserId = userId;

        new Thread(new Runnable() {
            @Override
            public void run() {
                User completeUser = Instagram.getUser(finalUserId);
                setComplete(completeUser, postUser);
            }
        }).start();
    }

    private void setComplete(final User completeUser, final boolean postUser) {
        if (!postUser) user = completeUser;
        header.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                upper.setUser(completeUser, postUser);
                lower.setValues(completeUser);
            }
        });
    }

    public void freshTitle(String newTitle) {
        if (title == null || !title.equals(newTitle)) {
            clearViews();
            upper.setTitle(newTitle);
            title = newTitle;
            setState(TITLE);
        }
    }

    private void setState(int state) {
        // No actual state variable, goes by null states..
        if (state != TITLE) title = null;
        if (state != POST) post = null;
        if (state != USER) user = null;
    }

    private void clearViews() {
        upper.clearViews();
        lower.clearViews();
    }

}
