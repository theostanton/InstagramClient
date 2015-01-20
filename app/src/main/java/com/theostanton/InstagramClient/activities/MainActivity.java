package com.theostanton.InstagramClient.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.theostanton.InstagramClient.bitmap.BitmapHandler;
import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.fragments.BaseFragment;
import com.theostanton.InstagramClient.fragments.FabFragment;
import com.theostanton.InstagramClient.fragments.PostsFragment;
import com.theostanton.InstagramClient.fragments.SettingsFragment;
import com.theostanton.InstagramClient.fragments.UserFragment;
import com.theostanton.InstagramClient.fragments.UsersFragment;
import com.theostanton.InstagramClient.fragments.header.HeaderFragment;
import com.theostanton.InstagramClient.fragments.header.LowerHeaderFragment;
import com.theostanton.InstagramClient.fragments.post.PostFragment;
import com.theostanton.InstagramClient.helpers.ViewHelper;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.listeners.OnPostSelectedListener;
import com.theostanton.InstagramClient.listeners.OnUserSelectedListener;
import com.theostanton.InstragramClient.R;

public class MainActivity extends Activity implements LowerHeaderFragment.OnFooterSelectedListener, OnPostSelectedListener, OnUserSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener, BaseFragment.OnFragmentScrollListener {

    public static final String BACK_INTENT = "Back intent";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BACK_INTENT)) {
                onBackPressed();
                return;
            }
            if (action.equals(HeaderFragment.HEIGHT_CHANGE_INTENT)) {
                int headerHeight = intent.getIntExtra(HeaderFragment.HEIGHT_EXTRA, getResources().getDimensionPixelSize(R.dimen.header_contracted));
//                drawerList.setTranslationY(headerHeight);
                return;
            }
        }
    };
    public static final String SET_HEADER_USER_INTENT = "set user header intent";
    public static final String USER_ID_EXTRA = "User ID extra";
    public static final String POST_ID_ARG = "Post ID argument";
    private static final String TAG = "MainActivity";
    // TODO arrange these better
    private static final String[] drawerTitles = {
            "Popular",
            "My Feed",
            "My Likes",
            "I Follow",
            "Followers",
            "Settings"
    };
    private SharedPreferences prefs;
    private BitmapHandler bitmapHandler;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private HeaderFragment headerFragment;
    private FabFragment fabFragment;

    private Instagram instagram;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instagram = Instagram.getInstance();

        float density = getResources().getDisplayMetrics().density;
        ViewHelper.setDensity(density);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        Instagram.getInstance(null);
        bitmapHandler = BitmapHandler.getInstance(getApplicationContext());

        assignPreferences();


        setContentView(R.layout.main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setOnItemClickListener(this);
        drawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, drawerTitles));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        PostsFragment postsFragment = new PostsFragment();
        transaction.add(R.id.main_container, postsFragment);

        headerFragment = new HeaderFragment();
        transaction.add(R.id.header_container, headerFragment);

        fabFragment = new FabFragment();
        transaction.add(R.id.fabs_container, fabFragment);


        transaction.commit();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    Intent intent = new Intent(HeaderFragment.TITLE_ACTION);
                    intent.putExtra(HeaderFragment.TITLE_EXTRA, "Instagram");
                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void assignPreferences() {

        boolean cacheJsonMemory = prefs.getBoolean(getResources().getString(R.string.cache_json_to_memory), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SET_HEADER_USER_INTENT);
        intentFilter.addAction(BACK_INTENT);
        intentFilter.addAction(HeaderFragment.HEIGHT_CHANGE_INTENT);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

//    private void setHeaderFromPost(String postId) {
//        Intent intent = new Intent(HeaderFragmentOLD.POST_FRAG_INTENT);
//        intent.putExtra(HeaderFragmentOLD.POST_ID_EXTRA, postId);
//        sendBroadcast(intent);
//    }

//    private void setHeaderFromUser(int userId, int footerSelected) {
//        Intent intent = new Intent(HeaderFragmentOLD.USER_FRAG_INTENT);
//        intent.putExtra(HeaderFragmentOLD.USER_ID_EXTRA, userId);
//        intent.putExtra(HeaderFragmentOLD.FOOTER_SELECTED_EXTRA, footerSelected);
//        sendBroadcast(intent);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(TAG, "onItemClick)");

        Fragment fragment;
        Bundle args = new Bundle();
//        args.putString(HeaderFragment.POST_ID_EXTRA, drawerTitles[position]);


        switch (position) {
            case PostsFragment.POPULAR_LIST:
                Log.d(TAG, "select popular from list");
                fragment = new PostsFragment();
                args.putInt(PostsFragment.POSTS_LIST_ARG, PostsFragment.POPULAR_LIST);
                args.putInt(PostsFragment.POSTS_TYPE_ARG, PostsFragment.GRID_TYPE);
                args.putString(PostsFragment.TITLE_ARG, drawerTitles[position]);
                fragment.setArguments(args);
                break;
            case PostsFragment.MY_FEED_LIST:
                Log.d(TAG, "select my feed from list");
                fragment = new PostsFragment();
                args.putInt(PostsFragment.POSTS_LIST_ARG, PostsFragment.MY_FEED_LIST);
                args.putInt(PostsFragment.POSTS_TYPE_ARG, PostsFragment.GRID_TYPE);
                args.putString(PostsFragment.TITLE_ARG, drawerTitles[position]);
                fragment.setArguments(args);
                break;
            case PostsFragment.MY_LIKES_LIST:
                Log.d(TAG, "select my feed from list");
                fragment = new PostsFragment();
                args.putInt(PostsFragment.POSTS_LIST_ARG, PostsFragment.MY_LIKES_LIST);
                args.putInt(PostsFragment.POSTS_TYPE_ARG, PostsFragment.GRID_TYPE);
                args.putString(PostsFragment.TITLE_ARG, drawerTitles[position]);
                fragment.setArguments(args);
                break;
            case PostsFragment.I_FOLLOW:
                Log.d(TAG, "select I Follow from list");
                fragment = new UsersFragment();
                args.putInt(UsersFragment.LIST_TYPE_ARG, UsersFragment.FOLLOWS_LIST);
                fragment.setArguments(args);
                break;
            case PostsFragment.FOLLOWERS:
                Log.d(TAG, "select Followers from list");
                fragment = new UsersFragment();
                args.putInt(UsersFragment.LIST_TYPE_ARG, UsersFragment.FOLLOWED_BY_LIST);
                fragment.setArguments(args);
                break;
            case PostsFragment.SETTINGS:
                Log.d(TAG, "select Settings from list");
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new Fragment();
        }
        setFragment(fragment);


        drawerLayout.closeDrawers();
    }

    private void setFragment(Fragment fragment) {


        if (fragment instanceof PostFragment) fabFragment.show();
        else fabFragment.hide();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPostSelected(Post post) {


        String postId = post.getId();

//        setHeaderFromPost(postId);

        PostFragment postFragment = new PostFragment();


        Bundle args = new Bundle();
        args.putString(POST_ID_ARG, postId);
        postFragment.setArguments(args);

        setFragment(postFragment);
    }

    private void onUserSelected(int userId, int footerSelected) {
//        setHeaderFromUser(userId, footerSelected);
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(UserFragment.USER_ID_ARG, userId);
        args.putInt(UserFragment.TRANSLATION_Y_ARG, headerFragment.getExpansion());
        args.putInt(UserFragment.FOOTER_SELECTED_ARG, footerSelected);
        fragment.setArguments(args);
        setFragment(fragment);
    }

    @Override
    public void onUserSelected(int userId) {
        Log.e(TAG, "ignore this onUserSelected");
        onUserSelected(userId, UserFragment.POSTS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed()");
    }

    @Override
    public void onClick(View v) {
//        headerFragment.zoom();
//        if (v.getId() == R.id.image_header) {
//            Log.d(TAG, "click image header");
//
//        } else if (v.getId() == R.id.title_header || v.getId() == R.id.username_title_header) {
//            Log.d(TAG, "click header title");
//        } else {
//            Log.d(TAG, "click header");
//        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            Log.d(TAG, "hasFocus");
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        } else {
//            Log.d(TAG, "!hasFocus");
//        }
    }

    @Override
    public void onFragmentScroll(int pos) {

    }

    @Override
    public void onFooterSelected(int userId, int footer) {
        Log.d(TAG, "onFooterSelected ");
        onUserSelected(userId, footer);
    }


}
