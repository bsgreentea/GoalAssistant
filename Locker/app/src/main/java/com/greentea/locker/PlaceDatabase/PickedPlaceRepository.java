package com.greentea.locker.PlaceDatabase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public List<PickedPlace> getAll(){
        try{
            places = new getAllAsyncTask(pickedPlaceDAO).execute().get();

        } catch (ExecutionException e){

        } catch (InterruptedException e){

        }
        return places;
    }

    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<PickedPlace>>{
        private PickedPlaceDAO asyncPlaceDao;

        getAllAsyncTask(PickedPlaceDAO dao){
            asyncPlaceDao = dao;
        }

        @Override
        protected List<PickedPlace> doInBackground(Void... voids) {
            return asyncPlaceDao.getAll();
        }
    }

    public PickedPlace getPlace(String pName){
        PickedPlace pickedPlace = new PickedPlace();
        try{
            pickedPlace = new getPickedPlaceAsyncTask(pickedPlaceDAO, pName).execute().get();

        } catch (ExecutionException e){

        } catch (InterruptedException e){

        }
        return pickedPlace;
    }

    private static class getPickedPlaceAsyncTask extends AsyncTask<Void, Void, PickedPlace> {
        private PickedPlaceDAO asyncPlaceDao;
        private String pName;

        getPickedPlaceAsyncTask(PickedPlaceDAO dao, String pName){
            asyncPlaceDao = dao;
            this.pName = pName;
        }

        @Override
        protected PickedPlace doInBackground(Void... voids) {
            return asyncPlaceDao.getPlace(pName);
        }
    }

    public void deletePlace(PickedPlace pickedPlace){
        new deletePlaceAsyncTask(pickedPlaceDAO).execute(pickedPlace);
    }

    private static class deletePlaceAsyncTask extends AsyncTask<PickedPlace, Void, Void>{
        private PickedPlaceDAO pickedPlaceDAO;

        deletePlaceAsyncTask(PickedPlaceDAO dao){
            pickedPlaceDAO = dao;
        }

        @Override
        protected Void doInBackground(PickedPlace... pickedPlaces) {
            pickedPlaceDAO.deletePlace(pickedPlaces[0]);
            return null;
        }
    }
}
