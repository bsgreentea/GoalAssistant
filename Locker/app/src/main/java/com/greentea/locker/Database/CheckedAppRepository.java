//package com.greentea.locker.Database;
//
//import android.app.Application;
//import android.os.AsyncTask;
//
//import androidx.lifecycle.LiveData;
//
//import java.nio.channels.AsynchronousByteChannel;
//import java.util.List;
//
//public class CheckedAppRepository {
//
//    private final CheckedAppDAO checkedAppDAO;
//    private final LiveData<List<CheckedApp>> allCheckedApp;
//
//    public CheckedAppRepository(Application application){
//        CheckedAppDB db = CheckedAppDB.getDB(application);
//        checkedAppDAO = db.checkedAppDAO();
//        allCheckedApp = checkedAppDAO.getAllCheckedApp();
//    }
//
//    public void insert(CheckedApp checkedApp){
//
//        new AsyncTask<CheckedApp, Void, Void>(){
//
//            @Override
//            protected Void doInBackground(CheckedApp... checkedApps) {
//
//                if(checkedAppDAO == null) return null;
//                else {
//                    checkedAppDAO.insert(checkedApps[0]);
//                    return null;
//                }
//            }
//
//        }.execute(checkedApp);
//    }
//
//    public void deleteAll(){
//
//        new AsyncTask<Void, Void, Void>(){
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                if(checkedAppDAO == null) return null;
//                else{
//                    checkedAppDAO.deleteAll();
//                    return null;
//                }
//            }
//        }.execute();
//    }
//
//    public void deleteCheckedApp(String id){
//
//        new AsyncTask<String ,Void, Void>(){
//            @Override
//            protected Void doInBackground(String... strings) {
//                if(checkedAppDAO == null) return null;
//                else{
//                    checkedAppDAO.deleteCheckedApp(strings[0]);
//                    return null;
//                }
//            }
//        }.execute(id);
//    }
//
//    public LiveData<List<CheckedApp>> getAllCheckedApp() {
//        return allCheckedApp;
//    }
//}
