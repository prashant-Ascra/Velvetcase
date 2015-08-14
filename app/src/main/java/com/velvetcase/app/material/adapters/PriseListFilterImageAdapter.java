package com.velvetcase.app.material.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.velvetcase.app.material.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Prashant Patil on 15-06-2015.
 */
public class PriseListFilterImageAdapter   extends PagerAdapter {
    // Declare Variables
    Context context;

    ArrayList<JSONArray> VariationsList;
    LayoutInflater inflater;
    String[] caption;

    public PriseListFilterImageAdapter(Context context,  ArrayList<JSONArray> VariationsList) {
        this.context = context;
        this.VariationsList = VariationsList;

    }

    @Override
    public int getCount() {
        return VariationsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgview;
        TextView  description;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.custom_prise_filter_row, container,
                false);
        imgview = (ImageView) itemView.findViewById(R.id.jwel_imag);
        description = (TextView) itemView.findViewById(R.id.jwel_price);
        JSONArray array = VariationsList.get(position);
        try {
            if (array.getString(1)== null || array.getString(1).equalsIgnoreCase("null")){
                if (array.getString(0).toLowerCase().contains("wg") ) {
                    description.setText(""+array.getString(0).replace("wg","WHITE GOLD"));
                }
                if (array.getString(0).toLowerCase().contains("yg") ) {
                    description.setText(""+array.getString(0).replace("yg","YELLOW GOLD"));
                }
                if (array.getString(1).toLowerCase().contains("rg")) {
                    description.setText(""+array.getString(0).replace("rg","ROSE GOLD"));
                }
            }
            else{
                if (array.getString(0).toLowerCase().contains("wg") ) {
                    description.setText(""+array.getString(0).replace("wg","WHITE GOLD")+"/\n"+array.getString(1));
                }
                if (array.getString(0).toLowerCase().contains("yg") ) {
                    description.setText(""+array.getString(0).replace("yg","YELLOW GOLD")+"/\n"+array.getString(1));
                }
                if (array.getString(0).toLowerCase().contains("rg")) {
                    description.setText(""+array.getString(0).replace("rg","ROSE GOLD")+"/\n"+array.getString(1));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Picasso.with(context).load(array.getString(4)).into(imgview);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        // Add viewpager_item.xml to ViewPager
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

