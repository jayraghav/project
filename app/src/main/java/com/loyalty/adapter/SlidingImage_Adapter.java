package com.loyalty.adapter;

import android.content.Context;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loyalty.R;

import java.util.List;

/**
 * Created by jayendrapratapsingh on 2/11/16.
 */
public class SlidingImage_Adapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Integer> images;
    public SlidingImage_Adapter(Context context,List<Integer> images)
    {

        this.context=context;
        this.images=images;
    }
    @Override
    public int getCount() {
        return images.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_images_adapter, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


        imageView.setImageResource(images.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }
}
