package com.loyalty.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loyalty.R;

import java.util.List;

/**
 * Created by jayendrapratapsingh on 10/9/16.
 */
public class ShoppingBasketAdapter extends RecyclerView.Adapter<ShoppingBasketAdapter.ViewHolder> {
    private View view;
    private ViewHolder viewHolder;

    private Context context;
    private List<String> commentlist;
    public ShoppingBasketAdapter(Context context)
    {

        this.context=context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyler,parent,false);

        viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position==2)
        {
            holder.tv_kam.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout tv_kam;
        private TextView tv_product;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_product=(TextView)itemView.findViewById(R.id.tv_product);
            tv_kam=(LinearLayout)itemView.findViewById(R.id.tv_kam);


        }
    }
}

