/*
package com.loyalty.activity.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itsonapp.itsonapp.R;
import com.itsonevents.constant.AppConstants;
import com.itsonevents.fragment.CategoryFilterPage1Fragment;
import com.itsonevents.model.IntersetFilterModel;
import com.itsonevents.service.RequestModel;
import com.itsonevents.service.RequestURL;
import com.itsonevents.service.ResponseModel;
import com.itsonevents.service.ServiceAsync;
import com.itsonevents.service.ServiceStatus;
import com.itsonevents.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilterActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView ivBack, iv_search;
    private TextView tv_step, tvHeaderGreen;
    final Handler handler = new Handler();
    private int i=0;
    private String[] radius={"5 km","10 km","15 km","20 km","50 km","100 km"};
    private Context context;

    private CategoryFilterPage1Fragment oneCategoryFilterPage1Fragment,twoCategoryFilterPage1Fragment,threeCategoryFilterPage1Fragment;

    private List<IntersetFilterModel>  oneIntersetFilterList ;
    private List<IntersetFilterModel>  twoIntersetFilterList ;
    private List<IntersetFilterModel>  threeIntersetFilterList ;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);
        context=CategoryFilterActivity.this;

        oneCategoryFilterPage1Fragment=new CategoryFilterPage1Fragment();
        twoCategoryFilterPage1Fragment=new CategoryFilterPage1Fragment();
        threeCategoryFilterPage1Fragment=new CategoryFilterPage1Fragment();

        getIds();
        setListners();

        if(CommonUtils.isOnline(context)) {
            callGetCategoryListApi();
        }
        else {
             */
/*CommonUtils.showSnackBar(tvHeaderGreen,context, AppConstants.INTERNET_NOT_AVAILABLE);*//*


            String response=CommonUtils.getPreferencesString(context,CommonUtils.getPreferencesString(context, AppConstants.USER_ID)+""+AppConstants.CATEGORY_FILTER_API);

            CommonUtils.showLog(context,"\n \n \n \n getting response from ( category filter )  save preference  : "+response+" \n \n \n \n");

            if(response!=null && response.trim().length()!=0){

                try {
                    if (response != null) {
                        Gson gson = new Gson();
                        final ResponseModel responseModel= gson.fromJson(response, ResponseModel.class);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setDataFromApi(responseModel);
                            }
                        },1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                CommonUtils.showLog(context,"No response from save preference");
            }
        }
    }

    private void setListners() {
        ivBack.setOnClickListener(this);
    }

    private void getIds() {
        viewPager = (ViewPager)findViewById(R.id.v_pager);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager.setOffscreenPageLimit(3);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        tvHeaderGreen=(TextView)findViewById(R.id.tvHeaderGreen);
        tv_step=(TextView)findViewById(R.id.tvStep);
        iv_search=(ImageView) findViewById(R.id.iv_search);
        iv_search.setVisibility(View.INVISIBLE);
        setupViewPager(viewPager);
        tv_step.setVisibility(View.GONE);
        tvHeaderGreen.setText("Category event list");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(oneCategoryFilterPage1Fragment, "Page 1");
        adapter.addFragment(twoCategoryFilterPage1Fragment, "Page 2");
        adapter.addFragment(threeCategoryFilterPage1Fragment, "Page 3");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
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


    private void callGetCategoryListApi() {
        String url = RequestURL.URL_CATEGORY_LIST;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty((RequestModel.USER_ID), CommonUtils.getPreferencesString(context, AppConstants.USER_ID)*/
/*"21"*//*
);

        CommonUtils.showLog(context," **req** " + new Gson().toJson(jsonObject));
        ServiceAsync serviceAsync = new ServiceAsync(context, jsonObject.toString(), url, RequestURL.POST
                , new ServiceStatus()
        {
            ResponseModel servicesResponse;

            @Override
            public void onSuccess(Object o) {
                servicesResponse = (ResponseModel) o;

                try {
                    setDataFromApi(servicesResponse);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(Object o) {
                ResponseModel servicesResponse = (ResponseModel) o;
                if(servicesResponse.responseCode.equalsIgnoreCase("400")) {
                    intent=new Intent( context, RegistrationActivity.class);
                    startActivity(intent);

                }
                else {
                    // CommonUtils.showSnackBar(tvHeaderGreen,context, servicesResponse.responseMessage);
                }
            }
        });

        serviceAsync.execute("");
    }

    private void setDataFromApi(ResponseModel servicesResponse){

        CommonUtils.savePreferencesString(context,CommonUtils.getPreferencesString(context,AppConstants.USER_ID)+""+AppConstants.CATEGORY_FILTER_API,new Gson().toJson(servicesResponse).toString());
        CommonUtils.showLog(context,"\n \n response : -->"+new Gson().toJson(servicesResponse).toString()+" \n \n");

        String response=CommonUtils.getPreferencesString(context,CommonUtils.getPreferencesString(context,AppConstants.USER_ID)+""+AppConstants.CATEGORY_FILTER_API);

        if (servicesResponse.responseCode.equalsIgnoreCase("200"))
        {
            if(servicesResponse.category_list!=null) {


                oneIntersetFilterList=new ArrayList<>();
                twoIntersetFilterList=new ArrayList<>();
                threeIntersetFilterList=new ArrayList<>();

                for (int i = 0; i < servicesResponse.category_list.size(); i++) {
                    {
                        IntersetFilterModel trophymodel=new IntersetFilterModel();

                        if(servicesResponse.category_list.get(i).event_icon!=null){
                            trophymodel.setImage(servicesResponse.category_list.get(i).event_icon);
                        }


                        if(servicesResponse.category_list.get(i).event_image!=null){
                            trophymodel.setStrServiceHeaderImage(servicesResponse.category_list.get(i).event_image);
                        }


                        if(servicesResponse.category_list.get(i).category_ID!=null){
                            trophymodel.setId(servicesResponse.category_list.get(i).category_ID);
                        }

                        if(servicesResponse.category_list.get(i).category_Name!=null){
                            trophymodel.setName(servicesResponse.category_list.get(i).category_Name);}



                        if(i<25){
                            oneIntersetFilterList.add(trophymodel);
                        }else
                        if(i>=25 && i<50){
                            twoIntersetFilterList.add(trophymodel);
                        }else if(i>=50 && i<75){
                            threeIntersetFilterList.add(trophymodel);
                        }
                    }

                }

                oneCategoryFilterPage1Fragment.setTwentyfiveList(oneIntersetFilterList);
                twoCategoryFilterPage1Fragment.setTwentyfiveList(twoIntersetFilterList);
                threeCategoryFilterPage1Fragment.setTwentyfiveList(threeIntersetFilterList);
            }
        }

    }


    public void setImageAndFinish(String url, String header, String category_id){
        Intent intent=new Intent();
        intent.putExtra(AppConstants.IMAGES_SELECTED_FROM_CATEGORY,url);
        intent.putExtra(AppConstants.IMAGES_SELECTED_FROM_CATEGORY_HEADER,header);
        intent.putExtra(AppConstants.CATEGORY_ID,category_id);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    public void finishesActivity(){
        finish();
    }

}
*/
