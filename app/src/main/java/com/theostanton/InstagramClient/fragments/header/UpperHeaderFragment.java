package com.theostanton.InstagramClient.fragments.header;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.views.UserImageView;
import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 16/01/15.
 */
public class UpperHeaderFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "UpperHeaderFragment";

    private View view;

    private TextView singleTextView;
    private TextView mainTextView;
    private TextView subTextView;
    private TextView detailTextView;
    private TextView bioTextView;
    private TextView websiteTextView;
    private UserImageView userImageView;

    private int contractedHeight;
    private HeaderFragment header;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.upper_header_fragment, container, false);

        view.setOnClickListener(this);

        mainTextView = (TextView) view.findViewById(R.id.title_text_header);
        singleTextView = (TextView) view.findViewById(R.id.single_text_header);
        subTextView = (TextView) view.findViewById(R.id.sub_text_header);
        detailTextView = (TextView) view.findViewById(R.id.detail_text_header);
        bioTextView = (TextView) view.findViewById(R.id.bio_text_header);
        websiteTextView = (TextView) view.findViewById(R.id.website_text_header);
        userImageView = (UserImageView) view.findViewById(R.id.user_image_header);

        contractedHeight = getResources().getDimensionPixelSize(R.dimen.header_contracted);

        return view;
    }

    public void setUser(User user) {
        setUser(user, false);
    }

    public void setUser(User user, boolean postUser) {
        setUserData(user);

        if (!postUser) detailTextView.setText("");
        singleTextView.setText("");
    }

    public void setPost(Post post) {
        User user = post.getUser();
        setUserData(user);

        detailTextView.setText(post.getTimeAgo());
        singleTextView.setText("");
    }

    public void setTitle(String title) {
        userImageView.useResource();
        singleTextView.setText(title);

        detailTextView.setText("");
        mainTextView.setText("");
        subTextView.setText("");
    }

    private void setUserData(User user) {
        mainTextView.setText(user.fullName);
        subTextView.setText(user.userName);
        userImageView.setUserId(user.id);

        if (user.isComplete()) {
            websiteTextView.setText(user.webSite);
            bioTextView.setText(user.bio);
        } else {
            websiteTextView.setText("");
            bioTextView.setText("");
        }
    }

    public int getHeight() {
        return view.getHeight();
    }

    public void setHeight(float heightFraction) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) (contractedHeight * heightFraction);
        view.setLayoutParams(params);
    }

    public void setUserImageSize(int userImageSize) {
        userImageView.getLayoutParams().height = userImageSize;
        userImageView.getLayoutParams().width = userImageSize;
    }

    public void setAlpha(float alpha) {
        bioTextView.setAlpha(alpha);
        websiteTextView.setAlpha(alpha);
        detailTextView.setAlpha(0.95f - alpha);
    }

    public void clearViews() {
        singleTextView.setText("");
        mainTextView.setText("");
        subTextView.setText("");
        detailTextView.setText("");
        bioTextView.setText("");
        websiteTextView.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != -1) Log.d(TAG, "onClick id=" + getResources().getResourceName(v.getId()));
        else Log.d(TAG, "onClick -1");
        if (!header.click()) header.setFooterPosition(-1);
    }

    public void setHeaderFragment(HeaderFragment header) {
        this.header = header;
    }

//
}
