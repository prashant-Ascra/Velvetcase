package com.velvetcase.app.material.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.squareup.picasso.Picasso;
import com.velvetcase.app.material.R;

import java.util.ArrayList;

/**
 * Created by Akash on 4/7/2015.
 */
public class ViewPagerAdapterslider extends PagerAdapter {
    // Declare Variables
    Context context;


    LayoutInflater inflater;
    String[] caption;
    ArrayList<String>Listimg;

    public ViewPagerAdapterslider(Context context,  ArrayList<String> ImagesList) {
        this.context = context;
        this.Listimg = ImagesList;

    }

    @Override
    public int getCount() {
        return Listimg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgview;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpagercustom_item, container,
                false);
        imgview = (ImageView) itemView.findViewById(R.id.img_row);

        Picasso.with(context)
                .load(Listimg.get(position))
                .into(imgview);

        ((ViewPager) container).addView(itemView);

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ActivityDetails)context).openDialog();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
