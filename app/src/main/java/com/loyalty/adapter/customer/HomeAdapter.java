package com.loyalty.adapter.customer;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.Listeners.OnBusinessClickListener;
import com.loyalty.R;
import com.loyalty.fragment.customer.CatalogueFragment;
import com.loyalty.fragment.customer.StoreFragment;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.GetBusineesDetailsResponse;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Shivangi on 25-08-2016.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>
{
    private Context mContext;
    private OnBusinessClickListener onBusinessClickListener;
    private List<GetBusineesDetailsResponse> businessDetails;
    private boolean isSelected;

    public HomeAdapter(Context context,List<GetBusineesDetailsResponse> businessDetails,OnBusinessClickListener onBusinessClickListener, boolean isSelected) {
        this.mContext=context;
        this.businessDetails=businessDetails;
        this.onBusinessClickListener=onBusinessClickListener;
        this.isSelected=isSelected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        if(isSelected){
            //For right side radio button
            if(businessDetails.get(position).businessTypeName!=null){
                holder.tvBuinessType.setText(businessDetails.get(position).productName);
                CommonUtils.savePreferencesString(mContext, AppConstant.BUSINESSTYPE_NAME,businessDetails.get(position).businessTypeName);
            }
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvPrice.setText("€ "+businessDetails.get(position).price);
            String profileImage=businessDetails.get(position).productImage;

            if(profileImage!=null && profileImage.trim().length()!=0) {
                Picasso.with(mContext).load(profileImage.trim()).resize(100,100).error(R.mipmap.gal).into(holder.ivBussinessLogo);

            }
        }else{
            //For left side radio button
            if(businessDetails.get(position).businessTypeName!=null){
                holder.tvBuinessType.setText(businessDetails.get(position).businessTypeName);
                CommonUtils.savePreferencesString(mContext, AppConstant.BUSINESSTYPE_NAME,businessDetails.get(position).businessTypeName);
            }
            String profileImage=businessDetails.get(position).businessImage;

            if(profileImage!=null && profileImage.trim().length()!=0) {
                Picasso.with(mContext).load(profileImage.trim()).resize(100,100).error(R.mipmap.gal).into(holder.ivBussinessLogo);

            }
        }
        holder.tvBusinessName.setText(businessDetails.get(position).storeName);

       // holder.tvBusinessName.setText(businessDetails.get(position).storeName);

        String value = String.valueOf(businessDetails.get(position).distance);
        businessDetails.get(position).distance = formatFigureTwoPlaces(Float.parseFloat(value));
        if(value.contains(".")) {
            holder.tvDistance.setText(value+ "m");
        }
        else
        {
            holder.tvDistance.setText(value+ "km");
        }

        holder.tvTakeMeThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBusinessClickListener.ontakeMeThereListener(position);
            }
        });
        holder.llRowHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBusinessClickListener.onBusinessClickistener(position);

            }
        });
    }

    public String formatFigureTwoPlaces(float value) {
        DecimalFormat myFormatter = new DecimalFormat("##0.00");
        return myFormatter.format(value);
    }

    @Override
    public int getItemCount() {


        return businessDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvBusinessName,tvBuinessType,tvDistance,tvTakeMeThere,tvPrice;
        LinearLayout llRowHome;
        private ImageView ivBussinessLogo;
        public ViewHolder(View itemView)
        {
            super(itemView);
            getIds();
            setFonts();
        }

        private void setFonts()
        {
            tvBusinessName.setTypeface(CommonUtils.setBook(mContext));
            tvBuinessType.setTypeface(CommonUtils.setBook(mContext));
            tvDistance.setTypeface(CommonUtils.setBook(mContext));
            tvTakeMeThere .setTypeface(CommonUtils.setBook(mContext));
            tvPrice .setTypeface(CommonUtils.setBook(mContext));
        }

        private void getIds()
        {
            llRowHome=(LinearLayout) itemView.findViewById(R.id.llRowHome);
            tvBusinessName=(TextView)itemView.findViewById(R.id.tvBussinessName);
            tvBuinessType=(TextView)itemView.findViewById(R.id.tvBussinessType);
            tvPrice=(TextView)itemView.findViewById(R.id.tvPrice);
            tvDistance=(TextView)itemView.findViewById(R.id.tvDistance);
            tvTakeMeThere=(TextView)itemView.findViewById(R.id.tvTakeMeThere);
            ivBussinessLogo=(ImageView)itemView.findViewById(R.id.ivBussinessLogo);
        }

    }
}


