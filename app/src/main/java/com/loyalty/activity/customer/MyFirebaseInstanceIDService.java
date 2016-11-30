package com.loyalty.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    String refreshedToken;
    private Context context=MyFirebaseInstanceIDService.this;

    @Override
    public void onTokenRefresh() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        CommonUtils.savePreferencesString(context, AppConstant.DEVICE_TOKEN,refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        sendBroadcast(new Intent("Token").putExtra("refreshedToken",refreshedToken));
    }
}
