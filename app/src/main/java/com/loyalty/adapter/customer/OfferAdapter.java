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
import com.loyalty.model.customer.ShoppingBasketModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayendrapratapsingh on 16/9/16.
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private Context mContext;
    private List<ShoppingBasketModel> list = new ArrayList<>();
    private ShoppingBasketModel shoppingBasketModel;
    private List<String> offer;

    public OfferAdapter(Context context, List<String> offer) {
        this.mContext = context;
        this.list = list;
        this.offer = offer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_offer_basket, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position==0)
        {
            holder.llOffer.setVisibility(View.VISIBLE);
        }
        else {
            holder.llOffer.setVisibility(View.GONE);

        }
        shoppingBasketModel = list.get(position);
        holder.tvProduct.setText(shoppingBasketModel.getProductname());
        holder.tvAmount.setText(shoppingBasketModel.getAmount());
    }

    @Override
    public int getItemCount() {


        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llOffer;
        private TextView tvProduct, tvAmount, tvCount;
        private ImageView ivPlus, ivMinus;
        int count = 10;

        public ViewHolder(View itemView) {
            super(itemView);

            tvProduct = (TextView) itemView.findViewById(R.id.tvProduct);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            ivMinus = (ImageView) itemView.findViewById(R.id.ivMinus);
            ivPlus = (ImageView) itemView.findViewById(R.id.ivPlus);
            llOffer=(LinearLayout)itemView.findViewById(R.id.ll_offer);

            ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count > 0) {
                        count--;
                        tvCount.setText(String.valueOf(count));
                    }

                }
            });
            ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    tvCount.setText(String.valueOf(count));
                }
            });
        }

    }
}