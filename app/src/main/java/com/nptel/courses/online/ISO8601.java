package com.nptel.courses.online;

import android.util.Log;

public class ISO8601 {

    public static String getTime(String time_iso8601) {
        StringBuilder time = new StringBuilder();
        boolean proceed = time_iso8601.charAt(0) == 'P' && time_iso8601.charAt(1) == 'T';
        if (!proceed)
            return time.toString();
        for (int i = 0; i < time_iso8601.length(); i++) {
            if (time_iso8601.charAt(i) == 'P' || time_iso8601.charAt(i) == 'T')
                continue;
            if (time_iso8601.charAt(i) == 'H' || time_iso8601.charAt(i) == 'M') {
                time.append(":");
                continue;
            }
            if (time_iso8601.charAt(i) == 'S')
                continue;
            time.append(time_iso8601.charAt(i));
        }
        return time.toString();
    }

    public static int getTimeInSeconds(String time_iso8601) {
        boolean proceed = time_iso8601.charAt(0) == 'P' && time_iso8601.charAt(1) == 'T';
        if (!proceed)
            return -1;
        int seconds = 0;
        String intvalue = "";

        for (int i = 0; i < time_iso8601.length(); i++) {

            if (time_iso8601.charAt(i) == 'P' || time_iso8601.charAt(i) == 'T')
                continue;

            if (time_iso8601.charAt(i) == 'H') {
                seconds = seconds + Integer.parseInt(intvalue) * 3600;
                intvalue = "";
                continue;
            }
            if (time_iso8601.charAt(i) == 'M') {
                seconds = seconds + Integer.parseInt(intvalue) * 60;
                intvalue = "";
                continue;
            }
            if (time_iso8601.charAt(i) == 'S') {
                seconds = seconds + Integer.parseInt(intvalue);
                continue;
            }
            try {
                intvalue = intvalue + Integer.parseInt(time_iso8601.charAt(i) + "");
            } catch (NumberFormatException e) {
                Log.e("ISO8601 to seconds", e.toString());
            }
        }
        return seconds;
    }
}
