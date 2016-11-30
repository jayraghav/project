package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrderDetailsReceiptActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rvFoodList;
    private ReceiptAdapter adapter;
    private TextView tvCheckOut, tvTitle,tvTitleRight;
    private ImageView ivToolbarRight1, ivToolbarRight2, ivHomeLogo;
    private Activity activity;
    private Context context;
    Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String TAG = PendingOrderDetailsReceiptActivity.class.getSimpleName();
    private TextView tvsubtotal, tvsubtotalRs, tvServiceTax, tvServiceTaxRs, tvVat, tvVatRs, tvGrandTotal, tvGrandTotalRs;
    List<ItemListReceipt> receiptItemList=new ArrayList<>();

    private GoogleApiClient client;
    private CircleImageView ivTeacherProfile;
    private TextView tvStoreName;
    String  storeId;
    String businessId,orderId;
    private String total;
    private String navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        context = PendingOrderDetailsReceiptActivity.this;
        orderId = getIntent().getStringExtra("orderId");
        storeId=getIntent().getStringExtra("storeId");
        businessId = getIntent().getStringExtra("businessId");
        navigate=getIntent().getStringExtra("navigate");
        findIds();
        setListeners();
        setToolbar();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvFoodList.setLayoutManager(mLayoutManager);
        adapter = new ReceiptAdapter(context,receiptItemList);
        rvFoodList.setAdapter(adapter);

        if (CommonUtils.isOnline(context)) {
            getOrderReceiptApi(orderId,businessId);
        }else {
            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
        }

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
                        finish();
                    }
                }
        );
    }
    private void findIds() {
        rvFoodList = (RecyclerView) findViewById(R.id.rvFoodLists);
        tvCheckOut = (TextView) findViewById(R.id.tvCheckOut);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        ivTeacherProfile=(CircleImageView)findViewById(R.id.ivTeacherProfile);
        tvStoreName =(TextView)findViewById(R.id.tv_storeName);
        ivToolbarRight1 = (ImageView) findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2 = (ImageView) findViewById(R.id.iv_toolbar_right2);
        tvTitleRight=(TextView) findViewById(R.id.toolbar_title_right);
        tvTitle.setText("Receipt");
        tvTitleRight.setText("Cancel");
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitleRight.setVisibility(View.VISIBLE);
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
                if (CommonUtils.isOnline(context)) {
                    genrateToken(orderId);
                } else {
                    CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                }
                break;
            case R.id.toolbar_title_right:
                getOrderCancelApi();
                break;

            default:
                break;
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
//                        startActivity(new Intent(context,NewStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                                 total=response.body().historyDetails.grandTotal;
                                receiptItemList.addAll(response.body().itemList);
                                tvsubtotalRs.setText(response.body().historyDetails.total);
                                tvServiceTaxRs.setText(response.body().historyDetails.serviceTax);
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
    public void genrateToken(String orderid) {
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
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        CommonUtils.savePreferencesString(context,AppConstant.TOKEN,response.body().token);
                        Log.e("TAG>>>>>Token",response.body().token);
                        Intent intent = new Intent(context, AddPaymentTwoActivity.class).putExtra("storeId",storeId).putExtra("businessId",businessId)
                                .putExtra("token",response.body().token).putExtra("orderId",orderId).putExtra("amount",total).putExtra("navigate",navigate);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("TAG>>>>>Token",call.request().toString());
                stopProgressBar();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();

    }


}
