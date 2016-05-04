package com.pktworld.physicalweb.ble;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.pktworld.physicalweb.util.ApplicationConstant;

/**
 * Created by ubuntu1 on 4/5/16.
 */
public class ScannServiceBootReceiver extends BroadcastReceiver {
    private static final String TAG = ScannServiceBootReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent gpsTrackerIntent = new Intent(context, ScannService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstant.APPLICATION_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int intervalInMinutes = sharedPreferences.getInt(ApplicationConstant.INTERVAL_MINUTE_BLE, 1);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                intervalInMinutes * 60 *1000 ,
                pendingIntent);

    }
}
