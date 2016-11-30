package com.loyalty.adapter.customer;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.activity.customer.HotDetailActivity;
import com.loyalty.activity.customer.NewStoreActivity;
import com.loyalty.fragment.customer.CatalogueFragment;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.HotDeals;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shivangi on 27-08-2016.
 */
public class HotDealsAdapter extends RecyclerView.Adapter<HotDealsAdapter.ViewHolder> {
    private Context mContext;
    private List<HotDeals> hotDealses;
    private OnHistoryListener onHistoryListener;

    public HotDealsAdapter(Context mContext, List<HotDeals> hotDealses, OnHistoryListener onHistoryListener) {
        this.mContext=mContext;
        this.hotDealses=hotDealses;
        this.onHistoryListener=  onHistoryListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_hot_deals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(hotDealses.get(position).dealDescription!=null && hotDealses.get(position).dealDescription.trim().length()>=0) {
            holder.tvOffer.setText(hotDealses.get(position).dealDescription);
        }
        if(hotDealses.get(position).dealImage!=null && hotDealses.get(position).dealImage.trim().length()>=0) {
            Picasso.with(mContext).load(hotDealses.get(position).dealImage.trim()).error(R.mipmap.gal).into(holder.ivHotDealImage);
        }
        holder.tvRedeem.setText("Add to Bucket");
        holder.tvRedeem.setVisibility(View.GONE);
        holder.tvOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    onHistoryListener.onHistoryClickListener(position);



            }
        });
    }

    @Override
    public int getItemCount() {
        return hotDealses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRedeem;
        private TextView tvOrderNow,tvOffer;
        private ImageView ivHotDealImage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvOffer=(TextView) itemView.findViewById(R.id.tvCafeAssist);
            tvOrderNow=(TextView) itemView.findViewById(R.id.tvOrderNow);
            ivHotDealImage=(ImageView) itemView.findViewById(R.id.ivHotDealImage);
            tvOffer.setTypeface(CommonUtils.setBook(mContext));
            tvOrderNow.setTypeface(CommonUtils.setBook(mContext));
            tvRedeem=(TextView)itemView.findViewById(R.id.tvRedeem);
        }
    }
}
