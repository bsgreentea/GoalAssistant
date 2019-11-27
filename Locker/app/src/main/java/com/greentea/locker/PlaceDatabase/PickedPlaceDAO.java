package com.greentea.locker.PlaceDatabase;

import android.icu.text.Replaceable;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PickedPlaceDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PickedPlace pickedPlace);

    @Query("DELETE FROM place_table")
    void deleteAll();

    @Query("DELETE FROM place_table WHERE placeName = :pName")
    void deletePlace(String pName);

    @Query("SELECT * FROM place_table ORDER BY placeName ASC")
    LiveData<List<PickedPlace>> getAllPickedPlace();

    @Query("SELECT * FROM place_table")
    List<PickedPlace> getAll();

    @Query("SELECT * FROM place_table WHERE placeName = :pName")
    PickedPlace getPlace(String pName);
}
