package com.theostanton.InstagramClient.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import com.theostanton.InstagramClient.adapters.PostsAdapter;
import com.theostanton.InstagramClient.adapters.UsersAdapter;
import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.fragments.header.HeaderFragment;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.listeners.OnPostSelectedListener;
import com.theostanton.InstagramClient.listeners.OnUserSelectedListener;
import com.theostanton.InstragramClient.R;

import java.util.ArrayList;

/**
 * Created by theo on 27/12/14.
 */
public class UserFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    public final static String FOOTER_SELECTED_ARG = "footer_selection_argument";
    public static final String TRANSLATION_Y_ARG = "translation_y_argument";
    public final static int POSTS = 0;
    public final static int LIKES = 1;
    public final static int FOLLOWS = 2;
    public final static int FOLLOWED_BY = 3;
    private static final String TAG = "UserFragment";
    public static String USER_ID_ARG = "User Id Argument";
    OnPostSelectedListener onPostSelectedListener;
    OnUserSelectedListener onuserSelectedListener;
    private View view;
    private GridView gridView = null;
    private ListView listView = null;
    private int translationY = 0;
    private int contractedHeaderheight;
    private int footerSelection = 0;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            translationY = intent.getIntExtra(HeaderFragment.HEIGHT_EXTRA, 50) - contractedHeaderheight;
            if (gridView != null) gridView.setTranslationY(translationY);
            if (listView != null) listView.setTranslationY(translationY);
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        View child = view.getChildAt(0);
        if(child!=null && firstVisibleItem==0) {

            int y = child.getTop();
            announceScrollY(-y);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            onPostSelectedListener = (OnPostSelectedListener) activity;
            onuserSelectedListener = (OnUserSelectedListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnPopularListItemSelceted");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        contractedHeaderheight = getResources().getDimensionPixelSize(R.dimen.header_contracted);

        Log.d(TAG,"onCreateView");

        final int userId = getArguments().getInt(USER_ID_ARG,-1);

        footerSelection = getArguments().getInt(FOOTER_SELECTED_ARG,POSTS);
        translationY = getArguments().getInt(TRANSLATION_Y_ARG,0) - contractedHeaderheight;

        Intent intent = new Intent(HeaderFragment.USER_FRAG_INTENT);
        intent.putExtra(HeaderFragment.USER_ID_EXTRA,userId);
        getActivity().sendBroadcast(intent);

        switch (footerSelection){
            case POSTS:
            case LIKES:
                view = inflater.inflate(R.layout.posts_grid_fragment,container,false);
                break;
            case FOLLOWED_BY:
            case FOLLOWS:
                view = inflater.inflate(R.layout.users_fragment,container,false);
//                translationY -= 150;
                break;
            default:
                return null;
        }



        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<User> users = null;
                ArrayList<Post> posts = null;
                switch (footerSelection){
                    case POSTS:
                        posts = Instagram.getFeed(userId);
                        break;
                    case LIKES:
                        posts = Instagram.getLikes(userId);
                        break;
                    case FOLLOWED_BY:
                        users = Instagram.getFollowewdByList(userId);
                        break;
                    case FOLLOWS:
                        users = Instagram.getFollowsList(userId);
                        break;
                    default:
                        Log.e(TAG,"switch defaulteed footerselection  =" + footerSelection);
                        return;

                }


                if(getActivity()==null) return;

                if(posts!=null){
                    populateGridView(posts);
                }
                else if(users!=null){
                    populateListView(users);
                }
            }
        }).start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(HeaderFragment.HEIGHT_CHANGE_INTENT);
        getActivity().registerReceiver(receiver,filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Object object = adapterView.getItemAtPosition(i);
        if(object instanceof Post){
            onPostSelectedListener.onPostSelected( (Post)object );
        }
        else{
            onuserSelectedListener.onUserSelected( ((User)object).id );
        }
    }

    private void populateListView(final ArrayList<User> users){
        final Fragment fragment = this;
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listView = (ListView) view.findViewById(R.id.users_list);

                UsersAdapter usersAdapter = new UsersAdapter(view.getContext(), users);
                listView.setAdapter(usersAdapter);
                listView.setOnItemClickListener((AdapterView.OnItemClickListener) fragment);
                listView.setTranslationY(translationY);
            }
        });
    }

    private void populateGridView(final ArrayList<Post> posts){

        final Fragment fragment = this;

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                gridView = (GridView) view.findViewById(R.id.gridView);

                PostsAdapter postsAdapter = new PostsAdapter(view.getContext(), R.layout.posts_grid_item, posts);
                gridView.setAdapter(postsAdapter);
                gridView.setOnItemClickListener((AdapterView.OnItemClickListener) fragment);
                Log.d(TAG,"gridView populated, translationY = " + translationY);
                gridView.setTranslationY(translationY);
            }
        });

    }
}
