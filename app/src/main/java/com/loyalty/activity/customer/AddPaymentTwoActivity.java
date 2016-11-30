package com.loyalty.activity.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.braintreepayments.api.PaymentMethod;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.CardBuilder;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.fragment.customer.PendingOrdersFragment;
import com.loyalty.model.customer.Questionnaire;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.DialogUtils;
import com.loyalty.utils.PickerView;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPaymentTwoActivity extends AppCompatActivity implements View.OnClickListener/*,Braintree.PaymentMethodCreatedListener, Braintree.PaymentMethodNonceListener, Braintree.BraintreeSetupFinishedListener*/ {
    private static final int PAYPAL_REQUEST_CODE = 101;
    private static final String TAG = AddPaymentTwoActivity.class.getSimpleName();
    private TextView btn_next;
    private EditText etCredit_card_number, edCvv;
    private TextView tvHeader,tvExpiry;
    private Context context;
    private ImageView ivBack,ivToolbarLeft;
    private String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
   // private Braintree mBraintree;
    private String brainTreeToken="";
    private String paymentType;
    private ProgressDialog progressDialog;
    private String Nonce;
    private String storeId;
    private String businessId;
    private String token;
    private String orderId;
    private TextView etNameOnCard;
    private Dialog dialog;
    private PickerView pickerMonth,pickerYear;
    private TextView tvSet;
    private TextView tvCancel;
    private String totalPayableAmount;
    private String navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_two);
        context = AddPaymentTwoActivity.this;
        getViewById();
        setListner();

        if(getIntent()!= null){
            //etCredit_card_number.setText(getIntent().getStringExtra(AppConstant.CARDNUMBER));
           // tvExpiry.setText(getIntent().getStringExtra(AppConstant.CARDEXPIRY));
        }

        businessId=CommonUtils.getPreferences(AddPaymentTwoActivity.this,"businessId");
        storeId=CommonUtils.getPreferences(AddPaymentTwoActivity.this,"storeId");
        orderId=CommonUtils.getPreferences(AddPaymentTwoActivity.this,"orderId");
        totalPayableAmount=CommonUtils.getPreferences(AddPaymentTwoActivity.this,"amount");
        navigate=CommonUtils.getPreferences(AddPaymentTwoActivity.this,"navigate");

        if (CommonUtils.isOnline(context)) {
          //  genrateToken(orderId);

        } else {
            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
        }
       // brainTreeToken=getIntent().getStringExtra("token");



    }
    private void setListner() {
        btn_next.setOnClickListener(this);
        etCredit_card_number.setOnClickListener(this);
        tvExpiry.setOnClickListener(this);
        ivToolbarLeft.setOnClickListener(this);
    }
    private void getViewById() {
        btn_next = (TextView) findViewById(R.id.btn_next);
        etNameOnCard= (EditText) findViewById(R.id.et_name_on_card);
        etCredit_card_number = (EditText) findViewById(R.id.etCredit_card_number);
        edCvv = (EditText) findViewById(R.id.edCvv);
        tvHeader = (TextView) findViewById(R.id.toolbar_title);
        tvExpiry = (TextView) findViewById(R.id.tvdate);
        ivToolbarLeft=(ImageView)findViewById(R.id.iv_toolbar_left);
        ivToolbarLeft.setVisibility(View.VISIBLE);
        tvHeader.setText(R.string.add_payment);
    }
    private boolean validation() {
        if (etCredit_card_number.getText().toString().trim().equalsIgnoreCase("")) {
            CommonUtils.showToast(context,"Please enter card number.");

            return false;
        }
        else if (etCredit_card_number.getText().toString().trim().length()<15) {
            CommonUtils.showToast(context,"Please enter 16 digits card number.");
            return false;
        }
        else if (edCvv.getText().toString().trim().equalsIgnoreCase("")) {
            CommonUtils.showToast(context,"Please enter CVV number.");
            return false;
        }
        else if (edCvv.getText().toString().trim().length()<2) {
            CommonUtils.showToast(context,"Please enter 3 digits CVV number.");
            return false;
        }

        else if (tvExpiry.getText().toString().equalsIgnoreCase("expiry")) {
            CommonUtils.showToast(context,"Please select expiry date.");
            return false;
        }
        /*else if (etNameOnCard.getText().toString().trim().length()==0)
        {

            return false;
        }*/
        else
        {
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvdate:
                DatePickerdialog();
                break;
            case R.id.btn_next:
                if(validation()){
                    brainTreeService();
                }

                break;
            case R.id.iv_toolbar_left:
                finish();
                break;
        }
    }
    private void DatePickerdialog() {
        dialog= DialogUtils.createCustomDialog(AddPaymentTwoActivity.this,R.layout.dialog_picker);
        pickerMonth = (PickerView)dialog.findViewById(R.id.pickerMonth);
        tvSet = (TextView) dialog.findViewById(R.id.tvSet);
        tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < months.length; i++){
            items.add(months[i]);
        }
        pickerMonth.setList(items);
        pickerYear = (PickerView)dialog.findViewById(R.id.pickerYear);
        ArrayList<String> items2 = new ArrayList<>();
        for (int j = Calendar.getInstance().get(Calendar.YEAR); j <=  Calendar.getInstance().get(Calendar.YEAR)+30; j++){
            items2.add("" + j);
        }
        pickerYear.setList(items2);
        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvExpiry.setText(""+pickerMonth.getSelectedIndex()+"/"+pickerYear.getSelected());
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExpiry.setText(""+pickerMonth.getSelectedIndex()+"/"+pickerYear.getSelected());
                dialog.dismiss();
            }
        });dialog.show();
    }
    /*@Override
    public void onBraintreeSetupFinished(boolean setupSuccessful, Braintree braintree, String errorMessage, Exception exception) {

        if(setupSuccessful) {
            this.mBraintree = braintree;
            braintree.addListener(this);
        }
    }
    @Override
    public void onPaymentMethodCreated(PaymentMethod paymentMethod) {
    }
    @Override
    public void onPaymentMethodNonce(String paymentMethodNonce) {
        Log.e(TAG,"paymentMethodNonce"+paymentMethodNonce);
        Nonce=paymentMethodNonce;
        if (CommonUtils.isNetworkConnected(context)) {
            paymentBraintreeApi(Nonce,totalPayableAmount);
        }
        else {
            Toast.makeText(AddPaymentTwoActivity.this, R.string.please_check, Toast.LENGTH_SHORT).show();
        }
    }*/
    public void brainTreeService() {
        if(brainTreeToken!=null &&brainTreeToken.trim().length()!=0) {
           // Log.e("TAG","on response :    "+brainTreeToken);
            final CardBuilder cardBuilder = new CardBuilder()
                    .cardNumber(etCredit_card_number.getText().toString())
                    .cvv(edCvv.getText().toString())
                    .expirationDate(tvExpiry.getText().toString().trim());
            try {
              //  mBraintree.tokenize(cardBuilder);
            } catch (Exception e) {
                Log.e("exc","exception",e);
            }
        }else{
           // Log.e("TAG","on response : null  ");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
               // mBraintree.finishPayWithPayPal(AddPaymentTwoActivity.this,resultCode, data);
            }
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
    private void paymentBraintreeApi(String nonce,String amount) {
        startProgressBar(context);
        RequestModel requestModel=new RequestModel();
        requestModel.businessId=businessId;
        requestModel.userId=CommonUtils.getPreferences(context, AppConstant.USER_ID);
        requestModel.orderId=orderId;
        requestModel.paymentAmount=amount;
        requestModel.nonce=nonce;
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call=apiServices.getCustomerCompleteOrderApi(AppConstant.BASIC_TOKEN,requestModel);
        Log.e("TAG","This is the request of payment"+new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e("TAG","This is the response of payment request hitted to service"+new Gson().toJson(response));
                stopProgressBar();
                if(response!=null&& response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(context,response.body().responseMessage);
                        boolean isQuestionExist=response.body().isQuestionnaireExist;
                        if(response.body().isQuestionnaireExist==true){
                            Intent intent=new Intent(AddPaymentTwoActivity.this,FeedBackQuestionActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            if (navigate.equalsIgnoreCase("navigate")) {
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"storeId","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"businessId","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"orderId","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"amount","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"navigate","");
                                finish();
                            }
                            else {
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"storeId","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"businessId","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"orderId","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"amount","");
                                CommonUtils.savePreferences(AddPaymentTwoActivity.this,"navigate","");
                                startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                            }
                        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
