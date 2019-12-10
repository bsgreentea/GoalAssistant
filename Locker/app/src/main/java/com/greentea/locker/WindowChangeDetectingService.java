package com.greentea.locker;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.PlaceDatabase.PickedPlaceRepository;
import com.greentea.locker.Utilities.CalculateDistance;

import java.util.List;

public class WindowChangeDetectingService extends AccessibilityService{

    LocationManager lm;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){

//            Log.d("packagename", event.getPackageName().toString());

            double longitude = 0, latitude = 0;

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            }
            else{
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

//            String temp = Double.toString(latitude) + Double.toString(longitude);
//            Log.i("coord_test", temp);

            List<PickedPlace> places = new PickedPlaceRepository(getApplication()).getAll();

            Log.i("places", String.valueOf(places.size()));

            String appNames = "";

            for(int i=0; i<places.size(); i++){
                Log.d("places[i]", places.get(i).getPlaceName());

                Double lat = places.get(i).getLat();
                Double lng = places.get(i).getLng();

                // 설정 거리 내에 있는 장소일 경우
                if(CalculateDistance.distance(latitude, longitude, lat,lng) <= 10) {

                    Log.d("chked?", "ehkced");

                    if(appNames.equals("")){
                        appNames = places.get(i).getCheckedList();
                    }
                    else{
                        appNames += "," + places.get(i).getCheckedList();
                    }
                }

                Log.d("appss",appNames);
            }

            String[] apps = appNames.split(",");

            Log.d("apps", appNames);

            boolean flag = false;

            for(String temp : apps){
                if(event.getPackageName().toString().equals(temp)){
                    flag = true;
                    break;
                }
            }

            if(flag) {
                Toast.makeText(getApplicationContext(), "앱을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                gotoHome();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void gotoHome() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_FORWARD_RESULT
                | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return false;
    }
}
