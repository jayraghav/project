package com.loyalty.adapter.customer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.TotalPrice;
import com.loyalty.activity.customer.CatalogueActivity;
import com.loyalty.activity.customer.CatalougeDetailActivity;
import com.loyalty.activity.customer.YourLoyaltyDetailActivity;
import com.loyalty.interfaces.OnHistoryListener;
import com.loyalty.model.customer.CatalougeModel;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.CatalogueDetailsList;
import com.loyalty.webserivcemodel.CatalogueProductDetails;
import com.loyalty.webserivcemodel.CatalogueProductList;
import com.loyalty.webserivcemodel.YourLoyaltyDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shivangi on 27-08-2016.
 */
public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.ViewHolder> {
    private Context mContext;
    private List<CatalogueProductList> detailsList=new ArrayList<>();
    private List<CatalogueProductList> searchResult=new ArrayList<CatalogueProductList>();
    private OnHistoryListener onHistoryListener;
    private TotalPrice totalPrice;
    private String str=new String();

   /* public CatalogueAdapter(Context mContext, List<CatalogueProductList> detailsList, OnHistoryListener onHistoryListener, TotalPrice totalPrice) {
        this.mContext = mContext;
        this.detailsList = detailsList;
        this.onHistoryListener = onHistoryListener;
        this.totalPrice = totalPrice;
        searchResult=new ArrayList<>();
        searchResult.addAll(detailsList);


    }
*/


    public CatalogueAdapter(Context context, List<CatalogueProductList> detailsList,OnHistoryListener onHistoryListener,  String searchData,
                            TotalPrice totalPrice) {
        this.mContext = context;
        this.detailsList = detailsList;
        this.onHistoryListener = onHistoryListener;
        this.totalPrice = totalPrice;
        this.str=searchData;
        if (str.trim().length()>0)
        {
            Collections.sort(this.detailsList, new Comparator()
            {
                @Override
                public int compare(Object o1, Object o2)
                {
                    CatalogueProductList p1 = (CatalogueProductList) o1;
                    CatalogueProductList p2 = (CatalogueProductList) o2;
                    return p1.productName.compareToIgnoreCase(p2.productName);
                }
            });
            searchData();
        }
        else {
            searchResult=this.detailsList;
        }

    }

    private void searchData() {
        int length = str.length();
        for (int i = 0; i < detailsList.size(); i++) {
            if (length <= detailsList.get(i).productName.length()) {
                String chk = detailsList.get(i).productName.substring(0, length);
                if (str.equalsIgnoreCase(chk)) {
                    searchResult.add(detailsList.get(i));
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_catalogue, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
           if (searchResult != null && searchResult.size() > 0) {
                holder.tvName.setText(searchResult.get(position).productName);
                holder.tvRupee.setText(searchResult.get(position).price);
                holder.tvItemCount.setText(String.valueOf(searchResult.get(position).count));
                if (searchResult.get(position).productImage != null && searchResult.get(position).productImage.trim().length() != 0) {
                    Picasso.with(mContext).load(searchResult.get(position).productImage.trim()).resize(100, 100).error(R.mipmap.gal).into(holder.ivItem);
                }

               holder.tvName.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       CatalogueProductList catalougeItemDetail = (searchResult.get(position));
                       Intent intent = new Intent(mContext,CatalougeDetailActivity.class);
                       intent.putExtra("CatalougeItemDetail",catalougeItemDetail);
                       mContext.startActivity(intent);
                   }
               });

            }
        }
        catch (Exception e)
        {
            Log.e("TAG>>>>",""+e.toString());
        }
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchResult.get(position).count++;
                holder.tvItemCount.setText(String.valueOf(searchResult.get(position).count));
                //int price = Integer.parseInt(holder.tvItemCount.getText().toString()) * Integer.parseInt(detailsList.get(position).price);
                totalPrice.priceChange(0,null);
                try {
                    ((CatalogueActivity) mContext).performDataSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        holder.ivsubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchResult.get(position).count > 0) {
                    searchResult.get(position).count--;
                    holder.tvItemCount.setText(String.valueOf(searchResult.get(position).count));

                    int price = Integer.parseInt(holder.tvItemCount.getText().toString()) * Integer.parseInt(searchResult.get(position).price);
                    totalPrice.priceChange(0,null);
                    try {
                        ((CatalogueActivity) mContext).performDataSet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        int i = Integer.parseInt(holder.tvItemCount.getText().toString());
    }
    @Override
    public int getItemCount() {

       /* if(searchResult !=null && searchResult.size()!=0){
            return searchResult.size();
        }else{*/
        return searchResult.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivsubtract;
        public TextView tvName, tvRupee, tvItemCount;
        public ImageView ivAdd, ivItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            ivAdd = (ImageView) itemView.findViewById(R.id.ivAdd);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvRupee = (TextView) itemView.findViewById(R.id.tvRupee);
            ivsubtract = (ImageView) itemView.findViewById(R.id.ivsubtract);
            tvItemCount = (TextView) itemView.findViewById(R.id.tv_item_count);

        }
    }
   /* public void getFilterList(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        if(detailsList !=null && detailsList.size()>0)
        {
            settingChangesInDetailsList(detailsList);
        }

        if (charText.length() == 0) {
            detailsList.clear();
            detailsList.addAll(searchResult);
        } else {
            detailsList.clear();
            for (int i = 0; i < searchResult.size();i++) {
                if (searchResult.get(i).productName.contains(charText)) {
                    detailsList.add(searchResult.get(i));
                }
            }
            notifyDataSetChanged();
        }

    }

    public void settingChangesInDetailsList(List<CatalogueProductList> detailsList) {

        for(CatalogueProductList list:detailsList){

            for (int i = 0; i < searchResult.size(); i++) {
                if(list.productId.equalsIgnoreCase(searchResult.get(i).productId))
                    if(list.count !=searchResult.get(i).count)
                        searchResult.get(i).count=list.count;

            }
        }
    }
*/}
