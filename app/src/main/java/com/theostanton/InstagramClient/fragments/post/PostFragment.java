package com.theostanton.InstagramClient.fragments.post;

import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Toast;

import com.theostanton.InstagramClient.activities.MainActivity;
import com.theostanton.InstagramClient.fragments.BaseFragment;
import com.theostanton.InstagramClient.fragments.header.HeaderFragment;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 30/12/14.
 */
public class PostFragment extends BaseFragment {

    protected static final String TAG = "PostFragment";
    final GestureDetector gesture = new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    int y = (int) e.getY() - upperPostFragment.getView().getMeasuredHeight() - space.getHeight();
                    Log.d(TAG, "onSingleTapComfirmed y=" + y);
                    lowerPostFragment.clickAtY(y);
                    return false;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.d(TAG, "onSingleTapUp");
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    Log.d(TAG, "onScroll distanceX " + distanceX + " distanceY " + distanceY);
//                    scrolling = true;
                    scrollBy(distanceY);
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    Log.d(TAG, "onDown");
//                    scrolling = false;
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;

                    Log.d(TAG, "onFling");

                    try {

                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                            return false;
                        }

                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d(TAG, "Right to Left");
                            next();

                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d(TAG, "Left to Right");
                            previous();

                        } else {
                            Log.d(TAG, "fling vY = " + velocityY);
                            lowerPostFragment.fling(-(int) velocityY);
                        }

                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
    private String postId;
    private UpperPostFragment upperPostFragment;
    private LowerPostFragment lowerPostFragment;
    private float maxOffset;
    private int currPos = 0;
    private int headerHeight = 0;
    private ValueAnimator headerAnimator = new ValueAnimator();
    private RelativeLayout rootLayout;
    private Space space;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setEnterTransition(new Fade());
        setExitTransition(new Fade());

        View view = inflater.inflate(R.layout.post_fragment, container, false);

        view.findViewById(R.id.post_gesture_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) onUp();
                return gesture.onTouchEvent(event);
            }
        });

        space = (Space) view.findViewById(R.id.space_header_post);
        rootLayout = (RelativeLayout) view.findViewById(R.id.post_root_layout);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle args = getArguments();

        postId = args.getString(MainActivity.POST_ID_ARG);

        updateHeadersPost();

        upperPostFragment = new UpperPostFragment();
        upperPostFragment.setArguments(args);

        transaction.add(R.id.upper_post_container, upperPostFragment);

        lowerPostFragment = new LowerPostFragment();
        lowerPostFragment.setArguments(args);
//        lowerPostFragment.setGestureDetector(gesture);

        transaction.add(R.id.lower_post_container, lowerPostFragment);
        transaction.commit();


        headerHeight = getResources().getDimensionPixelSize(R.dimen.header_contracted);
        currPos = headerHeight;
        Log.d(TAG, "currPos = " + maxOffset);

        return view;
    }

    private void updateHeadersPost(){
//        Intent intent = new Intent(HeaderFragment.POST_FRAG_INTENT);
//        intent.putExtra(HeaderFragment.POST_ID_EXTRA, postId);
//        getActivity().sendBroadcast(intent);
        Intent intent = new Intent(HeaderFragment.POST_ACTION);
        intent.putExtra(HeaderFragment.POST_ID_EXTRA, postId);
        getActivity().sendBroadcast(intent);
        Log.d(TAG, "updateHeadersPost()");
    }

    private void replaceWithNewPost(String postId, boolean right) {

        if (postId == null) {
            Log.e(TAG, "replaceWithNewPost stringId = null");
            Toast.makeText(getActivity().getApplicationContext(), "couldn't get post, postsArrayList has been replaced", Toast.LENGTH_SHORT).show();
            return;
        }

        updateHeadersPost();
//        setCurrOffset();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle args = new Bundle();
        args.putString(MainActivity.POST_ID_ARG, postId);

        upperPostFragment = new UpperPostFragment();
        upperPostFragment.setArguments(args);
//        upperPostFragment.setGestureListener(this);

        lowerPostFragment = new LowerPostFragment();
        lowerPostFragment.setArguments(args);
//        lowerPostFragment.setOnScrollListener(this);

        if (right) {
            transaction.setCustomAnimations(R.animator.slide_in_from_left, R.animator.slide_out_right);
        } else {
            transaction.setCustomAnimations(R.animator.slide_in_from_right, R.animator.slide_out_left);
        }

        transaction.replace(R.id.upper_post_container, upperPostFragment);

        transaction.replace(R.id.lower_post_container, lowerPostFragment);
        transaction.commit();
        animateHeaderReveal();


    }

    private void next() {
        postId = Instagram.getNextPostId(postId);
        replaceWithNewPost(postId, false);
    }

    private void previous(){
        postId = Instagram.getPreviousPostId(postId);
        replaceWithNewPost(postId, true);
    }

    private void updateSpaceHeight() {
        space.getLayoutParams().height = currPos;
        rootLayout.requestLayout();
        updateHeader();
    }

    private void scrollBy(float dy) {
        float newPos = currPos - dy;
        Log.d(TAG, "dy " + dy + " new " + newPos);
        if (newPos >= headerHeight) {
//            Log.d(TAG,"newPos >= 0");
            lowerPostFragment.setScroll(0.0f);
            currPos = headerHeight;
        } else if (newPos > 0) {
//            setHeader(true);
//            Log.d(TAG,"newPos > 0");
            currPos = (int) newPos;
            lowerPostFragment.setScroll(0.0f);
            updateSpaceHeight();
        } else {
//            Log.d(TAG,"newPos <= 0");
//            setHeader(false);
            currPos = (int) newPos;
            updateSpaceHeight();
            lowerPostFragment.scrollBy(dy);
//            lowerPostFragment.scrollBy(dy);
        }
    }

    private boolean onUp(){
//        Log.d(TAG,"onUp currPos = " + currPos + " vs " + headerHeight );
        if(currPos > 0 && currPos < headerHeight / 2){
            animateHeaderhide();
        }
        else if(currPos > 0 && currPos < headerHeight){
            animateHeaderReveal();
        }
        return false;
    }

    private void animateHeaderhide() {
        if (headerAnimator.isRunning()) return;

        headerAnimator = ValueAnimator.ofInt(currPos, 0);
        headerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currPos = (int) animation.getAnimatedValue();
                updateSpaceHeight();
                updateHeader();
            }
        });
        headerAnimator.setDuration(300L);
        headerAnimator.start();
    }

    private void animateHeaderReveal() {
        if (headerAnimator.isRunning()) return;

        headerAnimator = ValueAnimator.ofInt(currPos, headerHeight);
        headerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currPos = (int) animation.getAnimatedValue();
                updateSpaceHeight();
                updateHeader();
            }
        });
        headerAnimator.setDuration(300L);
        headerAnimator.start();
    }

//    private void setHeader(boolean visibility) {
//        Intent headerOpacityIntent = new Intent(HeaderFragmentOLD.SET_VISIBLE_INTENT);
//        headerOpacityIntent.putExtra(HeaderFragmentOLD.VISIBILTY_EXTRA, visibility);
//        getActivity().sendBroadcast(headerOpacityIntent);
//    }

    private void updateHeader() {
        Intent headerOpacityIntent = new Intent(HeaderFragment.TRANSLATEY_INTENT);
        headerOpacityIntent.putExtra(HeaderFragment.TRANSLATEY_EXTRA, currPos - headerHeight);
        getActivity().sendBroadcast(headerOpacityIntent);
    }

}
