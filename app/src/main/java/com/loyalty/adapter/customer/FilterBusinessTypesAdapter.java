package com.loyalty.adapter.customer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loyalty.R;
import com.loyalty.interfaces.SelectedInterface;
import com.loyalty.model.customer.FilterBusinessTypesModel;
import com.loyalty.utils.CommonUtils;
import com.loyalty.webserivcemodel.FilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aratipadhy on 26/9/16.
 */

public class FilterBusinessTypesAdapter extends RecyclerView.Adapter<FilterBusinessTypesAdapter.MyViewHolder> {

    private    List<FilterModel> videosList;
    private List<String> StringvideosList;
    private static Context context;
    private LayoutInflater inflater;
    private Dialog dialog;
    private SelectedInterface selectedInterface;
     private boolean reset;
    private int counter=0;

    public FilterBusinessTypesAdapter(Context context, List<FilterModel> videosList, Dialog dialog, Activity fragment,boolean reset) {
        this.videosList = videosList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.dialog = dialog;
       this.reset=reset;
    }
    public FilterBusinessTypesAdapter(Context context, List<FilterModel> videosList, Dialog dialog, Activity fragment,SelectedInterface selectedInterface) {
        this.videosList = videosList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.dialog = dialog;
        this.selectedInterface=selectedInterface;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_dialog_types, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FilterModel currentItem = videosList.get(position);

        if (reset==true)
        {
            videosList.get(position).isSelected=false;
            holder.cbBusinessName.setChecked(videosList.get(position).isSelected);
            holder.tvBusinessTypeName.setText(videosList.get(position).businessTypeName);
        }
        else {
            holder.cbBusinessName.setChecked(videosList.get(position).isSelected);
            holder.tvBusinessTypeName.setText(videosList.get(position).businessTypeName);
        }

        holder.cbBusinessName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset=false;
               if(counter==0) {
                   counter++;
                   videosList.get(position).isSelected = true;
                   holder.cbBusinessName.setChecked(videosList.get(position).isSelected);
               }
                else {
                   counter=0;
                   videosList.get(position).isSelected = false;
                   holder.cbBusinessName.setChecked(videosList.get(position).isSelected);

               }
                   notifyDataSetChanged();
            }
        });
        holder.llItemCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter=0;
                reset=false;
                videosList.get(position).isSelected=true;
                holder.cbBusinessName.setChecked(true);
                notifyDataSetChanged();
            }
        });

    }

    public boolean isAllChecked(){
        List<String> idsList = new ArrayList<>();
        for (FilterModel filterModel:videosList) {
            if (filterModel.isSelected) {
                idsList.add(filterModel.businessTypeId);
            }else {
                idsList.remove(filterModel.businessTypeId);
            }
        }
        return idsList.size()>0;
    }


    public   void setAllfalse(){
        for(int i=0;i<videosList.size();i++)
        {
            videosList.get(i).isSelected=false;
        }
    }

    public List<String> getSelectedIds(){
        List<String> idsList = new ArrayList<>();
        for (FilterModel filterModel:videosList) {
            if (filterModel.isSelected) {
                CommonUtils.savePreferencesBoolean(context, "businessTypeId"+filterModel.businessTypeId, true);

                idsList.add(filterModel.businessTypeId);
            }else {
                CommonUtils.savePreferencesBoolean(context, "businessTypeId"+filterModel.businessTypeId, false);
                idsList.remove(filterModel.businessTypeId);
            }
        }
        return idsList;
    }
    @Override
    public int getItemCount() {
        return videosList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBusinessTypeName;
        private LinearLayout llItemCheck;
        private CheckBox cbBusinessName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvBusinessTypeName = (TextView) itemView.findViewById(R.id.tvBusinessTypeName);
            llItemCheck = (LinearLayout) itemView.findViewById(R.id.llItemCheck);
            cbBusinessName = (CheckBox) itemView.findViewById(R.id.cbBusinessName);
        }
    }
}
