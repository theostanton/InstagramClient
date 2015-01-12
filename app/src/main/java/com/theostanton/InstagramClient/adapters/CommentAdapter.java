package com.theostanton.InstagramClient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.theostanton.InstagramClient.data.Comment;
import com.theostanton.InstragramClient.R;

import java.util.ArrayList;

/**
 * Created by theo on 27/12/14.
 */
public class CommentAdapter extends ArrayAdapter {

    private static final String TAG = "CommentAdapter";

    private ArrayList<Comment> comments;
    private LayoutInflater inflater;

    private View.OnTouchListener onTouchListener;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.comment, comments);
        this.comments = comments;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        Log.d(TAG, "comment size  = " + comments.size());
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.comment, null);

        Comment comment = (Comment)getItem(position);

        Log.d(TAG, "getView " + position + " userId " + comment.getUserName());

        final TextView usernameView = (TextView) convertView.findViewById(R.id.username_comment);
        usernameView.setText(comment.getUserName());

        final TextView textTextView = (TextView) convertView.findViewById(R.id.text_comment);
        textTextView.setText(comment.text);

        convertView.setTag(comment.getUserId());

        return convertView;
    }
}
