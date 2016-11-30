package com.loyalty.fragment.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.utils.CommonUtils;

/**
 * Created by Shivangi on 24-08-2016.
 */
public class ChangePasswodFragment extends Fragment implements View.OnClickListener {

    private EditText etOldPwd,etNewPwd,etConfirmPwd;
    private Context mContext;
    private View v;
    private Activity activity;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
    private TextView tvTitle,tvUpdate,tvErrorOldPwd,tvErrorNewPwd,tvErrorConfirmPwd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.fragment_change_password, container, false);
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
    }

    private void setFonts()
    {
        etConfirmPwd.setTypeface(CommonUtils.setBook(mContext));
        etNewPwd.setTypeface(CommonUtils.setBook(mContext));
        etOldPwd.setTypeface(CommonUtils.setBook(mContext));
        tvUpdate.setTypeface(CommonUtils.setBook(mContext));
        tvErrorNewPwd.setTypeface(CommonUtils.setBook(mContext));
        tvErrorConfirmPwd.setTypeface(CommonUtils.setBook(mContext));
        tvErrorOldPwd.setTypeface(CommonUtils.setBook(mContext));
    }

    private void findIds()
    {
        tvUpdate=(TextView) v.findViewById(R.id.tvUpdate);
        tvErrorOldPwd=(TextView) v.findViewById(R.id.tvErrorOldPwd);
        tvErrorNewPwd=(TextView) v.findViewById(R.id.tvErrorNewPwd);
        ivToolbarRight1=(ImageView) activity.findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView) activity.findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.GONE);
        ivToolbarRight2.setVisibility(View.GONE);
        tvErrorConfirmPwd=(TextView) v.findViewById(R.id.tvErrorConfirmedPwd);
        etOldPwd=(EditText) v.findViewById(R.id.etOldPwd);
        etNewPwd=(EditText) v.findViewById(R.id.etNewPwd);
        etConfirmPwd=(EditText) v.findViewById(R.id.etConfirmPwd);
        tvTitle=(TextView) activity.findViewById(R.id.toolbar_title);
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setText("Change Password");

    }
    private void setListeners()
    {
        tvUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.tvUpdate) {
        if(checkValidations()) {
            Toast.makeText(getContext(),"Your successfully changed your password.",Toast.LENGTH_SHORT).show();
        }
    }

    }

    private boolean checkValidations()
    {
        if (etOldPwd.getText().toString().trim().length() == 0)
        {
            tvErrorOldPwd.setVisibility(View.VISIBLE);
            tvErrorOldPwd.setText("*Please enter password");
            etOldPwd.requestFocus();
            etOldPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorOldPwd.setVisibility(View.GONE);

                }
            });
            return false;
        }
        else if (etOldPwd.getText().toString().trim().length() < 8) {
            tvErrorOldPwd.setVisibility(View.VISIBLE);
            tvErrorOldPwd.setText("*Old password must contain at least 8 characters");
            etOldPwd.requestFocus();
            etOldPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorOldPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if (etNewPwd.getText().toString().trim().length() == 0)
        {
            tvErrorNewPwd.setVisibility(View.VISIBLE);
            tvErrorNewPwd.setText("*Please enter new password");
            etNewPwd.requestFocus();
            etNewPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorNewPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if (etNewPwd.getText().toString().trim().length() < 8) {
            tvErrorNewPwd.setVisibility(View.VISIBLE);
            tvErrorNewPwd.setText("*New password must contain at least 8 characters");
            etNewPwd.requestFocus();
            etNewPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorNewPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if (etConfirmPwd.getText().toString().trim().length() == 0) {
            tvErrorConfirmPwd.setVisibility(View.VISIBLE);
            tvErrorConfirmPwd.setText("*Please enter new password");
            etConfirmPwd.requestFocus();
            etConfirmPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorConfirmPwd.setVisibility(View.GONE);

                }
            });

            return false;
        } else if(etConfirmPwd.getText().toString().trim().length() < 8||!etConfirmPwd.getText().toString().equalsIgnoreCase(etNewPwd.getText().toString())) {
            tvErrorConfirmPwd.setVisibility(View.VISIBLE);
            tvErrorConfirmPwd.setText("*Password & Confirm password does not match");
            etConfirmPwd.requestFocus();
            etConfirmPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    tvErrorConfirmPwd.setVisibility(View.GONE);

                }
            });

            return false;
        }
        else
        {
            tvErrorConfirmPwd.setVisibility(View.GONE);
            tvErrorNewPwd.setVisibility(View.GONE);
            tvErrorOldPwd.setVisibility(View.GONE);
            return true;
        }
    }
}
