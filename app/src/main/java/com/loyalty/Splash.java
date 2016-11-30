package com.loyalty;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loyalty.activity.customer.LoginActivity;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CheckPermission;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.LocationResult;
import com.loyalty.utils.LocationTracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by anjalipandey on 23/8/16.
 */
public class Splash extends AppCompatActivity {
    private Intent intent;
    private LocationManager locationManager;
    private boolean statusOfGPS;
    private Handler handler = new Handler();
    private String TAG=Splash.class.getSimpleName();
    private Context context;
    private String refreshedToken;
    /*server key for push:- AIzaSyC4O-Bbn463dx_zBvJSg8gg0ebjsnJqbAw*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=Splash.this;

        String  regid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("tag","device id "+regid);
        CommonUtils.savePreferencesString(context, AppConstant.DEVICE_KEY, regid);

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token",""+refreshedToken);

        CommonUtils.savePreferencesString(context,AppConstant.DEVICE_TOKEN,refreshedToken);
        Log.e("tag","DEVICE_TOKEN in splash "+refreshedToken);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        getKeyHash();
        statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(Splash.this, TutorialActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }, 3000);

   /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (refreshedToken.length() > 0) {
                        Log.e(TAG, "Token>>>>>>>>>>>>>>ddd>>" + refreshedToken.length());
                        CommonUtils.savePreferencesString(context, CommonUtils.AppConstants.FCM_REGISTRATION_ID, refreshedToken);
                        Intent i = new Intent(context, TutorialActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        CommonUtils.showToast(context, "Unable to fetch device token.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 3000);*/
/*.....................generate hash key ...............*/
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.loyalty",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }
    private void doThing() {
        if (statusOfGPS) {
            new LocationTracker(Splash.this, new LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    if (location != null) {
                        Log.e("Location", "Location_Latitude" + String.valueOf(location.getLatitude()));
                        Log.e("Location", "Location_Longitude" + String.valueOf(location.getLongitude()));
                        CommonUtils.savePreferencesString(Splash.this,AppConstant.LATTITUDE,String.valueOf(location.getLatitude()));
                        CommonUtils.savePreferencesString(Splash.this,AppConstant.LOGITUDE,String.valueOf(location.getLongitude()));
                    }
                }
            }).onUpdateLocation();
        }
        else {
            buildAlertMessageNoGps();
        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        doThing();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}


   /*if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            if (CheckPermission.checkIsMarshMallowVersion()) {
                //doThing();
            }
            else {
                buildAlertMessageNoGps();
            }

        }
*/

 /*  if (location != null) {
        Log.e("Location", "Location_Latitude" + String.valueOf(location.getLatitude()));
        Log.e("Location", "Location_Longitude" + String.valueOf(location.getLongitude()));
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (gpsTracker != null){
                if (gpsTracker.getLatitude() == 0 && gpsTracker.getLongitude() == 0){
                    handler.postDelayed(runnable,1000);
                }else {
                    handler.removeCallbacks(runnable);
                    doThing();
                }
            }
        }
    };*/