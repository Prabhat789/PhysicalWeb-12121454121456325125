package com.pktworld.physicalweb.ble;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.pktworld.physicalweb.util.ApplicationConstant;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.Collection;
public class ScannService extends Service implements BeaconConsumer, RangeNotifier {

    private static final String TAG = ScannService.class.getSimpleName();
    /*private Handler mHandler = new Handler();
    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 60000;
    private boolean mScanning;*/
    Context context = ScannService.this;
    private BeaconManager mBeaconManager;




    @Override
    public void onCreate() {
        super.onCreate();
        /*try {
            checkBLE();
            init();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }*/

        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the URL frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));
        mBeaconManager.bind(this);

    }

   /* private boolean enableBLE(){
        boolean ret=true;

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            ret=false;
        }
        return ret;
    }
    @SuppressLint("NewApi")
    private void init(){
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void startScan(boolean success){
        if(mBluetoothAdapter == null){
            init();
        }
        if(success){
           *//* mScanning=true;
            scanLeDevice(mScanning);*//*
            return;
        }
        mScanning=true;
        //scanLeDevice(mScanning);

    }*/
   /* @SuppressLint("NewApi") private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            Log.d(TAG,getCtx()+" scanLeDevice startLeScan:"+enable);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mHandler.postDelayed(new Runnable() {
                @SuppressLint("NewApi") @Override
                public void run() {
                    mScanning = false;
                    Log.d(TAG,getCtx() + "run stopLeScan");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                }
            }, SCAN_PERIOD);


        } else {
            Log.d(TAG,getCtx()+ " scanLeDevice stopLeScan:"+enable);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);

        }
    }
    private static String getCtx(){
        Date dt = new Date();
        return dt+ " thread:"+Thread.currentThread().getName();
    }

    @SuppressLint("NewApi") private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {



                        }
                    }).start();
                }
            };

    private  void checkBLE(){

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }*/



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Service Start");

       /* boolean ret = enableBLE();
        if (ret) {
            startScan(false);
        }*/
        return START_NOT_STICKY;
    }



    @SuppressLint("NewApi")
    @Override
    public void onDestroy() {
        super.onDestroy();
        //mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mBeaconManager.unbind(ScannService.this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendBroadcast(String url){
        Intent intent1 = new Intent();
        intent1.setAction(ApplicationConstant.BLE_URL_BROADCAST);
        intent1.putExtra(ApplicationConstant.BLE_DATA, url);
        context.sendBroadcast(intent1);
    }


    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.setRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for (Beacon beacon: beacons) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
                // This is a Eddystone-URL frame
                String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                Log.d(TAG, "I see a beacon transmitting a url: " + url +
                        " approximately " + beacon.getDistance() + " meters away.");

                sendBroadcast(url);
            }
        }
    }
}