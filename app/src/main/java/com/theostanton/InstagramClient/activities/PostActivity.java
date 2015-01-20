package com.theostanton.InstagramClient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.theostanton.InstagramClient.bitmap.LoadBitmapToImageview;
import com.theostanton.InstragramClient.R;

public class PostActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.post_activity);
        String url = getIntent().getStringExtra("URL");
        ImageView imageView = (ImageView) findViewById(R.id.imageview_postactivity);
        imageView.setTag(url);

        LoadBitmapToImageview loader = new LoadBitmapToImageview();
        loader.setAnimation(false);
        loader.execute(imageView);
    }
}
