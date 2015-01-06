package com.theostanton.InstragramClient.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.theostanton.InstragramClient.R;
import com.theostanton.InstragramClient.bitmap.BitmapHandler;
import com.theostanton.InstragramClient.bitmap.BitmapHelper;
import com.theostanton.InstragramClient.data.Post;
import com.theostanton.InstragramClient.data.User;
import com.theostanton.InstragramClient.helpers.AnimationHelper;
import com.theostanton.InstragramClient.helpers.HTTPHandler;
import com.theostanton.InstragramClient.helpers.ViewHelper;
import com.theostanton.InstragramClient.instagram.Instagram;

/**
 * Created by theo on 27/12/14.
 */
public class UserImageView extends ImageView {

    private static final String TAG = "UserImageView";

    private boolean bitmapLoaded = false;
    private boolean animate = true;
    private String url = null;

    private int DEFAULT_RESOURCE = R.drawable.instagram;
    private boolean usingResource = false;
    private int userId = -1;
    private String postId = null;  // can getPopular user from its post rather than downloading

    public UserImageView(Context context) {
        super(context);
    }

    public UserImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UserImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
//        int size = width < height ? width : height;
//        size = Math.min(ViewHelper.getDp(150),size);
//        Log.d(TAG,"onMeasure width=" +width + " onMeasure height " + height);
        setMeasuredDimension(width, height);
        setOutlineProvider(ViewHelper.getCicularOutline());

    }

    public void setAnimate(boolean animate){
        this.animate = animate;
    }

    public void setUserId(int userId){
        if(this.userId==userId){
            Log.d(TAG,"this.userId==userId, return");
            return;
        }
        this.userId = userId;
        loadBitmap();
    }

    public int getUserId(){
        if(userId < 0){
            Log.e(TAG,"getUserId < 0");
        }
        return userId;
    }

    public void setPostId(String postId){
        this.postId = postId;
        loadBitmap();
        new LoadBitmap().execute();
    }

    public void useResource(){
        // TODO should pre crop
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), DEFAULT_RESOURCE);
        setImageBitmap( BitmapHelper.getCroppedBitmap(bitmap) );
    }

    private void loadBitmap(){
        BitmapHandler bitmapHandler = BitmapHandler.getInstance();
        url = null;
        if(postId != null){
            Post post = Instagram.getPost(postId);
            User user = post.getUser();
            userId = user.id;
            url = user.profilePicture;
        }
        else if(userId > 0){
            User user = Instagram.getCachedUser(userId);
            url = user.profilePicture;
        }
        if(url==null){
            try {
                throw new Exception("url==null from user, would need to downlaod");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Bitmap bitmap = bitmapHandler.getFromCache(url);
        if(bitmap==null){
            setImageResource(R.drawable.user_place_holder);
            new LoadBitmap().execute();
        }
        else{
            // TODO cache user images
            setImageBitmap( BitmapHelper.getCroppedBitmap(bitmap) );
        }
    }

    private void revealSelf(){
        AnimationHelper.circularReveal(this);
    }


    private class LoadBitmap extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {

            BitmapHandler bitmapHandler = BitmapHandler.getInstance();
            if(url==null){
                Log.e(TAG,"url == null in doInBackgrond()");
                return null;
            }
            else{
                Bitmap bitmap = HTTPHandler.downloadBitmap(url);
                bitmapHandler.putInCache(url,bitmap);
                return BitmapHelper.getCroppedBitmap(bitmap);
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            setImageBitmap(bitmap);
            bitmapLoaded = true;
            if(animate) revealSelf();
//            Log.d("UserImageView LoadBitmap", "Finished");
        }
    }
}
