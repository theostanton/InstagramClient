package com.theostanton.InstagramClient.fragments.post;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.theostanton.InstagramClient.activities.MainActivity;
import com.theostanton.InstagramClient.adapters.CommentAdapter;
import com.theostanton.InstagramClient.data.Comment;
import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.fragments.BaseFragment;
import com.theostanton.InstagramClient.helpers.ViewHelper;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.listeners.OnUserSelectedListener;
import com.theostanton.InstragramClient.R;


/**
 * Created by theo on 30/12/14.
 */
public class LowerPostFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, AbsListView.OnScrollListener {

    protected static final String TAG = "LowerPostFragment";

    OnUserSelectedListener onUserSelectedListener;

    private TextView likesTextView;
    private float maxLikesTextViewHeight = -1;
    private ImageButton addCommentButton;
    private int contractedHeaderHeight;

    private int scrollHeader = -1;

    private int lastPos = 0;
    private float scroll;

    private ListView commentsListView;


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Comment comment = (Comment) parent.getItemAtPosition(position);
        onUserSelectedListener.onUserSelected(comment.getUserId());
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Add Comment clicked");
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        View child = view.getChildAt(0);


        if (scrollHeader < 0 && view.getChildAt(0) != null) {
//            ListView listView = (ListView) getView().findViewById(R.id.comments_listview_post_fragment);

            // Decides whether to scroll both fragments or just lowerFragment
            int listHeight = ViewHelper.getTotalHeightofListView((ListView) view);
            int viewHeight = getView().getHeight();
            Log.d(TAG, "listHeight = " + listHeight + " viewheight = " + viewHeight + " diff = " + (listHeight - viewHeight));
            if (listHeight - viewHeight < contractedHeaderHeight) scrollHeader = 0;
            else scrollHeader = 1;

        } else if (scrollHeader == 1) {

            if (view.getChildAt(0) != null && (firstVisibleItem == 0 || firstVisibleItem == 1)) {
                int y = view.getChildAt(0).getTop();


                if (firstVisibleItem == 1) y -= view.getChildAt(0).getMeasuredHeight();
//                Log.d(TAG,"firstVisibleitem = "+ firstVisibleItem + " 0=" + view.getChildAt(0).getTop() + " 1=" + view.getChildAt(1).getTop() );
                if (y != lastPos) {
                    announceScrollY(y);
                    //                Log.d(TAG, "y = " + y);
                    lastPos = y;
                }
            } else {
                announceScrollY(-999);
            }
        }
    }

    public void setOffsetRatio(float offsetRatio) {
        if (maxLikesTextViewHeight == -1) {
            maxLikesTextViewHeight = likesTextView.getMeasuredHeight();
            Log.d(TAG, "maxLikesTextViewHeight = " + maxLikesTextViewHeight);
        }
//        int likesHeight = (int)(maxLikesTextViewHeight * (1.0f - offsetRatio) );
//        likesTextView.getLayoutParams().height = likesHeight;
//        likesLinearLayout.requestLayout();
    }

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
//        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lower_post_fragment, container, false);

        contractedHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_contracted);


//        maxOffset = - 50.0f * getResources().getDisplayMetrics().density;

        String postId = getArguments().getString(MainActivity.POST_ID_ARG);
        Post post = Instagram.getPost(postId);
        // populate comments list
        CommentAdapter commentAdapter = new CommentAdapter(inflater.getContext(), post.getComments());
        commentsListView = (ListView) view.findViewById(R.id.comments_listview_post_fragment);
        commentsListView.setAdapter(commentAdapter);
        commentsListView.setOnItemClickListener(this);
        commentsListView.setOnScrollListener(this);
        myLastVisiblePos = commentsListView.getFirstVisiblePosition();
//        Theo.setListViewHeightBasedOnChildren(listView);


//        addCommentButton = (ImageButton) view.findViewById(R.id.add_comment_button);
//        addCommentButton.setOnClickListener(this);

        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.comments_header, commentsListView, false);
        commentsListView.addHeaderView(header, null, false);


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
        commentsListView.scrollTo(0, -(int) scroll);
    }
}
