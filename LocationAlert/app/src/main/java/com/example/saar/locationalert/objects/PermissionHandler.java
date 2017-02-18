package com.example.saar.locationalert.objects;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Saar on 16/02/2017.
 */
public class PermissionHandler {

    private static PermissionHandler permissionHandler = new PermissionHandler();

    private PermissionHandler() { }

    /* Static 'instance' method */
    public static PermissionHandler getInstance( ) {
        return permissionHandler;
    }

    /**
     * Checks if there is a permission for the fine location
     * @param context
     * @return True is permission granted, false other wise
     */
    public static boolean isPermissionGrantedForAccessFineLocation(Context context){
        if (Build.VERSION.SDK_INT >= AppConstants.targetSdk && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
            return false;

        return true;
    }

    /**
     * Checks if there is a permission for the coarse location
     * @param context
     * @return True is permission granted, false other wise
     */
    public static boolean isPermissionGrantedForAccessCoarseLocation(Context context){
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false;

        return true;
    }

    /**
     * Checks if there is a permission for the coarse location
     * @param context
     * @return True is permission granted, false other wise
     */
    public static boolean isPermissionGrantedForSendSms(Context context){
        if (Build.VERSION.SDK_INT >= AppConstants.targetSdk && ContextCompat.checkSelfPermission( context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            return false;

        return true;
    }

    /**
     * The function asks for a permission for the coarse and fine location from the user in runtime.
     * @param activity - The current activity.
     */
    public static void requestPermissionForAccessCoarseAndFineLocation(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION  },
                AppConstants.INT_ACCESS_COARSE_AND_FINE_LOCATION);
    }

    /**
     * The function asks for a permission for the coarse and fine location from the user in runtime.
     * @param activity - The current activity.
     */
    public static void requestAllPermissionsRequiredForApplication(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS  },
                AppConstants.INT_ACCESS_ALL_REQUIRED_PERMISSIONS);
    }
}

