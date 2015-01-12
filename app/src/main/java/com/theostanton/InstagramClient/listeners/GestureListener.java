package com.theostanton.InstagramClient.listeners;

/**
 * Created by theo on 10/01/15.
 */

public interface GestureListener {
//public interface GestureListener extends GestureDetector.SimpleOnGestureListener{
//
//    @Override
//    public boolean onSingleTapConfirmed(MotionEvent e) {
////                    if(e.getX()>getView().getWidth()/2) gestureListener.swipeLeft();
////                    else gestureListener.swipeRight();
//        return super.onSingleTapConfirmed(e);
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
////                    Log.d(TAG,"onScroll distanceX " + distanceX + " distanceY " + distanceY);
////                    float newHeight = (float)getView().getLayoutParams().height + distanceY;
////
////                    if(newHeight>maxHeight) newHeight = maxHeight;
////                    else if(newHeight < 0) newHeight = 0;
////
////                    Log.d(TAG,"set newheight " + newHeight);
////                    getView().getLayoutParams().height = (int)newHeight;
////                    getView().requestLayout();
//
////                    gestureListener.onScroll(distanceY);
////                    float currY = getView().getTranslationY();
////                    getView().setTranslationY( currY - distanceY );
//        return super.onScroll(e1, e2, distanceX, distanceY);
//    }
//
//    @Override
//    public boolean onDown(MotionEvent e) {
////                    Log.d(TAG,"onDown");
//        return true;
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                           float velocityY) {
//        final int SWIPE_MIN_DISTANCE = 120;
//        final int SWIPE_MAX_OFF_PATH = 250;
//        final int SWIPE_THRESHOLD_VELOCITY = 200;
//
//        try {
//
//            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
//                return false;
//            }
//
//            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                Log.d(TAG, "Right to Left");
//                gestureListener.swipeLeft();
//
//            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                Log.d(TAG, "Left to Right");
//                gestureListener.swipeRight();
//
//            }
//
//        } catch (Exception e) {
//            // nothing
//        }
//        return super.onFling(e1, e2, velocityX, velocityY);
//    }
//
}