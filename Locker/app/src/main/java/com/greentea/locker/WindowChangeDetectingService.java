package com.greentea.locker;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.PlaceDatabase.PickedPlaceDB;
import com.greentea.locker.Utilities.CalculateDistance;
import com.naver.maps.geometry.LatLng;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class WindowChangeDetectingService extends AccessibilityService{

    LocationManager lm;

    SharedPreferences sharedPreferences;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){

            sharedPreferences = getApplicationContext().getSharedPreferences("test", MODE_PRIVATE);

            Map<String, ?> allEntries = sharedPreferences.getAll();

            int flag = 0;
            String eventString = event.getPackageName().toString();

            for(Map.Entry<String, ?> entry : allEntries.entrySet()){
                if(entry.getKey().equals(eventString)){
                    flag = 1; break;
                }
            }

//            if( "com.kakao.talk".equals(event.getPackageName())){

            if(flag == 1) {
                double longitude = 0, latitude = 0;

                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if ( Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                }
                else{
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

//                PickedPlaceDB db = PickedPlaceDB.getDB(getApplication());
//
//                List<PickedPlace> places =  db.pickedPlaceDAO().getAll();

//                for(int i=0; i<places.size(); i++){
//
//                    Double lat = places.get(i).getLat();
//                    Double lng = places.get(i).getLng();
//
//                    if(CalculateDistance.distance(latitude, longitude, lat,lng) <= 1) {
//
//                        Toast.makeText(getApplicationContext(), "공부해라", Toast.LENGTH_SHORT).show();
//                        gotoHome(); break;
//                    }
//                }

                sharedPreferences = getApplicationContext().getSharedPreferences("place", MODE_PRIVATE);

                Map<String, ?> places = sharedPreferences.getAll();

                for(Map.Entry<String, ?> entry : places.entrySet()){
                    String temp[] = entry.getKey().split(" ");
                    String lats = temp[0];
                    String lngs = temp[1];
                    Double lat = Double.parseDouble(lats);
                    Double lng = Double.parseDouble(lngs);

                    if(CalculateDistance.distance(latitude, longitude, lat,lng) <= 1) {

                        Toast.makeText(getApplicationContext(), "공부해라", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), lats +" " + lngs, Toast.LENGTH_SHORT).show();
                        gotoHome();
                    }
                }

//                // ex) 하이테크센터
//                if(CalculateDistance.distance(latitude, longitude, 37.4505988,126.6551209) <= 1) {
//
//                    Toast.makeText(getApplicationContext(), "공부해라", Toast.LENGTH_SHORT).show();
//                    gotoHome();
//                }
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
