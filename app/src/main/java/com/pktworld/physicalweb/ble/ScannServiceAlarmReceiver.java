package com.pktworld.physicalweb.ble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ubuntu1 on 4/5/16.
 */
public class ScannServiceAlarmReceiver extends BroadcastReceiver {
    public void onReceive(final Context context, Intent intent) {
        context.startService(new Intent(context, ScannService.class));


    }
}
