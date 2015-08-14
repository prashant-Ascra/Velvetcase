package com.velvetcase.app.material;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.velvetcase.app.material.adapters.OrderAdapter;

import java.util.ArrayList;


/**
 * Created by abc on 6/5/2015.
 */
public class Order extends Activity {
    ImageView backarrow;
    ListView order_listview;
    OrderAdapter adapter;
    ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);
        order_listview = (ListView) findViewById(R.id.order_listview);
        PrepareList();
        adapter = new OrderAdapter(this,list);
        order_listview.setAdapter(adapter);
        backarrow = (ImageView) findViewById(R.id.back_arrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void PrepareList() {
        list.add("AAA");
        list.add("BBB");
        list.add("CCC");
    }
}
