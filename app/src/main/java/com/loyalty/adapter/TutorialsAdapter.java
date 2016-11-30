package com.loyalty.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loyalty.R;
import com.loyalty.activity.customer.LoginActivity;

import java.util.ArrayList;

/**
 * Created by Shivangi on 26-08-2016.
 */
public class TutorialsAdapter  extends PagerAdapter{
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context mcontext;

    public TutorialsAdapter(Context context, ArrayList<Integer> imagesArray) {
        this.mcontext = context;
        this.IMAGES = imagesArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position)
    {
        View viewLayout = inflater.inflate(R.layout.view_pager_tutorials, view, false);

        assert viewLayout != null;
        final ImageView imageView = (ImageView) viewLayout.findViewById(R.id.ivTutorialsImages);
       /* final TextView tvDone = (TextView) viewLayout.findViewById(R.id.tvDone);
        if(position==IMAGES.size()-1)
        {
        }
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mcontext).finish();
                mcontext.startActivity(new Intent(mcontext, LoginActivity.class));

            }
        });*/
        try {
            imageView.setImageResource(IMAGES.get(position));
            view.addView(viewLayout, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewLayout;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
}
