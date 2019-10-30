//package com.greentea.locker.Database;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//@Database(entities = {CheckedApp.class}, version = 1)
//public abstract class CheckedAppDB extends RoomDatabase {
//
//    public abstract CheckedAppDAO checkedAppDAO();
//
//    private static volatile CheckedAppDB INSTANCE;
//
//    static CheckedAppDB getDB(final Context context){
//        if(INSTANCE == null){
//            synchronized (CheckedAppDB.class){
//                if(INSTANCE == null){
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CheckedAppDB.class,
//                            "app_datdabase").build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//}
