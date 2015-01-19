package com.theostanton.InstagramClient.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.theostanton.InstagramClient.adapters.PostsAdapter;
import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.fragments.header.HeaderFragment;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.listeners.OnPostSelectedListener;
import com.theostanton.InstragramClient.R;

import java.util.ArrayList;

/**
 * Created by theo on 27/12/14.
 */
public class PostsFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String POSTS_LIST_ARG = "Posts list argument";
    public static final String POSTS_TYPE_ARG = "Posts type argument";
    public static final String USERID_ARG = "User id argument";
    public static final String TITLE_ARG = "title_argument";
    public static final int GRID_TYPE = 0;
    public static final int LIST_TYPE = 1;
    public static final int POPULAR_LIST = 0;
    public static final int MY_FEED_LIST = 1;
    public static final int MY_LIKES_LIST = 2;
    public static final int USER_FEED_LIST = 3;
    public static final int I_FOLLOW = 3;
    public static final int FOLLOWERS = 4;
    public static final int SETTINGS = 5;
    private static final String TAG = "PostsFragment";


    OnPostSelectedListener mCallback;
    private Context context;
    private View view;  // TODO: find a way to avoid variable
    private PostsAdapter postsAdapter;

    private int postsType;
    private int postsList;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridView gridView;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        View child = view.getChildAt(0);
        if (child != null && firstVisibleItem == 0) {

            int y = child.getTop();

            announceScrollY(-y);
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnPostSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPopularListItemSelceted");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Post post = (Post) adapterView.getItemAtPosition(i);
        mCallback.onPostSelected(post);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        if (savedInstanceState != null)
            Log.d(TAG, "savedinstancestate = " + savedInstanceState.toString());

        context = inflater.getContext();


        if (getArguments() != null) {
            postsList = getArguments().getInt(POSTS_LIST_ARG, -1);
            postsType = getArguments().getInt(POSTS_TYPE_ARG, -1);
            String title = getArguments().getString(TITLE_ARG, "No title argument");
            updateHeadersTitle(title);

        } else {
            postsList = MY_FEED_LIST;
            postsType = GRID_TYPE;
        }

        switch (postsType) {
            case GRID_TYPE:
                view = inflater.inflate(R.layout.posts_grid_fragment, container, false);
                gridView = (GridView) view.findViewById(R.id.gridView);
                gridView.setOnItemClickListener(this);
                break;
            case LIST_TYPE:
                view = inflater.inflate(R.layout.posts_lists_fragment, container, false);
                break;
            default:
                view = inflater.inflate(R.layout.posts_grid_fragment, container, false);
                break;
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        populate(false);

        return view;
    }

    private void populate(final boolean fresh) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Post> posts;
                switch (postsList) {
                    case POPULAR_LIST:
                        posts = Instagram.getPopular(fresh);
                        break;
                    case MY_FEED_LIST:
                        posts = Instagram.getMyFeed(fresh);
                        Log.d(TAG, "got my feed " + posts.size());
                        break;
                    case MY_LIKES_LIST:
                        posts = Instagram.getMyLikes(fresh);
                        break;
                    case USER_FEED_LIST:
                        int userId = getArguments().getInt(USERID_ARG, -3);
                        posts = Instagram.getFeed(userId, fresh);
                        break;
                    default:
                        posts = Instagram.getPopular(fresh);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        switch (postsType) {
                            case GRID_TYPE:
                                populateGrid(posts);
                                break;
                            case LIST_TYPE:
                                populateList(posts);
                                break;
                            default:
                                populateGrid(posts);
                                break;
                        }
                    }
                });
            }
        }).start();
    }

    private void populateGrid(ArrayList<Post> posts) {

        if (postsAdapter == null) {
            postsAdapter = new PostsAdapter(context, R.layout.posts_grid_item, posts);
            gridView.setAdapter(postsAdapter);
        } else {
            postsAdapter.ammendList(posts);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void populateList(ArrayList<Post> posts) {
        postsAdapter = new PostsAdapter(context, R.layout.posts_list_item, posts);
        ListView listView = (ListView) view.findViewById(R.id.posts_listview);
        listView.setAdapter(postsAdapter);
        listView.setOnItemClickListener(this);
    }


    private void updateHeadersTitle(String title) {
//        Intent intent = new Intent(HeaderFragment.POSTS_FRAG_INTENT);
//        intent.putExtra(HeaderFragment.TITLE_EXTRA, title);
//        getActivity().sendBroadcast(intent);
        Intent intent = new Intent(HeaderFragment.TITLE_ACTION);
        intent.putExtra(HeaderFragment.TITLE_EXTRA, title);
        getActivity().sendBroadcast(intent);
        Log.d(TAG, "updateHeadersTitle()");
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh()");
        populate(true);
    }
}
