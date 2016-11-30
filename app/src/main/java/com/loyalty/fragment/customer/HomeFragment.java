package com.loyalty.fragment.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loyalty.Listeners.OnBusinessClickListener;
import com.loyalty.R;
import com.loyalty.activity.customer.NewFilterActivity;
import com.loyalty.activity.customer.NewStoreActivity;
import com.loyalty.activity.customer.TakeMeThereActivity;
import com.loyalty.activity.customer.WhatsHotActivity;
import com.loyalty.activity.customer.YourLoyaltyActivity;
import com.loyalty.adapter.customer.HomeAdapter;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.LocationTracker;
import com.loyalty.utils.MyGpsTracker;
import com.loyalty.webserivcemodel.FilterModel;
import com.loyalty.webserivcemodel.GetBusineesDetailsResponse;
import com.loyalty.webserivcemodel.ResponseBusinessTypeModel;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shivangi on 25-08-2016.
 */
public class HomeFragment extends Fragment implements OnBusinessClickListener, com.loyalty.utils.LocationResult {
    private static final String TAG=HomeFragment.class.getSimpleName();
    private static final int PERMISSION_CODE_1 = 1234;
    private View v;
    private Context context;
    private RecyclerView rvCompanyList;
    private OnBusinessClickListener onBusinessClickListener;
    private HomeAdapter adapter;
    private HomeAdapter adapterType;
    private ImageView ivFilter,ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
    private TextView tvToolbarTitle,tvNoDealFound;
    private Activity activity;
    private EditText etSearch;
    private List<GetBusineesDetailsResponse> businessDetails;
    private List<GetBusineesDetailsResponse> itemsDetails;
    private Dialog progressDialog;
    public static List<FilterModel>businessTypeList;
    String BusinessName;
    private String filterlist;
    com.loyalty.utils.LocationResult locationResult;
    private  List <FilterModel> SelectedbusinessTypeList;

    private static List<String> selectedIds = new ArrayList<>();
    private LocationManager locmanager;
    private boolean statusOfGPS;
    private MyGpsTracker gpsTrack;
    private String lati;
    private String lng;
    private static String distance="true";
    private static String popularity="true";
    private static String ResetFilter="true";

    FilterModel filterModel;
    private Bundle bundle;
    private String id;
    private String businessTypeName;
    private RadioGroup rgType;
    private RadioButton rbBusiness, rbItems;
    private String search;
    private boolean isSelected=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        businessDetails=new ArrayList<>();
        itemsDetails=new ArrayList<>();
        businessTypeList=new ArrayList<>();
        SelectedbusinessTypeList=new ArrayList<>();

        v=inflater.inflate(R.layout.fragment_home, container, false);
        onBusinessClickListener=this;
        activity=getActivity();
        filterModel =  new FilterModel();
        findIds();
        setListeners();
        locationResult=this;

        if (CommonUtils.isOnline(activity)) {
            getCurrrentLatLang();
        }else {
            CommonUtils.showToast(activity, AppConstant.PLEASE_CHECK_INTERNET);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvCompanyList.setLayoutManager(mLayoutManager);
        adapter=new HomeAdapter(context,businessDetails,onBusinessClickListener,false);
        adapterType=new HomeAdapter(context,itemsDetails,onBusinessClickListener,true);
        rvCompanyList.setAdapter(adapter);


        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String filterlist = selectedIds.toString();
                if (filterlist.length()>2){
                    filterlist = filterlist.replaceAll("]", "");
                    filterlist = filterlist.replaceAll("\\[", "");
                }else {
                    filterlist = "";
                }
                searchApi(search,lati,lng,filterlist,distance,popularity);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String lat=lati;
                String longi=lng;
            search=     s.toString();
                String filterlist = selectedIds.toString();
                if (filterlist.length()>2){
                    filterlist = filterlist.replaceAll("]", "");
                    filterlist = filterlist.replaceAll("\\[", "");
                }else {
                    filterlist = "";
                }
                //searchApi(search,lat,longi,filterlist,distance,popularity);

                //
                searchApi(search,lat,longi,filterlist,distance,popularity);
            }
        });
        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    private void findIds() {
        ivFilter=(ImageView) v.findViewById(R.id.ivFilter);
        ivToolbarRight1=(ImageView) activity.findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView) activity.findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.VISIBLE);
        ivToolbarRight2.setImageResource(R.mipmap.fire);
        ivToolbarRight1.setImageResource(R.mipmap.crown);
        etSearch=(EditText) v.findViewById(R.id.etSearch);
        ivToolbarRight2.setVisibility(View.VISIBLE);
        rvCompanyList=(RecyclerView) v.findViewById(R.id.rvWhatsHot);
        tvToolbarTitle=(TextView) activity.findViewById(R.id.toolbar_title);
        tvNoDealFound=(TextView) activity.findViewById(R.id.tvNoDealFound);
        tvToolbarTitle.setVisibility(View.GONE);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.VISIBLE);
        rgType = (RadioGroup) v.findViewById(R.id.rgType);
        rbBusiness = (RadioButton) v.findViewById(R.id.rbBusiness);
        rbItems = (RadioButton) v.findViewById(R.id.rbItems);
    }
    private void setListeners() {

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewFilterActivity.class);
                intent.putExtra("Detail", (ArrayList<FilterModel>) businessTypeList);
                startActivityForResult(intent, 2);
            }
        });
        ivToolbarRight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, YourLoyaltyActivity.class));
            }
        });
        ivToolbarRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, WhatsHotActivity.class));
            }
        });
    }
    public void getBusinessListApi() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "", getResources().getString(R.string.please_wait));

        String filterlist = selectedIds.toString();
        if (filterlist.length()>2){
            filterlist = filterlist.replaceAll("]", "");
            filterlist = filterlist.replaceAll("\\[", "");
        }else {
            filterlist = "";
        }
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);//28.5603,77.2913
        Call<ResponseModel> call=apiServices.bussinessList(AppConstant.BASIC_TOKEN,lati,lng,filterlist,distance,popularity);

        Log.e(">>>>>>>>>>>>>>>>filter",new Gson().toJson(call.toString()));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response!=null && response.body()!=null) {
                //    stopProgressBar();
                    if(response.body().businessList!=null) {

                        businessTypeList.clear();
                        businessDetails.clear();
                        businessDetails.addAll(response.body().businessList);
                        if(response.body().businessTypeList!=null){
                            businessTypeList.addAll(response.body().businessTypeList);
                        }
                        else {
                            FilterModel filterModel =new FilterModel();
                            filterModel.businessTypeId="";
                            businessTypeList.add(filterModel);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
    @Override
    public void onBusinessClickistener(int Position) {
        String userid = CommonUtils.getPreferences(context, AppConstant.USER_ID);
        // if (userid != "" && userid != null)

        {
            if (CommonUtils.isOnline(context)) {
                String lat = businessDetails.get(Position).latitude;
                String longitutde = businessDetails.get(Position).longitude;
                Intent intent = new Intent(context, NewStoreActivity.class)
                        .putExtra("StoreId", String.valueOf(businessDetails.get(Position).storeId))
                        .putExtra("businessID", String.valueOf(businessDetails.get(Position).businessId))
                        .putExtra("businessTypeName", String.valueOf(businessDetails.get(Position).businessTypeName))
                        .putExtra("distance", String.valueOf(businessDetails.get(Position).distance))
                        .putExtra("storeName", String.valueOf(businessDetails.get(Position).storeName))
                        .putExtra("address", String.valueOf(businessDetails.get(Position).address))
                        .putExtra("businessImage", String.valueOf(businessDetails.get(Position).businessImage))
                        .putExtra("latitude", lat)
                        .putExtra("longitude", longitutde)
                        .putExtra("crntLat", lati)
                        .putExtra("crntLong", lng);
                CommonUtils.savePreferences(context, "businessid", String.valueOf(businessDetails.get(Position).businessId));
                Log.e("tag", "businessid " + String.valueOf(businessDetails.get(Position).businessId));
                startActivity(intent);
            } else {
                CommonUtils.showToast(context, AppConstant.PLEASE_CHECK_INTERNET);
            }
        }
    }/* else {
            CommonUtils.showAlertLogin("Only registered user can see the details. " + "Please login. ",activity);}
    }*/
    @Override
    public void ontakeMeThereListener(int position) {
        String lat=businessDetails.get(position).latitude;
        String longitutde=businessDetails.get(position).longitude;
        String uriString = "http://maps.google.com/maps?saddr="+lat+","+longitutde+"&daddr="+lati+","+lng;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(uriString));
        startActivity(intent);
        /*Intent intent=new Intent(context, TakeMeThereActivity.class).putExtra("Latitude",lat).putExtra("logi",longitutde)
                .putExtra("crntLat",lati).putExtra("crntLng",lng);
        startActivity(intent);*/
    }
    public void searchApi(String search,String latitude,String longitude,String businessTypeList,String distance,String popularity) {

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBusinessTypeModel> call=apiInterface.searchApi(AppConstant.BASIC_TOKEN,search,latitude,longitude,businessTypeList,distance,popularity);

        call.enqueue(new Callback<ResponseBusinessTypeModel>() {
            @Override
            public void onResponse(Call<ResponseBusinessTypeModel> call, Response<ResponseBusinessTypeModel> response) {

                if(response!=null && response.body()!=null) {
                   // Log.e("TAG home search resonse","home search resonse"+new Gson().toJson(response));
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        if(rbBusiness.isChecked()){
                            if (response.body().businessList.size()>0){
                                businessDetails.clear();
                                businessDetails.addAll(response.body().businessList);
                                adapter.notifyDataSetChanged();
                                rvCompanyList.setAdapter(adapter);
                            }
                        }else if(rbItems.isChecked()){
                            if (response.body().itemList.size()>0){
                                itemsDetails.clear();
                                itemsDetails.addAll(response.body().itemList);
                                adapterType.notifyDataSetChanged();
                                rvCompanyList.setAdapter(adapterType);
                            }
                        }

                    }
                    else {
                        tvNoDealFound.setVisibility(View.VISIBLE);

                    }

                }
            }
            @Override
            public void onFailure(Call<ResponseBusinessTypeModel> call, Throwable t) {
                t.printStackTrace();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(data.getExtras().getBoolean("distance",true)){
                distance=data.getExtras().getString("distance");
            }
            if(data.getExtras().getBoolean("popularity",true)){
                popularity=data.getExtras().getString("popularity");
            }
            if(data.getSerializableExtra("arrayBusinessTypeName")!=null){
                selectedIds= (List<String>) data.getSerializableExtra("arrayBusinessTypeName");
                Log.e("tag","selected ids "+selectedIds);
            }
            getBusinessFilterListApi();
        }
    }
    public void getBusinessFilterListApi()
    {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "", getResources().getString(R.string.please_wait));

        String selectId = selectedIds.toString();
        if (selectId.length()>2){
            selectId = selectId.replaceAll("]", "");
            selectId = selectId.replaceAll("\\[", "");
        }else {
            selectId = "";
        }
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call=apiServices.bussinessList(AppConstant.BASIC_TOKEN,lati,lng,selectId,distance,popularity);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response!=null && response.body()!=null) {
                    if(response.body().businessList!=null) {
                        businessTypeList.clear();
                        businessDetails.clear();
                        businessDetails.addAll(response.body().businessList);
                        if(response.body().businessTypeList!=null){
                            businessTypeList.addAll(response.body().businessTypeList);
                        }
                        else {
                            FilterModel filterModel =new FilterModel();
                            filterModel.businessTypeId="";
                            businessTypeList.add(filterModel);
                        }
                    }
                    else if(response.body().responseCode.equalsIgnoreCase("201"))
                    {
                        businessTypeList.clear();
                        businessDetails.clear();

                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
               t.printStackTrace();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
    public void getCurrrentLatLang(){
        locmanager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        statusOfGPS = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!statusOfGPS) {
            showSettingsAlert();
        } else {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    requestpermisions();
                }
            }else {
                setUpdatedLocation();
            }
            setUpdatedLocation();
        }
    }
    public void requestpermisions() {
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_1);
        setUpdatedLocation();
    }
    private void setUpdatedLocation() {
        LocationTracker locationTracker= new LocationTracker(context,locationResult);
        locationTracker.onUpdateLocation();
    }
    @Override
    public void gotLocation(Location location) {
        lati = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());
           getBusinessListApi();

    }
}
