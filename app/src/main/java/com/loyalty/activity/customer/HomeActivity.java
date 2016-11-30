package com.loyalty.activity.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.adapter.customer.DrawerItemAdapter;
import com.loyalty.fragment.PaymentFragment;
import com.loyalty.fragment.customer.HistoryFragment;
import com.loyalty.fragment.customer.HomeFragment;
import com.loyalty.fragment.customer.NewHistoryFragment;
import com.loyalty.fragment.customer.PendingOrdersFragment;
import com.loyalty.fragment.customer.ReferFriendFragment;
import com.loyalty.fragment.customer.SettingsFragment;
import com.loyalty.model.customer.DrawerItemModel;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.RecyclerItemClickListener;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AppCompatActivity.class.getSimpleName();
    private String[] drawerItems;
    private List<DrawerItemModel> arrayList=new ArrayList<>();
    private RecyclerView rvDrawer;
    private DrawerItemAdapter drawerItemAdapter;
    private DrawerLayout drawerLayout;
    private LinearLayout llDrawerLeft;
    private LinearLayout llParenHome;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView tvToolbarTitle;
    private ImageView ivToolbarLeft;
    private Toolbar toolbar;
    private Context context;
    String userid;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context=HomeActivity.this;
        getIds();
        setToolbar();
        setListeners();
        setFragments();

        CommonUtils.setFragment(new HomeFragment(),false,HomeActivity.this,R.id.flHome);
        rvDrawer.setLayoutManager(new LinearLayoutManager(this));
        userid = CommonUtils.getPreferences(context, AppConstant.USER_ID);

        if (userid != "" && userid != null) {
            drawerItems = getResources().getStringArray(R.array.drawerItems);
        } else {
            drawerItems = getResources().getStringArray(R.array.guest_drawerItems);
        }
        for (int i = 0; i < drawerItems.length; i++)
        {
            arrayList.add(new DrawerItemModel(drawerItems[i]));
        }
        drawerItemAdapter = new DrawerItemAdapter(arrayList, this);
        rvDrawer.setAdapter(drawerItemAdapter);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                CommonUtils.hideSoftKeyboard(HomeActivity.this);
                llParenHome.setTranslationX(slideOffset * drawerView.getWidth());
                llDrawerLeft.bringChildToFront(drawerView);
                llDrawerLeft.requestLayout();
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item)
            {
                drawerLayout.closeDrawer(Gravity.RIGHT);
                return super.onOptionsItemSelected(item);

            }

        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if(getIntent()!= null){
            if (getIntent().getStringExtra(AppConstant.PAYMODE) != null) {
              String  loginKey = getIntent().getStringExtra(AppConstant.PAYMODE);

                switch (loginKey) {
                    case "pay_mode":
                        displayView(0);
                        break;
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Exit")
                .setMessage("Do you want to exit from App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();

                    }
                }).setNegativeButton("No", null).show();
    }
    private void setFragments() {
        rvDrawer.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO handle item click

                        switch (position) {
                            case 0:
                                CommonUtils.setFragment(new HomeFragment(),false,HomeActivity.this,R.id.flHome);
                                drawerLayout.closeDrawers();
                                break;
                            case 1:
                                if (userid != "" && userid != null) {
                                    CommonUtils.setFragment(new ReferFriendFragment(),false,HomeActivity.this,R.id.flHome);
                                    drawerLayout.closeDrawers();
                                } else {
                                    CommonUtils.showAlertLogin("Only registered user can Refer A Friend. " + "Please login. ", HomeActivity.this);
                                    drawerLayout.closeDrawers();
                                }
                                break;
                            case 2:
                                if (userid != "" && userid != null) {
                                    CommonUtils.setFragment(new NewHistoryFragment(),false,HomeActivity.this,R.id.flHome);
                                    drawerLayout.closeDrawers();
                                } else {
                                    CommonUtils.showAlertLogin("Only registered user can see the History. " + "Please login. ", HomeActivity.this);
                                    drawerLayout.closeDrawers();
                                }
                                break;
                            case 3:
                                /*pending orders*/
                                if (userid != "" && userid != null) {
                                    CommonUtils.setFragment(new PendingOrdersFragment(),false,HomeActivity.this,R.id.flHome);
                                    drawerLayout.closeDrawers();
                                } else {
                                    CommonUtils.showAlertLogin("Only registered user can see the Pending Orders. " + "Please login. ", HomeActivity.this);
                                    drawerLayout.closeDrawers();
                                }
                                break;
                            case 4:

                                if (userid != "" && userid != null) {
                                    CommonUtils.setFragment(new PaymentFragment(),false,HomeActivity.this,R.id.flHome);
                                    drawerLayout.closeDrawers();
                                } else {
                                    CommonUtils.showAlertLogin("Only registered user can see the Pending Orders. " + "Please login. ", HomeActivity.this);
                                    drawerLayout.closeDrawers();
                                }
                                break;
                            case 5:
                                CommonUtils.setFragment(new SettingsFragment(),false,HomeActivity.this,R.id.flHome);
                                drawerLayout.closeDrawers();
                                break;
                            case 6:
                                showLogout();
                                break;
                            default:
                                break;
                        }
                    }

                })
        );

    }

    private void displayView(int position) {
        switch (position){
            case 0:
                if (userid != "" && userid != null) {
                    CommonUtils.setFragment(new PaymentFragment(),false,HomeActivity.this,R.id.flHome);
                    drawerLayout.closeDrawers();
                } else {
                    CommonUtils.showAlertLogin("Only registered user can see the Pending Orders. " + "Please login. ", HomeActivity.this);
                    drawerLayout.closeDrawers();
                }
                break;
        }
    }

    private void showLogout()
    {
        new AlertDialog.Builder(this).setTitle("Exit")
                .setMessage("Do you want to logout from the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (CommonUtils.isOnline(context)) {
                            getLogoutApi();
                        }else {
                            CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
                        }

                    }
                }).setNegativeButton("No", null).show();
    }

    private void setListeners()
    {
        ivToolbarLeft.setOnClickListener(this);

    }
    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText("Guava");
        ivToolbarLeft.setImageResource(R.mipmap.menu);
        ivToolbarLeft.setVisibility(View.VISIBLE);
    }

    private void getIds()
    {
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        llDrawerLeft= (LinearLayout) findViewById(R.id.llDrawer);
        llParenHome= (LinearLayout) findViewById(R.id.llParentHome);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        tvToolbarTitle= (TextView) findViewById(R.id.toolbar_title);
        rvDrawer= (RecyclerView) findViewById(R.id.rvDrawer);
        ivToolbarLeft=(ImageView)  findViewById(R.id.iv_toolbar_left);
        tvToolbarTitle.setTypeface(CommonUtils.setBook(this));

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_toolbar_left:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;

        }
    }
    public void getLogoutApi() {
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        final RequestModel requestModel=new RequestModel();
        requestModel.userId=CommonUtils.getPreferences(this, AppConstant.USER_ID);
        requestModel.deviceKey=CommonUtils.getPreferences(this,AppConstant.DEVICE_KEY);
        requestModel.deviceToken=CommonUtils.getPreferences(this,AppConstant.DEVICE_TOKEN);
        Log.e("tag","device token in home activity"+CommonUtils.getPreferences(this,AppConstant.DEVICE_TOKEN));

        requestModel.deviceType="Android";
        Call<ResponseModel> call=apiServices.LogoutUser(AppConstant.BASIC_TOKEN,requestModel);
        Log.e(TAG,new Gson().toJson(requestModel));
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e(TAG,new Gson().toJson(response));
                if (response!=null &&response.body()!=null) {
                    if (response.body().responseCode.equalsIgnoreCase("200"))
                    {
                        CommonUtils.showToast(HomeActivity.this,"You have logout successfully");
                        CommonUtils.savePreferences(HomeActivity.this,AppConstant.USER_ID,"");
                        Log.e("tag","user id after logout"+ CommonUtils.getPreferences(context,AppConstant.USER_ID));
                        CommonUtils.deleteAllPreferences(context);
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });
    }

}
