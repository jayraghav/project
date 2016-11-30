package com.loyalty.fragment.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.activity.customer.ShoppingBasketActivity;
import com.loyalty.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;



public class StoreFragment extends Fragment {

    private ViewPager mViewPager;
    private TextView tvTitle;
    private TabLayout mTab;
    private Context mContext;
    private Activity activity;
    private ImageView ivToolbarRight1,ivToolbarRight2,ivHomeLogo;
    View view;
    public StoreFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_store, container, false);
        activity=getActivity();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getIds();

    }

    private void getIds( )
    {
        mTab=(TabLayout)  view.findViewById(R.id.tabLayout);
        mViewPager=(ViewPager) view.findViewById(R.id.viewpager);
        tvTitle=(TextView) activity.findViewById(R.id.toolbar_title);
        tvTitle.setText("Store");
        ivToolbarRight1=(ImageView) getActivity().findViewById(R.id.iv_toolbar_right1);
        ivToolbarRight2=(ImageView) getActivity().findViewById(R.id.iv_toolbar_right2);
        ivToolbarRight1.setVisibility(View.VISIBLE);
        ivToolbarRight1.setImageResource(R.mipmap.trolly2);
        ivToolbarRight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShoppingBasketActivity.class));
            }
        });
        ivToolbarRight2.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        ivHomeLogo=(ImageView) activity.findViewById(R.id.ivHomeLogo);
        ivHomeLogo.setVisibility(View.GONE);
        tvTitle.setTypeface(CommonUtils.setBook(mContext));
        setupViewPager(mViewPager);
    }
    private void setupViewPager(ViewPager viewPager)
    {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new CatalogueFragment(), getResources().getString(R.string.catalogue));
        adapter.addFragment(new CatalogueFragment(), getResources().getString(R.string.checkin));
        viewPager.setAdapter(adapter);
        mTab.setupWithViewPager(viewPager);
        ViewGroup vg = (ViewGroup) mTab.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(CommonUtils.setBook(mContext));
                }
            }
        }
        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
