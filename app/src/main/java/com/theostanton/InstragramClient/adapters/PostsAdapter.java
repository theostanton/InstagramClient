package com.theostanton.InstragramClient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.theostanton.InstragramClient.R;
import com.theostanton.InstragramClient.bitmap.BitmapHandler;
import com.theostanton.InstragramClient.bitmap.LoadBitmapToImageview;
import com.theostanton.InstragramClient.data.Post;

import java.util.ArrayList;

public class PostsAdapter extends ArrayAdapter {

    private static final String TAG = "PostsAdapter";
    private BitmapHandler bitmapHandler;

    private ArrayList<Post> posts;
    private LayoutInflater inflater;

    public PostsAdapter(Context context, int resource, ArrayList<Post> posts) {
        super(context, resource, posts);
        bitmapHandler = BitmapHandler.getInstance();
        this.posts = posts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        Log.d(TAG,"getCount " + posts.size());
        return posts.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.posts_grid_item, null);

        Post post = (Post)getItem(position);

        final String url = post.getImageUrl();
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        imageView.setTag(url);

        LoadBitmapToImageview loader = new LoadBitmapToImageview();
        loader.setBorderless(false);
        loader.execute(imageView);

        return convertView;
    }



}
