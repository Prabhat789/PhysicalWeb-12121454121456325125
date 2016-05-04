package com.pktworld.physicalweb.ble;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.pktworld.physicalweb.util.ApplicationConstant;

/**
 * Created by ubuntu1 on 4/5/16.
 */
public class BluetoothStateReceiver extends BroadcastReceiver {
    private static final String TAG = BluetoothStateReceiver.class.getSimpleName();
    private AlarmManager scannAlarmManager;
    private Intent  intent;
    private PendingIntent pendingIntent;

    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    //setButtonText("Bluetooth off");
                    stopScanning(context);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    //setButtonText("Turning Bluetooth off...");
                    stopScanning(context);
                    break;
                case BluetoothAdapter.STATE_ON:
                   // setButtonText("Bluetooth on");
                    startScanning(context);
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    //setButtonText("Turning Bluetooth on...");
                    startScanning(context);
                    break;
            }
        }
    }


    private void startScanning(Context mContext) {
        Log.v(TAG, "BleService Start");
        try {
            SharedPreferences sharedPreferences =mContext.getSharedPreferences(
                    ApplicationConstant.APPLICATION_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(ApplicationConstant.INTERVAL_MINUTE_BLE, 1);
            scannAlarmManager = (AlarmManager) mContext
                    .getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(mContext, ScannServiceAlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
            int intervalInMinutes = sharedPreferences.getInt(ApplicationConstant.INTERVAL_MINUTE_BLE, 1);
            scannAlarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), intervalInMinutes * 60 * 1000 ,
                    pendingIntent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void stopScanning(Context mContext){
        Log.d(TAG, "cancelAlarmManager");
        Intent bleIntent = new Intent(mContext,
                ScannServiceAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                bleIntent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
