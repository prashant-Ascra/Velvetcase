package com.velvetcase.app.material.adapters;

import java.util.ArrayList;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.velvetcase.app.material.Models.Create;

import com.squareup.picasso.Picasso;
import com.velvetcase.app.material.R;

public class Template_Adapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Create> createList;
    private LayoutInflater mLayoutInflater = null;

    public Template_Adapter(Context context,
                            ArrayList<Create> createList) {
        this.mContext = context;
        this.createList = createList;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return createList.size();
    }

    @Override
    public Create getItem(int pos) {
        return createList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.template_row, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        Create c = createList.get(position);
        viewHolder.temp_name.setText(c.getcreate_name());
        try {

            Picasso.with(mContext).load(c.getcreate_thumnail().replace("original","medium")).resize(250,250).into(viewHolder.temp_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
}

class CompleteListViewHolder {
    // public TextView mTVItem;
    ImageView temp_image;
    TextView temp_name;



    public CompleteListViewHolder(View base) {
        temp_image = (ImageView) base.findViewById(R.id.template_image);
        temp_name = (TextView) base.findViewById(R.id.template_name);


    }

}