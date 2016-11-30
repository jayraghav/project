package com.loyalty.activity.customer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.ServiceUrls;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import retrofit2.Call;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher {
    private static final String TAG =SignUpActivity.class.getSimpleName();
    private TextView tvSubmit, tvBySigningUP, tvTermsConditions, tvOfTheApp, tv_errorfirstName, tv_errorLastName,
            tv_erroremail, tv_errormobile, tv_errorpassword, tv_errorConfirpassword, tvTitle,tvErrorAreaCode,tvErrorCountryCode;
    private EditText etFirstName, etLastName, etEmail, etMobileNumber, etPassword, etConfirmPwd,etAreaCode,etCounteryCode;
    private Context context;
    private Toolbar toolbar;
    public String firstName, lastName, mobileNumber, emailId, password;
    private String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private ProgressDialog progressDialog;
    String userId;
    private EditText etReferalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = SignUpActivity.this;
        getIds();
        setToolbar();
        setFonts();
        setListeners();
    }

    private void setFonts() {
        tvSubmit.setTypeface(CommonUtils.setBook(context));
        tvBySigningUP.setTypeface(CommonUtils.setBook(context));
        tvTermsConditions.setTypeface(CommonUtils.setBook(context));
        tvOfTheApp.setTypeface(CommonUtils.setBook(context));
        tv_errorfirstName.setTypeface(CommonUtils.setBook(context));
        tv_errorLastName.setTypeface(CommonUtils.setBook(context));
        tv_erroremail.setTypeface(CommonUtils.setBook(context));
        tv_errormobile.setTypeface(CommonUtils.setBook(context));
        tv_errorpassword.setTypeface(CommonUtils.setBook(context));
        tv_errorConfirpassword.setTypeface(CommonUtils.setBook(context));
        etFirstName.setTypeface(CommonUtils.setBook(context));
        etLastName.setTypeface(CommonUtils.setBook(context));
        etMobileNumber.setTypeface(CommonUtils.setBook(context));
        etEmail.setTypeface(CommonUtils.setBook(context));
        etPassword.setTypeface(CommonUtils.setBook(context));
        etConfirmPwd.setTypeface(CommonUtils.setBook(context));

        tvErrorAreaCode.setTypeface(CommonUtils.setBook(context));
        tvErrorCountryCode.setTypeface(CommonUtils.setBook(context));
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
        tvTitle.setText("Sign Up");
    }

    private void getIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvBySigningUP = (TextView) findViewById(R.id.tvBySigningUP);
        tvTermsConditions = (TextView) findViewById(R.id.tvTermsConditions);
        tvOfTheApp = (TextView) findViewById(R.id.tvOfTheApp);
        tv_errorfirstName = (TextView) findViewById(R.id.tv_errorfirstName);
        tv_errorLastName = (TextView) findViewById(R.id.tv_errorLastName);
        tv_erroremail = (TextView) findViewById(R.id.tv_erroremail);
        tv_errormobile = (TextView) findViewById(R.id.tv_errormobile);
        tv_errorpassword = (TextView) findViewById(R.id.tv_errorpassword);
        tv_errorConfirpassword = (TextView) findViewById(R.id.tv_errorConfirpassword);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPwd = (EditText) findViewById(R.id.etConfirmPwd);
        tvErrorAreaCode = (TextView) findViewById(R.id.tvErrorAreaCode);
        tvErrorCountryCode = (TextView) findViewById(R.id.tvErrorCountryCode);
        etAreaCode=(EditText)findViewById(R.id.etAreaCode);
        etCounteryCode=(EditText)findViewById(R.id.etCounteryCode);
        etReferalId=(EditText)findViewById(R.id.et_referal_id);
    }
    private void setListeners() {
        tvSubmit.setOnClickListener(this);
        tvTermsConditions.setOnClickListener(this);
        etFirstName.addTextChangedListener(this);
        etLastName.addTextChangedListener(this);
        etMobileNumber.addTextChangedListener(this);
        etEmail.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etConfirmPwd.addTextChangedListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                if(checkValidation()) {
                    if (CommonUtils.isOnline(context)) {
                        getRegister(ServiceUrls.SIGNUPURL);
                    } else {
                        CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                    }
                }
                else {

                }
                break;
            case R.id.tvTermsConditions:
                startActivity(new Intent(SignUpActivity.this, TermsConditionsActivity.class));
                break;
        }

    }
    private boolean checkValidation() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            tv_errorfirstName.setText(getString(R.string.please_enter_first_name));
            tv_errorfirstName.setVisibility(View.VISIBLE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etFirstName.requestFocus();
            return false;

        } else if (etLastName.getText().toString().trim().equalsIgnoreCase("")) {
            tv_errorLastName.setText(getString(R.string.please_enter_last_name));
            tv_errorLastName.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etLastName.requestFocus();
            return false;
        } else if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            tv_erroremail.setText(getString(R.string.please_enter_email));
            tv_erroremail.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etEmail.requestFocus();
            return false;
        } else if (!(etEmail.getText().toString().trim().matches(EMAIL_PATTERN))) {
            tv_erroremail.setText(getString(R.string.enter_valid_emailid));
            tv_erroremail.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etEmail.requestFocus();
            return false;
        }
        else if (etMobileNumber.getText().toString().trim().equalsIgnoreCase("")) {
            tv_errormobile.setText(getString(R.string.please_enter_mobile_number));
            tv_errormobile.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etMobileNumber.requestFocus();
            return false;
        } else if (etMobileNumber.getText().length() < 10) {
            tv_errormobile.setText(getString(R.string.mobile_number_must_be));
            tv_errormobile.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            return false;
        } else if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            tv_errorpassword.setText(getString(R.string.please_enter_password));
            tv_errorpassword.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etPassword.requestFocus();
            //etMobileNumber.requestFocus();
            return false;
        } else if (etPassword.getText().length() < 8) {
            tv_errorpassword.setText(getString(R.string.password_must_be));
            tv_errorpassword.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorConfirpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etPassword.requestFocus();
            return false;
        }
        else if (etConfirmPwd.getText().toString().trim().isEmpty()) {
            tv_errorConfirpassword.setText(getString(R.string.please_enter_conf_password));
            tv_errorConfirpassword.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etConfirmPwd.requestFocus();
            return false;
        } else if (!(etPassword.getText().toString().trim()).matches(etConfirmPwd.getText().toString().trim())) {
            tv_errorConfirpassword.setText(getString(R.string.password_doesnot_match));
            tv_errorConfirpassword.setVisibility(View.VISIBLE);
            tv_errorfirstName.setVisibility(View.GONE);
            tv_errorLastName.setVisibility(View.GONE);
            tv_erroremail.setVisibility(View.GONE);
            tv_errormobile.setVisibility(View.GONE);
            tv_errorpassword.setVisibility(View.GONE);
            tvErrorAreaCode.setVisibility(View.GONE);
            tvErrorCountryCode.setVisibility(View.GONE);
            etConfirmPwd.requestFocus();
            return false;
        }  else {
            return true;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (etFirstName.getText().toString().length() > 0) {
            tv_errorfirstName.setVisibility(View.GONE);
        }
        if (etLastName.getText().toString().length() > 0) {
            tv_errorLastName.setVisibility(View.GONE);
        }
        if (etEmail.getText().toString().length() > 0) {
            tv_erroremail.setVisibility(View.GONE);
        }
        if (etMobileNumber.getText().toString().length() > 0) {
            tv_errormobile.setVisibility(View.GONE);
        }
        if (etPassword.getText().toString().length() > 0) {
            tv_errorpassword.setVisibility(View.GONE);
        }
        if (etConfirmPwd.getText().toString().length() > 0) {
            tv_errorConfirpassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void getRegister(String url) {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        Log.e(TAG,apiServices.toString());
        Log.e(TAG,url);

        final RequestModel requestModel = new RequestModel();
        requestModel.email = etEmail.getText().toString().trim();
        requestModel.deviceType = "Android";
        String referID=etReferalId.getText().toString().trim();
        if(referID!=null&&referID.length()!=0)
        {
            requestModel.referById=referID;
        }
        else {
            requestModel.referById="";
        }
        requestModel.firstName = etFirstName.getText().toString().trim();
        requestModel.lastName = etLastName.getText().toString().trim();
        requestModel.password = etPassword.getText().toString().trim();
        requestModel.phone = etMobileNumber.getText().toString().trim();
//        requestModel.deviceToken = "jysdfsdjhfj";
        requestModel.deviceToken =CommonUtils.getPreferences(this,AppConstant.DEVICE_TOKEN);
        Log.e("tag","device token in signup"+ CommonUtils.getPreferences(this,AppConstant.DEVICE_TOKEN));

        requestModel.countryCode = etCounteryCode.getText().toString().trim();
        requestModel.userName = etFirstName.getText().toString().trim() + etLastName.getText().toString().trim();
        requestModel.areaCode = etAreaCode.getText().toString().trim();
        requestModel.profileImage = "";
        requestModel.socialId = "";
        requestModel.socialLoginType = "";
        requestModel.socialId = "";
        requestModel.deviceKey=CommonUtils.getPreferences(this,AppConstant.DEVICE_KEY);
        startProgressBar(context);

        Call<ResponseModel> call = apiServices.userSignup(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,"request : >>>> "+new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e(TAG,"response : >>>> "+new Gson().toJson(response.body()));
                stopProgressBar();
                if(response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        userId=response.body().userInfo.userId;
                        CommonUtils.savePreferencesString(context, AppConstant.USER_ID,response.body().userInfo.userId);
                        CommonUtils.savePreferencesString(context,AppConstant.DEVICE_ID,requestModel.deviceKey);
                        CommonUtils.savePreferencesString(context,AppConstant.DEVICE_TOKEN,requestModel.deviceToken);
                        CommonUtils.savePreferences(context,AppConstant.EMAIL_ID,response.body().userInfo.email);
                        CommonUtils.savePreferences(context,AppConstant.TOKEN,response.body().token);
                        CommonUtils.savePreferences(context,AppConstant.REFERENCEID,response.body().referenceId);
                        startActivity(new Intent(SignUpActivity.this,OTPActivity.class));
                        finish();
                    }
                    else{
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
    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null,AppConstant.PLEASE_WAIT,true,false);
    }

    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}