package com.loyalty.activity.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.TotalPrice;
import com.loyalty.adapter.customer.CatalogueAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.loyalty.webserivcemodel.OrderItems;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogueActivity extends AppCompatActivity implements View.OnClickListener,OnHistoryListener
{
    private Context context;
    private TextView tvTitle,tvCheckOut,tvbusinessType,tvTotalPrice,tvTotalValue;
    private ImageView iv_toolbar_right2,iv_toolbar_left;
    private RecyclerView rvCatalogue;
    private CatalogueAdapter madapter;
    private EditText etSearch;
    private List<CatalogueProductList> productList;
    private List<CatalogueProductList> productListOriginal;
    private ProgressDialog progressDialog;
    private OnHistoryListener onHistoryListener;
    String  storeId;
    String businessId,storeName;
    private int totalamount = 0;
    /*  private String storeId,businessId;
     */ private String TAG=CatalogueActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        context = CatalogueActivity.this;
        onHistoryListener=CatalogueActivity.this;

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context);
        productList=new ArrayList<>();
        productListOriginal=new ArrayList<>();
        storeId=getIntent().getStringExtra("storeId");
        businessId=getIntent().getStringExtra("businessId");
        storeName=getIntent().getStringExtra("storeName");

        getIds();
        setFonts();
        setListeners();
        rvCatalogue.setLayoutManager(mLayoutManager1);
        madapter = new CatalogueAdapter(context,productList,onHistoryListener, "",new TotalPrice() {
            @Override
            public void priceChange(double o,String o1) {
                double totalPrice = 0;
                for(int a = 0; a<productList.size();a++){
                    double price = 0;
                    price =productList.get(a).count *Double.parseDouble(productList.get(a).price);
                    totalPrice = totalPrice + price;
                }
                tvTotalValue.setText("€ " +String.valueOf(totalPrice));
                CommonUtils.savePreferences(context,"totalPrice",String.valueOf(totalPrice));
            }
        });
        rvCatalogue.setAdapter(madapter);

        if(CommonUtils.isOnline(context)) {
            getCatalogues(storeId,businessId);
        } else {
            CommonUtils.showToast(context,AppConstant.PLEASE_CHECK_INTERNET);
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                madapter = new CatalogueAdapter(context,productList,onHistoryListener,
                        etSearch.getText().toString().trim(),new TotalPrice() {
                    @Override
                    public void priceChange(double o,String o1) {
                        double totalPrice = 0;
                        for(int a = 0; a<productList.size();a++){
                            double price = 0;
                            price =productList.get(a).count *Double.parseDouble(productList.get(a).price);
                            totalPrice = totalPrice + price;
                        }
                        tvTotalValue.setText("€ " +String.valueOf(totalPrice));
                        CommonUtils.savePreferences(context,"totalPrice",String.valueOf(totalPrice));
                        Log.e("tag","save total price"+ String.valueOf(totalPrice));
                    }
                });
                rvCatalogue.setAdapter(madapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getIds() {
        tvCheckOut = (TextView) findViewById(R.id.tvCheckOut);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvbusinessType = (TextView) findViewById(R.id.tvbusinessType);
        tvbusinessType.setText(storeName);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        tvTotalValue = (TextView) findViewById(R.id.tvTotalValue);
        iv_toolbar_right2 = (ImageView) findViewById(R.id.iv_toolbar_right2);
        iv_toolbar_left = (ImageView) findViewById(R.id.iv_toolbar_left);
        rvCatalogue = (RecyclerView) findViewById(R.id.rvCatalogue);
        etSearch = (EditText) findViewById(R.id.etSearch);
        iv_toolbar_right2.setImageResource(R.mipmap.trolly);
        iv_toolbar_right2.setVisibility(View.VISIBLE);
        iv_toolbar_left.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("Catalogue");

    }
    private void setListeners() {
        iv_toolbar_right2.setOnClickListener(this);
        iv_toolbar_left.setOnClickListener(this);
        tvCheckOut.setOnClickListener(this);
    }
    private void setFonts() {
        tvbusinessType.setTypeface(CommonUtils.setBook(context));
        tvCheckOut.setTypeface(CommonUtils.setBook(context));
        etSearch.setTypeface(CommonUtils.setBook(context));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_toolbar_right2:
                if (CommonUtils.getPreferences(context,AppConstant.USER_ID)!= "") {
                    if(CommonUtils.isOnline(context)) {
                        dealsTocart(storeId,businessId);
                    } else {
                        CommonUtils.showToast(context,AppConstant.PLEASE_CHECK_INTERNET);
                    }
                } else {
                    CommonUtils.showAlertLogin("Only registered user can see the loyalty hot deals " + "Plese login ",CatalogueActivity.this);
                }
                break;
            case R.id.iv_toolbar_left:
                finish();
                break;
            case R.id.tvCheckOut:
                List<CatalogueProductList> list=getSelectedList();
                Log.e(TAG,">>>>>>>>>>>> Non count 0 >>>>>>>>>>>> ");
                for (int i=0;i<list.size();i++){
                    Log.e(TAG,">> count >> "+list.get(i).count+" >>Product >>  "+list.get(i).productId);
                }
                if(list.size()>0) {
                    if (CommonUtils.getPreferences(context, AppConstant.USER_ID) != "") {
                        if (CommonUtils.isOnline(context)) {
                            dealsTocart(storeId, businessId);
                        } else {
                            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                        }
                    } else {
                        CommonUtils.showAlertLogin("Only registered user can see the loyalty hot deals " +
                                "Plese login ", CatalogueActivity.this);
                    }
                }
                else {
                    CommonUtils.showToast(context,"Please select item first to checkout");
                }
                break;
        }
    }
    public void  getCatalogues(String storeId,String businessId) {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call=apiServices.catalogueOfStore(AppConstant.BASIC_TOKEN,storeId,businessId);
        startProgressBar(context);
        Log.e(TAG,call.toString());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if(response!=null && response.body()!=null) {
                    if (response.body().responseCode.equals("200")) {
                        if (response.body().productList != null) {
                            productList.addAll(response.body().productList);
                            //productListOriginal.addAll(response.body().productList);
                        }
                    }
                }
                madapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }
    private void  dealsTocart(final String storeId, final String businessId) {

        final List<CatalogueProductList> list=getSelectedList();
        final List<OrderItems> itemses=new ArrayList<>();

        for(int i=0;i<list.size();i++)
        {
            OrderItems orderItems=new OrderItems();
            Log.e(TAG,">> count >> "+list.get(i).count+" >>Product >>  "+list.get(i).productId+" >>Product >>  "+list.get(i).productName);

            double cost=Double.parseDouble(list.get(i).price);
            int itemcount=list.get(i).count;
            double price=cost*itemcount;
            String totalPrice=""+price;
            orderItems.productId=list.get(i).productId;
            orderItems.itemCount=String.valueOf(list.get(i).count);
            orderItems.productName=list.get(i).productName;
            orderItems.price=totalPrice;
            itemses.add(orderItems);
        }
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        final RequestModel requestModel=new RequestModel();
        requestModel.userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        requestModel.businessId=businessId;
        requestModel.storeId=storeId;
        requestModel.setOrderItems(itemses);
        Call<ResponseModel> call=apiServices.addProductTOCart(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,new Gson().toJson(requestModel));
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e(TAG,new Gson().toJson(response));
                stopProgressBar();
                if(response.body().responseCode !=null&&response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        if(itemses.size()>0) {
                            CommonUtils.showToast(context,itemses.size() +" Item added into basket successfully");
                        }
                        else {
                            Log.e("TAG","Nothing slected");
                        }
                          /* if(Application.list !=null){
                            Application.list.addAll(list);
                        }*/
                        Intent intent=new Intent(context,ShoppingBasketActivity.class).putExtra("storeid",storeId).putExtra("businessid",businessId);
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
                stopProgressBar();
            }
        });
    }
    @Override
    public void onHistoryClickListener(int postion) {
    }

    public void performDataSet(){
        int count=0;
        for (int i=0;i<productList.size();i++){
            count=count+productList.get(i).count;
        }
        tvTotalPrice.setText(count+"");
    }
    private List<CatalogueProductList> getSelectedList(){
        List<CatalogueProductList> listProductDetail=new ArrayList<>();
        for (int i=0;i<productList.size();i++){

            if(productList.get(i).count>0){
                listProductDetail.add(productList.get(i));
            }
        }
        return listProductDetail;
    }
    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null, AppConstant.PLEASE_WAIT,true,false);
    }
    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();  // optional depending on your needs
        finish();
//        startActivity(new Intent(CatalogueActivity.this,NewStoreActivity.class));


    }
}




