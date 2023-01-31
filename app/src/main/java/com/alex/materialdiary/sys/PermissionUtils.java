package com.alex.materialdiary.sys;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    private static final String TAG = "PermissionUtils";

    /*
        Inside this shared_preference file, we will just store information
        about whether the user had visited our app earlier or not.
    */

    private static final String PREFS_FILE_NAME = "preference_permission";
    private static final String PREFS_FIRST_TIME_KEY = "is_app_launched_first_time";


    //an interface containing 5 methods
    //...the scenario in which these callback will be called is written below each method declaration.
    public interface PermissionAskListener {


        void onPermissionGranted();
        /*
            User has already granted this permission
            The app must had been launched earlier and the user must had "allowed" that permission
         */


        void onPermissionRequest();
        /*
            The app is launched FIRST TIME..
            We don't need to show additional dialog, we just request for the permission..

         */


        void onPermissionPreviouslyDenied();
        /*
            The app was launched earlier and the user simply "denied" the permission..
            The user had NOT clicked "DO NOT SHOW AGAIN"
            We need to show additional dialog in this case explaining how "allowing this permission" would be useful to the user
         */


        void onPermissionDisabled();
        /*
            The app had launched earlier and the user "denied" the permission..
            AND ALSO had clicked "DO NOT ASK AGAIN"
            We need to show Toask/alertdialog/.. to indicate that the user had denied the permission by checking do not disturb too...
            So, you might want to take the user to setting>app>permission page where the user can allow the permission..


         */

    }

    // preference utility methods
    private static boolean getApplicationLaunchedFirstTime(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_FIRST_TIME_KEY, true);
    }

    private static void setApplicationLaunchedFirstTime(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_FIRST_TIME_KEY, false);
        editor.commit();
    }
    private static void setApplicationLaunchedFirstTimeTrue(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_FIRST_TIME_KEY, true);
        editor.commit();
    }


    private static boolean isRuntimePermissionRequired() {
        return (Build.VERSION.SDK_INT >= 23);
    }

    public static void checkPermission(Activity activity, String permission, PermissionAskListener permissionAskListener) {

        Log.d(TAG, "checkPermission");


        if (!isRuntimePermissionRequired()) {
            /*
                Runtime permission not required,
                THE DEVICE IS RUNNING ON < 23, So, no runtime permission required..
                Simply call **** permissionAskListener.onPermissionGranted() ****
             */


            permissionAskListener.onPermissionGranted();
        } else {
            //runtime permission required here...

            //check if the permission is already granted, i.e the application was launched earlier too, and the user had "allowed" the permission then.
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                /* We don't have permission, two cases arise:
                     1. App launched first time, 
                     2. App launched earlier too, and the user had denied the permission is last launch
                           2A. The user denied permission earlier WITHOUT checking "Never ask again"
                           2B. The user denied permission earlier WITH checking "Never ask again"
                */

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                    /* 
                       shouldShowRequestPermissionRationale returned true
                       this means Case: 2A
                       see the flowchart, the only case when shouldShowRequestPermissionRationale returns "true", is when the application was launched earlier too and the user had "denied" the permission in last launch WITHOUT checking "never show again"
                    */

                    permissionAskListener.onPermissionPreviouslyDenied();
                } else {
                    /*  
                         this means, either - 
                         Case: 1 or Case 2B
                         See Flowchart, shouldShowRequestPermissionRationale returns false, only when app is launched first time (Case: 1) or app was launched earlier too and user HAD checked "Never show again" then (Case: 2B)
                    */
                    if (getApplicationLaunchedFirstTime(activity)) {

                        //Case: 1
                        Log.d(TAG, "ApplicationLaunchedFirstTime");

                        setApplicationLaunchedFirstTime(activity);  //  ** DON'T FORGET THIS **
                        permissionAskListener.onPermissionRequest();

                    } else {
                        //Case: 2B
                        Log.d(TAG, "onPermissionDisabled");

                        permissionAskListener.onPermissionDisabled();
                    }
                }


            } else {
                Log.d(TAG, "Permission already granted");
                setApplicationLaunchedFirstTimeTrue(activity);
                permissionAskListener.onPermissionGranted();
            }
        }
    }
}