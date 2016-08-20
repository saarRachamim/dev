package com.example.saar.locationalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Saar on 30/07/2016.
 */
public class DBOperations {

    DBWrapper dbWrapper;
    SQLiteDatabase database;
    String[] columns = {"id", "cell", "message", "addressStr", "latitude", "longitude"};

    public DBOperations(Context context){
        dbWrapper = new DBWrapper(context);

    }

    public void open(){
        database = dbWrapper.getWritableDatabase();
    }

    public void close(){
        dbWrapper.close();
    }

    public void insertMetaDataToDb(String cell, String message, String addressStr, double latitude, double longtitude){
        ContentValues values = new ContentValues();
        open();

        values.put("cell", cell);
        values.put("message", message);
        values.put("addressStr", addressStr);
        values.put("latitude", latitude);
        values.put("longitude", longtitude);

        database.insert(DBWrapper.table, null, values);
        close();
    }

    public void removeMetaDataFromDb(int id){
        open();
        database.delete(DBWrapper.table, "id=" + id, null);
        close();
    }

    public List getAllMetaData(){
        List metaDatas = new ArrayList();
        open();
        Cursor cursor = database.query(DBWrapper.table, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            metaDatas.add(parseCursorToMetaData(cursor));
            cursor.moveToNext();
        }
        close();

        return metaDatas;
    }

    private MetaData parseCursorToMetaData(Cursor cursor){
        MetaData metaData = new MetaData();
        metaData.setId(cursor.getInt(0));
        metaData.setCell(cursor.getString(1));
        metaData.setMessage(cursor.getString(2));
        metaData.setAddress(cursor.getString(3));
        metaData.setLatitude(cursor.getDouble(4));
        metaData.setLongitude(cursor.getDouble(5));


        return metaData;
    }
}
