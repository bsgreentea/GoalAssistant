//package com.greentea.locker.Database;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//import com.greentea.locker.PlaceDatabase.PickedPlace;
//import com.greentea.locker.PlaceDatabase.PickedPlaceDAO;
//import com.greentea.locker.SettingDatabase.Setting;
//import com.greentea.locker.SettingDatabase.SettingDAO;
//
//@Database(entities = {PickedPlace.class, Setting.class}, version = 1)
//public abstract class AppDB extends RoomDatabase {
//
//    public abstract PickedPlaceDAO pickedPlaceDAO();
//    public abstract SettingDAO settingDAO();
//
//    private static volatile AppDB INSTANCE;
//
//    public static AppDB getDB(final Context context){
//        if(INSTANCE == null){
//            synchronized (AppDB.class){
//                if(INSTANCE == null){
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDB.class,
//                            "goalAssistant_database").build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//}
