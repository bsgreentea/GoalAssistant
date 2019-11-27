package com.greentea.locker;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greentea.locker.Adapter.RecyclerAdapter;
import com.greentea.locker.Database.DbOpenHelper;
import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.ViewModel.PickedPlaceViewModel;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import java.util.ArrayList;
import java.util.List;

//http://swlock.blogspot.com/2015/05/android_22.html
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NameDialog.NameDialogListener, RecyclerAdapter.OnListItemSelectedInterface {

    // NaverMap API 3.0
    private MapView mapView;
    private LocationButtonView locationButtonView;
//    private Button button;
    private FloatingActionButton button;

    // FusedLocationSource (Google)
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    private String placeName = "";

    private ArrayList<PickedPlace> list = new ArrayList<>();
    private LatLng tempLatLng;

    private PickedPlaceViewModel pickedPlaceViewModel;
    private RecyclerAdapter adapter;

    RecyclerView recyclerView;

//    https://github.com/yoondowon/InnerDatabaseSQLite/blob/master/app/src/main/java/com/example/user/innerdatabasesqlite/MainActivity.java
    private DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        if(!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
                        .show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle("title")
                .setRationaleMessage("message")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("OK")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        naverMapBasicSettings();

//        button = (FloatingActionButton) findViewById(R.id.fab_main);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), AppInfoActivity.class);
//                startActivityForResult(intent, 101);
//            }
//        });

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener(){
//            @Override
//            public void onItemClick(View v, int pos) {
//                Intent intent = new Intent(getApplicationContext(), AppInfoActivity.class);
//                startActivityForResult(intent, 102);
//            }
//        });

        init();
    }

    private void init() {

        pickedPlaceViewModel = ViewModelProviders.of(this).get(PickedPlaceViewModel.class);
        pickedPlaceViewModel.getAllPickedPlace().observe(this, new Observer<List<PickedPlace>>() {
            @Override
            public void onChanged(List<PickedPlace> pickedPlaces) {

                adapter.setPlaces(pickedPlaces);

                list = new ArrayList<>();
                if(pickedPlaces.size() > 0) {
                    for (int i = 0; i < pickedPlaces.size(); i++) {
                        list.add(pickedPlaces.get(i));
                    }
                }
            }
        });

    }

    @Override
    public void onItemSelected(View v, int position) {
        RecyclerAdapter.ViewHolder viewHolder = (RecyclerAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
//        viewHolder
    }

    // https://hyongdoc.tistory.com/177
    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);
        //내위치 버튼
        locationButtonView = findViewById(R.id.locationbuttonview);
        // 내위치 찾기 위한 source
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        //naverMap.getUiSettings().setLocationButtonEnabled(true);
        locationButtonView.setMap(naverMap);

        // ex) 하이테크센터
        final Marker marker = new Marker();

        // Location Change Listener를 사용하기 위한 FusedLocationSource 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        // 지도를 길게 누를 때 마커 추가하기
        naverMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

                tempLatLng = new LatLng(latLng.latitude, latLng.longitude);
                openDialog(pointF, latLng);

                Marker m = new Marker();

                m.setPosition(latLng);
                m.setMap(naverMap);
            }
        });

        if(list != null) {
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < list.size(); i++) {
                Marker m = new Marker();

                m.setPosition(new LatLng(list.get(i).getLat(), list.get(i).getLng()));
                m.setMap(naverMap);
            }
        }
    }

    @Override
    public void applyText(String name) {

        placeName = name;

        PickedPlace pickedPlace = new PickedPlace();
        pickedPlace.setPlaceName(name);
        pickedPlace.setCheckedList("com.greentea.locker");
        pickedPlace.setLat(tempLatLng.latitude);
        pickedPlace.setLng(tempLatLng.longitude);
        list.add(pickedPlace);
        pickedPlaceViewModel.insert(pickedPlace);
    }

    public void openDialog(PointF pointF, LatLng latLng){
        NameDialog nameDialog = new NameDialog();
        nameDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.d("asdasdfasdfasdfasdff","ASdf");
////        if(requestCode == 123){
            if(resultCode == 123) {
                Log.d("!!!!!!!!!!!!!!!!!!!!!1", "!!!!!!!!!!!!!!!!!11");
                PickedPlace pickedPlace1 = (PickedPlace) data.getSerializableExtra("origin");
                PickedPlace pickedPlace2 = (PickedPlace) data.getSerializableExtra("pickedPlace");
                pickedPlaceViewModel.deletePlace(pickedPlace1);
                pickedPlaceViewModel.insert(pickedPlace2);
            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    // https://jungwoon.github.io/android/2016/10/03/Accessibility-Service/
    public boolean checkAccessibilityPermissions() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

        // getEnabledAccessibilityServiceList는 현재 접근성 권한을 가진 리스트를 가져오게 된다
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);
        // default

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);

            // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    // 접근성 설정화면으로 넘겨주는 부분
    public void setAccessibilityPermissions() {
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("접근성 권한 설정");
        gsDialog.setMessage("접근성 권한을 필요로 합니다");
        gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 설정화면으로 보내는 부분
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                return;
            }
        }).create().show();
    }
}