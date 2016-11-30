package com.loyalty.adapter.customer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.ItemListReceipt;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shivangi on 23-08-2016.
 */
public class ReceiptAdapter  extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder>{
    private Context mContext;
    private List<ItemListReceipt> receiptItemList;
    public ReceiptAdapter(Context context, List<ItemListReceipt> receiptItemList) {
        this.mContext =context;
        this.receiptItemList=receiptItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_rv_food_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptAdapter.ViewHolder holder, int position) {
        holder.tvMealName.setTextColor(mContext.getResources().getColor(R.color.textGreen));
        holder.tvMealPrice.setTextColor(mContext.getResources().getColor(R.color.textGreen));
        holder.tvMealName.setText(receiptItemList.get(position).productName);
        holder.tvMealPrice.setText(receiptItemList.get(position).price);
        holder.tvItemCount.setText(receiptItemList.get(position).itemCount);
        if(receiptItemList.get(position).productImage!=null && receiptItemList.get(position).productImage.length()>=0){
            Picasso.with(mContext).load(receiptItemList.get(position).productImage.trim()).error(R.mipmap.pizza).into(holder.ivItemImage);
        }
        holder.tvMealPrice.setTypeface(CommonUtils.setBook(mContext), Typeface.BOLD);
        holder.tvMealName.setTypeface(CommonUtils.setBook(mContext), Typeface.BOLD);

    }

    @Override
    public int getItemCount()
    {
        return receiptItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llRowFood;
        private TextView tvMealName,tvMealPrice;
        private ImageView ivItemImage;
        private TextView tvItemCount;

        public ViewHolder(View itemView) {
            super(itemView);
            getIds();
            setFonts();
        }

        private void setFonts()
        {
            tvMealName.setTypeface(CommonUtils.setBook(mContext));
            tvMealPrice.setTypeface(CommonUtils.setBook(mContext));
        }

        private void getIds()
        {
            llRowFood=(LinearLayout) itemView.findViewById(R.id.llRowFood);
            tvMealName=(TextView) itemView.findViewById(R.id.tvMealName);
            tvMealPrice=(TextView) itemView.findViewById(R.id.tvMealPrice);
            ivItemImage=(ImageView)itemView.findViewById(R.id.iv_item_image);
            tvItemCount=(TextView)itemView.findViewById(R.id.tvItemCount);

        }

    }

}
