package com.loyalty.activity.customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.utils.CommonUtils;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private TextView tvTitle,tvPrivacyPolicy;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getIds();
        setToolbar();
        tvTitle.setTypeface(CommonUtils.setBook(PrivacyPolicyActivity.this));
        tvPrivacyPolicy.setTypeface(CommonUtils.setBook(PrivacyPolicyActivity.this));
    }
    private void getIds() {
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvPrivacyPolicy=(TextView) findViewById(R.id.tvPrivacyPolicy);
    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();                    }
                }

        );
        tvTitle.setText("Privacy Policy");
    }

}
