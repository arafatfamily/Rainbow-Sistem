package com.bitsolution.pln.Helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.bitsolution.pln.services.TrigerStartReceiver;

/**
 * Created by GILBERT on 22/08/2017.
 */

public class AppConfig {
    public String URI_DOMAIN = "";

    public static int INT_INTERVAL = 60000; // 1 menit

    public static void JAGA_SERVICES(Context context) {
        AlarmManager aManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TrigerStartReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        aManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), INT_INTERVAL, pIntent);
    }
}
