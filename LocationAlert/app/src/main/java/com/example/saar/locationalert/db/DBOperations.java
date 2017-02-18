package com.example.saar.locationalert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.saar.locationalert.objects.AppConstants;
import com.example.saar.locationalert.objects.MetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saar on 30/07/2016.
 */
public class DBOperations {

    DBWrapper dbWrapper;
    SQLiteDatabase database;
    String[] columns = {AppConstants.idStr, AppConstants.cellStr, AppConstants.messageStr, AppConstants.addressStr, AppConstants.latitudeStr, AppConstants.longitudeStr};

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

        values.put(AppConstants.cellStr, cell);
        values.put(AppConstants.messageStr, message);
        values.put(AppConstants.addressStr, addressStr);
        values.put(AppConstants.latitudeStr, latitude);
        values.put(AppConstants.longitudeStr, longtitude);

        database.insert(DBWrapper.table, null, values);
        close();
    }

    public void removeMetaDataFromDb(int id){
        open();
        database.delete(DBWrapper.table, AppConstants.idStr + "=" + id, null);
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

    public MetaData getMetaDataById(int id){
        open();
        Cursor cursor = database.query(DBWrapper.table, columns, null, null, null, null, null);
        cursor.moveToFirst();
        MetaData metaData = null;
        while(!cursor.isAfterLast()){
            metaData = parseCursorToMetaData(cursor);
            cursor.moveToNext();
            if(metaData.getId() == id)
                break;
        }
        close();

        return metaData;
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
