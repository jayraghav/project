package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.AllHotDeals;
import com.loyalty.webserivcemodel.LoyaltyHotDealList;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.YourLoyaltyDetails;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arati Padhy on 08-11-2016.
 */
public class YourLoyaltyDetailActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle,tvAddtoCart;
    private Toolbar toolbar;
    private ImageView ivHotdealImages,ivToolbarLeft;
    private String TAG = YourLoyaltyDetailActivity.class.getSimpleName();
    private Context mContext;
    private TextView tvDetails;
    private LoyaltyHotDealList allLoyaltyDealsModel;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_details);
        mContext = YourLoyaltyDetailActivity.this;

        if(getIntent()!=null){
            allLoyaltyDealsModel= (LoyaltyHotDealList) getIntent().getSerializableExtra("LoyaltyDetail");
        }else{
            allLoyaltyDealsModel=new LoyaltyHotDealList();
        }

        findIds();
        setListeners();

        if (allLoyaltyDealsModel.dealDesc!=null){
            tvDetails.setText(allLoyaltyDealsModel.dealDesc);
        }
        if (allLoyaltyDealsModel.productName!=null){
            tvTitle.setText(allLoyaltyDealsModel.productName);
        }
        if(allLoyaltyDealsModel.dealImage!=null && allLoyaltyDealsModel.dealImage.trim().length()>0){
            Picasso.with(mContext).load(allLoyaltyDealsModel.dealImage.trim()).error(R.mipmap.round).into(ivHotdealImages);
        }

        if(allLoyaltyDealsModel.isExist.equalsIgnoreCase("1")) {
            tvAddtoCart.setText("Already Redeemed");
        } else {
            tvAddtoCart.setText("Redeem");
        }


    }

    private void findIds() {
        toolbar=(Toolbar) findViewById(R.id.toolbar) ;
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        ivHotdealImages = (ImageView)findViewById(R.id.ivHotdealImages);
        tvDetails = (TextView)findViewById(R.id.tvDetails);
        tvAddtoCart = (TextView)findViewById(R.id.tvAddtoCart);
        ivToolbarLeft=(ImageView)findViewById(R.id.iv_toolbar_left);

    }
    private void setListeners() {
        tvAddtoCart.setOnClickListener(this);
        ivToolbarLeft.setOnClickListener(this);
        ivToolbarLeft.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_toolbar_left:
                finish();
                break;
            case R.id.tvAddtoCart:
                if(!allLoyaltyDealsModel.isExist.equalsIgnoreCase("1")) {
                    addToBasket();
                }
                break;
        }
    }
    private void addToBasket() {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel=new RequestModel();
        requestModel.dealId=allLoyaltyDealsModel.dealId;
        requestModel.storeId=allLoyaltyDealsModel.storeId;
        requestModel.userId=CommonUtils.getPreferences(mContext,AppConstant.USER_ID);
        Call<ResponseModel> call=apiServices.addItemToCart(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,new Gson().toJson(requestModel));
        startProgressBar(mContext);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if(response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(mContext,response.body().responseMessage);
                        allLoyaltyDealsModel.isExist="1";
                        tvAddtoCart.setText("Already Redeemed");
                    }
                    else
                    {
                        CommonUtils.showToast(mContext,response.body().responseMessage);
                    }
//                    mAdapter.notifyDataSetChanged();
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
}
