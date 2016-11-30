package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.cropimage.CropImage;
import com.loyalty.fragment.customer.ChangePasswodFragment;
import com.loyalty.image.CircleTransformation;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.TakePictureUtils;
import com.loyalty.utils.CheckPermission;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.UserProfileModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvFirstName, tvLastNameValue, tvLastName, tvEmailValue, tvCardNoValue, tvCvvValue, tvCardTypeValue, tvEmail,
            tvMobileNumberValue, tvMobileNumber, tvCardType, tvCardNo, tvCvv, tvExpiryDate, tvFirstNameValue, tvExpiryDateValue,
            tvChangePassword, tvTitle, tv_errorfirstName, tv_errorlastName, tv_erroremail, tv_errormobile, tv_errorcardType,
            tv_errorcardNumber, tv_errorCvv,tvAreacode;
    private Context context;
    private boolean profile = true;
    private String imageName;
    private ImageView iv_toolbar_right2;
    private byte[] byteArrayImage;
    private int x = 0;
    private ImageView ivProfileImage;
    private Toolbar toolbar;
    private String TAG = ProfileActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private TextView tvCountryCode;
    private TextView tvAreCodeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = ProfileActivity.this;

        getIds();
        setToolbar();
        setFonts();
        setListeners();

        if (CommonUtils.isOnline(context)) {
            getProfileAPi();
        }else {
            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
        }

        if(CommonUtils.getPreferencesBoolean(context,"isSocialLogin")){
            tvChangePassword.setVisibility(View.GONE);
        }else{
            tvChangePassword.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        ivProfileImage.setOnClickListener(this);
        iv_toolbar_right2.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvExpiryDateValue.setOnClickListener(this);

    }

    private void getIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
        tvCardType = (TextView) findViewById(R.id.tvCardType);
        tvAreacode = (TextView) findViewById(R.id.tv_areacode);
        tvChangePassword = (TextView) findViewById(R.id.tvChangePassword);
        tvExpiryDateValue = (TextView) findViewById(R.id.tvExpiryDateValue);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tv_errorfirstName = (TextView) findViewById(R.id.tv_errorfirstName);
        tv_errorlastName = (TextView) findViewById(R.id.tv_errorlastName);
        tv_erroremail = (TextView) findViewById(R.id.tv_erroremail);
        tv_errormobile = (TextView) findViewById(R.id.tv_errormobile);
        tv_errorcardType = (TextView) findViewById(R.id.tv_errorcardType);
        tv_errorcardNumber = (TextView) findViewById(R.id.tv_errorcardNumber);
        tv_errorCvv = (TextView) findViewById(R.id.tv_errorCvv);
        tvAreCodeNumber=(TextView)findViewById(R.id.tvAreCodeNumber);
        tvCountryCode=(TextView)findViewById(R.id.tvCountryCode);
        tvFirstNameValue = (TextView) findViewById(R.id.tvFirstNameValue);
        tvLastNameValue = (TextView) findViewById(R.id.tvLastNameValue);
        tvEmailValue = (TextView) findViewById(R.id.tvEmailValue);
        tvMobileNumberValue = (TextView) findViewById(R.id.tvMobileNumberValue);
        tvCvvValue = (TextView) findViewById(R.id.tvCvvValue);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        iv_toolbar_right2 = (ImageView) findViewById(R.id.iv_toolbar_right2);

    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
        tvTitle.setText("Profile");
        iv_toolbar_right2.setVisibility(View.VISIBLE);
        iv_toolbar_right2.setImageResource(R.mipmap.edit);
    }

    private void setFonts() {
        tvFirstName.setTypeface(CommonUtils.setBook(context));
        tvFirstNameValue.setTypeface(CommonUtils.setBook(context));
        tvLastName.setTypeface(CommonUtils.setBook(context));
        tvEmail.setTypeface(CommonUtils.setBook(context));
        tvMobileNumber.setTypeface(CommonUtils.setBook(context));
        tvCardType.setTypeface(CommonUtils.setBook(context));
        tvAreacode.setTypeface(CommonUtils.setBook(context));
        tvChangePassword.setTypeface(CommonUtils.setBook(context), Typeface.BOLD);
        tvFirstNameValue.setTypeface(CommonUtils.setBook(context));
        tvLastNameValue.setTypeface(CommonUtils.setBook(context));
        tvEmailValue.setTypeface(CommonUtils.setBook(context));
        tvMobileNumberValue.setTypeface(CommonUtils.setBook(context));
        tv_errorfirstName.setTypeface(CommonUtils.setBook(context));
        tv_errorlastName.setTypeface(CommonUtils.setBook(context));
        tv_errormobile.setTypeface(CommonUtils.setBook(context));
        tv_errorcardType.setTypeface(CommonUtils.setBook(context));
        tv_errorcardNumber.setTypeface(CommonUtils.setBook(context));
        tv_errorCvv.setTypeface(CommonUtils.setBook(context));
        tv_erroremail.setTypeface(CommonUtils.setBook(context));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_right2: {

                Intent intent = new Intent(context, EditProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.tvChangePassword:
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

    public void getProfileAPi() {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        Call<UserProfileModel> call=apiServices.userInfo(AppConstant.BASIC_TOKEN,Integer.parseInt(CommonUtils.getPreferences(this,AppConstant.USER_ID)));
        startProgressBar(context);
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                stopProgressBar();
                Log.e(TAG,response.body().toString());
                Log.e(TAG,new Gson().toJson(response));
                Log.d(TAG, "onResponse:"+new Gson().toJson(response.body().profileImage));
                if (response!=null&&response.body()!=null)
                {
                    if(response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        tvFirstNameValue.setText(response.body().firstName);
                        tvLastNameValue.setText(response.body().lastName);
                        tvEmailValue.setText(response.body().email);
                        tvMobileNumberValue.setText(response.body().phone);
                        if(response.body().areaCode!=null&&response.body().countryCode!=null||response.body().areaCode.equalsIgnoreCase("")
                                &&response.body().countryCode.equalsIgnoreCase("")) {
                            tvAreCodeNumber.setText(response.body().areaCode);
                            tvCountryCode.setText(response.body().countryCode);
                        }
                            String profileImage=response.body().profileImage;

                        if(profileImage!=null && profileImage.trim().length()!=0) {
                            Picasso.with(context).load(profileImage.trim()).resize(100,100).error(R.mipmap.user2).into(ivProfileImage);
                            Log.e("Image","Images : "+profileImage);
                        }
                        else
                        {
                            Log.e("TAG","Image Null");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                stopProgressBar();

            }
        });
    }
    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null,AppConstant.PLEASE_WAIT,true,false);
    }

    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}

