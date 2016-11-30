package com.loyalty.activity.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.loyalty.webserivcemodel.YourLoyaltyDetails;
import com.squareup.picasso.Picasso;

/**
 * Created by Arati Padhy on 08-11-2016.
 */
public class CatalougeDetailActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle,tvAddtoCart;
    private Toolbar toolbar;
    private ImageView ivHotdealImages,ivToolbarLeft;
    private String TAG = CatalougeDetailActivity.class.getSimpleName();
    private Context mContext;
    private TextView tvDetails;
    private CatalogueProductList allCatalougeItemModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_details);
        mContext = CatalougeDetailActivity.this;

        if(getIntent()!=null){
            allCatalougeItemModel= (CatalogueProductList) getIntent().getSerializableExtra("CatalougeItemDetail");
        }else{
            allCatalougeItemModel=new CatalogueProductList();
        }

        findIds();
        setListeners();

        if (allCatalougeItemModel.description!=null){
            tvDetails.setText(allCatalougeItemModel.description);
        }
        if (allCatalougeItemModel.productName!=null){
            tvTitle.setText(allCatalougeItemModel.productName);
        }
        if(allCatalougeItemModel.productImage!=null && allCatalougeItemModel.productImage.trim().length()>0){
            Picasso.with(mContext).load(allCatalougeItemModel.productImage.trim()).error(R.mipmap.gal).into(ivHotdealImages);
        }
    }

    private void findIds() {
        toolbar=(Toolbar) findViewById(R.id.toolbar) ;
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        ivHotdealImages = (ImageView)findViewById(R.id.ivHotdealImages);
        tvDetails = (TextView)findViewById(R.id.tvDetails);
        tvAddtoCart = (TextView)findViewById(R.id.tvAddtoCart);
        ivToolbarLeft=(ImageView)findViewById(R.id.iv_toolbar_left);
        tvAddtoCart.setVisibility(View.GONE);

    }
    private void setListeners() {
        tvAddtoCart.setOnClickListener(this);
        ivToolbarLeft.setOnClickListener(this);
        ivToolbarLeft.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_toolbar_left:
                finish();
                break;
        }
    }


}
