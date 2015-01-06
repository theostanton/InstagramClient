package com.theostanton.InstragramClient.fragments.post;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import com.theostanton.InstragramClient.R;
import com.theostanton.InstragramClient.activities.MainActivity;
import com.theostanton.InstragramClient.fragments.BaseFragment;
import com.theostanton.InstragramClient.fragments.header.HeaderFragment;
import com.theostanton.InstragramClient.instagram.Instagram;

/**
 * Created by theo on 30/12/14.
 */
public class PostFragment extends BaseFragment implements LowerPostFragment.OnFragmentScrollListener, UpperPostFragment.GestureListener{

    protected static final String TAG = "PostFragment";

    private String postId;
    private UpperPostFragment upperPostFragment;
    private LowerPostFragment lowerPostFragment;
    private float maxOffset;
    private float currOffset = 0.0f;
    private float offsetRatio = 0.0f;

    private Transition slideInFromLeft;
    private Transition slideOutRight;
    private Transition slideInFromRight;
    private Transition slideOutLeft;

    private RelativeLayout rootLayout;

    private Space space;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setEnterTransition(new Fade());
        setExitTransition(new Fade());

        View view = inflater.inflate(R.layout.post_fragment,container,false);



        space = (Space) view.findViewById(R.id.space_header_post);
        rootLayout = (RelativeLayout) view.findViewById(R.id.post_root_layout);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle args = getArguments();

        postId = args.getString(MainActivity.POST_ID_ARG);

        updateHeadersPost();


        upperPostFragment = new UpperPostFragment();
        upperPostFragment.setArguments(args);
        upperPostFragment.setGestureListener(this);

        transaction.add(R.id.upper_post_container, upperPostFragment);

        lowerPostFragment = new LowerPostFragment();
        lowerPostFragment.setArguments(args);
        lowerPostFragment.setOnScrollListener(this);


        transaction.add(R.id.lower_post_container,lowerPostFragment);
        transaction.commit();

        maxOffset = - 50.0f * getResources().getDisplayMetrics().density;

        Log.d(TAG, "maxOffset = " + maxOffset);

        return view;


    }

    private void updateHeadersPost(){
        Intent intent = new Intent(HeaderFragment.POST_FRAG_INTENT);
        intent.putExtra(HeaderFragment.POST_ID_EXTRA,postId);
        getActivity().sendBroadcast(intent);
        Log.d(TAG,"updateHeadersPost()");
    }

    private void replaceWithNewPost(String postId,boolean right){

        updateHeadersPost();
        offsetRatio = 0.0f;
        setCurrOffset();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle args = new Bundle();
        args.putString(MainActivity.POST_ID_ARG,postId);

        upperPostFragment = new UpperPostFragment();
        upperPostFragment.setArguments(args);
        upperPostFragment.setGestureListener(this);


        lowerPostFragment = new LowerPostFragment();
        lowerPostFragment.setArguments(args);
        lowerPostFragment.setOnScrollListener(this);

        if(right){
            transaction.setCustomAnimations(R.animator.slide_in_from_left, R.animator.slide_out_right);
        }
        else{
            transaction.setCustomAnimations(R.animator.slide_in_from_right, R.animator.slide_out_left);
        }


        transaction.replace(R.id.upper_post_container, upperPostFragment);



//        long duration = 500L;
//        TransitionSet exitSet = new TransitionSet();
//        exitSet.excludeTarget(R.id.add_comment_button, true);
//        exitSet.excludeTarget(R.id.favourite_button, true);
//        exitSet.addTransition(new Fade().setDuration(duration));
//
//        TransitionSet enterSet = new TransitionSet();
//        enterSet.excludeTarget(R.id.add_comment_button, true);
//        enterSet.excludeTarget(R.id.favourite_button, true);
//        enterSet.addTransition(new Fade().setDuration(duration));
//        enterSet.setStartDelay(duration);
//
//        lowerPostFragment.setExitTransition(exitSet);
//        lowerPostFragment.setEnterTransition(enterSet);

        transaction.replace(R.id.lower_post_container, lowerPostFragment);
        transaction.commit();

    }

    private void next(){
        postId = Instagram.getNextPostId(postId);
        replaceWithNewPost(postId,false);
    }

    private void previous(){
        postId = Instagram.getPreviousPostId(postId);
        replaceWithNewPost(postId,true);
    }

    @Override
    public void onFragmentScroll(int pos) {
        getOffsetRatio(pos);
        setCurrOffset();
    }

    private void setCurrOffset(){
        currOffset = - (int)(maxOffset * (1.0f - offsetRatio) );
        updateSpaceHeight();
        updateHeader();
    }

    private void updateSpaceHeight(){
        space.getLayoutParams().height = (int)currOffset;
        rootLayout.requestLayout();
    }

    private void getOffsetRatio(int pos){
        if(pos < maxOffset){
            offsetRatio = 1.0f;
        }
        else if(pos < 0.0f) {
            offsetRatio = (float)pos / maxOffset;
        }
        else{
            offsetRatio = 0.0f;
        }
    }

    @Override
    public void onScroll(float dy) {
//        Log.d(TAG, "dy = " + dy + " curroffset-dy = " + (currOffset - dy));
//        currOffset = Math.max(maxOffset,currOffset-dy);
//        currOffset = Math.min(0, currOffset-dy);
//        getOffsetRatio(-(int)(maxOffset-currOffset));
//        updateSpaceHeight();
//        updateHeader();
//        lowerPostFragment.setScroll(currOffset);
    }

    @Override
    public void swipeLeft() {
        next();
    }

    @Override
    public void swipeRight() {
        previous();
    }

    private void updateHeader(){
        Intent headerOpacityIntent = new Intent(HeaderFragment.ALPHA_INTENT);
        headerOpacityIntent.putExtra(HeaderFragment.ALPHA_EXTRA,1.0f-offsetRatio);
        getActivity().sendBroadcast(headerOpacityIntent);
    }
}
