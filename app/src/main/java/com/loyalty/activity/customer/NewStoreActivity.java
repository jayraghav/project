package com.loyalty.activity.customer;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.TutorialActivity;
import com.loyalty.adapter.customer.CheckoutAdapter;
import com.loyalty.adapter.customer.HotDealsAdapter;
import com.loyalty.fragment.customer.CatalogueFragment;
import com.loyalty.fragment.customer.CheckInFragment;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.viewpagerindicator.CirclePageIndicator;
import com.loyalty.webserivcemodel.BusinessInfo;
import com.loyalty.webserivcemodel.Cart;
import com.loyalty.webserivcemodel.HotDeals;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class NewStoreActivity extends AppCompatActivity implements View.OnClickListener, OnHistoryListener {

    private Context mContext;
    private TextView tvTitle, tvCheckIn, tvCatalouge, tvNoDealFound;
    private ImageView ivToolbarRight1, ivToolbarRight2, ivHomeLogo, ivtoolbarleft;
    private View view;
    private ViewPager mViewPager;
    private RecyclerView rvHotDeals;
    private HotDealsAdapter adapterHotDeals;
    private OnHistoryListener onHistoryListener;
    public List<Beacon> adapter;
    private List<HotDeals> hotDeals = new ArrayList<>();
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private String crntLat,crntLong;
    private String storeId, businessId, businessTypeName, distance, storeName;
    private BeaconManager beaconManager;
    private ProgressDialog progressDialog;
    private BusinessInfo businessInfoNew;
    private boolean checkBecon;
    private String uIID;
    private String minor ;
    private String major;
    private LinearLayout llLeftRating,llMiddleRating,llRightRating,llLast;
    private ImageView ivStarOne,ivStarTwo,ivStarThree;
    private double loyaltypoints;
    private int count =0;

    private CirclePageIndicator circlePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_store);
        mContext = NewStoreActivity.this;
        onHistoryListener = NewStoreActivity.this;

        storeId = getIntent().getStringExtra("StoreId");
        businessId = getIntent().getStringExtra("businessID");
        crntLat = getIntent().getStringExtra("crntLat");
        crntLong = getIntent().getStringExtra("crntLong");
        businessTypeName = getIntent().getStringExtra("businessTypeName");
        distance = getIntent().getStringExtra("distance");
        storeName=getIntent().getStringExtra("storeName");

        findId();
        setListeners();
        tvTitle.setText(storeName);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvHotDeals.setLayoutManager(mLayoutManager);
        adapterHotDeals = new HotDealsAdapter(mContext, hotDeals, onHistoryListener);
        rvHotDeals.setAdapter(adapterHotDeals);

        beaconManager = new BeaconManager(mContext);

        if (storeId != null) {
            if (CommonUtils.isOnline(mContext)) {
                getBusinessDetails(storeId);
            } else {
                CommonUtils.showToast(mContext, AppConstant.PLEASE_CHECK_INTERNET);
            }
        }
        if(CommonUtils.getPreferences(mContext,AppConstant.USER_CHECKEDIN_ID) !=null && !CommonUtils.getPreferences(mContext,AppConstant.USER_CHECKEDIN_ID).equalsIgnoreCase("")) {
            if(CommonUtils.getPreferences(mContext,AppConstant.CHECKEDIN_STORE_ID).equalsIgnoreCase(storeId)) {
                tvCheckIn.setText("Check Out");
            }else{
                tvCheckIn.setText("Check-In");
            }
        }else{
            tvCheckIn.setText("Check-In");
        }

        llLeftRating.setBackgroundColor(getResources().getColor(R.color.white));
        llRightRating.setBackgroundColor(getResources().getColor(R.color.white));
        llMiddleRating.setBackgroundColor(getResources().getColor(R.color.white));
        ivStarOne.setImageResource(R.mipmap.star3);
        ivStarTwo.setImageResource(R.mipmap.star3);
        ivStarThree.setImageResource(R.mipmap.star3);
    }

    private void findId() {
        mViewPager = (ViewPager) findViewById(R.id.Dealsviewpager);

        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvCatalouge = (TextView) findViewById(R.id.tvCatalouge);
        tvNoDealFound = (TextView) findViewById(R.id.tvNoDealFound);
        tvCheckIn = (TextView) findViewById(R.id.tvCheckIn);
        ivToolbarRight1 = (ImageView) findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2 = (ImageView) findViewById(R.id.iv_toolbar_right2);
        ivHomeLogo = (ImageView) findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setTypeface(CommonUtils.setBook(mContext));
        ivtoolbarleft = (ImageView) findViewById(R.id.iv_toolbar_left);
        rvHotDeals = (RecyclerView) findViewById(R.id.rvHotDeals);
        tvTitle.setVisibility(View.VISIBLE);
        ivToolbarRight1.setVisibility(View.GONE);
        ivtoolbarleft.setVisibility(View.VISIBLE);
        ivToolbarRight2.setImageResource(R.mipmap.trolly);
        ivToolbarRight2.setVisibility(View.VISIBLE);
        llLeftRating=(LinearLayout)findViewById(R.id.ll_left_rating);
        llRightRating=(LinearLayout)findViewById(R.id.ll_right_rating);
        llMiddleRating=(LinearLayout)findViewById(R.id.ll_middile_rating);
        llLast=(LinearLayout)findViewById(R.id.ll_last);
        ivStarOne=(ImageView)findViewById(R.id.ivGreyStar);
        ivStarTwo=(ImageView)findViewById(R.id.ivYellowStar);
        ivStarThree=(ImageView)findViewById(R.id.ivWhiteStar);
        circlePageIndicator= (CirclePageIndicator) findViewById(R.id.indicator);

        /*mIndicator=(CirclePageIndicator) findViewById(R.id.indicatorTutorial);*/
    }

    private void setListeners() {
        tvCheckIn.setOnClickListener(this);
        tvCatalouge.setOnClickListener(this);
        ivtoolbarleft.setOnClickListener(this);
        ivToolbarRight2.setOnClickListener(this);
    }



    private void getBusinessDetails(String id) {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        String userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
        if(userId != "" && userId != null)
        {
            userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
        }
        else {
            userId="-1";
        }
        Call<ResponseModel> call = apiServices.businessDetails(AppConstant.BASIC_TOKEN,id, userId);
        startProgressBar(mContext);
        Log.e("TAGREQUEST", call.request().url().toString());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                int point;
                if (response != null && response.body() != null) {
                    if (response.body().businessInfo != null) {
                        businessInfoNew = response.body().businessInfo;
                        if(businessInfoNew.points!=""&&businessInfoNew.points!=null&&businessInfoNew.points.length()!=0) {
                            loyaltypoints = Double.parseDouble(businessInfoNew.points);
                            point=(int)loyaltypoints;
                        }
                        else {
                            point = 1;
                        }
                        int oneStarPoint=Integer.parseInt(businessInfoNew.oneStarPoint);
                        int twostarPoint=Integer.parseInt(businessInfoNew.twoStarPoint);
                        int threestarPoint=Integer.parseInt(businessInfoNew.threeStarPoint);

                        if(point<=oneStarPoint) {
                            if (point==oneStarPoint) {
                                ivStarOne.setImageResource(R.mipmap.star);
                            } else {

                                int reatingBarValue = oneStarPoint / 4;
                                int result = point / reatingBarValue;
                                if (result == 1) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                } else if (result == 2) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                    llMiddleRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                } else if (result == 3) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                    llMiddleRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                    llRightRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                } else if (result == 4) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                    llMiddleRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                    llRightRating.setBackgroundColor(getResources().getColor(R.color.color_red));
                                    llLast.setBackgroundColor(getResources().getColor(R.color.color_red));
                                }
                            }
                        }
                        else if (point>oneStarPoint&&point<=twostarPoint) {
                            if (point == twostarPoint) {
                                ivStarOne.setImageResource(R.mipmap.star);
                                ivStarTwo.setImageResource(R.mipmap.star);
                            }
                            else {
                                {
                                    ivStarOne.setImageResource(R.mipmap.star);
                                    int ratingBarVAlue = twostarPoint / 4;
                                    int result = point/ratingBarVAlue;
                                    if (result == 1) {
                                        llLeftRating.setBackgroundColor(getResources().getColor(R.color.black));
                                    } else if (result == 2) {
                                        llLeftRating.setBackgroundColor(getResources().getColor(R.color.black));
                                        llMiddleRating.setBackgroundColor(getResources().getColor(R.color.black));
                                    } else if (result == 3) {
                                        llLeftRating.setBackgroundColor(getResources().getColor(R.color.black));
                                        llMiddleRating.setBackgroundColor(getResources().getColor(R.color.black));
                                        llRightRating.setBackgroundColor(getResources().getColor(R.color.black));
                                    } else if (result == 4) {
                                        llLeftRating.setBackgroundColor(getResources().getColor(R.color.black));
                                        llMiddleRating.setBackgroundColor(getResources().getColor(R.color.black));
                                        llRightRating.setBackgroundColor(getResources().getColor(R.color.black));
                                        llLast.setBackgroundColor(getResources().getColor(R.color.black));
                                    }
                                }
                            }
                        }
                        else if (point>twostarPoint&&point<=threestarPoint) {
                            if (point == threestarPoint) {
                                ivStarOne.setImageResource(R.mipmap.star);
                                ivStarTwo.setImageResource(R.mipmap.star);
                                ivStarThree.setImageResource(R.mipmap.star);
                            } else {
                                ivStarOne.setImageResource(R.mipmap.star);
                                ivStarTwo.setImageResource(R.mipmap.star);
                                int ratingBarVAlue = threestarPoint / 4;
                                int result = point/ratingBarVAlue;
                                if (result == 1) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                } else if (result == 2) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                    llMiddleRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                } else if (result == 3) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                    llMiddleRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                    llRightRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                } else if (result == 4) {
                                    llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                    llMiddleRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                    llRightRating.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                    llLast.setBackgroundColor(getResources().getColor(R.color.lightPink));
                                }
                            }
                        }
                        else if (point>threestarPoint)
                        {
                            ivStarOne.setImageResource(R.mipmap.star);
                            ivStarTwo.setImageResource(R.mipmap.star);
                            ivStarThree.setImageResource(R.mipmap.star);
                            int ratingBarVAlue=threestarPoint/4;
                            int result=point/ratingBarVAlue;
                            if (result==1)
                            {
                                llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                            }
                            else if(result==2)
                            {
                                llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                                llMiddleRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                            }
                            else if(result==3)
                            {
                                llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                                llMiddleRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                                llRightRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                            }
                            else if(result==4) {
                                llLeftRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                                llMiddleRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                                llRightRating.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                                llLast.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                            }
                        }
                    }
                    setupViewPager(mViewPager);
                    circlePageIndicator.setViewPager(mViewPager);
                }
                if (response.body().responseCode.equalsIgnoreCase("200")) {
                    if (response.body().hotDeals != null && response.body().hotDeals.size() > 0) {
                        hotDeals.addAll(response.body().hotDeals);
                        adapterHotDeals.notifyDataSetChanged();
                        tvNoDealFound.setVisibility(View.GONE);
                    } else {
                        tvNoDealFound.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }

    private void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CatalogueFragment catalogueFragment = new CatalogueFragment();
        catalogueFragment.setBusinessInfoFromActivity(businessInfoNew);
        Bundle args = new Bundle();
        args.putString("businessTypeName", businessTypeName);
        args.putString("crntLat", crntLat);
        args.putString("crntLong", crntLong);
        catalogueFragment.setArguments(args);
        CheckInFragment checkInFragment = new CheckInFragment();
        checkInFragment.setBusinessInfoFromActivity(businessInfoNew);
        Bundle args1 = new Bundle();
        args1.putString("distance", distance);
        args1.putString("crntLat", crntLat);
        args1.putString("crntLong", crntLong);
        checkInFragment.setArguments(args1);
        adapter.addFragment(catalogueFragment, getResources().getString(R.string.catalogue));
        adapter.addFragment(checkInFragment, getResources().getString(R.string.checkin));
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(1);
        viewpager.setCurrentItem(0);
    }

    private void getBluetoothAccess(){
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return;
        }
        if (bluetoothAdapter.isEnabled()) {
            connectToService();
        } else {
            bluetoothAdapter.enable();
            connectToService();
        }
    }

    private void connectToService() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                    beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
                } catch (Exception e) {
                    Toast.makeText(NewStoreActivity.this, "Cannot start ranging, something terrible happened", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getBeacons() {
        startProgressBar(mContext);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(final Region region, final List<Beacon> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null && list.size()> 0) {
                            Log.e("", "size " + list.size());
                            for(int i=0;i<list.size();i++) {
                                Beacon beacon = list.get(i);
                                uIID = beacon.getProximityUUID().toString();
                                minor = String.valueOf(beacon.getMinor());
                                major = String.valueOf(beacon.getMajor());
                                Log.e("tag","uIID of beacon "+uIID +"minor of beacon "+minor+"major of beacon "+major);
                                beaconManager.startMonitoring(region);
                                if (businessInfoNew.uuid.equalsIgnoreCase(uIID) && businessInfoNew.majorNo.equalsIgnoreCase(major) && businessInfoNew.minorNo.equalsIgnoreCase(minor))
                                {
                                    /*Log.e("tag","uIID of beacon from server "+businessInfoNew.uuid +"minor of beacon from server "+businessInfoNew.minorNo
                                    +"major of beacon from server "+businessInfoNew.majorNo);*/
                                    CommonUtils.savePreferencesString(mContext, AppConstant.UIID, list.get(i).getProximityUUID().toString());
                                    CommonUtils.savePreferencesString(mContext, AppConstant.MAJOR,String.valueOf(list.get(i).getMajor()));
                                    CommonUtils.savePreferencesString(mContext,AppConstant.MINOR,String.valueOf(list.get(i).getMinor()));
                                    if (!checkBecon) {
                                        checkBecon = true;
                                        stopProgressBar();
                                        beaconManager.disconnect();
                                        getBeaconListApi();
                                    }
                                }else{
                                    count +=1;
                                    if(count==10){
                                        stopProgressBar();
                                        beaconManager.disconnect();
                                        Toast.makeText(NewStoreActivity.this, "Beacon not matched.Please Check-In again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }else{
                            count +=1;
                            if(count==10){
                                stopProgressBar();
                                beaconManager.disconnect();
                                Toast.makeText(NewStoreActivity.this, "Beacon not found. Please Check-In again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    public void getBeaconListApi() {
        RequestModel requestModel = new RequestModel();
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        requestModel.userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
        if(requestModel.userId != "" && requestModel.userId != null)
        {
            requestModel.userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
        }
        else {
            requestModel.userId="-1";
        }

        requestModel.storeId = storeId;
        Call<ResponseModel> call = apiServices.checkIn(AppConstant.BASIC_TOKEN,requestModel);
        startProgressBar(mContext);
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null && response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {

                        CommonUtils.showToast(mContext, "CheckIn successfully");
                        CommonUtils.savePreferences(mContext,AppConstant.USER_CHECKEDIN_ID, response.body().userCheckedInId);
                        CommonUtils.savePreferences(mContext,AppConstant.CHECKEDIN_STORE_ID, storeId);
                        Log.e("tag","check in storeid "+storeId);
                        tvCheckIn.setText("Check Out");
                    } else if (response.body().responseCode.equalsIgnoreCase("201")) {
                        CommonUtils.showToast(mContext, response.body().responseMessage);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
                checkBecon=false;
            }
        });
    }

    private void checkoutApi() {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel = new RequestModel();
        requestModel.userCheckedInId = CommonUtils.getPreferences(mContext, AppConstant.USER_CHECKEDIN_ID);
        requestModel.userId=CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
        requestModel.storeId=storeId;
        Call<ResponseModel> call = apiServices.checkout(AppConstant.BASIC_TOKEN,requestModel);
        startProgressBar(mContext);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null) {
                    CommonUtils.savePreferences(mContext, AppConstant.USER_CHECKEDIN_ID,"");
                    checkBecon=true;
                    tvCheckIn.setText("Check-In");
                    CommonUtils.showToast(mContext, "CheckOut Successfully");
                } else {
                    CommonUtils.showToast(mContext, response.body().responseMessage);
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }

    private void addToBasket(String dealId, String StoreId) {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel = new RequestModel();
        requestModel.dealId = dealId;
        requestModel.storeId = StoreId;
        requestModel.userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
        Call<ResponseModel> call = apiServices.addItemToCart(AppConstant.BASIC_TOKEN,requestModel);
        startProgressBar(mContext);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        CommonUtils.showToast(mContext, "Deal successful added into your account");
                    } else {
                        CommonUtils.showToast(mContext, response.body().responseMessage);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
            }
        });
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCatalouge:
                Intent intent = new Intent(mContext, CatalogueActivity.class)
                        .putExtra("storeId", storeId)
                        .putExtra("businessId", businessId)
                        .putExtra("storeName", storeName);
                startActivity(intent);
                break;

            case R.id.tvCheckIn:
                String userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
                if(userId!=null && userId!="") {
                    String checkInCheckout = tvCheckIn.getText().toString();
                    if (checkInCheckout.equalsIgnoreCase("Check-In")) {
                        Toast.makeText(NewStoreActivity.this, "You don't have beacons", Toast.LENGTH_SHORT).show();

                    }
                }
                    else {
                        CommonUtils.showAlertLogin("Only registered user can add to basket. " + "Please login. ", NewStoreActivity.this);

                    }

               /* if (checkInCheckout.equalsIgnoreCase("Check-In")) {
                    checkBecon = false;
                    count = 0;
                    getBluetoothAccess();
                    getBeacons();
                } else if (checkInCheckout.equalsIgnoreCase("Check Out")) {
                    String uid = CommonUtils.getPreferences(mContext, AppConstant.UIID);
                    String major = CommonUtils.getPreferences(mContext, AppConstant.MAJOR);
                    String minor = CommonUtils.getPreferences(mContext, AppConstant.MINOR);
                    if (major != null && major != "" || minor != null && minor != "" || uid != null && uid != ""
                            || businessInfoNew.majorNo != null && businessInfoNew.majorNo != ""
                            || businessInfoNew.minorNo != null && businessInfoNew.minorNo != ""
                            || businessInfoNew.uuid != null && businessInfoNew.uuid != "") {
                        if (businessInfoNew.majorNo.equalsIgnoreCase(major) && businessInfoNew.minorNo.equalsIgnoreCase(minor)&& businessInfoNew.uuid.equalsIgnoreCase(uid)) {
                            checkoutApi();
                        }
                    }
                }*/
                break;
            case R.id.iv_toolbar_left:
                finish();
                break;
            case R.id.iv_toolbar_right2:
                String userId1 = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
                if(userId1!=null && userId1!="") {
                    intent = new Intent(this, ShoppingBasketActivity.class).putExtra("storeid", storeId).putExtra("businessid", businessId);
                    startActivity(intent);
                }
                else
                {
                    CommonUtils.showAlertLogin("Only registered user can add to basket. " + "Please login. ", NewStoreActivity.this);

                }
                break;
        }
    }

    @Override
    public void onHistoryClickListener(int postion) {
        if (CommonUtils.isOnline(mContext)) {
            String userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
            if(userId != "" && userId != null) {
                addToBasket(hotDeals.get(postion).dealId, storeId);
            }
            else
            {
                CommonUtils.showAlertLogin("Only registered user can add to basket. " + "Please login. ", NewStoreActivity.this);

            }
        } else {
            CommonUtils.showToast(mContext, AppConstant.PLEASE_CHECK_INTERNET);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBluetoothAccess();
    }

    @Override
    protected void onPause() {
        beaconManager.disconnect();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
