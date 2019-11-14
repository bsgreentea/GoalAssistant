package com.greentea.locker.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.PlaceDatabase.PickedPlaceRepository;

import java.util.List;

public class PickedPlaceViewModel extends AndroidViewModel {

    private final PickedPlaceRepository repository;
    private final LiveData<List<PickedPlace>> allPickedPlace;

    public PickedPlaceViewModel(Application application){
        super(application);
        repository = new PickedPlaceRepository(application);
        allPickedPlace = repository.getAllPickedPlace();
    }

    public void insert(PickedPlace pickedPlace) {repository.insert(pickedPlace);}

    public void deleteAll() {repository.deleteAll();}

    public LiveData<List<PickedPlace>> getAllPickedPlace() {
        return allPickedPlace;
    }
}
