package com.theostanton.InstagramClient.fragments;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by theo on 30/12/14.
 */
public class BaseFragment extends Fragment {

    protected static final String TAG = "BaseFragment";

    protected int myLastVisiblePos = 0;

    protected OnFragmentScrollListener scrollListener;

    public void setOnScrollListener(Fragment fragment){
        scrollListener = (OnFragmentScrollListener) fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        scrollListener = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            if(scrollListener==null) scrollListener = (OnFragmentScrollListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnPopularListItemSelceted");
        }

    }

    protected void announceScrollY(int scrollY){
        if(isVisible()) scrollListener.onFragmentScroll(scrollY);
    }

    public interface OnFragmentScrollListener {
        public void onFragmentScroll(int pos);
    }


}
