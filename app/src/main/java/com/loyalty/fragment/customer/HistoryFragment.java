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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loyalty.R;
import com.loyalty.activity.customer.ReceiptActivity;
import com.loyalty.adapter.customer.HistoryAdapter;
import com.loyalty.adapter.customer.YourLoyaltyAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.EndlessRecyclerViewScrollListener;
import com.loyalty.webserivcemodel.LoyaltyHistory;
import com.loyalty.webserivcemodel.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements OnHistoryListener {
    private View v;
    private Context mContext;
    private TextView tvTitle,tvNoHistory;
    private Activity activity;
    private RecyclerView rvYourLoyalty;
    private List<LoyaltyHistory> loyaltyHistoryList;
    private HistoryAdapter madapter;
    private OnHistoryListener onHistoryListener;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
    private String orderId,businessId;
    private int currentPage = 1, maxPage = 10;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.your_loyalty_fragment, container, false);
        activity=getActivity();

        loyaltyHistoryList=new ArrayList<LoyaltyHistory>();
        onHistoryListener=this;
        orderId=CommonUtils.getPreferences(mContext, AppConstant.ORDER_ID);
        businessId=CommonUtils.getPreferences(mContext, AppConstant.BUSINESS_ID);
        findIds();
        setListeners();

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvYourLoyalty.setLayoutManager(mLayoutManager);
        madapter=new HistoryAdapter(mContext,loyaltyHistoryList,onHistoryListener);
        rvYourLoyalty.setAdapter(madapter);

        if (CommonUtils.isOnline(activity)) {
            getHistoryApi();
        }else {
            CommonUtils.showToast(activity, AppConstant.PLEASE_CHECK_INTERNET);
        }

        rvYourLoyalty.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (maxPage >= currentPage) {
                    getHistoryApi();
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
        tvNoHistory=(TextView) v.findViewById(R.id.tvNoHistory);
        tvTitle=(TextView) activity.findViewById(R.id.toolbar_title);
        tvNoHistory.setTypeface(CommonUtils.setBook(mContext));
        tvTitle.setTypeface(CommonUtils.setBook(mContext));
        tvTitle.setText("History");
        tvTitle.setVisibility(View.VISIBLE);
        ivToolbarRight1=(ImageView)activity.findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView) activity.findViewById(R.id.iv_toolbar_right2);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        ivHomeLogo.setVisibility(View.GONE);
    }
    private void setListeners() {
    }
    public void getHistoryApi() {
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", getResources().getString(R.string.please_wait));

        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
//        Call<ResponseModel> call=apiServices.historyList(1);
       Call<ResponseModel> call=apiServices.historyList(AppConstant.BASIC_TOKEN,Integer.parseInt(CommonUtils.getPreferences(mContext, AppConstant.USER_ID)),
                                                             String.valueOf(currentPage),String.valueOf(maxPage));
        Log.e("TAG",new Gson().toJson(call.request().url().toString()));
//        startProgressBar(mContext);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        if (response.body().loyaltyHistory != null && response.body().loyaltyHistory.size() > 0) {
                            loyaltyHistoryList.addAll(response.body().loyaltyHistory);
                            madapter.notifyDataSetChanged();
                            tvNoHistory.setVisibility(View.GONE);
                        }else{
                            tvNoHistory.setVisibility(View.VISIBLE);
                        }

                        if(response.body().totalRecords != null){
                            if (!TextUtils.isEmpty(response.body().totalRecords)){
                                tvNoHistory.setVisibility(View.GONE);
                                try {
                                    maxPage = Integer.parseInt(response.body().totalRecords);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    maxPage = 1;
                                }
                            }
                            currentPage++;
                        }
                    }
                }
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

    @Override
    public void onHistoryClickListener(int position) {
        Intent intent=new Intent(mContext, ReceiptActivity.class).putExtra("orderId",loyaltyHistoryList.get(position).orderId);
        startActivity(intent);
    }
}
