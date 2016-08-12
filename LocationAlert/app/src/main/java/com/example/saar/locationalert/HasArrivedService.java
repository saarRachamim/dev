package com.example.saar.locationalert;

import android.app.Service;
import android.content.Intent;
import android.location.Geocoder;
import android.os.IBinder;
import android.telephony.SmsManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HasArrivedService extends Service {
    static List<MetaData> metaDatasList = new ArrayList<>();
    static DBOperations dbOperations;
    Geocoder geocoder;
    static GPSHandler gpsHandler;
    static Thread t;
    ArrayList<String> items;
    Locale tempLocale;


    public HasArrivedService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbOperations = new DBOperations(this);
        gpsHandler = new GPSHandler(this);
        t = PerformInBackGround();

        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * calculates the distance between two locations in MILES
     */
    private static double distance2(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75;
        ; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    public static Thread PerformInBackGround() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(30000);
                        dbOperations.open();
                        metaDatasList = dbOperations.getAllMetaData();
                        SmsManager smsManager = SmsManager.getDefault();
                        for (MetaData metadata : metaDatasList) {
                            try {
                                 gpsHandler.getLocation();
                                 sleep(5000);
                            } catch (Exception e) {
                                //Do nothing
                            }
                            double distance = distance(gpsHandler.getLatitude(), gpsHandler.getLongtitude(), metadata.getLatitude(), metadata.getLongitude());
                            if (distance <= 0.3) {
                                smsManager.sendTextMessage(metadata.cell, null, metadata.message, null, null);
                                dbOperations.removeMetaDataFromDb(metadata.getId());
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.print(e.getMessage());
                    }
                }
            }
        };
        if(t == null)
        {
            thread.start();
            return thread;
        }
        else
        {
            return t;
        }
    }
}
