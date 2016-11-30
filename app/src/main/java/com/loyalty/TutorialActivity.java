package com.loyalty;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loyalty.activity.customer.HomeActivity;
import com.loyalty.activity.customer.LoginActivity;
import com.loyalty.adapter.TutorialsAdapter;
import com.loyalty.utils.AppConstant;
import com.loyalty.utils.CommonUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TutorialActivity extends AppCompatActivity {
private ViewPager mViewPager;
    private TextView tvSkip;
    private CirclePageIndicator mIndicator;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private static final Integer[] images = {R.mipmap.tut1, R.mipmap.tut2, R.mipmap.tut3,R.mipmap.tut4,R.mipmap.tut5};
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getIds();
        initializePageIndicator();
    }

    private void getIds() {
        mViewPager=(ViewPager) findViewById(R.id.viewPagerTutorial);
        mIndicator=(CirclePageIndicator) findViewById(R.id.indicatorTutorial);
        tvSkip=(TextView) findViewById(R.id.tvSkip);
        tvSkip.setTypeface(CommonUtils.setBook(TutorialActivity.this));

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(TutorialActivity.this, HomeActivity.class));
                    finish();


            }
        });
    }

    private void initializePageIndicator()
    {
        for (int i = 0; i < images.length; i++)
            ImagesArray.add(images[i]);
        mViewPager.setAdapter(new TutorialsAdapter(TutorialActivity.this, ImagesArray));
        mIndicator.setViewPager(mViewPager);
        final float density = getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        mIndicator.setRadius(6 * density);
        NUM_PAGES = images.length;
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES)
                {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };
       /* Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*/

        // Pager listener over indicator
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int position)
            {
                currentPage = position;
                if(currentPage==4)
                    tvSkip.setText("Done");
                else
                    tvSkip.setText("Skip");
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int pos)
            {

            }
        });

    }


}
