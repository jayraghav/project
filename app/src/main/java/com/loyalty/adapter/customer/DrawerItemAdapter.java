package com.loyalty.adapter.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.activity.customer.HomeActivity;
import com.loyalty.model.customer.DrawerItemModel;
import com.loyalty.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivangi on 25-08-2016.
 */
public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.ViewHolder>  {
    private Context mContext;
    private List<DrawerItemModel> drawerList=new ArrayList<>();
    public DrawerItemAdapter(List<DrawerItemModel> arrayList, HomeActivity context) {
        this.drawerList=arrayList;
        this.mContext=context;
    }

    @Override
    public DrawerItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View view = layoutInflater.inflate(R.layout.row_drawer, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(DrawerItemAdapter.ViewHolder holder, int position)
    {
        holder.tvDrawerItem.setText(drawerList.get(position).getItem());

    }

    @Override
    public int getItemCount() {
        return drawerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDrawerItem;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDrawerItem=(TextView) itemView.findViewById(R.id.tvDrawerItem);
            tvDrawerItem.setTypeface(CommonUtils.setBook(mContext));
        }
    }
}
