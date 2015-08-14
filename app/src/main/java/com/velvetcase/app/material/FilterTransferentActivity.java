package com.velvetcase.app.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Created by Prashant Patil on 28-05-2015.
 */
public class FilterTransferentActivity extends Activity implements View.OnClickListener{

    ImageView img_price,img_metals;
    TextView txt_price,txt_metals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_transferent_layout);

        img_price = (ImageView) findViewById(R.id.dollar_image);
        img_metals = (ImageView) findViewById(R.id.metal_image);
        txt_metals  = (TextView) findViewById(R.id.txt_metals);
        txt_price  = (TextView) findViewById(R.id.txt_price);

        img_price.setOnClickListener(this);
        img_metals.setOnClickListener(this);
        txt_metals.setOnClickListener(this);
        txt_price.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(img_metals == v || txt_metals == v  ){
            Intent i = new Intent(FilterTransferentActivity.this,Shopdetail.class);
            startActivity(i);
        }
        if(img_price == v || txt_price == v ){
            Intent i = new Intent(FilterTransferentActivity.this,PriceFilterActivity.class);
            startActivity(i);
        }
    }
}
