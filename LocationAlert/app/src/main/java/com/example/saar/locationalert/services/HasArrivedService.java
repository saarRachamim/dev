package com.example.saar.locationalert.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import com.example.saar.locationalert.db.DBOperations;
import com.example.saar.locationalert.objects.AppConstants;
import com.example.saar.locationalert.objects.MetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HasArrivedService extends Service {
    static List<MetaData> metaDatasList = new ArrayList<>();
    static DBOperations dbOperations;
    static GPSHandler gpsHandler;
    static Thread t;
    static Context context;

    public HasArrivedService() {
        this.context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbOperations = new DBOperations(this);
        gpsHandler = new GPSHandler(this);
        t = PerformInBackGround();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * calculates the distance between two locations in km
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static Thread PerformInBackGround() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) try {
                    sleep(5000);
                    metaDatasList = dbOperations.getAllMetaData();
                    SmsManager smsManager = SmsManager.getDefault();
                    for (MetaData metadata : metaDatasList) {
                        try {
                            gpsHandler.getLocation();
                            sleep(5000);
                        } catch (Exception e) {
                            //Do nothing
                        }
                        double distance = distance(gpsHandler.getLatitude(), gpsHandler.getLongitude(), metadata.getLatitude(), metadata.getLongitude());
                        if (distance <= 0.3) {
                            if (Build.VERSION.SDK_INT >= AppConstants.targetSdk &&
                                    ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            smsManager.sendTextMessage(metadata.getCell(), null, metadata.getMessage(), null, null);
                            dbOperations.removeMetaDataFromDb(metadata.getId());
                        }
                    }
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
        };
        if (t == null) {
            thread.start();
            return thread;
        } else {
            return t;
        }
    }
}
