package com.loyalty.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.Listeners.onDeleteListner;
import com.loyalty.R;
import com.loyalty.activity.customer.AddPaymentTwoActivity;
import com.loyalty.adapter.PaymentCardAdapter;
import com.loyalty.adapter.customer.HomeAdapter;
import com.loyalty.model.CardDetail;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.JsonResponseCardDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by keshavkumar on 22/11/16.
 */
public class PaymentFragment extends Fragment implements onDeleteListner, View.OnClickListener {
    private View v;
    private RecyclerView rvPaymentCardList;
    private List<CardDetail> cardDetailList;
    private PaymentCardAdapter paymentCardAdapter;
    private Context context;
    private TextView tvTitle, tvAddCard, tvNoCards;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
    private Activity activity;
    onDeleteListner deleteListner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.fragment_payment, container, false);
        activity = getActivity();
        getIds();
        setListener();
        cardDetailList = new ArrayList<>();
        rvPaymentCardList.setLayoutManager(new LinearLayoutManager(getActivity()));
        paymentCardAdapter=new PaymentCardAdapter(getActivity(),cardDetailList,deleteListner);
        rvPaymentCardList.setAdapter(paymentCardAdapter);
        if(CommonUtils.isNetworkConnected(getActivity())){
            getCardList();
        }else{
            CommonUtils.showAlert(getResources().getString(R.string.connection_error),v,context);
        }
        return v;
    }

    private void setListener() {
        tvAddCard.setOnClickListener(this);
    }

    private void getIds() {
        tvTitle = (TextView) activity.findViewById(R.id.toolbar_title);
        tvNoCards = (TextView) v.findViewById(R.id.tvNoCards);
        ivToolbarRight1 = (ImageView) activity.findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2 = (ImageView) activity.findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);

        tvAddCard = (TextView) v.findViewById(R.id.tvAddCard);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        tvTitle.setText("Payment");
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setTypeface(CommonUtils.setBook(context));
        tvAddCard.setTypeface(CommonUtils.setBook(context));
        rvPaymentCardList = (RecyclerView) v.findViewById(R.id.rvPaymentCardList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        deleteListner=PaymentFragment.this;
    }

    private void getCardList() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "", getResources().getString(R.string.please_wait));
        ApiInterface apiServices= ApiClient.getClient().create(ApiInterface.class);

        final Call<JsonResponseCardDetail> jsonResponseCall=apiServices.getCardDetail(AppConstant.BASIC_TOKEN,CommonUtils.getPreferences(context,AppConstant.USER_ID));
        jsonResponseCall.enqueue(new retrofit2.Callback<JsonResponseCardDetail>() {
            @Override
            public void onResponse(Call<JsonResponseCardDetail> call, Response<JsonResponseCardDetail> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response!= null && response.body()!=null){
                    if(response.body().responseCode.equalsIgnoreCase("200")) {

                        if(response.body().cardList!= null && response.body().cardList.size()>0){
                            cardDetailList.clear();
                            cardDetailList.addAll(response.body().cardList);
                            paymentCardAdapter.notifyDataSetChanged();
                        }else{
                            tvNoCards.setVisibility(View.VISIBLE);
                            tvNoCards.setTypeface(CommonUtils.setBook(context));
                        }

                    }else{
                        CommonUtils.showAlert(response.body().responseMessage,v,context);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponseCardDetail> call, Throwable t) {
                t.printStackTrace();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void ondelete() {
        getCardList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAddCard:
                Intent intent = new Intent(context, AddPaymentTwoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
