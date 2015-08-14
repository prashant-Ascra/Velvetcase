package com.velvetcase.app.material.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.velvetcase.app.material.R;


/**
 * Created by Akash on 4/13/2015.
 */
public class DialogImageViewAdapter  extends PagerAdapter {
    // Declare Variables
    Context context;

    int[] ImagesList;
    LayoutInflater inflater;
    String[] caption;

    public DialogImageViewAdapter(Context context,  int[] ImagesList) {
        this.context = context;
        this.ImagesList = ImagesList;

    }

    @Override
    public int getCount() {
        return ImagesList.length;
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
        View itemView = inflater.inflate(R.layout.custom_dialog_imageview_row, container,
                false);

        imgview = (ImageView) itemView.findViewById(R.id.dialog_img);

        imgview.setImageResource(ImagesList[position]);

//        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
