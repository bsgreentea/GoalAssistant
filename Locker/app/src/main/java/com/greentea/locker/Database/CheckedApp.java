package com.greentea.locker.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "app_table")
public class CheckedApp {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "packageName")
    private String packageName;

    @NonNull
    @ColumnInfo(name = "appName")
    private String appName;

    @NonNull
    public String getPackageName(){
        return this.packageName;
    }

    public void setPackageName(@NonNull String packageName){
        this.packageName = packageName;
    }

    @NonNull
    public String getAppName(){
        return this.appName;
    }

    public void setAppName(@NonNull String appName){
        this.appName = appName;
    }
}
