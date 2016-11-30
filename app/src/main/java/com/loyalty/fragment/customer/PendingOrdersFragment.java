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
import com.loyalty.activity.customer.PendingOrderDetailsReceiptActivity;
import com.loyalty.activity.customer.ReceiptActivity;
import com.loyalty.adapter.customer.CustomerPendingOrderAdapter;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.utils.EndlessRecyclerViewScrollListener;
import com.loyalty.webserivcemodel.LoyaltyHistory;
import com.loyalty.webserivcemodel.PendingOrdersList;
import com.loyalty.webserivcemodel.ResponseModel;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class PendingOrdersFragment extends Fragment implements OnHistoryListener {
    private View v;
    private Context mContext;
    private TextView tvTitle,tvNoHistory;
    private Activity activity;
    private RecyclerView rvYourLoyalty;
    private List<PendingOrdersList> OrdersList;
    private CustomerPendingOrderAdapter madapter;
    private ProgressDialog progressDialog;
    private OnHistoryListener onHistoryListener;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
    private int currentPage = 1, limit = 10;
    private LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.your_loyalty_fragment, container, false);
        activity=getActivity();
        OrdersList = new ArrayList<>();
        onHistoryListener=this;
        findIds();
        setListeners();
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvYourLoyalty.setLayoutManager(mLayoutManager);
        madapter=new CustomerPendingOrderAdapter(mContext,OrdersList,onHistoryListener);
        rvYourLoyalty.setAdapter(madapter);
        if (CommonUtils.isOnline(activity)) {
            getPendingOrderApi();
        }else {
            CommonUtils.showToast(activity, AppConstant.PLEASE_CHECK_INTERNET);
        }
        rvYourLoyalty.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (limit >= currentPage) {
                    getPendingOrderApi();
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
    private void findIds() {
        rvYourLoyalty=(RecyclerView) v.findViewById(R.id.rvYourLoyalty);
        tvNoHistory=(TextView) v.findViewById(R.id.tvNoHistory);
        tvTitle=(TextView) activity.findViewById(R.id.toolbar_title);
        tvNoHistory.setTypeface(CommonUtils.setBook(mContext));
        tvTitle.setTypeface(CommonUtils.setBook(mContext));
        tvTitle.setText("Pending Orders");
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
    public void getPendingOrderApi() {
        ApiInterface apiServices = ApiClient.getClient().create(ApiInterface.class);
       Call<ResponseModel> call=apiServices.getPendingOredersApi(AppConstant.BASIC_TOKEN,CommonUtils.getPreferences(mContext, AppConstant.USER_ID),
                                                             String.valueOf(currentPage),String.valueOf(limit));
        Log.e("TAG",new Gson().toJson(call.request().url().toString()));
        startProgressBar(mContext);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stopProgressBar();
                if(response!=null && response.body()!=null) {
                    if(response.body().responseCode.equalsIgnoreCase("200")) {
                        if (response.body().PendingOrdersList != null && response.body().PendingOrdersList.size() > 0) {
//                            CommonUtils.showToast(mContext, response.body().responseMessage);
                            OrdersList.addAll(response.body().PendingOrdersList);
                            tvNoHistory.setVisibility(View.GONE);
                        } else {
                            tvNoHistory.setVisibility(View.VISIBLE);
                            tvNoHistory.setText(mContext.getString(R.string.no_pending_orders));
                        }
                        if(response.body().totalRecords != null){
                            if (!TextUtils.isEmpty(response.body().totalRecords)){
                                tvNoHistory.setVisibility(View.GONE);
                                try {
                                    limit = Integer.parseInt(response.body().totalRecords);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    limit = 1;
                                }
                            }
                            currentPage++;
                        }
                    }
                    madapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                stopProgressBar();
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
    public void onHistoryClickListener(int position) {
        Intent intent=new Intent(mContext, PendingOrderDetailsReceiptActivity.class)
                .putExtra("orderId",OrdersList.get(position).orderId)
                .putExtra("storeId",OrdersList.get(position).storeId)
                .putExtra("businessId",OrdersList.get(position).businessId)
                .putExtra("navigate","navigation");
        startActivity(intent);
    }
}
