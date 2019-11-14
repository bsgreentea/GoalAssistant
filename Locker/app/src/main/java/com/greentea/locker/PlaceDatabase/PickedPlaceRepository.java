package com.greentea.locker.PlaceDatabase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PickedPlaceRepository {

    private final PickedPlaceDAO pickedPlaceDAO;
    private final LiveData<List<PickedPlace>> allPickedPlace;
    private List<PickedPlace> places;

    public PickedPlaceRepository(Application application){
        PickedPlaceDB db = PickedPlaceDB.getDB(application);
        pickedPlaceDAO = db.pickedPlaceDAO();
        allPickedPlace = pickedPlaceDAO.getAllPickedPlace();
//        places = pickedPlaceDAO.getAll();
    }

    public void insert(final PickedPlace pickedPlace){
        new AsyncTask<PickedPlace, Void, Void>(){

            @Override
            protected Void doInBackground(PickedPlace... pickedPlaces) {

                if(pickedPlaceDAO == null) return null;
                else {
                    pickedPlaceDAO.insert(pickedPlaces[0]);
                    return null;
                }
            }

        }.execute(pickedPlace);
    }

    public void deletePlace(String id){

        new AsyncTask<String ,Void, Void>(){
            @Override
            protected Void doInBackground(String... strings) {
                if(pickedPlaceDAO == null) return null;
                else{
                    pickedPlaceDAO.deletePlace(strings[0]);
                    return null;
                }
            }
        }.execute(id);
    }

    public void deleteAll(){

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                if(pickedPlaceDAO == null) return null;
                else{
                    pickedPlaceDAO.deleteAll();
                    return null;
                }
            }
        }.execute();
    }

    public LiveData<List<PickedPlace>> getAllPickedPlace() {
        return allPickedPlace;
    }

//    public List<PickedPlace> getAll() {return places;}
}
