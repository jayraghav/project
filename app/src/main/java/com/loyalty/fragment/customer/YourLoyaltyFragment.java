package com.loyalty.fragment.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loyalty.R;
import com.loyalty.adapter.customer.YourLoyaltyAdapter;
import com.loyalty.interfaces.OnHistoryItemSelectedInterface;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.EndlessRecyclerViewScrollListener;
import com.loyalty.webserivcemodel.LoyaltyHistory;
import com.loyalty.webserivcemodel.ResponseModel;
import com.loyalty.webserivcemodel.YourLoyaltyDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aratipadhy on 13/10/2016.
 */
public class YourLoyaltyFragment extends Fragment implements OnHistoryListener {
    private View v;
    private Context mContext;
    private RecyclerView rvYourLoyalty;
    private List<YourLoyaltyDetails> loyaltyHistoryList;
    private YourLoyaltyAdapter madapter;
    private ProgressDialog progressDialog;
    private OnHistoryListener onHistoryListener;
    private String orderId,businessId;
    private int currentPage = 1, maxPage = 10;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loyaltyHistoryList=new ArrayList<YourLoyaltyDetails>();
        onHistoryListener=this;
        v=inflater.inflate(R.layout.your_loyalty_fragment, container, false);
        mContext=getActivity();
        findIds();
        setListeners();
        orderId=CommonUtils.getPreferences(mContext, AppConstant.ORDER_ID);
        businessId=CommonUtils.getPreferences(mContext, AppConstant.BUSINESS_ID);

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvYourLoyalty.setLayoutManager(mLayoutManager);
        madapter=new YourLoyaltyAdapter(mContext,loyaltyHistoryList,onHistoryListener);
        rvYourLoyalty.setAdapter(madapter);



        if (CommonUtils.isOnline(mContext)) {
            getLoyaltyListApi();
        }else {
            CommonUtils.showToast(mContext, AppConstant.PLEASE_CHECK_INTERNET);
        }


        rvYourLoyalty.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (maxPage >= currentPage) {
                    getLoyaltyListApi();
                }
            }
        });

        return v;
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

    private void findIds()
    {
        rvYourLoyalty=(RecyclerView) v.findViewById(R.id.rvYourLoyalty);
    }
    private void setListeners()
    {
    }
    public void getLoyaltyListApi() {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
        String userId=CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
         Call<ResponseModel> call=apiServices.getYourLoyaltyAPi(AppConstant.BASIC_TOKEN,userId);
//        startProgressBar(mContext);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                stopProgressBar();
                if(response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        loyaltyHistoryList.addAll(response.body().yourLoyaltyDetails);
                    }
                }
                madapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                stopProgressBar();
            }
        });
    }
    public void startProgressBar(Context context){
        progressDialog = ProgressDialog.show(context,null,AppConstant.PLEASE_WAIT,true,false);
    }

    public  void stopProgressBar(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    @Override
    public void onHistoryClickListener(int position) {




    }
}
