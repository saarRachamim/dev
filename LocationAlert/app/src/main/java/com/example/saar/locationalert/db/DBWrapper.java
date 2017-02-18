package com.example.saar.locationalert.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Saar on 29/07/2016.
 */
public class DBWrapper extends SQLiteOpenHelper{
    private static final String dbName = "location.db";
    private static final int dbVersion = 1;

    public static final String table = "locationMetaData";

    public static final String createTable = "create table "+ table +" (idStr integer primary key autoincrement, " +
           "cellStr text not null, messageStr text not null, addressStr text not null, latitudeStr real not null, longitudeStr real not null);";

    public DBWrapper(Context context){
        super(context, dbName, null, dbVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + table);
        onCreate(db);
    }
}
