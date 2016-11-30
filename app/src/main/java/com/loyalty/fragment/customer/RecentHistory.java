package com.loyalty.fragment.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loyalty.R;
import com.loyalty.activity.customer.ReceiptActivity;
import com.loyalty.adapter.customer.RecentHistoryAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.LoyaltyHistory;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aratipadhy on 13/10/16.
 */
public class RecentHistory extends Fragment  implements OnHistoryListener{
    private View v;
    private Context mContext;
    private RecyclerView rvYourLoyalty;
    private List<LoyaltyHistory> loyaltyHistoryList;
    private RecentHistoryAdapter madapter;
    private ProgressDialog progressDialog;
    private OnHistoryListener onHistoryListener;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.your_loyalty_fragment, container, false);
        activity=getActivity();

        loyaltyHistoryList=new ArrayList<LoyaltyHistory>();
        onHistoryListener=this;
        findIds();
        setListeners();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvYourLoyalty.setLayoutManager(mLayoutManager);
        madapter=new RecentHistoryAdapter(mContext,loyaltyHistoryList,onHistoryListener);
        rvYourLoyalty.setAdapter(madapter);
        if (CommonUtils.isOnline(activity)) {
       }else {
            CommonUtils.showToast(activity, AppConstant.PLEASE_CHECK_INTERNET);
        }

        return v;
    }



    private void findIds()
    {
        rvYourLoyalty=(RecyclerView) v.findViewById(R.id.rvYourLoyalty);
    }
    private void setListeners()
    {
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
    public void onHistoryClickListener(int position) {
        Intent intent=new Intent(mContext, ReceiptActivity.class).putExtra("orderId",loyaltyHistoryList.get(position).orderId);
        startActivity(intent);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext =context;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}


