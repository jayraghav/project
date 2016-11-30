package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.adapter.customer.ReceiptAdapter;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.ItemListReceipt;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.UseranswerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rvFoodList;
    private ReceiptAdapter adapter;
    private TextView tvCheckOut, tvTitle,tvTitleRight;
    private ImageView ivToolbarRight1, ivToolbarRight2, ivHomeLogo;
    private Activity activity;
    private Context context;
    Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String TAG = ReceiptActivity.class.getSimpleName();
    private String navigate="";
    private TextView tvsubtotal, tvsubtotalRs, tvServiceTax, tvServiceTaxRs, tvVat, tvVatRs, tvGrandTotal, tvGrandTotalRs;
    List<ItemListReceipt> receiptItemList=new ArrayList<>();

    private GoogleApiClient client;
    private CircleImageView ivTeacherProfile;
    private TextView tvStoreName;
    private String storeId;
    private String businessId,userCheckedInId,orderId;
    private String   grandtotal;
    private String brainTreeToken;
    private int REQUEST_CODE=1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        context = ReceiptActivity.this;

        storeId=getIntent().getStringExtra("storeId");
        businessId=getIntent().getStringExtra("businessId");
        orderId = getIntent().getStringExtra("orderId");
        Log.e("TAG","OrderID"+orderId+"StoreId"+storeId+"businessid"+businessId);

        if (CommonUtils.getPreferences(context,AppConstant.USER_CHECKEDIN_ID)!=""){
            userCheckedInId= CommonUtils.getPreferences(context,AppConstant.USER_CHECKEDIN_ID);
        }
        findIds();
        setListeners();
        setToolbar();

        Log.e("tag","store id"+storeId);
        Log.e("tag","businessid id"+businessId);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvFoodList.setLayoutManager(mLayoutManager);
        adapter = new ReceiptAdapter(context,receiptItemList);
        rvFoodList.setAdapter(adapter);

        if (CommonUtils.isOnline(context)) {
            getOrderReceiptApi(orderId,businessId);
        }else {
            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
        }
        CommonUtils.savePreferences(ReceiptActivity.this,"storeId",storeId);
        CommonUtils.savePreferences(ReceiptActivity.this,"businessId",storeId);
        CommonUtils.savePreferences(ReceiptActivity.this,"orderId",orderId);
        CommonUtils.savePreferences(ReceiptActivity.this,"amount",grandtotal);
        CommonUtils.savePreferences(ReceiptActivity.this,"navigate",navigate);

        genrateToken(orderId);
    }

    public void genrateToken(String orderid) {

        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        String userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        final String orderId=orderid;
        Call<ResponseModel> call=apiServices.genrateBrainTreeToken(AppConstant.BASIC_TOKEN,userId,orderId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                if (response!=null&&response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        CommonUtils.savePreferencesString(context,AppConstant.TOKEN,response.body().token);
                        Log.e("TAG>>>>>Token",response.body().token);
                        brainTreeToken = response.body().token;

                        // Braintree.setup(context, brainTreeToken.trim(),AddPaymentTwoActivity.this);
                        /*Intent intent = new Intent(context, AddPaymentTwoActivity.class).putExtra("storeId",storeId).putExtra("businessId",businessId)
                                .putExtra("token",response.body().token).putExtra("orderId",orderId).putExtra("amount",grandtotal)
                                .putExtra("navigate",navigate);
                        startActivity(intent);
                        Log.e("tag","order id in receip screen "+orderId);*/
                    }
                    else {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.printStackTrace();
                stopProgressBar();
            }
        });
    }



    private void setListeners() {
        tvCheckOut.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context,ShoppingBasketActivity.class)
                                .putExtra("storeid",storeId).putExtra("businessid",businessId)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }
        );
    }
    private void findIds() {
        rvFoodList = (RecyclerView) findViewById(R.id.rvFoodLists);
        tvCheckOut = (TextView) findViewById(R.id.tvCheckOut);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitleRight=(TextView) findViewById(R.id.toolbar_title_right);
        ivTeacherProfile=(CircleImageView)findViewById(R.id.ivTeacherProfile);
        tvStoreName =(TextView)findViewById(R.id.tv_storeName);
        tvTitle.setText("Receipt");
        tvTitleRight.setText("Cancel");
        ivToolbarRight1 = (ImageView) findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2 = (ImageView) findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeLogo = (ImageView) findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setTypeface(CommonUtils.setBook(context));
        tvCheckOut.setTypeface(CommonUtils.setBook(context));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvsubtotal = (TextView) findViewById(R.id.tvsubtotal);
        tvsubtotalRs = (TextView) findViewById(R.id.tvsubtotalRs);
        tvServiceTax = (TextView) findViewById(R.id.tvServiceTax);
        tvServiceTaxRs = (TextView) findViewById(R.id.tvServiceTaxRs);
        tvVat = (TextView) findViewById(R.id.tvVatTax);
        tvVatRs = (TextView) findViewById(R.id.tvVatTaxRs);
        tvGrandTotal = (TextView) findViewById(R.id.tvGrandTotal);
        tvGrandTotalRs = (TextView) findViewById(R.id.tvGrandTotalRs);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCheckOut:
                showPopup(context);

                break;
            case R.id.toolbar_title_right:
                getOrderCancelApi();
                break;
            default:
                break;
        }
    }

    private void showPopup(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final Dialog mDialog = new Dialog(mContext,
                android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
//        mDialog.getWindow().getAttributes().windowAnimations = R.anim;
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();

        View dialoglayout = inflater.inflate(R.layout.dialog_alert, null);
        mDialog.setContentView(dialoglayout);

        TextView tvMsg = (TextView)mDialog.findViewById(R.id.tvMsg);
        TextView tvSavedCard = (TextView)mDialog.findViewById(R.id.tvSavedCard);
        TextView tvNewCard = (TextView)mDialog.findViewById(R.id.tvNewCard);

        // tvMsg.setText(msg);

        tvSavedCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               Intent intent = new Intent(ReceiptActivity.this,HomeActivity.class);
                intent.putExtra(AppConstant.PAYMODE,"pay_mode");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                mDialog.dismiss();
            }
        });
        tvNewCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                /*Intent intent = new Intent(context, AddPaymentTwoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/

                PaymentRequest paymentRequest = new PaymentRequest()
                        .clientToken(brainTreeToken);
                startActivityForResult(paymentRequest.getIntent(ReceiptActivity.this), REQUEST_CODE);
               /* if (CommonUtils.isOnline(context)) {
                    genrateToken(orderId);

                } else {
                    CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                }*/
                mDialog.dismiss();


            }
        });
        mDialog.show();
    }

    /*public void genrateToken(String orderid) {
        startProgressBar(context);
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        String userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        final String orderId=orderid;
        Call<ResponseModel> call=apiServices.genrateBrainTreeToken(AppConstant.BASIC_TOKEN,userId,orderId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response!=null&&response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        CommonUtils.savePreferencesString(context,AppConstant.TOKEN,response.body().token);
                        Log.e("TAG>>>>>Token",response.body().token);

                        Intent intent = new Intent(context, AddPaymentTwoActivity.class).putExtra("storeId",storeId).putExtra("businessId",businessId)
                                .putExtra("token",response.body().token).putExtra("orderId",orderId).putExtra("amount",grandtotal)
                                .putExtra("navigate",navigate);
                        startActivity(intent);
                        Log.e("tag","order id in receip screen "+orderId);
                    }
                    else {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
               t.printStackTrace();
                stopProgressBar();
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );
                    String nonce = paymentMethodNonce.getNonce();
                    paymentBraintreeApi(nonce,CommonUtils.getPreferences(ReceiptActivity.this,"totalPrice"));
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
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

    private void getOrderReceiptApi(String oderId,String businessId) {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiServices.getOrderDetail(AppConstant.BASIC_TOKEN,oderId,businessId);
        Log.e(TAG, ">>>>>>>" + new Gson().toJson(call.request()));
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e(TAG, ">>>>>>>>" + new Gson().toJson(response) + "order receipt!!!");
                stopProgressBar();
                if (response != null && response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        if (response.body().historyDetails != null) {
                            if (response.body().itemList != null && response.body().itemList.size() > 0) {
                                receiptItemList.clear();
                                tvsubtotalRs.setText("");
                                receiptItemList.addAll(response.body().itemList);
                                tvsubtotalRs.setText(response.body().historyDetails.total);
                                tvServiceTaxRs.setText(response.body().historyDetails.serviceTax);
                                Log.e(TAG, "history details" +response.body().historyDetails.total);
                                grandtotal=response.body().historyDetails.grandTotal;
                                tvVatRs.setText(response.body().historyDetails.vat);
                                tvGrandTotalRs.setText(response.body().historyDetails.grandTotal);
                                tvStoreName.setText(response.body().historyDetails.storeName);
                                String image = response.body().historyDetails.storeImage;
                                if (image != null && image.trim().length() != 0) {
                                    Picasso.with(context).load(image.trim()).resize(100, 100).error(R.drawable.error).into(ivTeacherProfile);
                                }

                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
                CommonUtils.showToast(context, "Failure occured");
            }
        });
    }

    public void getOrderCancelApi() {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel=new RequestModel();
        requestModel.userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        requestModel.orderId=orderId;
        requestModel.storeId=storeId;
        Call<ResponseModel> call=apiServices.getCancelOrderApi(AppConstant.BASIC_TOKEN,requestModel);
        Log.e("tag", new Gson().toJson(requestModel));

        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                Log.e("tag",new Gson().toJson(response));

                if (response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(context,response.body().responseMessage);
                        startActivity(new Intent(context,ShoppingBasketActivity.class)
                                .putExtra("storeid",storeId).putExtra("businessid",businessId)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
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
        startActivity(new Intent(context,ShoppingBasketActivity.class)
                .putExtra("storeid",storeId).putExtra("businessid",businessId)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
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
        Log.e("TAG","This is the request of payment"+new com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e("TAG","This is the response of payment request hitted to service"+new com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson().toJson(response));
                stopProgressBar();
                if(response!=null&& response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(context,response.body().responseMessage);
                        boolean isQuestionExist=response.body().isQuestionnaireExist;
                        if(response.body().isQuestionnaireExist==true){
                            Intent intent=new Intent(ReceiptActivity.this,FeedBackQuestionActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            if (navigate.equalsIgnoreCase("navigate")) {
                                CommonUtils.savePreferences(ReceiptActivity.this,"storeId","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"businessId","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"orderId","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"amount","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"navigate","");
                                finish();
                            }
                            else {
                                CommonUtils.savePreferences(ReceiptActivity.this,"storeId","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"businessId","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"orderId","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"amount","");
                                CommonUtils.savePreferences(ReceiptActivity.this,"navigate","");
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

}
