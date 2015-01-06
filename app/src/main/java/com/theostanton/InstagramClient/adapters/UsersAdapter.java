package com.theostanton.InstagramClient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.theostanton.InstagramClient.data.User;
import com.theostanton.InstagramClient.views.UserImageView;
import com.theostanton.InstragramClient.R;

import java.util.ArrayList;

/**
 * Created by theo on 02/01/15.
 */
public class UsersAdapter extends ArrayAdapter<User>{

    private ArrayList<User> users;
    private LayoutInflater inflater;



    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.users_list_item, users);
        this.users = users;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.users_list_item,null);

        User user = getItem(position);

        final TextView usernameTextView = (TextView) convertView.findViewById(R.id.username_title);
        usernameTextView.setText(user.userName);

        final TextView fullNameTextView = (TextView) convertView.findViewById(R.id.fullname_title);
        fullNameTextView.setText(user.fullName);

        final UserImageView userImageView = (UserImageView) convertView.findViewById(R.id.user_image);
        userImageView.setUserId(user.id);

        return convertView;
    }
}
