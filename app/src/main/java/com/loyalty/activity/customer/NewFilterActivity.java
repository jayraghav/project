package com.loyalty.activity.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loyalty.R;
import com.loyalty.adapter.customer.FilterBusinessTypesAdapter;
import com.loyalty.fragment.customer.HomeFragment;
import com.loyalty.interfaces.SelectedInterface;
import com.loyalty.model.customer.FilterBusinessTypesModel;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.FilterModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class NewFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG= NewFilterActivity.class.getSimpleName();
    private TextView tvBusinessType;
    private Context context;
    private List<FilterBusinessTypesModel> models = new ArrayList<>();
    private Toolbar toolbar;
    private TextView btn_apply, tvTitle, toolbarTitleRight;
    private RadioButton rbDistance, rbPopularity;
    private LinearLayout llBusinessType;
    private static boolean check=false;

    private FilterModel filterModel;
    private ProgressDialog progressDialog;
    private List<FilterModel> businessTypeList;
    private List<FilterModel> SelectedbusinessTypeList = new ArrayList<>();;
    private List<FilterModel> orginalBusinessTypeList=new ArrayList<>();
    private boolean reset;
    private static boolean
            distancechecked;
    private static boolean popularitychecked;

    private SelectedInterface selectedInterface;
    private RadioGroup rgRestaurant;
    private static List<String> selectedIds = new ArrayList<>();

    private CheckBox cbBusinessType;

    private Boolean isChecked = false;
    private String distance = "false", popularity = "false";
    private String ResetFilter="false";
    private FilterBusinessTypesAdapter businessTypesAdapter;
    private Dialog mDialog;
    private RecyclerView rvBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_filter);
        context = NewFilterActivity.this;

        businessTypeList = new ArrayList<>();

        findIds();
        setListeners();
        setToolbar();
        if (getIntent() != null) {
            businessTypeList = (List<FilterModel>) getIntent().getSerializableExtra("Detail");
            orginalBusinessTypeList = (List<FilterModel>) getIntent().getSerializableExtra("Detail");

            Log.e("tag", "filter list" + businessTypeList.size());
            if(businessTypeList.size()>0&&businessTypeList!=null) {
                cbBusinessType.setChecked(true);
            }
            else {
                cbBusinessType.setChecked(false);
            }
        }

        rgRestaurant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rbDistance.isChecked()) {
                    distancechecked = true;
                } else {
                    distancechecked = false;
                }
                if (rbPopularity.isChecked()) {
                    popularitychecked = true;
                } else {
                    popularitychecked = false;
                }
            }
        });

        if (CommonUtils.getPreferences(context, AppConstant.STATE).equalsIgnoreCase("check")) {
            rbDistance.setChecked(false);
            rbPopularity.setChecked(true);
        } else if (CommonUtils.getPreferences(context, AppConstant.STATE).equalsIgnoreCase("checkDistance")) {
            rbDistance.setChecked(true);
            rbPopularity.setChecked(false);
        }
        try {
            String businesType = CommonUtils.getPreferences(context, AppConstant.BUSINESSS_SELECTED);
            if (businesType.equalsIgnoreCase("selected")) {
                cbBusinessType.setChecked(true);
            } else if (businesType.equalsIgnoreCase("notSelected")){
                cbBusinessType.setChecked(false);
            }
        }
        catch (Exception e)
        {
        }

        if(check)
        {
            cbBusinessType.setChecked(true);
        }
        else {
            cbBusinessType.setChecked(false);
        }
    }
    private void findIds() {
        tvBusinessType = (TextView) findViewById(R.id.tvBusinessType);
        rgRestaurant = (RadioGroup) findViewById(R.id.rgRestaurant);
        btn_apply = (TextView) findViewById(R.id.btn_apply);
        tvTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitleRight = (TextView) findViewById(R.id.toolbar_title_right);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llBusinessType = (LinearLayout) findViewById(R.id.llBusinessType);
        rbDistance = (RadioButton) findViewById(R.id.rbDistance);
        rbPopularity = (RadioButton) findViewById(R.id.rbPopularity);
        cbBusinessType = (CheckBox) findViewById(R.id.cbBusinessType);
    }
    private void setListeners() {
        tvBusinessType.setOnClickListener(this);
        toolbarTitleRight.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        cbBusinessType.setEnabled(false);
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        tvTitle.setText("Filter");
        toolbarTitleRight.setVisibility(View.VISIBLE);
        toolbarTitleRight.setText("Reset Filter");
    }

    public void setAllfalse() {
        for (int i = 0; i < businessTypeList.size(); i++)
            businessTypeList.get(i).isSelected = false;

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBusinessType:
                getBusinessDialog();
                break;
            case R.id.toolbar_title_right:
                cbBusinessType.setChecked(false);
                rbPopularity.setChecked(false);
                rbDistance.setChecked(true);
                reset=true;
                CommonUtils.savePreferences(context,AppConstant.BUSINESSS_SELECTED,"notSelected");
                     break;
            case R.id.btn_apply:

                Intent data = new Intent();
                String businesslist=CommonUtils.getPreferences(context,AppConstant.BUSINESSS_SELECTED);
                if (distancechecked) {
                    distance = "true";
                    CommonUtils.savePreferencesString(context, AppConstant.STATE,"checkDistance");
                } else {
                    distance = "false";

                    CommonUtils.savePreferencesString(context, AppConstant.STATE,"uncheckDistance");
                }
                if (popularitychecked) {
                    popularity = "true";
                    CommonUtils.savePreferencesString(context, AppConstant.STATE,"check");
                } else {
                    popularity = "false";
                    CommonUtils.savePreferencesString(context, AppConstant.STATE,"uncheck");
                }

                if(reset==true) {
                    reset=false;
                    data.putExtra("distance", distance);
                    data.putExtra("popularity", popularity);
                    data.putExtra("arrayBusinessTypeName", new ArrayList<String>());
                    setResult(RESULT_OK, data);
                    finish();
                    break;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
                }
                else {
                    data.putExtra("distance", distance);
                    data.putExtra("popularity", popularity);
                    data.putExtra("arrayBusinessTypeName", (Serializable) selectedIds);
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                }


        }
    }
    public void getBusinessDialog() {
        final LayoutInflater inflater = LayoutInflater.from(context);
        mDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.30f;
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        mDialog.getWindow().setAttributes(lp);
        final View dialogLayout = inflater.inflate(R.layout.dialog_business_types, null);
        mDialog.setContentView(dialogLayout);
        TextView tvConfirm = (TextView) dialogLayout.findViewById(R.id.tvConfirm);
        TextView tvBusinessTypeName = (TextView) dialogLayout.findViewById(R.id.tvBusinessTypeName);
        TextView tvCancel = (TextView) dialogLayout.findViewById(R.id.tvCancel);
        rvBusiness = (RecyclerView) dialogLayout.findViewById(R.id.rvBusiness);
        rvBusiness.setLayoutManager(new LinearLayoutManager(context));

        for (FilterModel filterModel : businessTypeList) {
            filterModel.isSelected = CommonUtils.getPreferencesBoolean(context, "businessTypeId" + filterModel.businessTypeId);
        }
            businessTypesAdapter = new FilterBusinessTypesAdapter(context, businessTypeList, mDialog, NewFilterActivity.this,reset);
            rvBusiness.setAdapter(businessTypesAdapter);
            cbBusinessType.setChecked(businessTypesAdapter.isAllChecked());

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check=false;
                cbBusinessType.setChecked(businessTypesAdapter.isAllChecked());
                mDialog.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIds = businessTypesAdapter.getSelectedIds();
                Log.e("NewFilterActivity", ":::SELECTED IDS:::" + selectedIds.toString());
                cbBusinessType.setChecked(businessTypesAdapter.isAllChecked());
                boolean businessTypeCheck=businessTypesAdapter.isAllChecked();

                if(businessTypeCheck==true)
                {
                    CommonUtils.savePreferences(context,AppConstant.BUSINESSS_SELECTED,"selected");
                    check=true;
                }
                else {
                    CommonUtils.savePreferences(context,AppConstant.BUSINESSS_SELECTED,"notSelected");
                    check=false;
                }
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }
}

