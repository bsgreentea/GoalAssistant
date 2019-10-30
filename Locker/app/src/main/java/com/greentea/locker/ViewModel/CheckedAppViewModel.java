//package com.greentea.locker.ViewModel;
//
//import android.app.Application;
//
//import androidx.core.widget.ListViewAutoScrollHelper;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//
//import com.greentea.locker.Database.CheckedApp;
//import com.greentea.locker.Database.CheckedAppRepository;
//
//import java.util.List;
//
//public class CheckedAppViewModel extends AndroidViewModel {
//
//    private final CheckedAppRepository repository;
//    private final LiveData<List<CheckedApp>> allCheckedApps;
//
//    public CheckedAppViewModel(Application application){
//        super(application);
//        repository = new CheckedAppRepository(application);
//        allCheckedApps =repository.getAllCheckedApp();
//    }
//
//    public void insert(CheckedApp checkedApp) {repository.insert(checkedApp);}
//
//    public void deleteAll() {repository.deleteAll();}
//
//    public void deleteCheckedApp(String id) {repository.deleteCheckedApp(id);}
//
//    public LiveData<List<CheckedApp>> getAllCheckedApps() {
//        return allCheckedApps;
//    }
//}
