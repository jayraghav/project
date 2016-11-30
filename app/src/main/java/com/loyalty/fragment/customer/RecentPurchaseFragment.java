package com.loyalty.fragment.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loyalty.R;

/**
 * Created by Shivangi on 23-08-2016.
 */
public class RecentPurchaseFragment extends Fragment{
private View v;
private ImageView ivConsumer;
    private RecyclerView rvSpendingList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.receipt_fragment, container, false);

        return v;
    }

   private void findId()
   {

   }

}
