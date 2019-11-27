package com.greentea.locker.ViewModel;

import android.app.Application;
import android.widget.ListView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.greentea.locker.PlaceDatabase.PickedPlace;
import com.greentea.locker.PlaceDatabase.PickedPlaceRepository;

import java.util.List;

public class PickedPlaceViewModel extends AndroidViewModel {

    private final PickedPlaceRepository repository;
    private final LiveData<List<PickedPlace>> allPickedPlace;

//    private List<PickedPlace> places;

    public PickedPlaceViewModel(Application application){
        super(application);
        repository = new PickedPlaceRepository(application);
        allPickedPlace = repository.getAllPickedPlace();
//        places = repository.getAll();
    }

    public void insert(PickedPlace pickedPlace) {repository.insert(pickedPlace);}

    public void deleteAll() {repository.deleteAll();}

    public void deletePlace(PickedPlace pickedPlace) {repository.deletePlace(pickedPlace);}

    public LiveData<List<PickedPlace>> getAllPickedPlace() {
        return allPickedPlace;
    }

//    public List<PickedPlace> getAll() {return places;}
}
