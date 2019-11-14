package com.greentea.locker.PlaceDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PickedPlace.class}, version = 1)
public abstract class PickedPlaceDB extends RoomDatabase {

    public abstract PickedPlaceDAO pickedPlaceDAO();

    private static volatile PickedPlaceDB INSTANCE;

    public static PickedPlaceDB getDB(final Context context){
        if(INSTANCE == null){
            synchronized (PickedPlaceDB.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PickedPlaceDB.class,
                            "place_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
