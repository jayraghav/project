package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.facebook.FacebookSdk;
import com.loyalty.utils.PathJSONParser;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.UserProfileData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends FragmentActivity implements View.OnClickListener, TextWatcher, View.OnTouchListener,GoogleApiClient.OnConnectionFailedListener
                                                                {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context context;
    private EditText etEmail, etPassword;
    private CheckBox checkboxRememberme;
    private TextView tvForgotPassword, tvLogin, tvIfNewUser, tvSignUp, tv_errorpassword, tv_erroremail;
    private ImageView ivFacebook, ivGplus;
    private CallbackManager callbackManager;
    private String socialType = "";
    private String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private ProgressDialog progressDialog;
    private String image_value;
    private static final int PROFILE_PIC_SIZE = 400;
    private String userId;
    private static final int RC_SIGN_IN =100;
    public GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private GoogleSignInOptions gso;
    private String deviceKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        context = LoginActivity.this;


        deviceKey=CommonUtils.getPreferences(this,AppConstant.DEVICE_KEY);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getIds();
        setListeners();
        setFonts();

        if (CommonUtils.getPreferences(context, AppConstant.IS_LOGGED_IN).equalsIgnoreCase("")) {
            etEmail.setText("");
            etPassword.setText("");
        } else {
            etEmail.setText(CommonUtils.getPreferences(context, AppConstant.USER_NAME));
            etPassword.setText(CommonUtils.getPreferences(context, AppConstant.PASSWORD));
            checkboxRememberme.setButtonDrawable(R.mipmap.check);
        }
    }

    private void getIds() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        checkboxRememberme = (CheckBox) findViewById(R.id.checkboxRememberme);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvIfNewUser = (TextView) findViewById(R.id.tvIfNewUser);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tv_errorpassword = (TextView) findViewById(R.id.tv_errorpassword);
        tv_erroremail = (TextView) findViewById(R.id.tv_erroremail);
        ivFacebook = (ImageView) findViewById(R.id.ivFacebook);
        ivGplus = (ImageView) findViewById(R.id.ivGPlus);
    }

    private void setFonts() {

        etEmail.setTypeface(CommonUtils.setBook(context));
        etPassword.setTypeface(CommonUtils.setBook(context));
        checkboxRememberme.setTypeface(CommonUtils.setBook(context));
        tvForgotPassword.setTypeface(CommonUtils.setBook(context));
        tvLogin.setTypeface(CommonUtils.setBook(context));
        tvIfNewUser.setTypeface(CommonUtils.setBook(context));
        tvSignUp.setTypeface(CommonUtils.setBook(context));
    }

    private void setListeners() {
        tvLogin.setOnClickListener(this);
        etEmail.setOnClickListener(this);
        checkboxRememberme.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        ivFacebook.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        ivGplus.setOnClickListener(this);
        etEmail.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin: {
                if (checkvalidation()) {
                    if (CommonUtils.isOnline(context)) {
                        getLoginApi();
                    } else {
                        CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                    }
                }
                break;
            }
            case R.id.ivGPlus:
                socialType = "google";
                userId = CommonUtils.getPreferences(context, AppConstant.USER_ID);
                Log.e("tag","user id google plus" + userId);
                if (userId.equalsIgnoreCase("")) {
                    if (CommonUtils.isOnline(context)) {
                        googleSignIn();
                    }else {
                        CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                    }
                }else {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
                break;
            case R.id.ivFacebook:
                socialType = "facebook";
                userId = CommonUtils.getPreferences(context, AppConstant.USER_ID);
                if (userId.equalsIgnoreCase("")) {
                    if (CommonUtils.isOnline(context)) {
                        loginWithFacebook();
                    } else {
                        CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                    }
                } else {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
                break;
            case R.id.checkboxRememberme:

                if (checkboxRememberme.isChecked()) {
                    checkboxRememberme.setButtonDrawable(R.mipmap.check);
                    CommonUtils.savePreferencesString(context, AppConstant.USER_NAME, etEmail.getText().toString().trim());
                    CommonUtils.savePreferencesString(context, AppConstant.PASSWORD, etPassword.getText().toString().trim());
                    CommonUtils.savePreferencesString(context, AppConstant.IS_LOGGED_IN, "Yes");

                } else {
                    checkboxRememberme.setButtonDrawable(R.mipmap.uncheck);
                    CommonUtils.savePreferencesString(context, AppConstant.IS_LOGGED_IN,"");
                }
                break;


            case R.id.tvForgotPassword:

                Intent intent = new Intent(context, ForgotPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.tvSignUp:

                Intent intent1 = new Intent(context, SignUpActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;

        }
    }

    private boolean checkvalidation() {

        boolean result = false;
        if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            tv_erroremail.setText(getString(R.string.please_enter_email));
            tv_erroremail.setVisibility(View.VISIBLE);
            tv_errorpassword.setVisibility(View.GONE);
            etEmail.requestFocus();
            result = false;
        } else if (!(etEmail.getText().toString().trim().matches(EMAIL_PATTERN))) {
            tv_erroremail.setText(getString(R.string.enter_valid_emailid));
            tv_erroremail.setVisibility(View.VISIBLE);
            tv_errorpassword.setVisibility(View.GONE);
            etEmail.requestFocus();
            result = false;
        } else if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            tv_erroremail.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.VISIBLE);
            tv_errorpassword.setText(R.string.please_enter_password);
            etPassword.requestFocus();
            result = false;

        } else if (etPassword.getText().length() < 8) {
            tv_errorpassword.setText("*Password must be atleast of 8 characters");
            tv_errorpassword.setVisibility(View.VISIBLE);
            tv_erroremail.setVisibility(View.GONE);
            etPassword.requestFocus();
            result = false;
        } else {
            result = true;
        }

        return result;
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (etEmail.getText().toString().length() > 0) {
            tv_erroremail.setVisibility(View.GONE);
        }
        if (etPassword.getText().toString().length() > 0) {
            tv_errorpassword.setVisibility(View.GONE);
        }
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public void afterTextChanged(Editable s) {

    }


    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        final String key=CommonUtils.getPreferences(this,AppConstant.DEVICE_KEY);
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String googleId = acct.getId();
            String googlepersonName = acct.getDisplayName();
            String googleLastName = acct.getDisplayName();
            String googlepersonPhotoUrl = String.valueOf(acct.getPhotoUrl());
            String email = acct.getEmail();
            String googleGender = acct.getEmail();
            Log.e("GooRESPONSE BY GRAPH: ", "" + email + googleId + googlepersonName + googlepersonPhotoUrl + googleGender );
            if (email != null && email.length() > 0) {
                image_value = googlepersonPhotoUrl.substring(0, googlepersonPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;
                getGoogleLogin(email, "1234567898", "123456789", googlepersonName, googleLastName, "91", googleLastName,
                        image_value, googleId,key, "4865DchgsvcsvcjHSbvcjhsZbcgjsfjhdsvchdsbvc986565636365634897243987",
                        "Android", "google", "1323");
            }

            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }


    private void  getLoginApi() {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel = new RequestModel();
        requestModel.email = etEmail.getText().toString().trim();
        requestModel.password = etPassword.getText().toString().trim();
        requestModel.deviceKey = deviceKey;
        requestModel.deviceToken = CommonUtils.getPreferences(this,AppConstant.DEVICE_TOKEN);
//        requestModel.deviceToken = "jysdfsdjhfj";
        requestModel.deviceType = "Android";

        Call<UserProfileData> call = apiServices.userLogin(AppConstant.BASIC_TOKEN,requestModel);
        startProgressBar(context);
        Log.e(TAG + "request>>>>>>>>>", new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<UserProfileData>() {
            @Override
            public void onResponse(Call<UserProfileData> call, Response<UserProfileData> profileDataResponse) {
                Log.e(TAG, "response : >>>> " + new Gson().toJson(profileDataResponse.body()));
                stopProgressBar();
                if (profileDataResponse.body() != null) {
                    if (profileDataResponse.body().responseCode.equalsIgnoreCase("200")) {

                        CommonUtils.savePreferencesString(context, AppConstant.USER_ID, profileDataResponse.body().userId);
                        CommonUtils.savePreferencesString(context, AppConstant.AREA_CODE, profileDataResponse.body().areaCode);
                        CommonUtils.savePreferencesString(context,AppConstant.CUSTOMER_TOKEN,profileDataResponse.body().customerToken);
                        CommonUtils.savePreferences(context,AppConstant.REFERENCEID,profileDataResponse.body().referenceId);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                    } else if (profileDataResponse.body().responseCode.equalsIgnoreCase("201")) {
                        CommonUtils.showToast(context, profileDataResponse.body().responseMessage);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<UserProfileData> call, Throwable t) {
                CommonUtils.showToast(context, "Service resonse error Failure successfully");
                Log.e(TAG, t.toString());
                stopProgressBar();

            }
        });
    }
    public void getSocialTypeLogin(String email, String phone, String password, String firstName,
                                   String lastName, String countryCode, String userName, String profileImage, String socialId,
                                   String deviceKey, String deviceToken, String deviceType, String socialLoginType,
                                   String areaCode) {
        startProgressBar(context);
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel = new RequestModel();
        requestModel.email = email;
        requestModel.password = password;
        requestModel.deviceToken = deviceToken;
        requestModel.deviceKey =deviceKey;
        ;
        requestModel.profileImage = profileImage;
        requestModel.socialId = socialId;
        requestModel.socialLoginType = socialLoginType;
        requestModel.areaCode = areaCode;
        requestModel.countryCode = countryCode;
        requestModel.firstName = firstName;
        requestModel.lastName = lastName;
        requestModel.deviceType = deviceType;
        requestModel.userName = userName;
        requestModel.phone="";
        requestModel.customerToken="";
        requestModel.referById="";

        Call<ResponseModel> call = apiServices.socialSignup(AppConstant.BASIC_TOKEN,requestModel);
        Log.e("Login request",new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null&&response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {

                        CommonUtils.savePreferencesString(context, AppConstant.USER_ID, response.body().userInfo.userId);
                        CommonUtils.savePreferencesString(context, AppConstant.AREA_CODE, response.body().userInfo.areaCode);
                        CommonUtils.savePreferencesString(context,AppConstant.CUSTOMER_TOKEN,response.body().customerToken);
                        CommonUtils.savePreferencesBoolean(context,"isSocialLogin",true);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else if(response.body().responseCode.equalsIgnoreCase("201"))
                    {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                    else {
                        CommonUtils.showToast(context,response.body().responseMessage);

                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();

            }
        });
    }

    public void getGoogleLogin(String email, String phone, String password, String firstName,
                               String lastName, String countryCode, String userName, String profileImage, String socialId,
                               final String deviceKey, String deviceToken, String deviceType, String socialLoginType,
                               String areaCode) {
        startProgressBar(context);
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel = new RequestModel();
        requestModel.email = email;
        requestModel.password = password;
        requestModel.deviceToken = deviceToken;
        requestModel.deviceKey =deviceKey;
        requestModel.profileImage = profileImage;
        requestModel.socialId = socialId;
        requestModel.socialLoginType = socialLoginType;
        requestModel.areaCode = areaCode;
        requestModel.countryCode = countryCode;
        requestModel.firstName = firstName;
        requestModel.lastName = lastName;
        requestModel.deviceType = deviceType;
        requestModel.userName = userName;
        requestModel.customerToken="";
        requestModel.referById="";

        Call<ResponseModel> call = apiServices.socialSignup(AppConstant.BASIC_TOKEN,requestModel);
        startProgressBar(context);
        Log.e("Login request",new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null && response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        CommonUtils.savePreferencesString(context, AppConstant.USER_ID, response.body().userInfo.userId);
                        CommonUtils.savePreferencesString(context,AppConstant.CUSTOMER_TOKEN,response.body().customerToken);
                        CommonUtils.savePreferencesString(context, AppConstant.AREA_CODE, response.body().userInfo.areaCode);
                        CommonUtils.savePreferencesBoolean(context,"isSocialLogin",true);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else if(response.body().responseCode.equalsIgnoreCase("201"))
                    {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                    else {
                        CommonUtils.showToast(context,response.body().responseMessage);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }

    public void loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        final String key=CommonUtils.getPreferences(this,AppConstant.DEVICE_KEY);
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "user_birthday", "public_profile", "user_friends", "user_location"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String accessToken = loginResult.getAccessToken().getToken();
                if (accessToken != null) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.e("Graph response", response.toString());
                                    String fbid = response.getJSONObject().optString("id");
                                    String first_name = response.getJSONObject().optString("first_name");
                                    String last_name = response.getJSONObject().optString("last_name");
                                    String user_name = response.getJSONObject().optString("name");
                                    String gender = response.getJSONObject().optString("gender");
                                    String email = response.getJSONObject().optString("email");
                                    // String phone=response.getJSONObject().optString("phone");
                                    String userName = first_name + " " + last_name;
                                    Log.e("RESPONSE BY GRAPH: ", "" + email + fbid + first_name + last_name + user_name + gender);
                                    if (email != null && email.length() > 0) {
                                        image_value = "https://graph.facebook.com/" + fbid + "/picture?type=large";
                                        getSocialTypeLogin(email, "1234567898", "123456789", first_name, last_name, "91", userName,
                                                image_value, fbid,key, "4865DchgsvcsvcjHSbvcjhsZbcgjsfjhdsvchdsbvc986565636365634897243987",
                                                "Android", "fb", "1323");
                                    } else {
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name,last_name,name,birthday,gender,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                Log.i("debug", "LoginManager FacebookCallback onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("debug", "LoginManager FacebookCallback onError");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null ) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("connection Failed", "connection failed");
        if (!mIsResolving && mShouldResolve) {
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
            }
        } else {
        }
    }

    public void startProgressBar(Context context) {
        progressDialog = ProgressDialog.show(context, null, AppConstant.PLEASE_WAIT, true, false);
    }
    public void stopProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}



