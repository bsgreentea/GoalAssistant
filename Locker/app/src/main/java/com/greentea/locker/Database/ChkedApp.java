package com.greentea.locker.Database;

import com.orm.SugarRecord;

public class ChkedApp extends SugarRecord {

    String packageName;
    String appName;

    public ChkedApp() {

    }

    public ChkedApp(String packageName, String appName) {
        this.packageName = packageName;
        this.appName = appName;
    }
}
