package com.loyalty.activity.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.ForgetPasswordResponse;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher{
    private static final String TAG =ForgotPasswordActivity.class.getSimpleName() ;
    private TextView tvHeader,tvForgotPassword,tvEnterEmailId,tvSubmit,tvTitle,tv_erroremail;
    private EditText etEmail;
    private Context context;
    private Toolbar toolbar;
    private String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context=ForgotPasswordActivity.this;
        getIds();
        setFonts();
        setToolbar();
        setListeners();
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
        tvTitle.setText("Forgot Password");
    }

    private void setFonts() {
        tvForgotPassword.setTypeface(CommonUtils.setBook(context));
        tvEnterEmailId.setTypeface(CommonUtils.setBook(context));
        tvSubmit.setTypeface(CommonUtils.setBook(context));
        etEmail.setTypeface(CommonUtils.setBook(context));
        tvTitle.setTypeface(CommonUtils.setBook(context));
        tv_erroremail.setTypeface(CommonUtils.setBook(context));
        etEmail.addTextChangedListener(this);
    }

    private void getIds() {

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvForgotPassword=(TextView)findViewById(R.id.tvForgotPassword);
        tvEnterEmailId=(TextView)findViewById(R.id.tvEnterEmailId);
        tvSubmit=(TextView)findViewById(R.id.tvSubmit);
        etEmail=(EditText) findViewById(R.id.etEmail);
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        tv_erroremail=(TextView)findViewById(R.id.tv_erroremail);
    }

    private void setListeners() {
        tvSubmit.setOnClickListener(this);
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tvSubmit: {
                if (checkvalidation()) {
                    if (CommonUtils.isOnline(context)) {
                        getForgetpassword();
                    }else {
                        CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                    }
                }
            }
        }

    }

    private boolean checkvalidation() {
        if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
            tv_erroremail.setText(getString(R.string.please_enter_email));
            tv_erroremail.setVisibility(View.VISIBLE);
            etEmail.requestFocus();
            return false;
        } else if(!(etEmail.getText().toString().trim().matches(EMAIL_PATTERN))) {
            tv_erroremail.setText(getString(R.string.enter_valid_emailid));
            tv_erroremail.setVisibility(View.VISIBLE);
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (etEmail.getText().toString().length() > 0) {
            tv_erroremail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    public void getForgetpassword()
    {
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        final RequestModel requestModel=new RequestModel();
        requestModel.email=etEmail.getText().toString().trim();
        Log.e(TAG,"request.........."+new Gson().toJson(requestModel));
        Call<ForgetPasswordResponse> call=apiServices.forgetPasswordApi(AppConstant.BASIC_TOKEN,requestModel);
        call.enqueue(new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {

                if (response.body().responseCode!=null)
                {
                    Log.e(TAG,new Gson().toJson(requestModel));
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        showMessage();
                    } else {
                        CommonUtils.showToast(ForgotPasswordActivity.this,response.body().responseMessage);

                    }
                }
            }
            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {

            }
        });

    }
    public void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your password has been sent to your email")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
