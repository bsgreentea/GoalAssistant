package com.greentea.locker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.PlaceDatabase.PickedPlaceRepository;
import com.greentea.locker.Utilities.AppInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AppInfoActivity extends AppCompatActivity {

    // 메뉴 KEY
    private View mLoadingContainer;
    private ListView mListView = null;
    private AppInfoAdapter mAdapter = null;

//    SharedPreference sharedPreference;

    SharedPreferences sharedPreferences;
    HashSet<String> hashSet;

    private List<AppInfo> appInfoList;
//    private CheckedAppViewModel checkedAppViewModel;

    private PickedPlace pickedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        hashSet = new HashSet<>();
        sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);

        mLoadingContainer = findViewById(R.id.loading_container);
        mListView = (ListView) findViewById(R.id.listView1);

        Intent intent = getIntent();
//        String placeName = intent.getExtras().getString("placeName");
        pickedPlace = (PickedPlace) intent.getSerializableExtra("pickedPlace");

        Toast.makeText(this, pickedPlace.getPlaceName(), Toast.LENGTH_SHORT).show();

        String appList = pickedPlace.getCheckedList();

        String[] apps = appList.split(",");

        for(String temp : apps){
            hashSet.add(temp);
        }

        Log.d("list_test", appList);

        mAdapter = new AppInfoAdapter(this, appList);
        appInfoList = mAdapter.getApplist();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position, long id) {

                String appName = ((TextView) view.findViewById(R.id.app_name)).getText().toString();
                String packageName = ((TextView) view.findViewById(R.id.app_package)).getText().toString();

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);

                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                    hashSet.remove(packageName);
                    Toast.makeText(AppInfoActivity.this, appName + " 차단을 해제합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    checkBox.setChecked(true);
                    hashSet.add(packageName);
                    Toast.makeText(AppInfoActivity.this, appName + "을(를) 차단 목록에 추가합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 작업 시작
        startTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_btn1:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goBack(){

        List<String> list = new ArrayList<>();
        list.addAll(hashSet);

        // Locker 앱도 차단할 경우, com.greentea.locker 추가하기
        String apps = "";

        for(int i = 0; i<list.size(); i++){
            apps += list.get(i);
            if(i < list.size() - 1) apps += ",";
        }

        Intent intent = new Intent();

        intent.putExtra("origin", pickedPlace);

        pickedPlace.setCheckedList(apps);
        intent.putExtra("pickedPlace", pickedPlace);

        setResult(123, intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // 작업 시작
    private void startTask() {
        new AppTask().execute();
    }

    /**
     * 로딩뷰 표시 설정
     *
     * @param isView
     *            표시 유무
     */
    private void setLoadingView(boolean isView) {
        if (isView) {
            // 화면 로딩뷰 표시
            mLoadingContainer.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            // 화면 어플 리스트 표시
            mListView.setVisibility(View.VISIBLE);
            mLoadingContainer.setVisibility(View.GONE);
        }
    }

    // 작업 태스크
    private class AppTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // 로딩뷰 시작
            setLoadingView(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // 어플리스트 작업시작
            mAdapter.rebuild();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 어댑터 갱신
            mAdapter.notifyDataSetChanged();

            // 로딩뷰 정지
            setLoadingView(false);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, MENU_DOWNLOAD, 1, R.string.menu_download);
//        menu.add(0, MENU_ALL, 2, R.string.menu_all);
//
//        return true;
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (MENU_MODE == MENU_DOWNLOAD) {
//            menu.findItem(MENU_DOWNLOAD).setVisible(false);
//            menu.findItem(MENU_ALL).setVisible(true);
//        } else {
//            menu.findItem(MENU_DOWNLOAD).setVisible(true);
//            menu.findItem(MENU_ALL).setVisible(false);
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int menuId = item.getItemId();
//
//        if (menuId == MENU_DOWNLOAD) {
//            MENU_MODE = MENU_DOWNLOAD;
//        } else {
//            MENU_MODE = MENU_ALL;
//        }
//
//        startTask();
//
//        return true;
//    }
}
