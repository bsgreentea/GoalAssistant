package com.greentea.locker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.concurrent.ExecutionException;

public class MyLocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.greentea.locker.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){
                    Location location = result.getLastLocation();

                    String str = location.getLatitude() + " " + location.getLongitude();

                    Log.d("????", str);

//                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
