package com.loyalty.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.adapter.customer.HotDealsAdapter;
import com.loyalty.fragment.customer.CatalogueFragment;
import com.loyalty.fragment.customer.CheckInFragment;

import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.Cart;
import com.loyalty.webserivcemodel.ChangePasswordModel;
import com.loyalty.webserivcemodel.CheckInBeaconsModel;
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

public class NewcatalogueActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TextView tvTitle, tvCheckIn, tvCatalouge;
    private TabLayout mTab;
    private Context mContext;
    private Activity activity;
    private ImageView ivToolbarRight1, ivToolbarRight2, ivHomeLogo;
    View view;
    private String storeId;
    private String businessId;
    private LinearLayout llTab;
    private ImageView ivtoolbarleft;

    private BeaconManager beaconManager;
    private BluetoothAdapter mBtAdapter;
    private String scanId;
    public List<Beacon> adapter;
    private ProgressDialog progressDialog;
    private String latitude;
    private String logintude;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private String crntLat;
    private String crntLong;
    private String businessTypeName;
    private RecyclerView rvDeals;
    private List<HotDeals> hotDeals;
    private HotDealsAdapter hotDealsAdapter;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NewcatalogueActivity.this;
        setContentView(R.layout.activity_newcatalogue);
        storeId = getIntent().getStringExtra("StoreId");
        businessId = getIntent().getStringExtra("businessID");
        latitude = getIntent().getStringExtra("latitude");
        logintude = getIntent().getStringExtra("longitude");
        crntLat = getIntent().getStringExtra("crntLat");
        crntLong = getIntent().getStringExtra("crntLong");
        businessTypeName = getIntent().getStringExtra("businessTypeName");
        findId();
        setListeners();
        beaconManager = new BeaconManager(mContext);

        CommonUtils.getPreferences(mContext,AppConstant.USER_CHECKEDIN_ID);
        Log.e("tag","uer checked in id in catalouge activity"+AppConstant.USER_CHECKEDIN_ID);
  }

    private void getBeacons() {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null && list.size() > 0) {
                            Log.e("", "size " + list.size());
                            Beacon beacon = list.get(0);
                            String uIID = beacon.getProximityUUID().toString();
                            String minor = String.valueOf(beacon.getMinor());
                            String major = String.valueOf(beacon.getMajor());
                            Log.e("tag", "uiid" + uIID);
                            Log.e("tag", "minor" + minor);
                            Log.e("tag", "major" + major);
                            Log.e("", "size " + list.size());
                            Toast.makeText(NewcatalogueActivity.this, "Check In successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.e("onEnteredRegion", "onEnteredRegion");
                Toast.makeText(NewcatalogueActivity.this, "beacons entered", Toast.LENGTH_SHORT).show();
              }

            @Override
            public void onExitedRegion(Region region) {
                Log.e("onExitedRegion", "onExitedRegion");
                beaconManager.stopMonitoring(region);
            }
        });
    }

    public void findId() {
        llTab = (LinearLayout) findViewById(R.id.llTab);
        mTab = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvCatalouge = (TextView) findViewById(R.id.tvCatalouge);
        tvCheckIn = (TextView) findViewById(R.id.tvCheckIn);
        tvTitle.setText("Store");
        ivToolbarRight1 = (ImageView) findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2 = (ImageView) findViewById(R.id.iv_toolbar_right2);
        ivHomeLogo = (ImageView) findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setTypeface(CommonUtils.setBook(mContext));
        ivtoolbarleft = (ImageView) findViewById(R.id.iv_toolbar_left);
        setupViewPager(mViewPager);
        tvTitle.setVisibility(View.VISIBLE);
        llTab.setVisibility(View.VISIBLE);
        mTab.setVisibility(View.GONE);
        ivToolbarRight1.setVisibility(View.GONE);

        ivtoolbarleft.setVisibility(View.VISIBLE);
        ivToolbarRight2.setImageResource(R.mipmap.trolly);
        ivToolbarRight2.setVisibility(View.VISIBLE);
    }

    private void setListeners() {
        tvCheckIn.setOnClickListener(this);
        tvCatalouge.setOnClickListener(this);
        ivtoolbarleft.setOnClickListener(this);
        ivToolbarRight2.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CatalogueFragment catalogueFragment = new CatalogueFragment();
        Bundle args = new Bundle();
        args.putString("StoreId", storeId);
        args.putString("businessID", businessId);
        args.putString("latitude", latitude);
        args.putString("logintude", logintude);
        args.putString("crntLat", crntLat);
        args.putString("crntLong", crntLong);
        args.putString("businessTypeName", businessTypeName);
        catalogueFragment.setArguments(args);

        CheckInFragment checkInFragment = new CheckInFragment();
        Bundle args1 = new Bundle();
        args1.putString("StoreId", storeId);
        args1.putString("businessID", businessId);
        args1.putString("businessTypeName", businessTypeName);
        checkInFragment.setArguments(args1);

        adapter.addFragment(catalogueFragment, getResources().getString(R.string.catalogue));
        adapter.addFragment(checkInFragment, getResources().getString(R.string.checkin));

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCatalouge:
                Intent intent = new Intent(mContext, CatalogueActivity.class)
                        .putExtra("storeId", storeId)
                        .putExtra("businessId", businessId);
                startActivity(intent);
                break;
            case R.id.tvCheckIn:
                getBeacons();
                break;
            case R.id.iv_toolbar_left:
                finish();
                break;
            case R.id.iv_toolbar_right2:
                intent = new Intent(this, ShoppingBasketActivity.class).putExtra("storeid", storeId).putExtra("businessid", businessId);
                CommonUtils.getPreferences(mContext,AppConstant.USER_CHECKEDIN_ID);
                Log.e("tag","uer checked in id in catalouge activity"+AppConstant.USER_CHECKEDIN_ID);
                startActivity(intent);
                break;
        }
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

    public void getBeaconListApi() {
        List<Cart> cartlist = new ArrayList<>();
        Cart cart = new Cart();
        cart.setName("beacon");
        cart.setInfo("1300");
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        cartlist.add(cart);
        RequestModel requestModel = new RequestModel();
        requestModel.userId = CommonUtils.getPreferences(NewcatalogueActivity.this, AppConstant.USER_ID);
        requestModel.storeId = storeId;
        requestModel.setCart(cartlist);

        Call<ResponseModel> call = apiServices.checkIn(AppConstant.BASIC_TOKEN,requestModel);
        startProgressBar(mContext);
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if (response != null) {
                    CommonUtils.showToast(mContext, "CheckIn Successfully");
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

    public void startProgressBar(Context context) {
        progressDialog = ProgressDialog.show(context, null, AppConstant.PLEASE_WAIT, true, false);
    }

    public void stopProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    Toast.makeText(NewcatalogueActivity.this, "Cannot start ranging, something terrible happened", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    private void getBusinessDetails(String id) {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        String userId = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
        Call<ResponseModel> call = apiServices.businessDetails(AppConstant.BASIC_TOKEN,id, userId);
        startProgressBar(mContext);
        Log.e("TAG", "request catalouge++++++" + new Gson().toJson(call.request()));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                 stopProgressBar();
                Log.e("TAGREQUEST", "responce catalouge" + call.request().body());

                if (response != null && response.body() != null) {
                    if (response.body().responseCode.equalsIgnoreCase("200")) {
                        if (response.body().hotDeals != null && response.body().hotDeals.size() > 0) {
                            hotDeals.addAll(response.body().hotDeals);
                        } else {
                            CommonUtils.showToast(mContext, "Hotdelas not available yet");
                        }
                        hotDealsAdapter.notifyDataSetChanged();

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