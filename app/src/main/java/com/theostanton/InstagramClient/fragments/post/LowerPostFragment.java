package com.theostanton.InstagramClient.fragments.post;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.theostanton.InstagramClient.activities.MainActivity;
import com.theostanton.InstagramClient.adapters.CommentAdapter;
import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.fragments.BaseFragment;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.listeners.OnUserSelectedListener;
import com.theostanton.InstagramClient.views.ScrollessScrollView;
import com.theostanton.InstragramClient.R;


/**
 * Created by theo on 30/12/14.
 */
public class LowerPostFragment extends BaseFragment {

    protected static final String TAG = "LowerPostFragment";

    OnUserSelectedListener onUserSelectedListener;

    private TextView likesTextView;
    private float maxLikesTextViewHeight = -1;
    private int contractedHeaderHeight;

    private int scrollHeader = -1;

    private int lastPos = 0;

    private GestureDetector gestureDetector;

    private ScrollessScrollView commentsScrollView;
    private LinearLayout commentsLinearLayout;
    private ListView commentsListView;
    private boolean scrolling = false;
    private float firstPos = 0;
    private float dy = 0;
    private float lastY = 0;
    private float maxScroll = 999999.0f;


//    @Override
//    public void onClick(View v) {
//        Log.d(TAG,"onClick");
//        int userId = (int) v.getTag();
//        onUserSelectedListener.onUserSelected(userId);
//    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onUserSelectedListener = (OnUserSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPopularListItemSelceted");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lower_post_fragment, container, false);

        contractedHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_contracted);


        String postId = getArguments().getString(MainActivity.POST_ID_ARG);
        Post post = Instagram.getPost(postId);

        // populate comments list

        commentsScrollView = (ScrollessScrollView) view.findViewById(R.id.comments_scrollview);
        commentsScrollView.setScrollingEnabled(false);
        commentsLinearLayout = (LinearLayout) view.findViewById(R.id.comments_layout_post_fragment);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.comments_header, commentsListView, false);
        commentsLinearLayout.addView(header);

        CommentAdapter commentAdapter = new CommentAdapter(inflater.getContext(), post.getComments());

        for (int i = 0; i < commentAdapter.getCount(); i++) {
            View comment = commentAdapter.getView(i, null, null);
            commentsLinearLayout.addView(comment);
        }

        //set likes names
        likesTextView = (TextView) header.findViewById(R.id.likes_textview_post_fragment);
        likesTextView.setText(post.getLikesNames());

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setScroll(float scroll) {
        commentsScrollView.scrollTo(0, -(int) scroll);
    }

    public void fling(int velocityY) {
        commentsScrollView.fling(velocityY);
    }

    public void scrollBy(float distanceY) {

        float newScrollTo = commentsScrollView.getScrollY() + distanceY;

        Log.d(TAG, "scrollby " + distanceY + " scrollY=" + commentsScrollView.getScrollY() + " to " + newScrollTo);
        if (newScrollTo < 0) {
            commentsScrollView.scrollTo(0, 0);
        } else if (newScrollTo > commentsLinearLayout.getHeight()) {
            commentsScrollView.scrollTo(0, commentsLinearLayout.getHeight());
        } else commentsScrollView.scrollBy(0, (int) distanceY);


    }

    public void clickAtY(int y) {
        y += commentsScrollView.getScrollY();
        for (int i = 0; i < commentsLinearLayout.getChildCount(); i++) {
            View view = commentsLinearLayout.getChildAt(i);
            int vTop = view.getTop();
            int vBot = view.getBottom();
            view.callOnClick();
//            Log.d(TAG,"top " + vTop + " vBot " + vBot);
            if (y > vTop && y < vBot) {
                if (view.getTag() == null) {
                    Log.e(TAG, "Click header");
                } else {
                    int userId = (int) view.getTag();
                    Log.d(TAG, "click view " + i + " " + userId);
                    onUserSelectedListener.onUserSelected(userId);
                }
                return;
            }
        }
        Log.d(TAG, "none");
    }
}
