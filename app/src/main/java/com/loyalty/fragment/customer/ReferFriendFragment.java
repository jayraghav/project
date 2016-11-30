package com.loyalty.fragment.customer;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.adapter.customer.ReceiptAdapter;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;

/**
 * Created by Shivangi on 25-08-2016.
 */
public class ReferFriendFragment extends Fragment implements View.OnClickListener{
    private View v;
    private Context context;
    private Activity activity;
    private TextView tvReferralCode,tvTitle,tvCode,tvInvite;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
    private  String referralId="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_refer_friend, container, false);
        activity=getActivity();

        referralId=CommonUtils.getPreferences(context,AppConstant.REFERENCEID);



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
    }
    private void setListeners()
    {
        tvInvite.setOnClickListener(this);
        if(referralId!=null){
            tvCode.setText(referralId);
        }
    }
    private void findIds() {
        tvInvite=(TextView) v.findViewById(R.id.tvInvite);
        tvCode=(TextView) v.findViewById(R.id.tvCode);
        tvReferralCode=(TextView) v.findViewById(R.id.tvReferral);
        tvTitle=(TextView) activity.findViewById(R.id.toolbar_title);
        tvTitle.setText("Refer a friend");
        tvTitle.setTypeface(CommonUtils.setBook(context));
        tvCode.setTypeface(CommonUtils.setBook(context));
        tvReferralCode.setTypeface(CommonUtils.setBook(context));
        tvInvite.setTypeface(CommonUtils.setBook(context));
        ivToolbarRight1=(ImageView)activity.findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView) activity.findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvInvite:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hello this is the loyalty app and referral ID "+referralId);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Loyalty App");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            default:
                break;
        }
    }
}

