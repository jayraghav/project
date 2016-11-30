package com.loyalty.activity.customer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.utils.CommonUtils;

import org.w3c.dom.Text;

public class TermsConditionsActivity extends AppCompatActivity {
    private TextView tvTitle,tvTermsConditions;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        getIds();
        setToolbar();
        tvTitle.setTypeface(CommonUtils.setBook(TermsConditionsActivity.this));
        tvTermsConditions.setTypeface(CommonUtils.setBook(TermsConditionsActivity.this));
    }
    private void getIds() {
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tvTermsConditions=(TextView) findViewById(R.id.tvTermsConditions);
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
        tvTitle.setText("Terms & Conditions");
    }
}
