package com.theostanton.InstagramClient.fragments.header;

import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.theostanton.InstagramClient.activities.MainActivity;
import com.theostanton.InstagramClient.fragments.BaseFragment;
import com.theostanton.InstragramClient.R;

/**
 * Created by theo on 16/01/15.
 */
public class HeaderFragment extends BaseFragment implements View.OnClickListener, ValueAnimator.AnimatorUpdateListener, View.OnTouchListener {

    public static final String TAG = "HeaderFragmentStates";
    public static final String COLLAPSE_INTENT = "collapse_intent";
    public static final String EXPAND_INTENT = "expand_intent";
    public static final String TRANSLATEY_INTENT = "translatey_intent";
    public static final String TRANSLATEY_EXTRA = "translatey_extra";
    public static final String HEIGHT_CHANGE_INTENT = "height_change_intent";
    public static final String HEIGHT_EXTRA = "height_extar";
    public static final String TITLE_ACTION = "title_action";
    public static final String TITLE_EXTRA = "title_extra";
    public static final String USER_ACTION = "user_action";
    public static final String USER_ID_EXTRA = "user_id_extra";
    public static final String POST_ACTION = "post_action";
    public static final String POST_ID_EXTRA = "post_id_extra";

    // Outgoing
    public static final String EXPAND_EXTRA = "expand_extra";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d(TAG, "receive " + action);

            broadcastHeight(view.getHeight());

            if (action.equals(USER_ACTION)) {
                int userId = intent.getIntExtra(USER_ID_EXTRA, -7);
                boolean expand = intent.getBooleanExtra(EXPAND_EXTRA, false);
                if (expand) expand();
                state.freshUser(userId);
                return;
            }

            if (action.equals(POST_ACTION)) {
                String postId = intent.getStringExtra(POST_ID_EXTRA);
                contract();
                state.freshPost(postId);
                return;
            }

            if (action.equals(TITLE_ACTION)) {
                String title = intent.getStringExtra(TITLE_EXTRA);
                contract();
                state.freshTitle(title);
                return;
            }

            if (action.equals(TRANSLATEY_INTENT)) {
                final float transateY = intent.getIntExtra(TRANSLATEY_EXTRA, 0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Log.d(TAG,"translateY " + transateY);
                        view.setTranslationY(transateY);
                    }
                });
            }
        }
    };
    public static final String CONTRACT_EXTRA = "contract_extra";
    private UpperHeaderFragment upper;
    private LowerHeaderFragment lower;
    private View view;

    // Incoming
    private HeaderStateHandler state;
    private int contractedHeight;
    private int expandedHeight;
    private int heightDiff;
    private int userimageMaxSize;
    private boolean expanded = false;
    private ValueAnimator heightAnimator = new ValueAnimator();
    private float currExpansionFraction = 0.0f;
    private float startTranslation = 0.0f; // if translationY will animate if this is nonzero when expanding/contracting
    private int footerPosition;

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(USER_ACTION);
        filter.addAction(POST_ACTION);
        filter.addAction(TITLE_ACTION);
        filter.addAction(TRANSLATEY_INTENT);

        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contractedHeight = getResources().getDimensionPixelSize(R.dimen.header_contracted);
        userimageMaxSize = 2 * contractedHeight;
        expandedHeight = 2 * contractedHeight + getResources().getDimensionPixelSize(R.dimen.footer_height);
        heightDiff = expandedHeight - contractedHeight;

        Log.d(TAG, "contractedHeight=" + contractedHeight + " expandedHeight=" + expandedHeight + " heightDiff=" + heightDiff);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.header_fragment_states, container, false);
        view.setOnClickListener(this);

        view.findViewById(R.id.back_button).setOnClickListener(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        upper = new UpperHeaderFragment();
        upper.setHeaderFragment(this);
        lower = new LowerHeaderFragment();
        lower.setHeaderFragment(this);
//
        transaction.replace(R.id.upper_header_fragment, upper);
        transaction.replace(R.id.lower_header_fragment, lower);
        transaction.commit();

        state = new HeaderStateHandler(this, upper, lower);


        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() != -1) Log.d(TAG, "onClick id=" + getResources().getResourceName(v.getId()));
        else Log.d(TAG, "onClick -1");

        if (v.getId() == R.id.back_button) {
            Intent intent = new Intent(MainActivity.BACK_INTENT);
            getActivity().sendBroadcast(intent);
            return;
        }

        toggle();


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() != -1) Log.d(TAG, "onClick id=" + getResources().getResourceName(v.getId()));
        else Log.d(TAG, "onClick -1");

        if (v.getId() == R.id.back_button) {
            Intent intent = new Intent(MainActivity.BACK_INTENT);
            getActivity().sendBroadcast(intent);
            return true;
        }

        toggle();

        return false;
    }


    private void setHeightFraction(float fraction) {
        currExpansionFraction = fraction;
        int newHeight = (int) (contractedHeight + heightDiff * fraction);
        Log.d(TAG, "fraction= " + fraction + " newHeight=" + newHeight);


        setHeight(newHeight);

        if (newHeight < userimageMaxSize && newHeight > contractedHeight) {
            upper.setUserImageSize(newHeight);
        }

        float exp = fraction * fraction;
        if (exp >= 0.5f) {
            float alpha = exp * 2.0f - 1.0f;
            lower.setAlpha(alpha);
        } else {
            float alpha = exp * 2.0f;
            upper.setAlpha(alpha);
        }


    }

    private void setHeight(int height) {
        Log.d(TAG, "setHeight() " + height);
        broadcastHeight(height);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    private void broadcastHeight(int newHeight) {
        Intent intent = new Intent(HEIGHT_CHANGE_INTENT);
        intent.putExtra(HEIGHT_EXTRA, newHeight + (int) view.getTranslationY());
        getActivity().sendBroadcast(intent);
    }

    public void toggle() {
        if (expanded) contract();
        else expand();
    }

    public void expand() {
        if (expanded) return;
        lower.setClickEnabled(true);
        animateToHeight(1.0f);
        expanded = true;
        view.setTranslationY(0.0f);
    }

    public void contract() {
        if (!expanded) return;
        lower.setClickEnabled(false);
        animateToHeight(0.0f);
        expanded = false;
        view.setTranslationY(0.0f);
    }

    private void animateToHeight(int newHeight) {
        if (heightAnimator.isRunning()) heightAnimator.cancel();

        heightAnimator = ValueAnimator.ofInt(0, newHeight);
        heightAnimator.addUpdateListener(this);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimator.setDuration(1000L);
        heightAnimator.start();

    }

    private void animateToHeight(float newFraction) {
        if (heightAnimator.isRunning()) heightAnimator.cancel();

        startTranslation = view.getTranslationY();

        Log.d(TAG, "from " + currExpansionFraction + " to " + newFraction);
        heightAnimator = ValueAnimator.ofFloat(currExpansionFraction, newFraction);
        heightAnimator.addUpdateListener(this);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnimator.setDuration(1000L);
        heightAnimator.start();

    }

    public int getExpansion() {
        return 100;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float fraction = (float) animation.getAnimatedValue();
        setHeightFraction(fraction);
        if (startTranslation != 0.0f) view.setTranslationY(startTranslation * (1.0f - fraction));

//        if(animation==heightAnimator){
//        }
    }

    public boolean click() {
        toggle();
        return expanded;
//        if(expanded) return false;
//        return true;
    }

    public void setFooterPosition(int footerPosition) {
        lower.setOnFooterSelected(footerPosition);
    }
}
