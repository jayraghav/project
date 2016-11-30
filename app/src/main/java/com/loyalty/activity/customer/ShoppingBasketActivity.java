package com.loyalty.activity.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.TotalPrice;
import com.loyalty.adapter.customer.OfferAdapter;
import com.loyalty.adapter.customer.ShoppingBasketAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.interfaces.RemoveDealListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.CatalogueDetailsList;
import com.loyalty.webserivcemodel.CatalogueProductDetails;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.loyalty.webserivcemodel.Deal;
import com.loyalty.webserivcemodel.EditDeals;
import com.loyalty.webserivcemodel.OrderItems;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.ShoppingBasketUpdateRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ShoppingBasketActivity extends AppCompatActivity implements RemoveDealListener,View.OnClickListener/*,TotalPrice*/ {
    private TextView tvStatusBar,tvTotal,tvPlaceOreder,tvTitle,TotalValue;
    private Context context;
    private TextView tvContinue,tvIfNot,tvInput;
    private EditText etTableNumber;
    private ImageView ivCross;
    private RecyclerView rvbasketData;
    private Toolbar toolbar;
    private LayoutInflater inflater;
    private Dialog mDialog;
    private ImageView iv_toolbar_right1;
    private View dialoglayout;
    private ShoppingBasketAdapter mAdapter;
    private OnHistoryListener onHistoryListener;
    private List<CatalogueDetailsList> arrCatalougeProduct =new ArrayList<>();
    private ProgressDialog progressDialog;
    private String TAG=ShoppingBasketActivity.class.getSimpleName();
    private RecyclerView rvbasketofferData;
    private OfferAdapter mAdapteroffer;
    private String storeId;
    private String businessId;
    private RemoveDealListener removeDealListener;
    private List<CatalogueProductDetails> productlist;
    private TextView tvUpdate;
    private TotalPrice totalPrice;
    public List<CatalogueProductDetails> removedealsList=new ArrayList<>();
    private List<CatalogueProductList> productLists=new ArrayList<>();
    private List<CatalogueDetailsList> detailsLists=new ArrayList<>();
    private int size;
    private int productListSize;
    private float totalchagedAmount=0;
    List<OrderItems> itemses=new ArrayList<>();
    List<Deal> dealsList=new ArrayList<>();
    private String orderId="";
    String userCheckedInId;
    private String businessName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);
        context=ShoppingBasketActivity.this;
        removeDealListener=ShoppingBasketActivity.this;
        productlist=new ArrayList<>();
        storeId=getIntent().getStringExtra("storeid");
        businessId=getIntent().getStringExtra("businessid");


        getIds();
        setToolbar();
        setListeners();
        setFonts();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvbasketData.setLayoutManager(mLayoutManager);
        mAdapter=new ShoppingBasketAdapter(context,productLists,detailsLists, removeDealListener, new TotalPrice() {
            @Override
            public void priceChange(double obj1, String obj2) {
                double totalPrice = Double.parseDouble(TotalValue .getText().toString().replace("€ ",""));
                double price=0.0;
                String type="";
                price= obj1;
                type= obj2;

                if(type.equalsIgnoreCase("Add")) {
                    TotalValue.setText("€ "+String.valueOf(totalPrice + price));
                }
                else if(type.equalsIgnoreCase("Sub")){
                    TotalValue.setText("€ "+String.valueOf(totalPrice - price));
                }

            }

        });
        rvbasketData.setAdapter(mAdapter);
        if (CommonUtils.isOnline(context)) {
            getAllBasket();
        }else {
            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
        }
    }
    private void getIds() {
        tvStatusBar=(TextView)findViewById(R.id.tvStatusBar);
        tvTotal=(TextView)findViewById(R.id.tvTotal);
        TotalValue=(TextView)findViewById(R.id.TotalValue);
        tvPlaceOreder=(TextView)findViewById(R.id.tvPlaceOreder);
        rvbasketData=(RecyclerView)findViewById(R.id.rvbasketData);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvTitle=(TextView)findViewById(R.id.toolbar_title);
        iv_toolbar_right1=(ImageView) findViewById(R.id.iv_toolbar_right1);
        tvUpdate=(TextView)findViewById(R.id.tv_update);
    }
    private void setListeners() {
        tvPlaceOreder.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent=new Intent(context,CatalogueActivity.class).putExtra("storeId",storeId).putExtra("businessId",businessId);
                        startActivity(intent);*/
                        finish();
                    }
                }

        );
        tvTitle.setText("Shopping Basket");
        iv_toolbar_right1.setVisibility(View.GONE);
        iv_toolbar_right1.setImageResource(R.mipmap.trolly);
    }

    private void setFonts() {
        tvStatusBar.setTypeface(CommonUtils.setBook(context),Typeface.BOLD);
        tvTotal.setTypeface(CommonUtils.setBook(context),Typeface.BOLD);
        tvPlaceOreder.setTypeface(CommonUtils.setBook(context),Typeface.BOLD);
        tvTitle.setTypeface(CommonUtils.setBook(context));
        TotalValue.setTypeface(CommonUtils.setBook(context),Typeface.BOLD);
    }

    private void getAllBasket() {
        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        String userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        Call<ResponseModel> call=apiServices.basketList(AppConstant.BASIC_TOKEN,userId,storeId,businessId);
        Log.e(TAG,new Gson().toJson(call.toString()));
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                Log.e(TAG,new Gson().toJson(response));
                if(response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        if(response.body().businessSubCategory!=null){
                            businessName= response.body().businessSubCategory.trim();
                            //CommonUtils.savePreferencesString(context,AppConstant.BUSINESSCATAGORY_NAME,response.body().businessSubCategory);
                            Log.e("tag","businesstype business " +businessName);
                            Log.e("tag","businesstype business subcategory " +response.body().businessSubCategory);
                        }
                        if(response.body().dealsList!=null && response.body().productList!=null) {
                            productLists.clear();
                            detailsLists.clear();
                            productLists.addAll(response.body().productList);
                            detailsLists.addAll(response.body().dealsList);
                            CommonUtils.savePreferences(context,"dealId",AppConstant.DEAL_ID);
                            mAdapter.notifyDataSetChanged();
                            double totalAmount= 0.0;

                            for (int i = 0; i < productLists.size(); i++) {
                                if (productLists.get(i).price!=null && productLists.get(i).itemCount!=null){
                                    totalAmount += Double.parseDouble(productLists.get(i).price) * Integer.parseInt(productLists.get(i).itemCount);
                                }
                            }
                            TotalValue.setText("€ "+totalAmount);
                        }
                    }
                    else {
                        CommonUtils.showToast(context,AppConstant.PLEASE_CHECK_INTERNET);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }
    private void placeAnOrderApi() {
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        final RequestModel requestModel=new RequestModel();
        List<CatalogueProductDetails> dealsLists=new ArrayList<>();
        CatalogueProductDetails dealLists=new CatalogueProductDetails();
        dealLists.dealId=CommonUtils.getPreferences(context,"dealId");
        dealsLists.add(dealLists);
        List<OrderItems> itemses=new ArrayList<>();
        itemses.clear();
        dealsList.clear();
        for(int i=0;i<productLists.size();i++)
        {
            OrderItems orderItems=new OrderItems();
            Log.e(TAG,">> count >> "+productLists.get(i).itemCount+" >>Product >>  "+productLists.get(i).productId+" >>Product >>  "+productLists.get(i).productName);
            float cost=Float.parseFloat(productLists.get(i).price);
            int itemcount=Integer.parseInt(productLists.get(i).itemCount);
            float price=cost*itemcount;
            String totalPrice=""+price;
            totalchagedAmount=totalchagedAmount+price;
            orderItems.productId=productLists.get(i).productId;
            orderItems.itemCount=String.valueOf(productLists.get(i).itemCount);
            orderItems.productName=productLists.get(i).productName;
            orderItems.price=totalPrice;
            itemses.add(orderItems);
        }
        for(int i=0;i<detailsLists.size();i++)
        {
            Deal deal=new Deal();
            deal.dealId=detailsLists.get(i).dealId;
            dealsList.add(deal);
        }
        requestModel.setUserId(CommonUtils.getPreferences(context, AppConstant.USER_ID));
        requestModel.setStoreId(storeId);
        requestModel.setBusinessId(businessId);
        requestModel.setTotal(""+totalchagedAmount);
        requestModel.dealsList=dealsList;
      if(businessName!=null && businessName.toLowerCase().trim().equalsIgnoreCase("restaurant")){
//            requestModel.tableNo=etTableNumber.getText().toString().trim();
          requestModel.setTableNumber(etTableNumber.getText().toString().trim());

      }else{
//            requestModel.tableNo="";
          requestModel.setTableNumber("");

      }
        requestModel.setOrderItems(itemses);

        Call<ResponseModel> call=apiServices.placeOrder(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,">>>>>>>>>>"+new Gson().toJson(requestModel));
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if(response!=null)
                {
                    if(response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        CommonUtils.showToast(context,"Your order placed successfully");
                        orderId=response.body().orderId;
                        startActivity(new Intent(ShoppingBasketActivity.this,ReceiptActivity.class).putExtra("orderId",orderId)
                        .putExtra("storeId",storeId).putExtra("businessId",businessId));
                        // finish();

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

    public void editCartDetailApi() {
        itemses.clear();
        dealsList.clear();
        List<OrderItems> itemses=new ArrayList<>();
        for(int i=0;i<productLists.size();i++)
        {
            OrderItems orderItems=new OrderItems();

            Log.e(TAG,">> count >> "+productLists.get(i).count+" >>Product >>  "+productLists.get(i).productId+" >>Product >>  "+productLists.get(i).productName);
            Log.e(TAG,"product list size"+productlist.size());

            float cost=Float.parseFloat(productLists.get(i).price);
            int itemcount=Integer.parseInt(productLists.get(i).itemCount);
            float price=cost*itemcount;
            String totalPrice=""+price;
            orderItems.productId=productLists.get(i).productId;
            orderItems.itemCount=String.valueOf(productLists.get(i).itemCount);
            orderItems.productName=productLists.get(i).productName;
            orderItems.price=totalPrice;
            itemses.add(orderItems);
        }

        for(int i=0;i<detailsLists.size();i++)
        {
            Deal deal=new Deal();
            deal.dealId=detailsLists.get(i).dealId;
            dealsList.add(deal);
        }

        ApiInterface apiServices=ApiClient.getClient().create(ApiInterface.class);
        final ShoppingBasketUpdateRequest requestModel=new ShoppingBasketUpdateRequest();
        requestModel.userId=CommonUtils.getPreferences(context,AppConstant.USER_ID);
        requestModel.storeId=storeId;
        requestModel.businessId=businessId;
        requestModel.orderItems.addAll(itemses);
        requestModel.dealsList.addAll(dealsList);

        Call<ResponseModel> call=apiServices.editCartApi(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,new Gson().toJson(requestModel));
        startProgressBar(context);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if(response!=null && response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
//                        CommonUtils.showToast(context,"Your cart is updated Successfully.");
                        getAllBasket();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }

    public void performDataSet(List<CatalogueProductList> productlist){
        productLists.clear();
        productLists.addAll(productlist);

    }

    public void performDataDealsSet(List<CatalogueDetailsList> dealslist){

        detailsLists.clear();
        detailsLists.addAll(dealslist);

    }

    @Override
    public void removeDeals(int position) {
        int dealSize=size-productListSize;
        for (int i=0;i<dealSize;i++)
        {
            Log.e(TAG,">>>>>>>"+removedealsList.get(i)+"Removed position"+position);
        }
        removedealsList.remove(position);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent=new Intent(context,CatalogueActivity.class).putExtra("storeId",storeId).putExtra("businessId",businessId);
        startActivity(intent);*/
        finish();
    }


    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null, AppConstant.PLEASE_WAIT,true,false);
    }
    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    private void showInputTableNoDialog() {
        inflater = LayoutInflater.from(this);
        mDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        mDialog.getWindow().setAttributes(lp);
        mDialog.setCanceledOnTouchOutside(false);
        dialoglayout = inflater.inflate(R.layout.dialog_input_table_no, null);
        mDialog.setContentView(dialoglayout);
        mDialog.show();

        tvContinue=(TextView) mDialog.findViewById(R.id.tvContinue);
        tvIfNot=(TextView) mDialog.findViewById(R.id.tvIfNot);
        tvInput=(TextView) mDialog.findViewById(R.id.tvInput);
        ivCross=(ImageView) mDialog.findViewById(R.id.ivCross);
        etTableNumber=(EditText) mDialog.findViewById(R.id.etTableNumber);
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeAnOrderApi();
                mDialog.dismiss();
            }
        });
        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        tvInput.setTypeface(CommonUtils.setBook(context));
        tvIfNot.setTypeface(CommonUtils.setBook(context));
        tvContinue.setTypeface(CommonUtils.setBook(context));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
         /*   case  R.id.tvPlaceOreder:
                if (CommonUtils.isOnline(context)) {
                    if (CommonUtils.getPreferences(context, AppConstant.USER_CHECKEDIN_ID) != "") {
                        if(businessName.toLowerCase().trim().equalsIgnoreCase("restaurant")){
                            Log.e("tag","restaurant name matched"+businessName);
                            showInputTableNoDialog();
                        }else {
                            Log.e("tag","restaurant name not matched "+businessName);
                            placeAnOrderApi();
                        }
                    } else {
                        CommonUtils.showToast(context, "Please check-in into the shop to complete the order");
                    }
                } else {
                    CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                }
                break;*/
            case R.id.tv_update:
                if (CommonUtils.isOnline(context)) {
                    editCartDetailApi();
                }else {
                    CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                }
                break;
           case R.id.tvPlaceOreder:
                if(businessName.toLowerCase().trim().equalsIgnoreCase("restaurant")){
                    Log.e("tag","restaurant name matched"+businessName);
                    showInputTableNoDialog();
                }else {
                    Log.e("tag","restaurant name not matched "+businessName);
                    placeAnOrderApi();
                }

               CommonUtils.savePreferences(context,"totalPrice",TotalValue .getText().toString().replace("€ ",""));
                break;

        }
    }

}
