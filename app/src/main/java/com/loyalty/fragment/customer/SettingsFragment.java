package com.loyalty.fragment.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;

import com.loyalty.activity.customer.PrivacyPolicyActivity;
import com.loyalty.activity.customer.ProfileActivity;
import com.loyalty.activity.customer.TermsConditionsActivity;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;

/**
 * Created by Shivangi on 24-08-2016.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private View v;
    private Context mContext;
    private Activity activity;
    private ImageView ivToolbarRight1,ivHomeLogo,ivToolbarRight2,ivGoToHistory,ivGoToProfile,ivGoToTerms,ivGoToPrivacyPolicy,ivGoToReferFriend;
    private TextView tvTitle,tvHistory,tvProfile,tvPrivacyPolicy,tvTermsConditions,tvReferFriend;
    private LinearLayout llHistory,llProfile,llPrivacyPolicy,llTermsConditions,tllReferFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_settings, container, false);
        activity=getActivity();

        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findIds();
        setListeners();
        setFonts();

        String userid = CommonUtils.getPreferences(activity, AppConstant.USER_ID);
        if (userid != "" && userid != null) {

            llProfile.setVisibility(View.VISIBLE);
        } else {
            llProfile.setVisibility(View.GONE);
        }





    }

    private void setFonts()
    {
        tvHistory.setTypeface(CommonUtils.setBook(mContext));
        tvProfile.setTypeface(CommonUtils.setBook(mContext));
        tvTermsConditions.setTypeface(CommonUtils.setBook(mContext));
        tvPrivacyPolicy.setTypeface(CommonUtils.setBook(mContext));
        tvReferFriend.setTypeface(CommonUtils.setBook(mContext));
    }

    private void findIds()
    {
        ivGoToHistory=(ImageView) v.findViewById(R.id.ivGoToHistory);
        ivGoToPrivacyPolicy=(ImageView) v.findViewById(R.id.ivGoToPrivacyPolicy);
        ivGoToProfile=(ImageView) v.findViewById(R.id.ivGoToProfile);
        ivGoToReferFriend=(ImageView) v.findViewById(R.id.ivGoToReferFriend);
        ivGoToTerms=(ImageView) v.findViewById(R.id.ivGoToTerms);
        tvHistory=(TextView) v.findViewById(R.id.tvHistory);
        tvProfile=(TextView) v.findViewById(R.id.tvProfile);
        tvTermsConditions=(TextView) v.findViewById(R.id.tvTermsConditions);
        tvPrivacyPolicy=(TextView) v.findViewById(R.id.tvPrivacyPolicy);
        tvReferFriend=(TextView) v.findViewById(R.id.tvReferFriend);
        tvTitle=(TextView) activity.findViewById(R.id.toolbar_title);
        tvTitle.setText("Settings");
        ivToolbarRight1=(ImageView) getActivity().findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView) getActivity().findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setTypeface(CommonUtils.setBook(mContext));
        llHistory=(LinearLayout) v.findViewById(R.id.llGoToHistory);
        llPrivacyPolicy=(LinearLayout) v.findViewById(R.id.llGotOPrivacy);
        llProfile=(LinearLayout) v.findViewById(R.id.llGoToProfile);
        llTermsConditions=(LinearLayout) v.findViewById(R.id.llGoToTerms);
        tllReferFriend=(LinearLayout) v.findViewById(R.id.llReferFriend);

    }
    private void setListeners() {
        llHistory.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        llPrivacyPolicy.setOnClickListener(this);
        tllReferFriend.setOnClickListener(this);
        llTermsConditions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.llGoToHistory:
                CommonUtils.setFragment(new HistoryFragment(),false, (FragmentActivity) mContext,R.id.flHome);
                break;
            case R.id.llGoToProfile:
                Intent intent=new Intent(mContext, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.llGoToTerms: Intent intent1=new Intent(mContext, TermsConditionsActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
            case R.id.llGotOPrivacy: Intent intent2=new Intent(mContext, PrivacyPolicyActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
            case R.id.llReferFriend:
                CommonUtils.setFragment(new ReferFriendFragment(),false, (FragmentActivity) mContext,R.id.flHome);
                break;
            default:
                break;

        }
    }
}
