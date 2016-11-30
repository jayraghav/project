package com.loyalty.activity.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.ChangePasswordModel;
import com.loyalty.webserivcemodel.RequestModel;

import retrofit2.Call;
import retrofit2.Response;

public class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etOldPwd,etNewPwd,etConfirmPwd;
    private Context mContext;

    private ImageView ivToolbarLeft,ivToolbarRight1,ivToolbarRight2;
    private TextView tvTitle,tvUpdate,tvErrorOldPwd,tvErrorNewPwd,tvErrorConfirmPwd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_password);
        mContext=ChangePasswordActivity.this;
        findIds();
        setListeners();
        setFonts();
    }

    private void setFonts()
    {
        etConfirmPwd.setTypeface(CommonUtils.setBook(mContext));
        etNewPwd.setTypeface(CommonUtils.setBook(mContext));
        etOldPwd.setTypeface(CommonUtils.setBook(mContext));
        tvUpdate.setTypeface(CommonUtils.setBook(mContext), Typeface.BOLD);
        tvErrorNewPwd.setTypeface(CommonUtils.setBook(mContext));
        tvErrorConfirmPwd.setTypeface(CommonUtils.setBook(mContext));
        tvErrorOldPwd.setTypeface(CommonUtils.setBook(mContext));
    }

    private void findIds() {
        tvUpdate=(TextView) findViewById(R.id.tvUpdate);
        tvErrorOldPwd=(TextView) findViewById(R.id.tvErrorOldPwd);
        tvErrorNewPwd=(TextView) findViewById(R.id.tvErrorNewPwd);
        ivToolbarRight1=(ImageView) findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView) findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvErrorConfirmPwd=(TextView) findViewById(R.id.tvErrorConfirmedPwd);
        etOldPwd=(EditText) findViewById(R.id.etOldPwd);
        etNewPwd=(EditText) findViewById(R.id.etNewPwd);
        etConfirmPwd=(EditText) findViewById(R.id.etConfirmPwd);
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText("Change Password");
        ivToolbarLeft=(ImageView) findViewById(R.id.iv_toolbar_left);
        ivToolbarLeft.setVisibility(View.VISIBLE);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void setListeners()
    {
        tvUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvUpdate) {
            if(checkValidations()) {
                if(CommonUtils.isOnline(mContext)) {
                    getChangePasswordApi();
                } else {
                    CommonUtils.showToast(mContext,AppConstant.PLEASE_CHECK_INTERNET);
                }
            }
        }

    }

    private boolean checkValidations()
    {
        if (etOldPwd.getText().toString().trim().length() == 0)
        {
            tvErrorOldPwd.setVisibility(View.VISIBLE);
            tvErrorOldPwd.setText("*Please enter password");
            etOldPwd.requestFocus();
            etOldPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorOldPwd.setVisibility(View.GONE);

                }
            });
            return false;
        }
        else if (etOldPwd.getText().toString().trim().length() < 8) {
            tvErrorOldPwd.setVisibility(View.VISIBLE);
            tvErrorOldPwd.setText("*Old password must contain at least 8 characters");
            etOldPwd.requestFocus();
            etOldPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorOldPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if (etNewPwd.getText().toString().trim().length() == 0)
        {
            tvErrorNewPwd.setVisibility(View.VISIBLE);
            tvErrorNewPwd.setText("*Please enter new password");
            etNewPwd.requestFocus();
            etNewPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorNewPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if (etNewPwd.getText().toString().trim().length() < 8) {
            tvErrorNewPwd.setVisibility(View.VISIBLE);
            tvErrorNewPwd.setText("*New password must contain at least 8 characters");
            etNewPwd.requestFocus();
            etNewPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorNewPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if (etConfirmPwd.getText().toString().trim().length() == 0) {
            tvErrorConfirmPwd.setVisibility(View.VISIBLE);
            tvErrorConfirmPwd.setText("*Please enter new password");
            etConfirmPwd.requestFocus();
            etConfirmPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorConfirmPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if(etConfirmPwd.getText().toString().trim().length() < 8||!etConfirmPwd.getText().toString().equalsIgnoreCase(etNewPwd.getText().toString())) {
            tvErrorConfirmPwd.setVisibility(View.VISIBLE);
            tvErrorConfirmPwd.setText("*Password & Confirm password does not match");
            etConfirmPwd.requestFocus();
            etConfirmPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorConfirmPwd.setVisibility(View.GONE);

                }
            });

            return false;
        }
        else
        {
            tvErrorConfirmPwd.setVisibility(View.GONE);
            tvErrorNewPwd.setVisibility(View.GONE);
            tvErrorOldPwd.setVisibility(View.GONE);
            return true;
        }
    }
    public void getChangePasswordApi() {
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);

        RequestModel requestModel=new RequestModel();
        requestModel.userId=CommonUtils.getPreferences(ChangePasswordActivity.this, AppConstant.USER_ID);
        requestModel.password=etOldPwd.getText().toString().trim();
        requestModel.newPassword=etNewPwd.getText().toString().trim();
        Call<ChangePasswordModel> call=apiServices.getChangePassoword(AppConstant.BASIC_TOKEN,requestModel);
        startProgressBar(ChangePasswordActivity.this);
        call.enqueue(new retrofit2.Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                stopProgressBar();
                if (response!=null)
                {
                    if (response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        CommonUtils.showToast(ChangePasswordActivity.this,"Your password has been changed successfully");
                        finish();
                    }
                    else {
                        CommonUtils.showToast(ChangePasswordActivity.this,response.body().responseMessage);
                    }
                }

            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }
    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null, AppConstant.PLEASE_WAIT,true,false);
    }

    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
