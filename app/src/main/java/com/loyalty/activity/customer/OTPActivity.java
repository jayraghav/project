package com.loyalty.activity.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;

import retrofit2.Call;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResend, tvVerify;
    private EditText etOTP;
    private String otp;
    private TextView tvErrorOtp;
    private Context context;
    private ProgressDialog progressDialog;
    private String TAG = OTPActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        context = OTPActivity.this;
        findId();
        setListener();

    }

    private void findId() {
        tvResend = (TextView) findViewById(R.id.tv_resend);
        tvVerify = (TextView) findViewById(R.id.tv_verify);
        etOTP = (EditText) findViewById(R.id.et_Otp_code);
        tvErrorOtp = (TextView) findViewById(R.id.tv_errorOtp);

    }

    private boolean validate() {
        if (etOTP.getText().toString().trim() != "" && etOTP.length() < 4) {
            tvErrorOtp.setVisibility(View.VISIBLE);
            etOTP.requestFocus();
            return false;
        } else {
            otp = etOTP.getText().toString().trim();
            return true;
        }
    }

    private void setListener() {
        tvVerify.setOnClickListener(this);
        tvResend.setOnClickListener(this);
    }

    private void otpVerificationApi(String otp) {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);

        RequestModel requestModel = new RequestModel();
        requestModel.setEmail(CommonUtils.getPreferences(context, AppConstant.EMAIL_ID));
        requestModel.setOtp(otp);
        Call<ResponseModel> call = apiServices.verifyOtp(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG, new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null && response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        CommonUtils.showToast(context, "Congratulations !!! You have registered successfully");
                        Intent intent = new Intent(OTPActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        CommonUtils.showToast(context, response.body().responseMessage);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void resendOtp() {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel = new RequestModel();
        requestModel.setEmail(CommonUtils.getPreferences(context, AppConstant.EMAIL_ID));
        Call<ResponseModel> call = apiServices.resendOtp(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG, new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        CommonUtils.showToast(context, "OTP code has been sent to you email address");
                    } else {
                        CommonUtils.showToast(context, response.body().responseMessage);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });

    }

    public void startProgressBar(Context context) {
        progressDialog = ProgressDialog.show(context, null, AppConstant.PLEASE_WAIT, true, false);
    }

    public void stopProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_verify:
                boolean check = validate();
                if (check) {
                    if (CommonUtils.isOnline(context)) {
                        otpVerificationApi(otp);
                    } else {
                        CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                    }
                }

                break;
            case R.id.tv_resend:

                if (CommonUtils.isOnline(context)) {
                    resendOtp();
                } else {
                    CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                }
                break;
        }
    }
}

