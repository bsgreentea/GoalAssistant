package com.greentea.locker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.PlaceDatabase.PickedPlaceRepository;
import com.greentea.locker.Utilities.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// List Adapter
public class AppInfoAdapter extends BaseAdapter {

    private final int MENU_DOWNLOAD = 0;
    private final int MENU_ALL = 1;
    private int MENU_MODE = MENU_DOWNLOAD;

    private PackageManager pm;

    private Context mContext = null;

    private List<ApplicationInfo> mAppList = null;
    private List<AppInfo> checkedApps;
    private ArrayList<AppInfo> mListData = new ArrayList<AppInfo>();

    SharedPreferences sharedPreferences;

    String placeNames;

    public AppInfoAdapter(Context mContext, String placeNames) {
        super();
//        this.checkedApps = list;
        this.mContext = mContext;
        this.placeNames = placeNames;
//        sharedPreferences = mContext.getSharedPreferences("test", mContext.MODE_PRIVATE);
    }

    public List<AppInfo> getApplist(){
        return mListData;
    }

    // List Fast Holder
    private class ViewHolder {
        // App Icon
        public ImageView mIcon;
        // App Name
        public TextView mName;
        // App Package Name
        public TextView mPackage;
        // chk
        public CheckBox checkBox;
    }

    public int getCount() {
        return mListData.size();
    }

    public AppInfo getItem(int position) {
        return checkedApps.get(position);
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_layout, null);

            holder.mIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.mName = (TextView) convertView.findViewById(R.id.app_name);
            holder.mPackage = (TextView) convertView.findViewById(R.id.app_package);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = mListData.get(position);

        if (appInfo.mIcon != null) {
            holder.mIcon.setImageDrawable(appInfo.mIcon);
        }

        holder.mName.setText(appInfo.mAppName);
        holder.mPackage.setText(appInfo.mAppPackage);

        holder.checkBox.setChecked(appInfo.chkFlag);

//        if (checkItem(appInfo)) {
//            holder.checkBox.setChecked(true);
//        } else {
//            holder.checkBox.setChecked(false);
//        }

        return convertView;
    }

    // 어플리케이션 리스트 작성
    public void rebuild() {


        if (mAppList == null) {

            // 패키지 매니저 취득
            pm = mContext.getPackageManager();

            // 설치된 어플리케이션 취득
            mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS);
        }

        AppInfo.AppFilter filter;
        switch (MENU_MODE) {
            case MENU_DOWNLOAD:
                filter = AppInfo.THIRD_PARTY_FILTER;
                break;
            default:
                filter = null;
                break;
        }

        if (filter != null) {
            filter.init();
        }

        // 기존 데이터 초기화
        mListData.clear();

//        sharedPreferences = mContext.getSharedPreferences("test", Context.MODE_PRIVATE);

//        String string;

        String[] strings = placeNames.split(",");

        AppInfo addInfo = null;
        ApplicationInfo info = null;
        for (ApplicationInfo app : mAppList) {
            info = app;

            if (filter == null || filter.filterApp(info)) {
                // 필터된 데이터

                addInfo = new AppInfo();
                // App Icon
                addInfo.mIcon = app.loadIcon(pm);
                // App Name
                addInfo.mAppName = app.loadLabel(pm).toString();
                // App Package Name
                addInfo.mAppPackage = app.packageName;

//                string = sharedPreferences.getString(addInfo.mAppPackage, "");

                boolean flag = false;
                for(String temp : strings){
                    if(addInfo.mAppPackage.equals(temp)){
                        flag = true; break;
                    }
                }

                addInfo.chkFlag = flag;

//                if(string == ""){
//                    addInfo.chkFlag = false;
//                }
//                else{
//                    addInfo.chkFlag = true;
//                }

                mListData.add(addInfo);
            }
        }

        // 알파벳 이름으로 소트(한글, 영어)
        Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
    }

//    public boolean checkItem(AppInfo cApp) {
//        boolean check = false;
//        List<AppInfo> favorites = sharedPreference.getList(mContext);
//        if (favorites != null) {
//            for (AppInfo appInfo : favorites) {
//                if (appInfo.equals(cApp)) {
//                    check = true;
//                    break;
//                }
//            }
//        }
//        return check;
//    }
}
