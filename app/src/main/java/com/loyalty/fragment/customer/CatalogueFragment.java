package com.loyalty.fragment.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.loyalty.R;
import com.loyalty.activity.customer.TakeMeThereActivity;
import com.loyalty.utils.AppConstant;
import com.loyalty.webserivcemodel.BusinessInfo;
import com.loyalty.webserivcemodel.HotDeals;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivangi on 27-08-2016.
 */
public class CatalogueFragment extends Fragment {
    private static final String TAG = CatalogueFragment.class.getSimpleName();
    private Context mContext;
    private View view;
    private Activity activity;
    private ImageView ivStoreImage;
    private TextView  tvItemName,tvTimings,tvTakeMeThere;
    private  String storeId;
    private List<HotDeals> hotDeals;
    private NestedScrollView nsCatalouge;
    private ProgressDialog progressDialog;
    private String latitude,longitude,crntLat,crntLong,businessTypeName,businessId;
    private BusinessInfo businessInfoNew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.new_fragment_catalogue, container, false);
        activity=getActivity();

        Bundle bundle = this.getArguments();
        hotDeals =new ArrayList<>();
        businessTypeName=bundle.getString("businessTypeName");
        latitude=businessInfoNew.latitude;
        longitude=businessInfoNew.longitude;
        crntLat=bundle.getString("crntLat");
        crntLong=bundle.getString("crntLong");
        findIds();

        if(businessTypeName !=null &&businessTypeName.length()>0){
            tvTimings.setText(businessTypeName);
        }
        if(businessInfoNew.storeName !=null &&businessInfoNew.storeName.length()>0){
            tvItemName.setText(businessInfoNew.storeName);
        }
        if(businessInfoNew.businessImage!=null && businessInfoNew.businessImage.length()>=0) {
            Picasso.with(mContext).load(businessInfoNew.businessImage.trim()).error(R.mipmap.gal).into(ivStoreImage);
        }

        tvTakeMeThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, TakeMeThereActivity.class)
                        .putExtra("Latitude",latitude)
                        .putExtra("logi",longitude)
                        .putExtra("crntLat",crntLat).putExtra("crntLng",crntLong);
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;

    }
    private void findIds() {
        tvItemName=(TextView) view.findViewById(R.id.tvItemName);
        tvTimings=(TextView)view.findViewById(R.id.tvTimings);
        tvTakeMeThere=(TextView)view.findViewById(R.id.tvTakeMeThere);
        ivStoreImage=(ImageView)view.findViewById(R.id.iv_storeImage);
        nsCatalouge=(NestedScrollView) view.findViewById(R.id.nsCatalouge);
    }

    public void setBusinessInfoFromActivity(BusinessInfo businessInfoNew){
        this.businessInfoNew=businessInfoNew;
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

