package com.loyalty.adapter.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.activity.customer.HotDetailActivity;
import com.loyalty.activity.customer.WhatsHotActivity;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.AllHotDeals;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shivangi on 23-08-2016.
 */
public class WhatsHotAdapter  extends RecyclerView.Adapter<WhatsHotAdapter.ViewHolder> {

    private Context mContext;
    private List<AllHotDeals> allHotDealsList=new ArrayList<>();
    private ViewHolder viewHolder;
    private List<AllHotDeals> list=new ArrayList<>();
    private OnHistoryListener onHistoryListener;
    private AllHotDeals allHotDeals;
    private boolean firstTime=true;
    private String searchCharacter=new String();
    private String status;

    public WhatsHotAdapter(Context context, List<AllHotDeals> list,OnHistoryListener onHistoryListener,String searchData) {
        this.mContext = context;
        this.list = list;
        this.onHistoryListener = onHistoryListener;
        this.searchCharacter = searchData;
        if (searchCharacter.trim().length()>0&&searchData!="")
        {
            Collections.sort(this.list, new Comparator()
            {
                @Override
                public int compare(Object o1, Object o2)
                {
                    AllHotDeals p1 = (AllHotDeals) o1;
                    AllHotDeals p2 = (AllHotDeals) o2;
                    return p1.productName.compareToIgnoreCase(p2.productName);
                }
            });
            searchData();
        }
        else {
            allHotDealsList=this.list;
        }
    }
        private void searchData() {
            int length = searchCharacter.length();
            for (int i = 0; i < list.size(); i++) {
                if (length <= list.get(i).productName.length()) {
                    String chk = list.get(i).productName.substring(0, length);
                    if (searchCharacter.equalsIgnoreCase(chk)) {
                        allHotDealsList.add(list.get(i));
                    }
                }
            }
        }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_whats_hot, parent, false);
        viewHolder=new ViewHolder(view);
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvCafeAssist.setText(allHotDealsList.get(position).productName);
        holder.tvCafeAssistOne.setText(allHotDealsList.get(position).businessName);
        holder.tvDiscount.setText(allHotDealsList.get(position).dealDesc);
        if (allHotDealsList.get(position).dealImage != null && allHotDealsList.get(position).dealImage.length() >= 0) {
            Picasso.with(mContext).load(allHotDealsList.get(position).dealImage.trim()).error(R.mipmap.pizza).into(holder.ivHotdealImages);
        }

        holder.ivHotdealImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllHotDeals allDealsDetail = (allHotDealsList.get(position));
                Intent intent = new Intent(mContext, HotDetailActivity.class);
                intent.putExtra("Detail", allDealsDetail);
                mContext.startActivity(intent);
            }
        });
        holder.tvCafeAssist.setTypeface(CommonUtils.setBook(mContext));
        holder.tvCafeAssistOne.setTypeface(CommonUtils.setBook(mContext));
        holder.tvDiscount.setTypeface(CommonUtils.setBook(mContext));

        if (allHotDealsList.get(position).isExist.equalsIgnoreCase("1")) {
            holder.tvAddtoCart.setText("Already added");
// By Abhinav
           /* AllHotDeals allDealsDetail = (allHotDealsList.get(position));
            Intent intent = new Intent(mContext,HotDetailActivity.class);
            intent.putExtra("Detail",allDealsDetail);
            mContext.startActivity(intent);*/
        } else if (allHotDealsList.get(position).isExist.equalsIgnoreCase("0")) {
            holder.tvAddtoCart.setText("Add to Basket");
        }


        holder.tvAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allHotDealsList.get(position).isExist.equalsIgnoreCase("0")) {
                    String userid = CommonUtils.getPreferences(mContext, AppConstant.USER_ID);
                    if (userid != "" && userid != null) {
                        viewHolder.fmHotDealRow.setVisibility(View.VISIBLE);
                        onHistoryListener.onHistoryClickListener(position);
                        holder.tvAddtoCart.setText("Already added");
                    } else {
                        CommonUtils.showAlertLogin("Only registered user can add to basket. " + "Please login. ", (Activity) mContext);
                    }
                }
                else {
                    CommonUtils.showToast(mContext,"Offer had been already added to your basket.Please check your basket.");
                }
            }
        });



        holder.tvAddtoCart.setVisibility(View.VISIBLE);
       /* if (allHotDealsList!=null && allHotDealsList.size()>0) {
            holder.fmHotDealRow.setVisibility(View.VISIBLE);
            holder. tvNoDealFound.setVisibility(View.GONE);
        }else {
            holder.fmHotDealRow.setVisibility(View.GONE);
            holder.tvNoDealFound.setVisibility(View.VISIBLE);
        }*/
    }
    @Override
    public int getItemCount() {

            return allHotDealsList.size();

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCafeAssist, tvDiscount, tvCost,tvAddtoCart,tvNoDealFound,tvCafeAssistOne;
        private ImageView ivHotdealImages;
        private FrameLayout fmHotDealRow;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCafeAssist = (TextView) itemView.findViewById(R.id.tvCafeAssist);
            tvCafeAssistOne = (TextView) itemView.findViewById(R.id.tvCafeAssistOne);
            tvDiscount = (TextView) itemView.findViewById(R.id.tvDiscount);
            tvCost = (TextView) itemView.findViewById(R.id.tv_cost);
            tvAddtoCart=(TextView)itemView.findViewById(R.id.tvRedeem);
            tvNoDealFound=(TextView)itemView.findViewById(R.id.tvNoDealFound);
            ivHotdealImages=(ImageView)itemView.findViewById(R.id.ivHotdealImages);
            fmHotDealRow=(FrameLayout) itemView.findViewById(R.id.fmHotDealRow);
        }
    }

}
