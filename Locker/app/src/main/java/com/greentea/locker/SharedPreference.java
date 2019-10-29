package com.greentea.locker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.greentea.locker.Utilities.AppInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public static final String PREFS_NAME = "LOCKER";
    public static final String CHECKED = "APP_CHECKED";

    public SharedPreference(){
        super();
    }

    public void saveLists(Context context, List<AppInfo> list){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        editor = settings.edit();

        Gson gson = new Gson();
        String jsonChecked = gson.toJson(list);

        editor.putString(CHECKED, jsonChecked);

        editor.commit();
    }

    public void addAppInfo(Context context, AppInfo appInfo){
        List<AppInfo> list = getList(context);

        if(list == null){
            list = new ArrayList<AppInfo>();
        }

        list.add(appInfo);
        saveLists(context, list);
    }

    public void removeAppInfo(Context context, AppInfo appInfo){
        ArrayList<AppInfo> list = getList(context);
        if(list != null){
            list.remove(appInfo);
            saveLists(context, list);
        }
    }

    public ArrayList<AppInfo> getList(Context context) {

        SharedPreferences settings;
        List<AppInfo> list;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if(settings.contains(CHECKED)){
            String jsonChecked = settings.getString(CHECKED, null);
            Gson gson = new Gson();
            AppInfo[] chks = gson.fromJson(jsonChecked, AppInfo[].class);

            list = Arrays.asList(chks);
            list = new ArrayList<AppInfo>(list);
        }
        else return null;

        return (ArrayList<AppInfo>) list;
    }
}
