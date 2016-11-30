package com.loyalty.adapter.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.LoyaltyHistory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentHistoryAdapter extends RecyclerView.Adapter<RecentHistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<LoyaltyHistory> loyaltyHistoryList;
    private OnHistoryListener onHistoryListener;
    public RecentHistoryAdapter(Context context, List<LoyaltyHistory> loyaltyHistoryList, OnHistoryListener onHistoryListener) {
        this.mContext=context;
        this.loyaltyHistoryList=loyaltyHistoryList;
        this.onHistoryListener=onHistoryListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_your_loyalty, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvBussinessName.setText(loyaltyHistoryList.get(position).businessName);
        if (loyaltyHistoryList.get(position).grandTotal!=null){
            holder.tvMealPrice.setText("RS." + loyaltyHistoryList.get(position).grandTotal);
        }
        String profileImage=loyaltyHistoryList.get(position).businessImage;

        if(profileImage!=null && profileImage.trim().length()!=0) {
            Picasso.with(mContext).load(profileImage.trim()).resize(100,100).error(R.drawable.error).into(holder.ivBussinessLogo);
        }
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryListener.onHistoryClickListener(position);
            }
        });

        if (loyaltyHistoryList.get(position).points!=null){
            if(Integer.parseInt(loyaltyHistoryList.get(position).points)>=5 && Integer.parseInt(loyaltyHistoryList.get(position).points)<=10
                    && Integer.parseInt(loyaltyHistoryList.get(position).points)<=15 && Integer.parseInt(loyaltyHistoryList.get(position).points)<=20){
                holder.ivStar1.setImageResource(R.mipmap.star);
                holder.ivStar2.setImageResource(R.mipmap.star2);
                holder.ivStar3.setImageResource(R.mipmap.star2);
                holder.tvProgress1.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress2.setBackgroundResource(R.drawable.background_edittext);
                holder.tvProgress3.setBackgroundResource(R.drawable.background_edittext);
                holder.tvProgress4.setBackgroundResource(R.drawable.background_edittext);
            }else if(Integer.parseInt(loyaltyHistoryList.get(position).points)>=10 && Integer.parseInt(loyaltyHistoryList.get(position).points)<=15
                    && Integer.parseInt(loyaltyHistoryList.get(position).points)<=20){
                holder.ivStar1.setImageResource(R.mipmap.star);
                holder.ivStar2.setImageResource(R.mipmap.star);
                holder.ivStar3.setImageResource(R.mipmap.star2);
                holder.tvProgress1.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress2.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress3.setBackgroundResource(R.drawable.background_edittext);
                holder.tvProgress4.setBackgroundResource(R.drawable.background_edittext);
            }else if (Integer.parseInt(loyaltyHistoryList.get(position).points)>=15 && Integer.parseInt(loyaltyHistoryList.get(position).points)<=20){
                holder.ivStar1.setImageResource(R.mipmap.star);
                holder.ivStar2.setImageResource(R.mipmap.star);
                holder.ivStar3.setImageResource(R.mipmap.star);
                holder.tvProgress1.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress2.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress3.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress4.setBackgroundResource(R.drawable.background_edittext);
            }else if (Integer.parseInt(loyaltyHistoryList.get(position).points)>=20){
                holder.ivStar1.setImageResource(R.mipmap.star);
                holder.ivStar2.setImageResource(R.mipmap.star);
                holder.ivStar3.setImageResource(R.mipmap.star);
                holder.tvProgress1.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress2.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress3.setBackgroundResource(R.drawable.background_edittext_green);
                holder.tvProgress4.setBackgroundResource(R.drawable.background_edittext_green);
            }else {
                holder.ivStar1.setImageResource(R.mipmap.star2);
                holder.ivStar2.setImageResource(R.mipmap.star2);
                holder.ivStar3.setImageResource(R.mipmap.star2);
                holder.tvProgress1.setBackgroundResource(R.drawable.background_edittext);
                holder.tvProgress2.setBackgroundResource(R.drawable.background_edittext);
                holder.tvProgress3.setBackgroundResource(R.drawable.background_edittext);
                holder.tvProgress4.setBackgroundResource(R.drawable.background_edittext);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(loyaltyHistoryList!=null)
        {
            return loyaltyHistoryList.size();
        }
        else
        {
            return 0;
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBussinessLogo,ivStar1,ivStar2,ivStar3;
        private TextView tvBussinessName,tvMealPrice,tvProgress1,tvProgress2,tvProgress3,tvProgress4;
        private LinearLayout llParent;
        public ViewHolder(View itemView) {
            super(itemView);
            getIds();
            setFonts();
        }
        private void setFonts() {
            tvBussinessName.setTypeface(CommonUtils.setBook(mContext));
            tvMealPrice.setTypeface(CommonUtils.setBook(mContext));
        }
        private void getIds() {
            ivStar1=(ImageView) itemView.findViewById(R.id.ivStar1);
            ivStar2=(ImageView) itemView.findViewById(R.id.ivStar2);
            ivStar3=(ImageView) itemView.findViewById(R.id.ivStar3);
            ivBussinessLogo=(ImageView) itemView.findViewById(R.id.ivBussinessLogo);
            tvBussinessName=(TextView) itemView.findViewById(R.id.tvBussinessName);
            tvMealPrice=(TextView) itemView.findViewById(R.id.tvMealPrice);
            llParent=(LinearLayout)itemView.findViewById(R.id.ll_parent);
            tvProgress1=(TextView) itemView.findViewById(R.id.tvProgress1);
            tvProgress2=(TextView) itemView.findViewById(R.id.tvProgress2);
            tvProgress3=(TextView) itemView.findViewById(R.id.tvProgress3);
            tvProgress4=(TextView) itemView.findViewById(R.id.tvProgress4);

        }
    }
}
