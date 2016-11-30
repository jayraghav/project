package com.loyalty.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by vinay.tripathi on 2/6/16.
 */
public class CheckPermission {
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    public static boolean checkIsMarshMallowVersion() {
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion >= Build.VERSION_CODES.M;

    }

    public static boolean checkCameraPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;

    }

    public static boolean checkContactsPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;

    }

    public static void requestCameraPermission(Activity activity, int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

        }
    }
    public static void requestContactsPermission(Activity activity, int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }


}
