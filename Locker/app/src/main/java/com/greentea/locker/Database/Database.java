package com.greentea.locker.Database;

import android.provider.BaseColumns;

public final class Database {

    public static final class CreateDB implements BaseColumns{
        public static final String PLACENAME = "place_name";
        public static final String CHKLIST = "chk_list";
//        public static final String AGE = "age";
//        public static final String GENDER = "gender";
        public static final String _TABLENAME0 = "place_table";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
//                +_ID+" integer primary key autoincrement, "
                + PLACENAME +" text not null primary key, "
                + CHKLIST +" text not null) ;";
//                +AGE+" integer not null , "
//                +GENDER+" text not null );";
    }

}
