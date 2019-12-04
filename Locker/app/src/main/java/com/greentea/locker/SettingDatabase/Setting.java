package com.greentea.locker.SettingDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "setting_table")
public class Setting implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "message")
    private String alertMessage = "앱을 사용할 수 없습니다.";

    @NonNull
    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(@NonNull String alertMessage) {
        this.alertMessage = alertMessage;
    }
}
