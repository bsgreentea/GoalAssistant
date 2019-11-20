package com.greentea.locker.PlaceDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PickedPlaceDAO {

    @Insert
    void insert(PickedPlace pickedPlace);

    @Query("DELETE FROM place_table")
    void deleteAll();

    @Query("DELETE FROM place_table WHERE placeName = :pName")
    void deletePlace(String pName);

    @Query("SELECT * FROM place_table ORDER BY placeName ASC")
    LiveData<List<PickedPlace>> getAllPickedPlace();

    @Query("SELECT * FROM place_table")
    List<PickedPlace> getAll();
}
