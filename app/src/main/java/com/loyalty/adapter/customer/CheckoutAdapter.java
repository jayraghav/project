package com.loyalty.adapter.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.webserivcemodel.ItemListReceipt;

import java.util.List;

/**
 * Created by jayendrapratapsingh on 9/9/16.
 */
public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    private Context mContext;
    private List<ItemListReceipt> receipt;

    public CheckoutAdapter(Context mContext, List<ItemListReceipt> receipt) {
        this.mContext=mContext;
        this.receipt=receipt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_catalogue_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCost.setText(receipt.get(position).price);
        holder.tvMealName.setText(receipt.get(position).productName);
    }

    @Override
    public int getItemCount() {
        return receipt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       private ImageView ivUser;
        private TextView tvMealName,tvCost;
        public ViewHolder(View itemView) {
            super(itemView);
            ivUser=(ImageView)itemView.findViewById(R.id.iv_meal_image);
            tvCost=(TextView)itemView.findViewById(R.id.tv_meal_cost);
            tvMealName=(TextView)itemView.findViewById(R.id.tv_meal_name);

        }
    }
}
