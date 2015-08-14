package com.velvetcase.app.material.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.velvetcase.app.material.OrderDetailActivity;
import com.velvetcase.app.material.R;

import java.util.ArrayList;

/**
 * Created by Prashant Patil on 01-08-2015.
 */
public class OrderAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> list = new ArrayList<String>();
    LayoutInflater inflater;
    public OrderAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.order_custom_row_item,null);
        }

        TextView txt_viewDetails = (TextView) convertView.findViewById(R.id.txt_viewDetails);
        txt_viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OrderDetailActivity.class);
                context.startActivity(i);
            }
        });
        return convertView;
    }
}
