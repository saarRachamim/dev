package com.example.saar.locationalert.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.example.saar.locationalert.objects.AppConstants;

/**
 * Created by Saar on 4/9/2016.
 */
public class GPSHandler extends Service implements LocationListener {

    //Data Members
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    //Constants for the scope
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES_IN_METERS = 10;
    private static final long MIN_MILLISECONDS_TO_UPDATE = 1000 * 60;

    protected LocationManager locationManager;

    final Context context;

    public GPSHandler(Context context) {
        this.context = context;
        getLocation();
    }

    /**
     * The function get the current location.
     * It sets the longitudeStr, latitudeStr.
     */
    public void getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled || isGPSEnabled)
                    getLocationAndSetValues();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The function get the location using the location manager object.
     * Then it sets the altitude, longitudeStr and altitude.
     */
    private void getLocationAndSetValues() {
        if ( Build.VERSION.SDK_INT >= AppConstants.targetSdk &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_MILLISECONDS_TO_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATES_IN_METERS, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    private void getLocationAndSetValue(){

    }
    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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
