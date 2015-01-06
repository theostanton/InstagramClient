package com.theostanton.InstragramClient.fragments.post;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import com.theostanton.InstragramClient.R;
import com.theostanton.InstragramClient.activities.MainActivity;
import com.theostanton.InstragramClient.bitmap.BitmapHandler;
import com.theostanton.InstragramClient.bitmap.BitmapHelper;
import com.theostanton.InstragramClient.bitmap.LoadBitmapToImageview;
import com.theostanton.InstragramClient.data.Post;
import com.theostanton.InstragramClient.fragments.BaseFragment;
import com.theostanton.InstragramClient.instagram.Instagram;

/**
 * Created by theo on 27/12/14.
 */
public class UpperPostFragment extends BaseFragment {

    protected static final String TAG = "UpperPostFragment";

    private ImageView imageView;
//    private int baseHeight = 500;
    private int maxHeight;
    private int yOffset = 0;

    public interface GestureListener{
        public void onScroll(float dy);
        public void swipeLeft();
        public void swipeRight();
    }

    private GestureListener gestureListener;

    public void setGestureListener(Fragment fragment){
        gestureListener = (GestureListener) fragment;
    }

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

        int userId = post.getUser().id;
        Intent intent = new Intent(MainActivity.SET_HEADER_USER_INTENT);
        intent.putExtra(MainActivity.USER_ID_EXTRA,userId);
        getActivity().sendBroadcast(intent);

        String imageUrl = post.getImageUrl();

        View view = inflater.inflate(R.layout.upper_post_fragment,container,false);

        // laod main image
        imageView = (ImageView) view.findViewById(R.id.mainimage_post_fragment);
        BitmapHandler bitmapHandler = BitmapHandler.getInstance();
        Bitmap bitmap = bitmapHandler.getFromCache( imageUrl );

        if(bitmap!=null) {
            if (bitmapHandler.cropBorderless()) imageView.setImageBitmap(BitmapHelper.getBorderless(bitmap));
            else imageView.setImageBitmap(bitmap);
        }

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
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

    final GestureDetector gesture = new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
//                    if(e.getX()>getView().getWidth()/2) gestureListener.swipeLeft();
//                    else gestureListener.swipeRight();
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                    Log.d(TAG,"onScroll distanceX " + distanceX + " distanceY " + distanceY);
//                    float newHeight = (float)getView().getLayoutParams().height + distanceY;
//
//                    if(newHeight>maxHeight) newHeight = maxHeight;
//                    else if(newHeight < 0) newHeight = 0;
//
//                    Log.d(TAG,"set newheight " + newHeight);
//                    getView().getLayoutParams().height = (int)newHeight;
//                    getView().requestLayout();

//                    gestureListener.onScroll(distanceY);
//                    float currY = getView().getTranslationY();
//                    getView().setTranslationY( currY - distanceY );
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }

                @Override
                public boolean onDown(MotionEvent e) {
//                    Log.d(TAG,"onDown");
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;

                    try {

                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
                            return false;
                        }

                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d(TAG, "Right to Left");
                            gestureListener.swipeLeft();

                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d(TAG, "Left to Right");
                            gestureListener.swipeRight();

                        }

                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });

}
