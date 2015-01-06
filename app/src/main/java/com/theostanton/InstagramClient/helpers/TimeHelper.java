package com.theostanton.InstagramClient.helpers;

/**
 * Created by theo on 30/12/14.
 */
public class TimeHelper {

    private static final String TAG = "TimeHelper";

    private static final long SECOND = 1000L;
    private static final long MINUTE = 60L*SECOND;
    private static final long HOUR = 60L * MINUTE;
    private static final long DAY = 24L * HOUR;
    private static final long YEAR = 365L * DAY;

    public static String getTimeAgo(long ms){

        StringBuilder sb = new StringBuilder();

        long msSince = System.currentTimeMillis() - ms*1000L;
//        Log.d(TAG,"current = " + System.currentTimeMillis() + " ms = " + ms + " msSince = " + msSince);

        if(msSince<MINUTE){
            sb.append(msSince / SECOND);
            sb.append(" seconds ago");
        }
        else if(msSince<HOUR){
            int minsSince = (int)( msSince / MINUTE );
            sb.append(minsSince);
            sb.append(" min");
            if(minsSince>1) sb.append("s");
            sb.append(" ago");
        }
        else if(msSince<DAY){
            int hoursSince = (int)( msSince / HOUR );
            sb.append(hoursSince);
            sb.append(" hour");
            if(hoursSince>1) sb.append("s");
            sb.append(" ago");
        }
        else if(msSince<YEAR){
            int daysSince = (int)( msSince / DAY );
            sb.append(daysSince);
            sb.append(" day");
            if(daysSince>1) sb.append("s");
            sb.append(" ago");
        }
        else{
            int daysSince = (int)( msSince / DAY );
            int monthsSince = daysSince / 31;
            sb.append(monthsSince);
            sb.append(" month");
            if(monthsSince>1) sb.append("s");
            sb.append(" ago");
        }
//        Log.d(TAG,"msSince = " + msSince + " sb= " + sb.toString());
        return sb.toString();
    }



}
