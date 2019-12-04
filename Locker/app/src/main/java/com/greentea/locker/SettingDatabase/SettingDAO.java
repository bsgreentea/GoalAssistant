package com.greentea.locker.SettingDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SettingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Setting setting);

    @Query("SELECT * FROM setting_table")
    Setting getSettingInfo();
}
