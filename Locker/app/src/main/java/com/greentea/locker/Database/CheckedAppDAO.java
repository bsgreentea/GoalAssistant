//package com.greentea.locker.Database;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface CheckedAppDAO {
//
//    @Insert
//    void insert(CheckedApp checkedApp);
//
//    @Query("DELETE FROM app_table")
//    void deleteAll();
//
//    @Query("DELETE FROM app_table WHERE packageName = :pName")
//    void deleteCheckedApp(String pName);
//
//    @Query("SELECT * FROM app_table ORDER BY appName ASC")
//    LiveData<List<CheckedApp>> getAllCheckedApp();
//}
