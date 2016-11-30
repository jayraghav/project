package com.loyalty.fragment.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.activity.customer.TakeMeThereActivity;
import com.loyalty.adapter.customer.CatalogueAdapter;
import com.loyalty.adapter.customer.HotDealsAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.BusinessInfo;
import com.loyalty.webserivcemodel.HotDeals;
import com.loyalty.webserivcemodel.RequestModel;
import com.loyalty.webserivcemodel.ResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shivangi on 27-08-2016.
 */
public class CheckInFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View view;
    private CatalogueAdapter adapter;
    private HotDealsAdapter adapterHotDeals;
    private RecyclerView rvStoreItems,rvStoreHotDeals;
    private Activity activity;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo,ivShare,ivStoreImage;
    private TextView  tvItemName,tvTimings,tvPhnNumber,tvAddress,tvTakeMeThere,tvDistance;
    private  String storeId;
    private List<HotDeals> hotDeals;
    private String businessId,distance;
    private ShareDialog shareDialog;
    private Context context;
    private NestedScrollView nsCheckIn;
    private String lattitude;
    private String longitude,storeName,startTime,endTime,contactNo,address,businessImage,latitude;
    private String crntLat;
    private String crntLong;
    private BusinessInfo businessInfoNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_check_in, container, false);
        activity=getActivity();

        hotDeals =new ArrayList<>();
        Bundle bundle = this.getArguments();
        crntLat=bundle.getString("crntLat");
        crntLong=bundle.getString("crntLong");
        distance=bundle.getString("distance");
        latitude=businessInfoNew.latitude;
        longitude=businessInfoNew.latitude;

        findIds();
        setListener();

        if(distance !=null &&distance.length()>0){
            tvDistance.setText(distance+" km");
        }

        if(businessInfoNew.storeName !=null &&businessInfoNew.storeName.length()>0) {
            tvItemName.setText(businessInfoNew.storeName);
        }

        if(businessInfoNew.startTime!=null && businessInfoNew.startTime.length()>0 && businessInfoNew.endTime !=null &&businessInfoNew.endTime.length()>0) {
            tvTimings.setText("Timing "+businessInfoNew.startTime+" to "+businessInfoNew.endTime);
        }

        if(businessInfoNew.contactNo!=null && businessInfoNew.contactNo.length()>0) {
            tvPhnNumber.setText(businessInfoNew.contactNo);
        }
        if(businessInfoNew.address!=null && businessInfoNew.address.length()>0) {
            tvAddress.setText(businessInfoNew.address);
        }

        if(businessInfoNew.businessImage!=null && businessInfoNew.businessImage.length()>0) {
            Picasso.with(mContext).load(businessInfoNew.businessImage.trim()).error(R.mipmap.burger).into(ivStoreImage);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void findIds() {
        tvItemName=(TextView) view.findViewById(R.id.tvItemName);
        tvTimings=(TextView)view.findViewById(R.id.tvTimings);
        tvPhnNumber=(TextView)view.findViewById(R.id.tvPhnNumber);
        tvAddress=(TextView)view.findViewById(R.id.tvAddress);
        tvTakeMeThere=(TextView)view.findViewById(R.id.tvTakeMeThere);
        ivShare=(ImageView)view.findViewById(R.id.ivShare);
        tvDistance=(TextView)view.findViewById(R.id.tvDistance);
        ivStoreImage=(ImageView)view.findViewById(R.id.iv_storeImage);
        rvStoreHotDeals=(RecyclerView) view.findViewById(R.id.rvStoreHotDeals);
        nsCheckIn=(NestedScrollView) view.findViewById(R.id.nsCheckIn);
    }
    private void setListener() {
        ivShare.setOnClickListener(this);
        tvTakeMeThere.setOnClickListener(this);
        tvTakeMeThere.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivShare :
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.tvTakeMeThere :
                Intent intent=new Intent(mContext, TakeMeThereActivity.class)
                        .putExtra("Latitude",latitude)
                        .putExtra("logi",longitude)
                        .putExtra("crntLat",crntLat)
                        .putExtra("crntLng",crntLong);
                startActivity(intent);
                break;
        }
    }
    public void setBusinessInfoFromActivity(BusinessInfo businessInfoNew){
        this.businessInfoNew=businessInfoNew;
    }
}
