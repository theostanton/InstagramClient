package com.theostanton.InstagramClient.fragments.header;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Space;
import android.widget.TextView;

import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.fragments.BaseFragment;
import com.theostanton.InstagramClient.fragments.UserFragment;
import com.theostanton.InstagramClient.helpers.ViewHelper;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstagramClient.views.UserImageView;
import com.theostanton.InstragramClient.R;

import java.util.ArrayList;

/**
 * Created by theo on 04/01/15.
 */
public class HeaderFragment extends BaseFragment implements View.OnClickListener {

    //INCOMING
    public static final String TITLE_INTENT = "title_intent";
    public static final String USERS_FRAG_INTENT = "users_frag_intent";
    public static final String USER_FRAG_INTENT = "user_frag_intent";
    public static final String POST_FRAG_INTENT = "post_frag_intent";
    public static final String POSTS_FRAG_INTENT = "posts_frag_intent";
    public static final String ALPHA_INTENT = "alpha_intent";
    public static final String EXPAND_INTENT = "expand_intent";
    public static final String COLLAPSE_INTENT = "collapse_intent";
    public static final String USER_ID_EXTRA = "user_id_extra";
    public static final String POST_ID_EXTRA = "post_id_extra";
    public static final String TITLE_EXTRA = "title_extra";
    public static final String ALPHA_EXTRA = "alpha_extra";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TITLE_INTENT)) {
                final String title = intent.getStringExtra(TITLE_EXTRA);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFromTitle(title);
                    }
                });
            }

            if (action.equals(EXPAND_INTENT)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        expand();
                    }
                });
                return;
            }

            if (action.equals(COLLAPSE_INTENT)) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contract();
                    }
                });
                return;
            }

            if (action.equals(USER_FRAG_INTENT)) {
                final int userId = intent.getIntExtra(USER_ID_EXTRA, -1);
                setFromUser(userId);
                return;
            }

            if (action.equals(POST_FRAG_INTENT)) {
                final String postId = intent.getStringExtra(POST_ID_EXTRA);
                setFromPost(postId);
                return;
            }

            if (action.equals(USERS_FRAG_INTENT)) {
                final int userId = intent.getIntExtra(USER_ID_EXTRA, -1);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setMinimumFromUser(userId);
                    }
                });
                return;
            }

            if (action.equals(POSTS_FRAG_INTENT)) {
                final String title = intent.getStringExtra(TITLE_EXTRA);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFromTitle(title);
                    }
                });
                return;
            }

            if (action.equals(ALPHA_INTENT)) {
//                Log.e(TAG,"ALPHA_INTENT deprecated");
                final float alpha = intent.getFloatExtra(ALPHA_EXTRA, 0.5f);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAlpha(alpha);
                    }
                });
            }
        }
    };
    // OUTGOING
    public static final String HEIGHT_CHANGE_INTENT = "height_change_intent";
    public static final String HEIGHT_EXTRA = "height_extra";
    private static final String TAG = "HeaderFragment";
    private static final int NUM_FOOTERS = 4;
    private static int TOP_MARGIN_EXPANDED = 10;
    private Handler handler;
    private View view;
    private ValueAnimator expansionAnimation = new ValueAnimator();
    // these are all reassigned dp equivs in createVIew
    private int expandedHeight = 450;
    private int contractedHeight = 150;
    private int heightDiff = expandedHeight - contractedHeight;
    private int userimageMaxSize = 450;
    private float currExpandFraction = 0.0f;
    private View footerPointerView;
    private int footerPos = 0;
    private int footerWidth = 0;
    private float footerCurrTranslation = 0.0f;
    private ValueAnimator footerPointerAnimation = new ValueAnimator();
    private Post post;
    private User user;
    private boolean expanded = false;
    private ViewHolder v;
    private TextView postsTextView;
    private TextView likesTextView;
    private TextView followsTextView;
    private TextView followedbyTextView;
    private boolean numbersPopulated = false;
    private TextView postsNumberView;
    private TextView likesNumberView;
    private TextView followsNumberView;
    private TextView followedbyNumberView;
    private TextView bioTextView;
    private TextView websiteTextView;
    private Space topMargin;
    private Space midVerticalMargin;
    private ArrayList<View> viewsVisibleOnExpansion;
    private OnFooterSelectedListener onFooterSelected;

    public int getExpansion() {
        return view.getLayoutParams().height;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            onFooterSelected = (OnFooterSelectedListener) activity;
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick");
        int id = v.getId();

        if(!expanded) toggleExpansion();

        switch (id){
            case R.id.header_follows_number :
            case R.id.header_follows_text :
                Log.d(TAG,"click Follows footer");
                onFooterSelected.onFooterSelected(user.id, UserFragment.FOLLOWS);
                animateFooterPointer(2);
                break;
            case R.id.header_followedby_number :
            case R.id.header_followedby_text :
                Log.d(TAG,"click Followers footer");
                onFooterSelected.onFooterSelected(user.id, UserFragment.FOLLOWED_BY);
                animateFooterPointer(3);
                break;
            case R.id.header_likes_number :
            case R.id.header_likes_text :
                Log.e(TAG,"click Likes footer, ignore");
//                onFooterSelected.onFooterSelected(user.id, UserFragment.LIKES);
//                animateFooterPointer(1);
                break;
            case R.id.header_posts_text :
            case R.id.header_posts_number :
                Log.d(TAG,"click Posts footer");
                onFooterSelected.onFooterSelected(user.id, UserFragment.POSTS);
                animateFooterPointer(0);
                break;
            default:
                Log.d(TAG,"switch defualted");
                toggleExpansion();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        HandlerThread handlerThread = new HandlerThread("Header Receiver Thread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        handler = new Handler(looper);

        IntentFilter filter = new IntentFilter();
        filter.addAction(USER_FRAG_INTENT);
        filter.addAction(POST_FRAG_INTENT);
        filter.addAction(POSTS_FRAG_INTENT);
        filter.addAction(ALPHA_INTENT);
        filter.addAction(EXPAND_INTENT);
        filter.addAction(COLLAPSE_INTENT);
        filter.addAction(TITLE_INTENT);

        getActivity().registerReceiver(receiver, filter, null, handler);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int contracted =  getResources().getDimensionPixelSize(R.dimen.header_contracted);
        Log.d(TAG,"set contractedHeight to "+ contracted);
        contractedHeight = contracted;
        userimageMaxSize = 2*contractedHeight;
        int footerHeight = getResources().getDimensionPixelSize(R.dimen.footer_number_height);
        footerHeight += getResources().getDimensionPixelSize(R.dimen.footer_text_height);
        expandedHeight = footerHeight + userimageMaxSize + 4*TOP_MARGIN_EXPANDED;
        Log.d(TAG, "exoandedhegiht = " + expandedHeight + " footerHeight = " + footerHeight + " userImageMax=" + userimageMaxSize);
        heightDiff = expandedHeight - contractedHeight;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_header_fragment, container, false);

        view.setOnClickListener(this);

        post = null;
        user = null;

        topMargin =  (Space) view.findViewById(R.id.top_margin_header);
        midVerticalMargin =  (Space) view.findViewById(R.id.vertical_mid_margin_header);
        Space bottomMargin =  (Space) view.findViewById(R.id.bottom_margin_header);
        bottomMargin.getLayoutParams().height = TOP_MARGIN_EXPANDED;
        midVerticalMargin.getLayoutParams().width = TOP_MARGIN_EXPANDED;

        v = new ViewHolder();
        v.mainTextView = (TextView) view.findViewById(R.id.title_text_header);
        v.singleTextView = (TextView) view.findViewById(R.id.single_text_header);
        v.subTextView = (TextView) view.findViewById(R.id.sub_text_header);
        v.detailTextView = (TextView) view.findViewById(R.id.detail_text_header);
        v.userImageView = (UserImageView) view.findViewById(R.id.user_image_header);

        bioTextView = (TextView) view.findViewById(R.id.bio_text_header);
        websiteTextView = (TextView) view.findViewById(R.id.website_text_header);

        postsTextView = (TextView) view.findViewById(R.id.header_posts_text);
        likesTextView = (TextView) view.findViewById(R.id.header_likes_text);
        followsTextView = (TextView) view.findViewById(R.id.header_follows_text);
        followedbyTextView = (TextView) view.findViewById(R.id.header_followedby_text);

        postsNumberView = (TextView) view.findViewById(R.id.header_posts_number);
        likesNumberView = (TextView) view.findViewById(R.id.header_likes_number);
        followsNumberView = (TextView) view.findViewById(R.id.header_follows_number);
        followedbyNumberView = (TextView) view.findViewById(R.id.header_followedby_number);

        viewsVisibleOnExpansion = new ArrayList<View>();

        viewsVisibleOnExpansion.add(bioTextView);
        viewsVisibleOnExpansion.add(websiteTextView);

        viewsVisibleOnExpansion.add(postsTextView);
        viewsVisibleOnExpansion.add(followsTextView);
        viewsVisibleOnExpansion.add(likesTextView);
        viewsVisibleOnExpansion.add(followedbyTextView);

        viewsVisibleOnExpansion.add(postsNumberView);
        viewsVisibleOnExpansion.add(likesNumberView);
        viewsVisibleOnExpansion.add(followsNumberView);
        viewsVisibleOnExpansion.add(followedbyNumberView);


        footerPointerView = view.findViewById(R.id.footer_pointer);

        footerWidth = getResources().getDisplayMetrics().widthPixels / NUM_FOOTERS;
        footerPointerView.getLayoutParams().width = footerWidth;
        viewsVisibleOnExpansion.add(footerPointerView);

        for(View view:viewsVisibleOnExpansion){
            view.setOnClickListener(this);
        }

        setFromTitle("Instagram");

        return view;

    }

    private void updateUserImage(){
        v.userImageView.setUserId(user.id);
    }

    public void setFromTitle(String title, String subTitle){
        setTitles(title,subTitle);
        v.detailTextView.setText("");
        v.userImageView.useResource();
        contract();
    }

    public void setFromTitle(String title){
        setFromTitle(title, null);
    }


    // SetFrom methods reassess whole layout if necessary

    public void setFromPost(String postId){

        if(post!=null && post.getId().equals(postId)){
            Log.d(TAG,"setFromPost postIds match, return");
            return;
        }

        final Post post = Instagram.getPost(postId);
        if(post!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setFromPost(post);
                }
            });
        }
        else{
            try {
                throw new Exception("post==null postId= " + postId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFromPost(Post newPost){

        Log.d(TAG, "setFromPost()");

        contract();

        if(user!=null && newPost.getUser().id==user.id){
            Log.d(TAG,"setFromPost userIds match");
        }
        else{
            user = newPost.getUser();
            setTitles(user.fullName, user.userName);
            updateUserImage();
        }

        post = newPost;

        v.detailTextView.setText(newPost.getTimeAgo());
        contract();

    }

    private void setTitles(String main, String sub){
        if(sub==null || sub.length()==0){
            v.mainTextView.setText("");
            v.subTextView.setText("");
            v.singleTextView.setText(main);
        }
        else{
            v.mainTextView.setText(main);
            v.subTextView.setText(sub);
            v.singleTextView.setText("");
        }
    }

    private void fromPostToSameUser(){
        post = null;
        v.detailTextView.setText("");
        expand();
    }

    public void setFromUser(int userId){
        if(user.id==userId){
            Log.d(TAG, "setFromUser ids match, return");
            if(post!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fromPostToSameUser();
                    }
                });
            }
            return;
        }
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                contract();
//            }
//        });

        final User user = Instagram.getUser(userId);
        if(user!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setFromUser(user);
                }
            });
        }
        else{
            Log.e(TAG, "user==null");
        }
    }

    public void setMinimumFromUser(int userId){

        contract();

        if(user.id==userId) {
            Log.d(TAG, "setFromUser ids match, conracted and returned");
            return;
        }

        User newUser = Instagram.getCachedUser(userId);
        if(newUser==null){
            Log.e(TAG,"newUser==null in setMinimumFromUser");
            return;
        }
        if(newUser.id<0){
            Log.e(TAG,"newUser.id = " + newUser.id + " in setMinimumFromUser");
            return;
        }

        user = newUser;
        post = null;

        clearFooters();
        setTitles(user.fullName,user.userName);
        v.detailTextView.setText("");
        updateUserImage();

        Log.d(TAG, "done setMinimumFromUser");
    }

    public void setFromUser(User newUser){

        animateFooterPointer(0);

        Log.d(TAG,"setFromUser()");

        user = newUser;
        post = null;

        setTitles(user.fullName,user.userName);
        v.detailTextView.setText("");
        updateUserImage();
        populateNumbers();
        expand();
    }

    public void setY(int y){
        if(getView()!=null) getView().setY(y);
    }

    public void setAlpha(float alpha){
        view.setAlpha(alpha);
    }

    // Root effects

    public void toggleExpansion(){
        if(expanded) contract();
        else expand();
    }

    public void expand(){
        setAlpha(1.0f);
        if(user==null) return;
        if(expanded) return;

        if(!numbersPopulated) {
            populateNumbers();
            // populateNumbers() starts thread and calls expand on completion, so return here
            return;
        }

        calculateExpandedHeight();


        animateToFraction(1.0f);
        expanded = true;

    }

    public void contract(){
        setAlpha(1.0f);
//        clearFooters();
        if(!expanded) return;
        animateToFraction(0.0f);
        expanded = false;
    }

    private void calculateExpandedHeight(){

        boolean calculate = false;
        if(calculate){
            int height = 2* TOP_MARGIN_EXPANDED; // add mid margin at same time
            Log.d(TAG,"2*top margin = " + height);

            int addHeight = v.mainTextView.getMeasuredHeight();
            Log.d(TAG,"v.mainTextView= " + addHeight);

            height += getTextViewHeight(v.subTextView);
            height += getTextViewHeight(bioTextView);
            height += getTextViewHeight(websiteTextView);

            Log.d(TAG,"height vs maxImage is " + height + " vs " + userimageMaxSize);
            height = Math.max(userimageMaxSize,height);

            addHeight = followsNumberView.getMeasuredHeight();
            Log.d(TAG,"followsNumberView " + addHeight);
            height += addHeight;

            addHeight = followsTextView.getMeasuredHeight();
            Log.d(TAG,"followsTextView " + addHeight);
            height += addHeight;

            // this is silly
            expandedHeight = height + contractedHeight;
            Log.d(TAG,"expandedHeight " + expandedHeight);
            heightDiff = expandedHeight - contractedHeight;
            Log.d(TAG,"heightDiff " + heightDiff);
        }
    }

    private int getTextViewHeight(TextView textView){
        int addHeight = ViewHelper.calculateTextViewHeight(textView);
        Log.d(TAG,"addHeight = " + addHeight + " text=" + textView.getText());
        return addHeight;
    }

    private void clearFooters(){
        followedbyNumberView.setText("");
        followsNumberView.setText("");
        postsNumberView.setText("");
        bioTextView.setText("");
        websiteTextView.setText("");
        numbersPopulated = false;
    }

    private void animateToFraction(float newFraction){
        if(expansionAnimation.isRunning()) expansionAnimation.cancel();
        Log.d(TAG,"animate from " + currExpandFraction + " to " + newFraction);
        expansionAnimation = ValueAnimator.ofFloat(currExpandFraction, newFraction);
        expansionAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (Float) valueAnimator.getAnimatedValue();
                setExpandFraction(val);
            }
        });
        float duration = 1000.0f * Math.abs( newFraction - currExpandFraction );

        expansionAnimation.setInterpolator(new DecelerateInterpolator());
        expansionAnimation.setDuration((long)duration);
        expansionAnimation.start();

    }

    private void animateToheight(int newHeight){
        ValueAnimator anim = ValueAnimator.ofFloat(view.getMeasuredHeight(), newHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (val < userimageMaxSize) {
                    v.userImageView.getLayoutParams().height = val;
                    v.userImageView.getLayoutParams().width = val;
                }
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
                broadcastHeight(val);
            }
        });
        anim.setDuration(1000L);
        anim.start();
    }

    private void setExpandFraction(float expandFraction){
        currExpandFraction = expandFraction;

        updateHeight();
        updateFooterAlpha();
    }

    private void updateFooterAlpha(){
        float exp = currExpandFraction * currExpandFraction;
        for(View view:viewsVisibleOnExpansion){
            view.setAlpha(exp);
        }
        v.detailTextView.setAlpha(1.0f - exp);
    }

    private void updateHeight(){
        int space = (int)( TOP_MARGIN_EXPANDED * currExpandFraction);
        this.topMargin.getLayoutParams().height = space;
//        this.midVerticalMargin.getLayoutParams().width = space;

        int newHeight = (int)( contractedHeight + heightDiff * currExpandFraction );

//        Log.d(TAG,"newHeight " + newHeight);

        if(newHeight< userimageMaxSize) {
            v.userImageView.getLayoutParams().height = newHeight;
            v.userImageView.getLayoutParams().width = newHeight;
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = newHeight;
        view.setLayoutParams(layoutParams);

        broadcastHeight(newHeight);
    }

    private void setTextIfAny(TextView view, String text){
        Log.d(TAG,"setTextIfAny " + text);
        if(text==null){
            view.setText("");
            view.setVisibility(View.GONE);
        }
        else{
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        }
    }

    private boolean isVisible(View view){
        return view.getVisibility() != View.GONE;
    }

    // Set Texts

//    private void setSingleTitle(boolean single){
//        if(single) v.mainTextView.getLayoutParams().height = contractedHeight;
//        else v.mainTextView.getLayoutParams().height = contractedHeight / 2;
//        v.mainTextView.requestLayout();
//    }

    private void setSubTitle(String subTitle){
        if(subTitle==null){
            v.subTextView.setText("");
            v.subTextView.setVisibility(View.GONE);
        }
        else{
            v.subTextView.setVisibility(View.VISIBLE);
            v.subTextView.setText(subTitle);
        }
    }

    private void populateNumbers(){
        clearFooters();
        if(user.isComplete()){
            Log.d(TAG, "user.isComplete()");
            followedbyNumberView.setText( user.getFollowedByCount() );
            followsNumberView.setText( user.getFollowsCount() );
            setTextIfAny(websiteTextView, user.webSite);
            setTextIfAny(bioTextView,user.bio);
            postsNumberView.setText( user.getMediaCount() );

            numbersPopulated = true;
            expand();
        }
        else {
            clearFooters();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "user not complete, fetching user.id=" + user.id);
                    User newUser = Instagram.getUser(user.id);
                    if (newUser.id>0) {
                        user = newUser;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateNumbers();
                            }
                        });
                    }
                    else{
                        Log.e(TAG,"newUser.id = " + newUser.id);
                    }
                }
            }).start();
        }
    }

    private void broadcastHeight(int newHeight){
        Intent intent = new Intent(HEIGHT_CHANGE_INTENT);
        intent.putExtra(HEIGHT_EXTRA,newHeight);
        getActivity().sendBroadcast(intent);
    }

    private void animateFooterPointer(int newFooterPos){
        if(footerPointerAnimation.isRunning()) footerPointerAnimation.cancel();

        float newTranslation = (float) newFooterPos * footerWidth;
//        Log.d(TAG,"animate from " + currExpandFraction + " to " + newFraction);
        footerPointerAnimation = ValueAnimator.ofFloat(footerCurrTranslation, newTranslation);
        footerPointerAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                footerCurrTranslation = (Float) valueAnimator.getAnimatedValue();
                footerPointerView.setTranslationX(footerCurrTranslation);
            }
        });
//        float duration = 1000.0f * Math.abs( newFraction - currExpandFraction );


        footerPointerAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        footerPointerAnimation.setDuration(300L);
        footerPointerAnimation.start();
    }

    // Communication

    public interface OnFooterSelectedListener {
        public void onFooterSelected(int userId, int footer);
    }

    // Footer pointer

    static class ViewHolder {
        TextView singleTextView;
        TextView mainTextView;
        TextView subTextView;
        TextView detailTextView;
        UserImageView userImageView;
    }

}
