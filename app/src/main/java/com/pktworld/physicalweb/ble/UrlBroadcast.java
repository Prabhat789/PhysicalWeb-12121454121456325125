package com.pktworld.physicalweb.ble;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pktworld.physicalweb.R;
import com.pktworld.physicalweb.activity.MainActivity;
import com.pktworld.physicalweb.util.ApplicationConstant;

/**
 * Created by ubuntu1 on 4/5/16.
 */
public class UrlBroadcast extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    PendingIntent contentIntent;
    public static int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        Log.e("Data",action);
        if(action.equals(ApplicationConstant.BLE_URL_BROADCAST)){
            String state = intent.getExtras().getString(ApplicationConstant.BLE_DATA);
            Log.e("Data1",state);
            sendNotification(state, context);
        }



    }

    private void sendNotification(String msg, Context context) {
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(ApplicationConstant.DATA,msg);
        contentIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Simplicity-Creations")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }

}
