package com.theostanton.InstagramClient.fragments.header;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.fragments.UserFragment;
import com.theostanton.InstagramClient.views.ButtonHeaderBackground;
import com.theostanton.InstragramClient.R;


/**
 * Created by theo on 16/01/15.
 */
public class LowerHeaderFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "LowerHeaderFragment";

    private View view;
    private HeaderFragment header;

    private TextView postsNumberView;
    private TextView likesNumberView;
    private TextView followsNumberView;
    private TextView followedbyNumberView;
    private TextView postsTextView;
    private TextView likesTextView;
    private TextView followsTextView;
    private TextView followedbyTextView;

    private int expandedHeight = 0;

    private int userId;

    private ButtonHeaderBackground headerBackground;

    private OnFooterSelectedListener onFooterSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lower_header_fragment, container, false);

        headerBackground = (ButtonHeaderBackground) view.findViewById(R.id.header_background);

        postsNumberView = (TextView) view.findViewById(R.id.header_posts_number);
        likesNumberView = (TextView) view.findViewById(R.id.header_likes_number);
        followsNumberView = (TextView) view.findViewById(R.id.header_follows_number);
        followedbyNumberView = (TextView) view.findViewById(R.id.header_followedby_number);


        postsTextView = (TextView) view.findViewById(R.id.header_posts_text);
        likesTextView = (TextView) view.findViewById(R.id.header_likes_text);
        followsTextView = (TextView) view.findViewById(R.id.header_follows_text);
        followedbyTextView = (TextView) view.findViewById(R.id.header_followedby_text);

        view.setOnClickListener(this);

        view.setAlpha(0.0f);

        expandedHeight = getResources().getDimensionPixelSize(R.dimen.footer_height);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onFooterSelected = (OnFooterSelectedListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public void setHeaderFragment(HeaderFragment header) {
        this.header = header;
    }

    public void setValues(User user) {

        userId = user.id;

        // TODO perform checks in HeaderFragment

        if (user == null) {
            Log.e(TAG, "setVales user==null");
//            clearViews();
            return;
        }

        if (!user.isComplete()) {
//            clearViews();
            Log.e(TAG, "setValues user != complete");
            return;
        }

        followedbyNumberView.setText(user.getFollowedByCount());
        followsNumberView.setText(user.getFollowsCount());
        postsNumberView.setText(user.getMediaCount());
    }

    public void clearViews() {
        postsNumberView.setText("");
//        likesNumberView.setText("dasd");
        followsNumberView.setText("");
        followedbyNumberView.setText("");
    }

    public void setTranslationY(int translationY) {
        view.setTranslationY(translationY);
    }

    public void setHeight(float heightFraction) {

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) (expandedHeight * heightFraction);
        view.setLayoutParams(params);
    }

    public void setAlpha(float alpha) {
        view.setAlpha(alpha);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != -1) Log.d(TAG, "onClick " + getResources().getResourceName(v.getId()));
        else Log.d(TAG, "onClick -1");

        if (header.click()) return;

        switch (v.getId()) {
            case R.id.header_follows_number:
            case R.id.header_follows_text:
                Log.d(TAG, "click Follows footer");
                onFooterSelected.onFooterSelected(userId, UserFragment.FOLLOWS);
                headerBackground.setFooter(2);
                break;
            case R.id.header_followedby_number:
            case R.id.header_followedby_text:
                Log.d(TAG, "click Followers footer");
                onFooterSelected.onFooterSelected(userId, UserFragment.FOLLOWED_BY);
                headerBackground.setFooter(3);
                break;
            case R.id.header_likes_number:
            case R.id.header_likes_text:
                Log.e(TAG, "click Likes footer, ignore");
//                onFooterSelected.onFooterSelected(user.id, UserFragment.LIKES);
//                animateFooterPointer(1);
                break;
            case R.id.header_posts_text:
            case R.id.header_posts_number:
                Log.d(TAG, "click Posts footer");
                onFooterSelected.onFooterSelected(userId, UserFragment.POSTS);
                headerBackground.setFooter(0);
                break;
            default:
                Log.d(TAG, "onClick switch defaulted");
        }
    }

    public void setClickEnabled(boolean touchEnabled) {

        if (touchEnabled) {
            postsNumberView.setOnClickListener(this);
            likesNumberView.setOnClickListener(this);
            followsNumberView.setOnClickListener(this);
            followedbyNumberView.setOnClickListener(this);

            postsTextView.setOnClickListener(this);
            likesTextView.setOnClickListener(this);
            followsTextView.setOnClickListener(this);
            followedbyTextView.setOnClickListener(this);
        } else {
            postsNumberView.setOnClickListener(null);
            likesNumberView.setOnClickListener(null);
            followsNumberView.setOnClickListener(null);
            followedbyNumberView.setOnClickListener(null);

            postsTextView.setOnClickListener(null);
            likesTextView.setOnClickListener(null);
            followsTextView.setOnClickListener(null);
            followedbyTextView.setOnClickListener(null);
        }
    }

    public void setOnFooterSelected(int newPosition) {
        headerBackground.setFooter(newPosition);
    }

    public interface OnFooterSelectedListener {
        public void onFooterSelected(int userId, int footer);
    }
}
