package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.adapter.customer.LoyaltyListAdapter;
import com.loyalty.adapter.customer.ReceiptAdapter;
import com.loyalty.adapter.customer.YourLoyaltyAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.LoyaltyHotDealList;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourLoyaltyActivity extends AppCompatActivity implements OnHistoryListener {
    private TextView tvTitle,tvNoDealFound;
    private Toolbar toolbar;
    private RecyclerView rvLoyaltyList;
    private LoyaltyListAdapter mAdapter;
    private Context mContext;
    private List<LoyaltyHotDealList> loyaltyHotDealLists;
    private ProgressDialog progressDialog;
    private String TAG = YourLoyaltyActivity.class.getSimpleName();
    private Context context=YourLoyaltyActivity.this;
    private OnHistoryListener onHistoryListener;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_loyalty);
        mContext=YourLoyaltyActivity.this;
        loyaltyHotDealLists=new ArrayList<>();
        onHistoryListener=YourLoyaltyActivity.this;
        getIds();
        setToolbar();
        setRecylerView();
       userid = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);

       /* if(CommonUtils.isOnline(mContext)) {
            if (userid != "" && userid != null) {
                getLoyaltyHotDeals();
            } else {
                CommonUtils.showAlertLogin("Only registered user can see the loyalty hot deals. " +
                        "Please login. ", YourLoyaltyActivity.this);
            }
        }
        else {
            CommonUtils.showToast(mContext,AppConstant.PLEASE_CHECK_INTERNET);
        }*/



    }



    private void setRecylerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(YourLoyaltyActivity.this);
        rvLoyaltyList.setLayoutManager(mLayoutManager);
        mAdapter = new LoyaltyListAdapter(YourLoyaltyActivity.this, loyaltyHotDealLists,onHistoryListener);
        rvLoyaltyList.setAdapter(mAdapter);
    }


    private void getIds() {
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
       tvNoDealFound = (TextView) findViewById(R.id.tvNoDealFound);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle.setText(R.string.your_loyalty);
        rvLoyaltyList = (RecyclerView) findViewById(R.id.rvLoyaltyList);
        tvTitle.setTypeface(CommonUtils.setBook(YourLoyaltyActivity.this));
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
        tvTitle.setText("Your Loyalty");
    }

    public void getLoyaltyHotDeals() {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiServices.getLoayltyHotDeals(AppConstant.BASIC_TOKEN,CommonUtils.getPreferences(mContext, AppConstant.USER_ID));
        startProgressBar(YourLoyaltyActivity.this);
        Log.e(TAG + ">>>>>>>>", new Gson().toJson(call.toString()));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e(TAG + ">>>>>>>>", new Gson().toJson(response));
                stopProgressBar();
                Log.e(TAG + ">>>>>>>>", new Gson().toJson(response));
                  loyaltyHotDealLists.clear();
                if (response != null && response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        if (response.body().loyaltyHotDealList != null && response.body().loyaltyHotDealList.size()>0){
                            loyaltyHotDealLists.addAll(response.body().loyaltyHotDealList);
                            rvLoyaltyList.setVisibility(View.VISIBLE);
                            tvNoDealFound.setVisibility(View.GONE);
                        }else{
                            rvLoyaltyList.setVisibility(View.GONE);
                            tvNoDealFound.setVisibility(View.VISIBLE);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
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
    private void addToBasket(String dealId, String StoreId, final int position) {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel=new RequestModel();
        requestModel.dealId=dealId;
        requestModel.storeId=StoreId;
        requestModel.userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        Call<ResponseModel> call=apiServices.addItemToCart(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,new Gson().toJson(requestModel));
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if(response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        CommonUtils.showToast(context,"Deal succesfull added into your account");
                        loyaltyHotDealLists.get(position).isExist="1";
                    } else {
                        CommonUtils.showToast(context,response.body().responseMessage);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }

    @Override
    public void onHistoryClickListener(int postion) {


        if(loyaltyHotDealLists.get(postion).isExist.equalsIgnoreCase("1"))
        {

        }
        else if(loyaltyHotDealLists.get(postion).isExist.equalsIgnoreCase("0")) {
            addToBasket(loyaltyHotDealLists.get(postion).dealId, loyaltyHotDealLists.get(postion).storeId,postion);
        }

    }

      @Override
    protected void onResume() {
        super.onResume();
        if(CommonUtils.isOnline(mContext)) {
            if (userid != "" && userid != null) {
                getLoyaltyHotDeals();
            } else {
                CommonUtils.showAlertLogin("Only registered user can see the loyalty hot deals. " +
                        "Please login. ", YourLoyaltyActivity.this);
            }
        }
        else {
            CommonUtils.showToast(mContext,AppConstant.PLEASE_CHECK_INTERNET);
        }
    }

}
