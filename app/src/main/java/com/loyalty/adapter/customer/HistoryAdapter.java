package com.loyalty.adapter.customer;

import android.content.Context;
import android.graphics.Color;
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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<LoyaltyHistory> loyaltyList;
    private OnHistoryListener onHistoryListener;
    private double loyaltypoint;
    int point;
    public HistoryAdapter(Context context, List<LoyaltyHistory> loyaltyList, OnHistoryListener onHistoryListener) {
        this.mContext=context;
        this.loyaltyList=loyaltyList;
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
        holder.tvBussinessName.setText(loyaltyList.get(position).businessName);

        loyaltypoint=Double.parseDouble(loyaltyList.get(position).points);
        point=(int)loyaltypoint;
        int oneStarPoint=Integer.parseInt(loyaltyList.get(position).oneStarPoint);
        int twostarPoint=Integer.parseInt(loyaltyList.get(position).twoStarPoint);
        int threestarPoint=Integer.parseInt(loyaltyList.get(position).threeStarPoint);
        if (loyaltyList.get(position).grandTotal!=null){
            holder.tvMealPrice.setText("RS." + loyaltyList.get(position).grandTotal);
        }
        String profileImage=loyaltyList.get(position).businessImage;

        if(profileImage!=null && profileImage.trim().length()!=0) {
            Picasso.with(mContext).load(profileImage.trim()).resize(100,100).error(R.mipmap.gal).into(holder.ivBussinessLogo);
        }
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryListener.onHistoryClickListener(position);
            }
        });
        if(point<=oneStarPoint) {
            if (point==oneStarPoint) {
               holder.ivStarOne.setImageResource(R.mipmap.star);
            } else {

                int reatingBarValue = oneStarPoint / 4;
                int result = point / reatingBarValue;
                if (result == 1) {
                    holder.tvProgress1.setBackgroundResource(R.color.color_red);
                } else if (result == 2) {
                    holder.tvProgress1.setBackgroundResource(R.color.color_red);
                    holder.tvProgress2.setBackgroundResource(R.color.color_red);
                } else if (result == 3) {
                    holder.tvProgress1.setBackgroundResource(R.color.color_red);
                    holder.tvProgress2.setBackgroundResource(R.color.color_red);
                    holder.tvProgress3.setBackgroundResource(R.color.color_red);
                } else if (result == 4) {
                    holder.tvProgress1.setBackgroundResource(R.color.color_red);
                    holder.tvProgress2.setBackgroundResource(R.color.color_red);
                    holder.tvProgress3.setBackgroundResource(R.color.color_red);
                    holder.tvProgress4.setBackgroundResource(R.color.color_red);
                    //ivStarOne.setImageResource(R.mipmap.star);
                }
            }
        }
        else if (point>oneStarPoint&&point<=twostarPoint) {
            if (point == twostarPoint) {
                holder.ivStarOne.setImageResource(R.mipmap.star);
                holder.ivStarTwo.setImageResource(R.mipmap.star);
            }
            else {
                {
                    holder.ivStarOne.setImageResource(R.mipmap.star);
                    int ratingBarVAlue = twostarPoint / 4;
                    int result = point/ratingBarVAlue;
                    if (result == 1) {
                        holder.tvProgress1.setBackgroundResource(R.color.black);
                    } else if (result == 2) {
                        holder.tvProgress1.setBackgroundResource(R.color.black);
                        holder.tvProgress2.setBackgroundResource(R.color.black);
                    } else if (result == 3) {
                        holder.tvProgress1.setBackgroundResource(R.color.black);
                        holder.tvProgress2.setBackgroundResource(R.color.black);
                        holder.tvProgress3.setBackgroundResource(R.color.black);
                    } else if (result == 4) {
                        holder.tvProgress1.setBackgroundResource(R.color.black);
                        holder.tvProgress2.setBackgroundResource(R.color.black);
                        holder.tvProgress3.setBackgroundResource(R.color.black);
                        holder.tvProgress4.setBackgroundResource(R.color.black);
                    }
                }
            }
        }
        else if (point>twostarPoint&&point<=threestarPoint) {
            if (point == threestarPoint) {
               holder.ivStarOne.setImageResource(R.mipmap.star);
                holder.ivStarTwo.setImageResource(R.mipmap.star);
                holder.ivStarThree.setImageResource(R.mipmap.star);
            } else {
                holder.ivStarOne.setImageResource(R.mipmap.star);
                holder.ivStarTwo.setImageResource(R.mipmap.star);
                int ratingBarVAlue = threestarPoint / 4;
                int result = point/ratingBarVAlue;
                if (result == 1) {
                    holder.tvProgress1.setBackgroundResource(R.color.lightPink);
                } else if (result == 2) {
                    holder.tvProgress1.setBackgroundResource(R.color.lightPink);
                    holder.tvProgress2.setBackgroundResource(R.color.lightPink);
                } else if (result == 3) {
                    holder.tvProgress1.setBackgroundResource(R.color.lightPink);
                    holder.tvProgress2.setBackgroundResource(R.color.lightPink);
                    holder.tvProgress3.setBackgroundResource(R.color.lightPink);
                } else if (result == 4) {
                    holder.tvProgress1.setBackgroundResource(R.color.lightPink);
                    holder.tvProgress2.setBackgroundResource(R.color.lightPink);
                    holder.tvProgress3.setBackgroundResource(R.color.lightPink);
                    holder.tvProgress4.setBackgroundResource(R.color.lightPink);
                }
            }
        }
        else if (point>threestarPoint)
        {
            holder.ivStarOne.setImageResource(R.mipmap.star);
            holder.ivStarTwo.setImageResource(R.mipmap.star);
            holder.ivStarThree.setImageResource(R.mipmap.star);
            int ratingBarVAlue=threestarPoint/4;
            int result=point/ratingBarVAlue;
            if (result==1)
            {
                holder.tvProgress1.setBackgroundResource(R.color.lightGreen);
            }
            else if(result==2)
            {
                holder.tvProgress1.setBackgroundResource(R.color.lightGreen);
                holder.tvProgress2.setBackgroundResource(R.color.lightGreen);
            }
            else if(result==3)
            {
                holder.tvProgress1.setBackgroundResource(R.color.lightGreen);
                holder.tvProgress2.setBackgroundResource(R.color.lightGreen);
                holder.tvProgress3.setBackgroundResource(R.color.lightGreen);
            }
            else if(result==4) {
                holder.tvProgress1.setBackgroundResource(R.color.lightGreen);
                holder.tvProgress2.setBackgroundResource(R.color.lightGreen);
                holder.tvProgress3.setBackgroundResource(R.color.lightGreen);
                holder.tvProgress4.setBackgroundResource(R.color.lightGreen);
            }
        }
    }
    @Override
    public int getItemCount() {
        if(loyaltyList!=null)
        {
            return loyaltyList.size();
        }
        else
        {
            return 0;
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBussinessLogo,ivStarOne,ivStarTwo,ivStarThree;
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
            ivStarOne=(ImageView) itemView.findViewById(R.id.ivStar1);
            ivStarTwo=(ImageView) itemView.findViewById(R.id.ivStar2);
            ivStarThree=(ImageView) itemView.findViewById(R.id.ivStar3);
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
