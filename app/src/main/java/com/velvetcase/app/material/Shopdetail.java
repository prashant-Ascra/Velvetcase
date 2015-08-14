package com.velvetcase.app.material;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.util.Log;
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

import com.velvetcase.app.material.adapters.ImageSliderListAdapter;
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
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * Created by Akash on 4/20/2015.
 */
public class Shopdetail extends ActionBarActivity {
    // Scrolling flag
    private boolean scrolling = false;
    private int mActiveDiamonds[] = new int[]{
            1, 1, 1, 1
    };
    private int mActivemetals;
    ImageView backButton;
    public ArrayList<String> Newmetalslist = new ArrayList<String>();
    public ArrayList<String> Newstoneslist = new ArrayList<String>();
    public ArrayList<String> NewDiamondlist = new ArrayList<String>();
    public ArrayList<String> Priselist = new ArrayList<String>();
    public ArrayList<String> ImageListlist = new ArrayList<String>();
    public ArrayList<String> IconList = new ArrayList<String>();

    ArrayList<Products> productList = null;
    JSONArray variations;
    String CurrentDiamond = "", CurrentMetal = "", CurrentStones = "";
    TextView txt_prise;
    ImageView product_img;
    ViewPager view_pager;
    JSONArray ImageList;
    ImageSliderListAdapter adapter;
    SessionManager session;
    Button ShopNowBtn;
    String Shop_url;
    String product_id;
    TextView stone_heading, Diamond_heading, Metal_heading;
    GoogleAnalytics analytics;
    Tracker tracker;
    String product_name;
    WizRocketAPI wr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_now);
        session = new SessionManager(this);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        txt_prise = (TextView) findViewById(R.id.jwel_price);
        product_img = (ImageView) findViewById(R.id.jwel_imag);
        ShopNowBtn = (Button) findViewById(R.id.button1);
        stone_heading = (TextView) findViewById(R.id.stone_heading);
        Diamond_heading = (TextView) findViewById(R.id.metal_heading);
        Metal_heading = (TextView) findViewById(R.id.diamond_heading);
        final AbstractWheel metals = (AbstractWheel) findViewById(R.id.metal);
        final AbstractWheel stones = (AbstractWheel) findViewById(R.id.stone);
        final AbstractWheel Diamond = (AbstractWheel) findViewById(R.id.diamond);

        String ProductSelectionFlag = AllProductsModelBase.getInstance().getProductSelectionFlag();
        if (ProductSelectionFlag.equalsIgnoreCase("Product")) {
            productList = AllProductsModelBase.getInstance().getProductsList();
        } else if (ProductSelectionFlag.equalsIgnoreCase("Designer")) {
            productList = AllProductsModelBase.getInstance().getSingleDesignerList();
        } else if (ProductSelectionFlag.equalsIgnoreCase("Looks")) {
            productList = AllProductsModelBase.getInstance().getSingleProductData();
        } else if (ProductSelectionFlag.equalsIgnoreCase("SavedLooks")) {
            productList = AllProductsModelBase.getInstance().getSingleProductData();
        } else if (ProductSelectionFlag.equalsIgnoreCase("Discover")){
            productList = AllProductsModelBase.getInstance().getDiscoverSimilarProductData();
        }
        else if (ProductSelectionFlag.equalsIgnoreCase("DiscoverNew")){
            productList = AllProductsModelBase.getInstance().getDiscoverNewProductData();
        }


        if (productList.size() > 0) {
            for (int i = 0; i < productList.size(); i++) {
                Products pi = productList.get(i);
                if (pi.getproduct_id() == AllProductsModelBase.getInstance().getProductID()) {
                    Shop_url = pi.getshop_url();
                    variations = pi.getvariations();
                    ImageList = pi.getphotos();
                    product_name = pi.getname();
                }
            }

            if (variations != null) {
                for (int i = 0; i < variations.length(); i++) {
                    try {
                        JSONArray Var_array = variations.getJSONArray(i);
                        IconList.add(Var_array.getString(0));
                        Newmetalslist.add(Var_array.getString(0));
                        NewDiamondlist.add(Var_array.getString(1));
                        Newstoneslist.add(Var_array.getString(2));
                        Priselist.add(Var_array.getString(3));
                        ImageListlist.add(Var_array.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Newmetalslist = RemoveDuplicate(Newmetalslist);
        if (Newmetalslist.size() > 0) {
            CurrentMetal = Newmetalslist.get(0);
            Metal_heading.setVisibility(View.VISIBLE);
            metals.setViewAdapter(new metalsAdapter(this, Newmetalslist, RemoveDuplicate(IconList)));
        } else {
            Metal_heading.setVisibility(View.GONE);
        }
        Newstoneslist = RemoveDuplicate(Newstoneslist);
        if (Newstoneslist.size() > 0) {
            if (!Newstoneslist.get(0).contains("null")) {
                stone_heading.setVisibility(View.INVISIBLE);
                CurrentStones = Newstoneslist.get(0);
                stones.setViewAdapter(new StonesAdapter(this, Newstoneslist));
            } else {
                stone_heading.setVisibility(View.INVISIBLE);
            }
        } else {
            stone_heading.setVisibility(View.INVISIBLE);
        }

        NewDiamondlist = RemoveDuplicate(NewDiamondlist);
        if (NewDiamondlist.size() > 0) {

            if (!NewDiamondlist.get(0).contains("null")) {
                Diamond_heading.setVisibility(View.VISIBLE);
                CurrentDiamond = NewDiamondlist.get(0);
                Diamond.setViewAdapter(new DiamondAdapter(this, RemoveDuplicate(NewDiamondlist)));
            }else {
                Diamond_heading.setVisibility(View.INVISIBLE);
            }
        } else {
            Diamond_heading.setVisibility(View.INVISIBLE);
        }

        FilterPrise();
        metals.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
            }
        });
        metals.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                scrolling = true;
            }

            public void onScrollingFinished(AbstractWheel wheel) {
                scrolling = false;
                CurrentMetal = Newmetalslist.get(wheel.getCurrentItem());
                FilterPrise();
            }
        });
        Diamond.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                scrolling = true;
            }

            public void onScrollingFinished(AbstractWheel wheel) {
                scrolling = false;
                CurrentDiamond = NewDiamondlist.get(wheel.getCurrentItem());
                FilterPrise();
            }
        });
        stones.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(AbstractWheel wheel) {
                scrolling = true;
            }

            public void onScrollingFinished(AbstractWheel wheel) {
                scrolling = false;
                CurrentStones = Newstoneslist.get(wheel.getCurrentItem());
                FilterPrise();

            }
        });

        backButton = (ImageView) findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
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

    public void OpenBrowser(String url) {
        if (url != null) {
            String url_string[] = url.split("=");
            url = url_string[0] + "=" + product_id;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            GoogleAnalyticTrackingSend(CurrentMetal+"|"+CurrentDiamond+"|"+CurrentStones);
        }
    }

    public ArrayList<String> RemoveDuplicate(ArrayList<String> list) {
        // add elements to al, including duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }
    public void FilterPrise() {
        String filterPrice = "";
        if(CurrentMetal!=null){
            if (variations != null) {
                for (int i = 0; i < variations.length(); i++) {
                    try {
                        JSONArray Var_array = variations.getJSONArray(i);
                        if(CurrentMetal.equalsIgnoreCase(Var_array.getString(0))) {
                            try {
                                txt_prise.setText("\u20B9 " + Var_array.getString(3));;
                                product_id = Var_array.getString(5);
                                ImageList.put(0, Var_array.getString(4));
                                adapter = new ImageSliderListAdapter(Shopdetail.this, ImageList);
                                view_pager.setAdapter(adapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (filterPrice != "")
                    txt_prise.setText("\u20B9 " + filterPrice);
            }
        }
        if(CurrentMetal!=null && CurrentDiamond!=null){
            if (variations != null) {
                for (int i = 0; i < variations.length(); i++) {
                    try {
                        JSONArray Var_array = variations.getJSONArray(i);
                        if(CurrentMetal.equalsIgnoreCase(Var_array.getString(0))&& CurrentDiamond.equalsIgnoreCase(Var_array.getString(1))) {
                            try {
                                txt_prise.setText("\u20B9 " + Var_array.getString(3));;
                                product_id = Var_array.getString(5);
                                ImageList.put(0, Var_array.getString(4));
                                adapter = new ImageSliderListAdapter(Shopdetail.this, ImageList);
                                view_pager.setAdapter(adapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }

        if(CurrentMetal!=null && CurrentDiamond!=null && CurrentStones!=null){
            if (variations != null) {
                for (int i = 0; i < variations.length(); i++) {
                    try {
                        JSONArray Var_array = variations.getJSONArray(i);
                        if(CurrentMetal.equalsIgnoreCase(Var_array.getString(0))&& CurrentDiamond.equalsIgnoreCase(Var_array.getString(1))&& CurrentStones.equalsIgnoreCase(Var_array.getString(2))) {
                            try {
                                txt_prise.setText("\u20B9 " + Var_array.getString(3));;
                                product_id = Var_array.getString(5);
                                ImageList.put(0, Var_array.getString(4));
                                adapter = new ImageSliderListAdapter(Shopdetail.this, ImageList);
                                view_pager.setAdapter(adapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void GoogleAnalyticTrackingSend(String label){
        tracker.setScreenName("Shop Show");
             /*send parameter to tracking */
        tracker.send(new HitBuilders.EventBuilder()
                .setAction("Metal")
                .setLabel(label)
                .build());

           /*send parameter to Wizrocket tracking */
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put("Metal", label);
        wr.event.push("Shop Show (MO)", prodViewedAction);
    }
    /**
     * Adapter for countries
     */
    private class metalsAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private ArrayList<String> newMetalslist;
        private ArrayList<String> iconlist;

        protected metalsAdapter(Context context, ArrayList<String> metalslist, ArrayList<String> iconlist) {
            super(context, R.layout.country_item, NO_RESOURCE);
            setItemTextResource(R.id.country_name);
            this.newMetalslist = metalslist;
            this.iconlist = iconlist;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            ImageView img = (ImageView) view.findViewById(R.id.flag);
            TextView country_name = (TextView) view.findViewById(R.id.country_name);
            if (iconlist.get(index).contains("wg")) {
                img.setImageResource(R.drawable.whitegold);
                // country_name.setText("GOLD");
                country_name.setText(newMetalslist.get(index).replace("wg", "GOLD"));
            }
            if (iconlist.get(index).contains("yg")) {
                img.setImageResource(R.drawable.yellowcircle);
                //  country_name.setText("GOLD");
                country_name.setText(newMetalslist.get(index).replace("yg", "GOLD"));
            }
            if (iconlist.get(index).contains("rg")) {
                //  country_name.setText("GOLD");
                img.setImageResource(R.drawable.rosegold);
                country_name.setText(newMetalslist.get(index).replace("rg", "GOLD"));
            }
            if (iconlist.get(index).contains("Silver")) {
                //  country_name.setText("GOLD");
                img.setImageResource(R.drawable.whitegold);
                country_name.setText(newMetalslist.get(index).replace("Silver", "Silver"));
            }
            return view;
        }

        @Override
        public int getItemsCount() {
            return newMetalslist.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return newMetalslist.get(index);
        }
    }

    private class StonesAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private ArrayList<String> Cstoneslist;

        protected StonesAdapter(Context context, ArrayList<String> stoneslist) {
            super(context, R.layout.country_item, NO_RESOURCE);
            setItemTextResource(R.id.country_name);
            this.Cstoneslist = stoneslist;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            ImageView img = (ImageView) view.findViewById(R.id.flag);
            img.setImageResource(R.drawable.yellowcircle);
            img.setVisibility(View.GONE);
            return view;
        }

        @Override
        public int getItemsCount() {
            return Cstoneslist.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return Cstoneslist.get(index);
        }
    }

    private class DiamondAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private ArrayList<String> Cdiamondlist;

        protected DiamondAdapter(Context context, ArrayList<String> diamondlist) {
            super(context, R.layout.country_item, NO_RESOURCE);
            setItemTextResource(R.id.country_name);
            this.Cdiamondlist = diamondlist;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            ImageView img = (ImageView) view.findViewById(R.id.flag);
            img.setImageResource(R.drawable.yellowcircle);
            img.setVisibility(View.GONE);
            return view;
        }
        @Override
        public int getItemsCount() {
            return Cdiamondlist.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return Cdiamondlist.get(index);
        }
    }
}
