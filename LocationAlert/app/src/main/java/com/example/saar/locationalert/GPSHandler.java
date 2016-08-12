package com.example.saar.locationalert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.security.Provider;

/**
 * Created by Saar on 4/9/2016.
 */
public class GPSHandler extends Service implements LocationListener {

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longtitude;
    double altitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES_IN_METERS = 10;
    private static final long MIN_MILLISECONDS_TO_UPDATE = 1000 * 60;

    protected LocationManager locationManager;

    final Context context;

    public GPSHandler(Context context) {
        this.context = context;
        getLocation();
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled || isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_MILLISECONDS_TO_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATES_IN_METERS, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();
                            altitude = location.getAltitude();

                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getAltitude() {
        return altitude;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
