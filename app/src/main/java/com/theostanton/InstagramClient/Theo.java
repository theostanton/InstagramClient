package com.theostanton.InstagramClient;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

/**
 * Created by theo on 27/12/14.
 */
public class Theo {

    public static boolean errorIfOnUIThread(String TAG){
        if( Looper.getMainLooper().getThread() == Thread.currentThread() ){
            Log.e(TAG, "error: method should not be run on UI thread");
            return true;
        }
        return false;
    }

    public static boolean isOnUIThread(){
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String abreviate(long number){
        return abreviate((int)number);
    }

    public static String abreviate(int number){

        if(number<1000) return String.valueOf(number);

        number /= 1000;
        if(number <1000) return String.valueOf(number) + "k";

        return String.valueOf(number/1000) + "m";
    }

}
