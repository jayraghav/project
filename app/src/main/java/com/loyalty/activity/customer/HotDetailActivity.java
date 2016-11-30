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
import android.widget.Toast;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.AllHotDeals;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arati Padhy on 08-11-2016.
 */
public class HotDetailActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle,tvAddtoCart;
    private Toolbar toolbar;
    private ImageView ivHotdealImages,ivToolbarLeft;
    private String TAG = HotDetailActivity.class.getSimpleName();
    private Context mContext;
    private TextView tvDetails;
    private AllHotDeals allHotDealsModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_details);
        mContext = HotDetailActivity.this;

        if(getIntent()!=null){
            allHotDealsModel= (AllHotDeals) getIntent().getSerializableExtra("Detail");
        }else{
            allHotDealsModel=new AllHotDeals();
        }
        findIds();
        setListeners();

        if (allHotDealsModel.dealDesc!=null){
            tvDetails.setText(allHotDealsModel.dealDesc);
        }
        if (allHotDealsModel.productName!=null){
            tvTitle.setText(allHotDealsModel.productName);
        }

        if(allHotDealsModel.dealImage!=null && allHotDealsModel.dealImage.trim().length()>0){
            Picasso.with(mContext).load(allHotDealsModel.dealImage.trim()).error(R.mipmap.round).into(ivHotdealImages);
        }

        if (allHotDealsModel.isExist.equalsIgnoreCase("1")) {
            tvAddtoCart.setText("Already added");
        } else if(allHotDealsModel.isExist.equalsIgnoreCase("0")) {
            tvAddtoCart.setText("Add to Basket");
        }
        else {
        }



                tvAddtoCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (allHotDealsModel.isExist.equalsIgnoreCase("0")) {
                        String userid = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
                        if (userid != "" && userid != null) {
                            addToBasket(allHotDealsModel.dealId, allHotDealsModel.storeId);
                            tvAddtoCart.setText("Already added");
                        }
                        else{
                            CommonUtils.showAlertLogin("Only registered user can add to basket. " + "Please login. ", HotDetailActivity.this);
                        }
                    }
                        else {

                            CommonUtils.showToast(mContext,"Offer had been already added to your basket.Please check your basket.");

                        }
                    }
                });


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
           /* case R.id.tvAddtoCart:
                addToBasket(allHotDealsModel.dealId,allHotDealsModel.storeId);
                break;*/
            case R.id.iv_toolbar_left:
                finish();
                break;
        }
    }

    private void addToBasket(String dealId,String StoreId) {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel=new RequestModel();
        requestModel.dealId=dealId;
        requestModel.storeId=StoreId;
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
                        CommonUtils.showToast(mContext,"Deal successfully added into your account");
                        allHotDealsModel.isExist="1";
                    }
                    else {
                        CommonUtils.showToast(mContext,response.body().responseMessage);
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
        progressDialog = ProgressDialog.show(context,null, AppConstant.PLEASE_WAIT,true,false);
    }
    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
