package com.theostanton.InstagramClient.fragments.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.theostanton.InstagramClient.activities.MainActivity;
import com.theostanton.InstagramClient.bitmap.BitmapHandler;
import com.theostanton.InstagramClient.bitmap.BitmapHelper;
import com.theostanton.InstagramClient.bitmap.LoadBitmapToImageview;
import com.theostanton.InstagramClient.data.Post;
import com.theostanton.InstagramClient.fragments.BaseFragment;
import com.theostanton.InstagramClient.instagram.Instagram;
import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 27/12/14.
 */
public class UpperPostFragment extends BaseFragment {

    protected static final String TAG = "UpperPostFragment";

    private ImageView imageView;
//    private int baseHeight = 500;
    private int maxHeight;
    private int yOffset = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        maxHeight = getResources().getDisplayMetrics().widthPixels;

//        setExitTransition(new Fade());
//        setEnterTransition(new Fade());

        String postId = getArguments().getString(MainActivity.POST_ID_ARG);
        Post post = Instagram.getPost(postId);

        if(post==null){
            try {
                throw new Exception("onCreateView() post == null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String imageUrl = "";
        if (post != null) {
            int userId = post.getUser().id;
            Intent intent = new Intent(MainActivity.SET_HEADER_USER_INTENT);
            intent.putExtra(MainActivity.USER_ID_EXTRA, userId);
            getActivity().sendBroadcast(intent);
            imageUrl = post.getImageUrl();
        }


        View view = inflater.inflate(R.layout.upper_post_fragment,container,false);

        // laod main image
        imageView = (ImageView) view.findViewById(R.id.mainimage_post_fragment);
        BitmapHandler bitmapHandler = BitmapHandler.getInstance();
        Bitmap bitmap = bitmapHandler.getFromCache( imageUrl );

        if(bitmap!=null) {
            if (bitmapHandler.cropBorderless()) imageView.setImageBitmap(BitmapHelper.getBorderless(bitmap));
            else imageView.setImageBitmap(bitmap);
        }

//        imageView.setY(100.0f);

        String stdUrl = post.getStdResUrl();
        imageView.setTag(stdUrl);
        LoadBitmapToImageview loader = new LoadBitmapToImageview();
        loader.setAnimation(false);
        loader.execute(imageView);

        return view;
    }

    public void setOffsetRatio(float ratio){
        // no need for these
        int offset = (int)(-50.0f * ratio);
        Log.d(TAG,"offset " + offset);
        ViewGroup.LayoutParams params = getView().getLayoutParams();
//        params.height = baseHeight + offset;
//        imageView.setY(offset);
        getView().setLayoutParams(params);

    }

}
