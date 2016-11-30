package com.loyalty.adapter.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.TotalPrice;
import com.loyalty.activity.customer.ShoppingBasketActivity;
import com.loyalty.interfaces.RemoveDealListener;
import com.loyalty.webserivcemodel.CatalogueDetailsList;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by anjalipandey on 27/8/16.
 */
public class ShoppingBasketAdapter extends RecyclerView.Adapter<ShoppingBasketAdapter.ViewHolder> {
    private  Context mContext;
    private List<CatalogueProductList> productlist;
    private List<CatalogueDetailsList> dealist;
    private RemoveDealListener removeDealListener;
    private TotalPrice totalPrice;

    public ShoppingBasketAdapter(Context context, List<CatalogueProductList> productlist,List<CatalogueDetailsList> dealist,RemoveDealListener removeDealListener, TotalPrice totalPrice ) {
        this.mContext=context;
        this.productlist=productlist;
        this.dealist=dealist;
        this.removeDealListener=removeDealListener;
        this.totalPrice=totalPrice;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_shopping_basket, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.ll_Main.setVisibility(View.VISIBLE);
        holder.llOffer.setVisibility(View.GONE);

        if(position<productlist.size()) {
            try {
                holder.ivMinus.setVisibility(View.VISIBLE);
                holder.ivPlus.setVisibility(View.VISIBLE);
                holder.ivRemovedeal.setVisibility(View.GONE);
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.tvAmount.setVisibility(View.VISIBLE);

                holder.ll_incrementanddecrement.setVisibility(View.VISIBLE);
                holder.tvProduct.setText(productlist.get(position).productName);
                holder.tvAmount.setText(productlist.get(position).price);
                holder.tvCount.setText(productlist.get(position).itemCount);
                String productImage = productlist.get(position).productImage;
                if (productImage != null && productImage.trim().length() != 0)
                {
                    Picasso.with(mContext).load(productImage.trim()).resize(100, 100).error(R.mipmap.gal).into(holder.ivItemImage);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(position>productlist.size()) {
            holder.tvProduct.setText(dealist.get(position-(productlist.size()+1)).dealDesc);
            holder.ivMinus.setVisibility(View.GONE);
            holder.ivPlus.setVisibility(View.GONE);
            holder.tvCount.setVisibility(View.GONE);
            holder.tvAmount.setVisibility(View.GONE);
            // holder.tvAmount.setText(dealist.get(position).dealDesc);
            holder.ivRemovedeal.setVisibility(View.VISIBLE);
            String dealImage =dealist.get(position-(productlist.size()+1)).dealImage;
            if (dealImage != null && dealImage.trim().length() != 0) {
                Picasso.with(mContext).load(dealImage.trim()).resize(100, 100).error(R.drawable.error).into(holder.ivItemImage);
            }
        }
        if (position==productlist.size()){
            holder.llOffer.setVisibility(View.VISIBLE);
            holder.ll_Main.setVisibility(View.GONE);
        }
        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(position<productlist.size()) {
                        int c=Integer.parseInt(productlist.get(position).itemCount);
                        c=c+1;
                        productlist.get(position).itemCount=c+"";
                        holder.tvCount.setText(String.valueOf(productlist.get(position).count));
                        holder.tvCount.setText(""+c);
                        double price = Double.parseDouble(productlist.get(position).price);


                        notifyDataSetChanged();

                        totalPrice.priceChange(price,"Add");

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(position<productlist.size()) {

                       double price=0.0;
                        int c = Integer.parseInt(productlist.get(position).itemCount);
                        if (c<=1) {
                             price = Double.parseDouble(productlist.get(position).price);

                            productlist.remove(productlist.get(position));
                            notifyDataSetChanged();
                        }
                        else if(c>0){
                            c = c - 1;
                            productlist.get(position).itemCount=c+"";
                             price = Double.parseDouble(productlist.get(position).price);
                            notifyDataSetChanged();

                        }

                        totalPrice.priceChange(price,"Sub");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        holder.ivRemovedeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // removeDealListener.removeDeals(position-productlist.size());

                dealist.remove(dealist.get(position-(productlist.size()+1)));
                notifyDataSetChanged();
                ((ShoppingBasketActivity)mContext).performDataDealsSet(dealist);
            }
        });

        if(dealist.size()==0)
        {
            holder.llOffer.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return productlist.size()+dealist.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivItemImage;
        private final ImageView ivRemovedeal;
        private final LinearLayout ll_incrementanddecrement;
        private LinearLayout llOffer,ll_Main;
        private TextView tvProduct, tvAmount, tvCount;
        private ImageView ivPlus, ivMinus;
        public ViewHolder(View itemView) {
            super(itemView);
            tvProduct = (TextView) itemView.findViewById(R.id.tvProduct);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            ivMinus = (ImageView) itemView.findViewById(R.id.ivMinus);
            llOffer = (LinearLayout) itemView.findViewById(R.id.ll_offer);
            ivPlus = (ImageView) itemView.findViewById(R.id.ivPlus);
            ivItemImage=(ImageView)itemView.findViewById(R.id.iv_item_image);
            ivRemovedeal=(ImageView)itemView.findViewById(R.id.iv_removedeal);
            ll_incrementanddecrement=(LinearLayout)itemView.findViewById(R.id.ll_incrementanddecrement);
            ll_Main=(LinearLayout)itemView.findViewById(R.id.ll_Main);
        }
    }
}