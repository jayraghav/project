package com.loyalty.adapter.customer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.activity.customer.YourLoyaltyActivity;
import com.loyalty.activity.customer.YourLoyaltyDetailActivity;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.LoyaltyHotDealList;
import com.loyalty.webserivcemodel.YourLoyaltyDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shivangi on 23-08-2016.
 */
public class LoyaltyListAdapter extends RecyclerView.Adapter<LoyaltyListAdapter.ViewHolder>{
    private  Context mContext;
    private List<LoyaltyHotDealList> loyaltyHotDealLists;
    private OnHistoryListener onHistoryListener;
    public LoyaltyListAdapter(Context context,List<LoyaltyHotDealList> loyaltyHotDealLists,OnHistoryListener onHistoryListener) {
        this.mContext=context;
        this.loyaltyHotDealLists=loyaltyHotDealLists;
        this.onHistoryListener=onHistoryListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_loyalty_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String dealImage=loyaltyHotDealLists.get(position).dealImage;
        if(dealImage!=null && dealImage.trim().length()!=0) {
            Picasso.with(mContext).load(dealImage.trim()).resize(100, 100).error(R.drawable.error).into(holder.ivprImage);
        }

        if(loyaltyHotDealLists.get(position).isExist.equalsIgnoreCase("1"))
        {
            holder.tvRedeem.setText("Already Redeemed");
        } else
        {
            holder.tvRedeem.setText("Redeem");
        }

        holder.tvDiscount.setText(loyaltyHotDealLists.get(position).dealDesc);
        holder.tvCafeAssistOne.setText(loyaltyHotDealLists.get(position).businessName);

        holder.tvRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryListener.onHistoryClickListener(position);
                holder.tvRedeem.setText("Already Redeemed");
            }
        });
        holder.ivprImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoyaltyHotDealList allLoyaltyDealsDetail = (loyaltyHotDealLists.get(position));
                Intent intent = new Intent(mContext,YourLoyaltyDetailActivity.class);
                intent.putExtra("LoyaltyDetail",allLoyaltyDealsDetail);
                mContext.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return loyaltyHotDealLists.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCafeAssist,tvDiscount,tvRedeem,tvCafeAssistOne;
        private ImageView ivprImage;
        public ViewHolder(View itemView) {
            super(itemView);
            getIds();
            setFonts();
        }
        private void setFonts()
        {
            tvRedeem.setTypeface(CommonUtils.setBook(mContext));
            tvCafeAssist.setTypeface(CommonUtils.setBook(mContext));
            tvCafeAssistOne.setTypeface(CommonUtils.setBook(mContext));
            tvDiscount.setTypeface(CommonUtils.setBook(mContext));
        }
        private void getIds() {
            tvCafeAssist=(TextView) itemView.findViewById(R.id.tvCafeAssist);
            tvCafeAssistOne=(TextView) itemView.findViewById(R.id.tvCafeAssistOne);
            tvDiscount=(TextView) itemView.findViewById(R.id.tvDiscount);
            ivprImage=(ImageView)itemView.findViewById(R.id.iv_prImage);
            tvRedeem=(TextView) itemView.findViewById(R.id.tvRedeem);
            tvRedeem.setVisibility(View.VISIBLE);
        }
    }
}
