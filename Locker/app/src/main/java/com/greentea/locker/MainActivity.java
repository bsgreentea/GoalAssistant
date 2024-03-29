package com.greentea.locker;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Geocoder;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greentea.locker.Adapter.RecyclerAdapter;
import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.ViewModel.PickedPlaceViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NameDialog.NameDialogListener, RecyclerAdapter.OnListItemSelectedInterface {

    // NaverMap API 3.0
    private MapView mapView;
    private LocationButtonView locationButtonView;

    // FusedLocationSource (Google)
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    private String placeName = "";

    private ArrayList<PickedPlace> list = new ArrayList<>();
    private LatLng tempLatLng;

    private PickedPlaceViewModel pickedPlaceViewModel;
    private RecyclerAdapter adapter;

    RecyclerView recyclerView;

    private Button b1;
    private EditText et1;
    private Geocoder geocoder;
    private FloatingActionButton fab;

    private NaverMap map;
    private boolean map_flag = false;

    static MainActivity instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, LoadingActivity.class);
//        startActivity(intent);

        b1 = (Button) findViewById(R.id.button);
        et1 = (EditText) findViewById(R.id.editText);
        geocoder = new Geocoder(this);
        fab = findViewById(R.id.setting_fab);

        if (!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }

        map_flag = false;

        dexterPermission();

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        naverMapBasicSettings();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0) {
                    pickedPlaceViewModel.deleteAll();
                    Toast.makeText(MainActivity.this, "초기화합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLocation() {
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent(){

        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(1);
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

    public void dexterPermission(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "권한 승인 후 사용 가능합니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }


    @Override
    public void onItemSelected(View v, int position) {
        RecyclerAdapter.ViewHolder viewHolder = (RecyclerAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
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
    public void onMapReady(@NonNull final NaverMap naverMap) {
        //naverMap.getUiSettings().setLocationButtonEnabled(true);
        locationButtonView.setMap(naverMap);
        map_flag = true;

        map = naverMap;

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<android.location.Address> list = null;

                String str = et1.getText().toString();
                try {
                    list = geocoder.getFromLocationName(
                            str, // 지역 이름
                            10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        Log.e("test","해당되는 주소 정보는 없습니다");
                        Toast.makeText(MainActivity.this, "해당되는 주소 정보는 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude()));
                        naverMap.moveCamera(cameraUpdate);
                    }
                }
            }
        });

        // Location Change Listener를 사용하기 위한 FusedLocationSource 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        // 지도를 길게 누를 때 장소 등록 다이얼로그 띄우기
        naverMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

                tempLatLng = new LatLng(latLng.latitude, latLng.longitude);
                openDialog(pointF, latLng);
            }
        });

        updateMarker(naverMap);
    }

    void updateMarker(NaverMap naverMap){

        if(list != null) {
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

//        pickedPlace.setCheckedList("com.greentea.locker");
        pickedPlace.setCheckedList("");
        pickedPlace.setLat(tempLatLng.latitude);
        pickedPlace.setLng(tempLatLng.longitude);
        list.add(pickedPlace);
        pickedPlaceViewModel.insert(pickedPlace);

        if(map_flag){
            updateMarker(map);
        }
    }

    public void openDialog(PointF pointF, LatLng latLng){
        NameDialog nameDialog = new NameDialog();
        nameDialog.show(getSupportFragmentManager(), "dialog");
    }

    // appinfo activity 정보 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode == 123) {
                Log.d("!!!!!!!!!!!!!!!!!!!!!1", "!!!!!!!!!!!!!!!!!11");
                PickedPlace pickedPlace1 = (PickedPlace) data.getSerializableExtra("origin");
                PickedPlace pickedPlace2 = (PickedPlace) data.getSerializableExtra("pickedPlace");
                pickedPlaceViewModel.deletePlace(pickedPlace1);
                pickedPlaceViewModel.insert(pickedPlace2);
            }
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