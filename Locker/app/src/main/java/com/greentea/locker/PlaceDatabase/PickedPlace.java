package com.greentea.locker.PlaceDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "place_table")
public class PickedPlace implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "placeName")
    private String placeName;

    @NonNull
    @ColumnInfo(name = "checkedList")
    private String checkedList;

    @NonNull
    @ColumnInfo(name = "lat")
    private Double lat;

    @NonNull
    @ColumnInfo(name = "lng")
    private Double lng;

    @NonNull
    public Double getLat() {
        return lat;
    }

    public void setLat(@NonNull Double lat) {
        this.lat = lat;
    }

    @NonNull
    public Double getLng() {
        return lng;
    }

    public void setLng(@NonNull Double lng) {
        this.lng = lng;
    }

    @NonNull
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(@NonNull String placeName) {
        this.placeName = placeName;
    }

    @NonNull
    public String getCheckedList() {
        return checkedList;
    }

    public void setCheckedList(@NonNull String checkedList) {
        this.checkedList = checkedList;
    }
}
