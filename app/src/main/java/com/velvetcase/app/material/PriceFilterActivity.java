package com.velvetcase.app.material;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.Products;

import com.velvetcase.app.material.adapters.PriseListFilterImageAdapter;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.SessionManager;
import com.wizrocket.android.sdk.WizRocketAPI;
import com.wizrocket.android.sdk.exceptions.WizRocketMetaDataNotFoundException;
import com.wizrocket.android.sdk.exceptions.WizRocketPermissionsNotSatisfied;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * Created by Prashant Patil on 28-05-2015.
 */
public class PriceFilterActivity extends ActionBarActivity {
    // Scrolling flag
    private boolean scrolling = false;
    private int mActiveCities[] = new int[] {
            1, 1, 1, 1
    };
    ImageView back_arrow;
    ArrayList<Products> productList = null;
    public ArrayList<String> Priselist = new ArrayList<String>();
    JSONArray variations;
    ArrayList<HashMap<String,ArrayList<JSONArray>>> priseFilterList = new ArrayList<HashMap<String,ArrayList<JSONArray>>>();
    ViewPager viewPager;
    PriseListFilterImageAdapter priselistimageadapter;
    SessionManager session;
    Button ShopNowBtn;
    String Shop_url;
    String product_id;
    String variation_id;
    public ArrayList<String> VariationIDList = new ArrayList<String>();
    private GoogleAnalytics analytics;
    private Tracker tracker;
    String product_name;
    WizRocketAPI wr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_filter_screen);
        session = new SessionManager(this);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ShopNowBtn = (Button) findViewById(R.id.button1);
        String ProductSelectionFlag = AllProductsModelBase.getInstance().getProductSelectionFlag();
        if (ProductSelectionFlag.equalsIgnoreCase("Product")) {
            productList = AllProductsModelBase.getInstance().getProductsList();
        }else if (ProductSelectionFlag.equalsIgnoreCase("Designer")) {
            productList = AllProductsModelBase.getInstance().getSingleDesignerList();
        }else if (ProductSelectionFlag.equalsIgnoreCase("Looks")){
            productList = AllProductsModelBase.getInstance().getSingleProductData();
        } else if (ProductSelectionFlag.equalsIgnoreCase("SavedLooks")){
            productList = AllProductsModelBase.getInstance().getSingleProductData();
        } else if (ProductSelectionFlag.equalsIgnoreCase("Discover")){
            productList = AllProductsModelBase.getInstance().getDiscoverSimilarProductData();
        }
        else if (ProductSelectionFlag.equalsIgnoreCase("DiscoverNew")){
            productList = AllProductsModelBase.getInstance().getDiscoverNewProductData();
        }
        if (productList.size() > 0){
            for (int i=0; i<productList.size(); i++){
                Products pi = productList.get(i);
                if (pi.getproduct_id() == AllProductsModelBase.getInstance().getProductID()){
                    Shop_url = pi.getshop_url();
                    variations = pi.getvariations();
                    product_name = pi.getname();
                }
            }
            if (variations!= null){
                for (int i = 0; i<variations.length(); i++){
                    try {
                         JSONArray Var_array = variations.getJSONArray(i);
                         Priselist.add( Var_array.getString(3));
                         VariationIDList.add( Var_array.getString(5));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Priselist = RemoveDuplicate(Priselist);

                for (int j = 0; j<Priselist.size(); j++){
                    ArrayList<JSONArray> RawproductList = new ArrayList<JSONArray>();
                    HashMap<String,ArrayList<JSONArray>> rawMap =  new HashMap<String,ArrayList<JSONArray>>();
                    String prise = Priselist.get(j);
                    for (int k = 0; k<variations.length(); k++){
                        product_id = VariationIDList.get(k);
                        try {
                            JSONArray Vararray = variations.getJSONArray(k);
                            if (Vararray.getString(3).equalsIgnoreCase(prise)){
                                RawproductList.add(Vararray);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    rawMap.put(prise,RawproductList);
                    priseFilterList.add(rawMap);
                }
            }
        }
        HashMap<String,ArrayList<JSONArray>> map = priseFilterList.get(0);
        priselistimageadapter = new PriseListFilterImageAdapter(PriceFilterActivity.this, map.get(Priselist.get(0)));
        viewPager.setAdapter(priselistimageadapter);

        try {

        }catch (NullPointerException e){

        }

        final AbstractWheel country = (AbstractWheel) findViewById(R.id.price_filter);
        country.setViewAdapter(new CountryAdapter(this));
        country.setCurrentItem(1);

        country.addScrollingListener( new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                scrolling = true;
            }
            public void onScrollingFinished(AbstractWheel wheel) {
                scrolling = false;
                updateAdapter(Priselist.get(wheel.getCurrentItem()));
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ShopNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenBrowser(Shop_url);
                tracker.setScreenName("Shop Show");
             /*send parameter to tracking */
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Shop Show")
                        .setAction(""+AllProductsModelBase.getInstance().getProductID())
                        .setLabel(""+product_name)
                        .build());
                      /*send parameter to Wizrocket tracking */
                HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                prodViewedAction.put("Price", AllProductsModelBase.getInstance().getProductID());
                wr.event.push("Shop Show (MO)", prodViewedAction);

            }
        });

             /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
         /*analytic tracking code end*/


        try{
            wr = WizRocketAPI.getInstance(getApplicationContext());
            WizRocketAPI.setDebugLevel(1);
        } catch (WizRocketMetaDataNotFoundException e) {
            e.printStackTrace();
            // The WizRocketMetaDataNotFoundException is thrown when you haven�t specified your WizRocket Account ID and/or the Account Token in your AndroidManifest.xml
        } catch (WizRocketPermissionsNotSatisfied e) {
            e.printStackTrace();
            // WizRocketPermissionsNotSatisfiedException is thrown when you haven�t requested the required permissions in your AndroidManifest.xml
        }
    }

    public void GoogleAnalyticTrackingSend(String label){
        tracker.setScreenName("Shop Show");
             /*send parameter to tracking */
        tracker.send(new HitBuilders.EventBuilder()
                .setAction("Price")
                .setLabel(label)
                .build());

            /*send parameter to Wizrocket tracking */
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put("Price", label);
        wr.event.push("Shop Show", prodViewedAction);
    }
    private void updateAdapter(String prise) {
       for (int i= 0; i< priseFilterList.size(); i++) {
           HashMap<String, ArrayList<JSONArray>> map = priseFilterList.get(i);
           if (map.containsKey(prise)) {
               priselistimageadapter = new PriseListFilterImageAdapter(PriceFilterActivity.this, map.get(prise));
               viewPager.setAdapter(priselistimageadapter);
               JSONArray array = map.get(prise).get(0);
               try {
                   variation_id =  array.getString(5);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }
    }

    public void OpenBrowser(String url){
        if(url!=null){
            String url_str[]=url.split("=");
            url=url_str[0]+"="+variation_id;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }
    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {

        protected CountryAdapter(Context context) {
            super(context, R.layout.country_item, NO_RESOURCE);
            setItemTextResource(R.id.country_name);
        }
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView country_name = (TextView) view.findViewById(R.id.country_name);
            country_name.setText("\u20B9 "+Priselist.get(index));
            return view;
        }

        @Override
        public int getItemsCount() {
            return Priselist.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return Priselist.get(index);
        }
    }

    public ArrayList<String> RemoveDuplicate(ArrayList<String> list){
// add elements to al, including duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }
}
