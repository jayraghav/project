package com.loyalty.activity.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.adapter.customer.LoyaltyListAdapter;
import com.loyalty.adapter.customer.WhatsHotAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.AllHotDeals;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhatsHotActivity extends AppCompatActivity implements View.OnClickListener,OnHistoryListener {
    private static final String TAG = WhatsHotActivity.class.getSimpleName();
    private TextView tvTitle;
    private OnHistoryListener onHistoryListener;
    private ImageView ivFilter,ivSearch,iv;
    private WhatsHotAdapter mAdapter;
    private RecyclerView rvWhatsHot;
    private EditText etSearch;
    private Toolbar toolbar;
    private Context context;
    private List<AllHotDeals> allHotDealsList;
    private ProgressDialog progressDialog;
    private ImageView ivToolbarRight2,ivToolbarLeft;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_hot);
        context=WhatsHotActivity.this;
        allHotDealsList = new ArrayList<>();
        onHistoryListener = WhatsHotActivity.this;
        getIds();
        setListeners();
        setFonts();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(WhatsHotActivity.this);
        rvWhatsHot.setLayoutManager(mLayoutManager);
        mAdapter = new WhatsHotAdapter(context, allHotDealsList, onHistoryListener,"");
        rvWhatsHot.setAdapter(mAdapter);
        userid=CommonUtils.getPreferences(context,AppConstant.USER_ID);

      /*  if(CommonUtils.isOnline(context)) {
                getAllHotDealApi();
        } else {
            CommonUtils.showToast(context,AppConstant.PLEASE_CHECK_INTERNET);
        }*/
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter = new WhatsHotAdapter(context, allHotDealsList, onHistoryListener,etSearch.getText().toString().trim());
                rvWhatsHot.setAdapter(mAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CommonUtils.isOnline(context)) {
            getAllHotDealApi();
        } else {
            CommonUtils.showToast(context,AppConstant.PLEASE_CHECK_INTERNET);
        }
    }

    private void setFonts() {
        tvTitle.setTypeface(CommonUtils.setBook(this));
        etSearch.setTypeface(CommonUtils.setBook(this));
    }
    private void setListeners() {
        ivToolbarLeft.setOnClickListener(this);
    }

    private void getIds() {
        ivSearch=(ImageView) findViewById(R.id.ivSearch);
        etSearch=(EditText) findViewById(R.id.etSearch);
        toolbar=(Toolbar) findViewById(R.id.toolbar) ;
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        ivToolbarLeft=(ImageView)findViewById(R.id.iv_toolbar_left);
        rvWhatsHot=(RecyclerView) findViewById(R.id.rvWhatsHot);
        ivToolbarRight2=(ImageView)findViewById(R.id.iv_toolbar_right2);
        ivToolbarLeft.setVisibility(View.VISIBLE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvTitle.setText(R.string.whatshot);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_left:
                finish();
                break;
            default:
                break;
        }
    }
    public void getAllHotDealApi() {
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        String userId;
        userId  =CommonUtils.getPreferences(WhatsHotActivity.this, AppConstant.USER_ID);
        if(userId.equalsIgnoreCase(""))
        {
            userId  ="-1";

        } else {
            userId  =CommonUtils.getPreferences(WhatsHotActivity.this, AppConstant.USER_ID);
        }
        Call<ResponseModel> call=apiServices.getAllHotDeal(AppConstant.BASIC_TOKEN,userId);
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                allHotDealsList.clear();
                stopProgressBar();
                if(response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        if(response.body().hotDealList!=null&&response.body().hotDealList.size()>0) {
                            allHotDealsList.addAll(response.body().hotDealList);
                          }
                        else {
                            CommonUtils.showToast(WhatsHotActivity.this,response.body().responseMessage);
                        }
                    }
                    else {
                        CommonUtils.showToast(WhatsHotActivity.this,"Hot deals not available yet");
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e(TAG,t.toString()+""+"Failure occured");
                stopProgressBar();
            }
        });
    }
    private void addToBasket(String dealId,String StoreId, final int position) {
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
                        CommonUtils.showToast(context,"Deal successfully added into your account");
                        allHotDealsList.get(position).isExist="1";
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
         public void onHistoryClickListener(int postion) {
        if(allHotDealsList.get(postion).isExist.equalsIgnoreCase("0")) {
            String userid = CommonUtils.getPreferences(context, AppConstant.USER_ID);
            if (CommonUtils.isOnline(context)) {
                if (userid != "" && userid != null) {
                    addToBasket(allHotDealsList.get(postion).dealId, allHotDealsList.get(postion).storeId,postion);
                } else {
                    CommonUtils.showAlertLogin("Only registered user can add to basket. " + "Please login. ", WhatsHotActivity.this);
                }
            } else {
                CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
            }
        }
        else {
            CommonUtils.showToast(context,"Deals already added into your basket");
        }
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