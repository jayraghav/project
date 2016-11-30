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
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.LoyaltyHistory;
import com.loyalty.webserivcemodel.PendingOrdersList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomerPendingOrderAdapter extends RecyclerView.Adapter<CustomerPendingOrderAdapter.ViewHolder> {
    private Context mContext;
    private List<PendingOrdersList> PendingList;
    private OnHistoryListener onHistoryListener;
    public CustomerPendingOrderAdapter(Context context, List<PendingOrdersList> PendingList, OnHistoryListener onHistoryListener) {
        this.mContext=context;
        this.PendingList=PendingList;
        this.onHistoryListener=onHistoryListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_customer_pending_orders, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (PendingList.get(position).businessName!=null){
            holder.tvBussinessName.setText(PendingList.get(position).businessName);
        }
        if (PendingList.get(position).storeName!=null){
            holder.tvStoreName.setText(PendingList.get(position).storeName);
        }
        if (PendingList.get(position).grandTotal!=null){
            holder.tvPrice.setText("€ " + PendingList.get(position).grandTotal);
        }

        String profileImage=PendingList.get(position).businessImage;
        if(profileImage!=null && profileImage.trim().length()!=0) {
            Picasso.with(mContext).load(profileImage.trim()).resize(100,100).error(R.mipmap.gal).into(holder.ivBussinessLogo);
        }
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryListener.onHistoryClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PendingList.size();
       /* if(PendingList!=null)
        {
            return PendingList.size();
        }
        else
        {
            return 0;
        }*/
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBussinessLogo;
        private TextView tvBussinessName,tvPrice,tvStoreName;
        private LinearLayout llParent;
        public ViewHolder(View itemView) {
            super(itemView);
            getIds();
            setFonts();
        }
        private void setFonts() {
            tvBussinessName.setTypeface(CommonUtils.setBook(mContext));
            tvPrice.setTypeface(CommonUtils.setBook(mContext));
            tvStoreName.setTypeface(CommonUtils.setBook(mContext));
        }
        private void getIds() {
            ivBussinessLogo=(ImageView) itemView.findViewById(R.id.ivBussinessLogo);
            tvBussinessName=(TextView) itemView.findViewById(R.id.tvBussinessName);
            tvPrice=(TextView) itemView.findViewById(R.id.tvPrice);
            tvStoreName=(TextView) itemView.findViewById(R.id.tvStoreName);
            llParent=(LinearLayout) itemView.findViewById(R.id.llParent);
        }
    }
}
