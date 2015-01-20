package com.theostanton.InstagramClient.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.theostanton.InstagramClient.adapters.UsersAdapter;
import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.fragments.header.HeaderFragment;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.listeners.OnUserSelectedListener;
import com.theostanton.InstragramClient.R;

import java.util.ArrayList;

/**
 * Created by theo on 02/01/15.
 */
public class UsersFragment extends BaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String LIST_TYPE_ARG = "List type argument";
    public static final int FOLLOWS_LIST = 0;
    public static final int FOLLOWED_BY_LIST = 1;
    public static final String USERID_ARG = "User id argument";
    private static final String TAG = "UsersFragment";
    OnUserSelectedListener onUserSelectedListener;
    private Context context;
    private View view;

    private UsersAdapter usersAdapter;
    private ListView listView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int userId;
    private int listType;

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

        view = inflater.inflate(R.layout.users_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        context = inflater.getContext();

        setEnterTransition(new Fade());
        setExitTransition(new Fade());


        if (getArguments() == null) {
            userId = -1;
            listType = FOLLOWS_LIST;
        } else {
            userId = getArguments().getInt(USERID_ARG, -1);
            listType = getArguments().getInt(LIST_TYPE_ARG, FOLLOWS_LIST);
        }

        Log.d(TAG, "onCreateView userId = " + userId + " listType= " + listType);

        Intent intent = new Intent(HeaderFragment.TITLE_ACTION);
        if (listType == FOLLOWS_LIST) intent.putExtra(HeaderFragment.TITLE_EXTRA, "I Follow");
        else intent.putExtra(HeaderFragment.TITLE_EXTRA, "Followers");
        getActivity().sendBroadcast(intent);

        listView = (ListView) view.findViewById(R.id.users_list);
        listView.setOnItemClickListener(this);

        populate(true);

        return view;
    }


    private void populate(final boolean fresh) {
        new PopulateTask().execute(fresh);
    }

    private void populateList(ArrayList<User> users) {

        if (usersAdapter == null) {
            usersAdapter = new UsersAdapter(context, users);
            listView.setAdapter(usersAdapter);
            usersAdapter.notifyDataSetChanged();
        } else {
            usersAdapter.ammendList(users);
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        User user = (User) parent.getItemAtPosition(position);
        onUserSelectedListener.onUserSelected(user.id);
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh()");
        populate(true);
    }

    class PopulateTask extends AsyncTask<Boolean, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(Boolean... params) {
            if (listType == FOLLOWS_LIST) return Instagram.getFollowsList(userId, params[0]);
            else return Instagram.getFollowewdByList(userId, params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            populateList(users);
        }
    }
}
