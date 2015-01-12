package com.theostanton.InstagramClient.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class UsersFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    public static final String LIST_TYPE_ARG = "List type argument";
    public static final int FOLLOWS_LIST = 0;
    public static final int FOLLOWED_BY_LIST = 1;
    public static final String USERID_ARG = "User id argument";
    private static final String TAG = "UsersFragment";
    OnUserSelectedListener onUserSelectedListener;
    private Context context;
    private View view;

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
        context = inflater.getContext();

        setEnterTransition(new Fade());
        setExitTransition(new Fade());

        final int userId;
        final int listType;
        if (getArguments() == null) {
            userId = -1;
            listType = FOLLOWS_LIST;
        } else {
            userId = getArguments().getInt(USERID_ARG, -1);
            listType = getArguments().getInt(LIST_TYPE_ARG, FOLLOWS_LIST);
        }

        Log.d(TAG, "onCreateView userId = " + userId + " listType= " + listType);

        Intent intent = new Intent(HeaderFragment.TITLE_INTENT);
        if (listType == FOLLOWS_LIST) intent.putExtra(HeaderFragment.TITLE_EXTRA, "I Follow");
        else intent.putExtra(HeaderFragment.TITLE_EXTRA, "Followers");
        getActivity().sendBroadcast(intent);


        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<User> users;
                if (listType == FOLLOWS_LIST) users = Instagram.getFollowsList(userId);
                else users = Instagram.getFollowewdByList(userId);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateList(users);

                        }
                    });
                }

            }
        }).start();
        return view;
    }

    private void populateList(ArrayList<User> users) {
        UsersAdapter adapter = new UsersAdapter(context, users);
        ListView listView = (ListView) view.findViewById(R.id.users_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        User user = (User) parent.getItemAtPosition(position);
        onUserSelectedListener.onUserSelected(user.id);
    }

    private void updateHeadersTitle() {

    }
}
