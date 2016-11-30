package com.loyalty.activity.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.adapter.customer.CheckoutAdapter;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.ItemListReceipt;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    private TextView tv_cafe_name,tvCheckOut;
    private View v;
    private ImageView ivToolbarLeft,ivConsumer;
    private RecyclerView rvSpendingList;
    private CheckoutAdapter checkoutAdapter;
    private Context context;
    private String  orderId;
    private List<ItemListReceipt> receipt;
    private TextView toolbarTitle;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receipt=new ArrayList<>();
        setContentView(R.layout.activity_checkout);
        orderId = getIntent().getExtras().getString("orderId");
        context=CheckoutActivity.this;
        findId();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CheckoutActivity.this);
        rvSpendingList.setLayoutManager(mLayoutManager);
        checkoutAdapter=new CheckoutAdapter(context,receipt);
        rvSpendingList.setAdapter(checkoutAdapter);

    }
    private void findId()
    {
        ivConsumer=(ImageView)findViewById(R.id.iv_consumer);
        tv_cafe_name=(TextView)findViewById(R.id.tv_cafe_name);
        tvCheckOut=(TextView)findViewById(R.id.tvCheckOut);
        rvSpendingList=(RecyclerView)findViewById(R.id.rvFoodLists);
        ivToolbarLeft=(ImageView)findViewById(R.id.iv_toolbar_left);
        ivToolbarLeft.setImageResource(R.mipmap.back);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Receipt");
    }
    public void getUserCheckoutDetailApi() {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call=apiServices.userCheckoutDetails(AppConstant.BASIC_TOKEN,orderId);
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        receipt.addAll(response.body().itemList);
                    }
                    checkoutAdapter.notifyDataSetChanged();
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
