package com.loyalty.fragment.customer;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.adapter.customer.ReceiptAdapter;
import com.loyalty.restrofit.ApiClient;
import com.loyalty.restrofit.ApiInterface;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shivangi on 23-08-2016.
 */
public class ReceiptFragment extends Fragment implements View.OnClickListener {
    private View v;
    private Context context;
    private RecyclerView rvFoodList;
    ReceiptAdapter adapter;
    private TextView tvCheckOut,tvTitle;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
      private Activity activity;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.receipt_fragment, container, false);
        activity=getActivity();
        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findIds();
        setListeners();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    }
    private void setListeners()
    {
        tvCheckOut.setOnClickListener(this);
    }
    private void findIds()
    {
        rvFoodList=(RecyclerView) v.findViewById(R.id.rvFoodLists);
        tvCheckOut=(TextView) v.findViewById(R.id.tvCheckOut);
        tvTitle=(TextView) getActivity().findViewById(R.id.toolbar_title);
        tvTitle.setText("Receipt");
        ivToolbarRight1=(ImageView) activity.findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView)  activity.findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setTypeface(CommonUtils.setBook(context));
        tvCheckOut.setTypeface(CommonUtils.setBook(context));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvCheckOut:

                break;
            default:
                break;
        }
    }


}
