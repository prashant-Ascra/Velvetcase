package com.velvetcase.app.material;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.transition.Explode;
import android.transition.Fade;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.DBHandlers.WishListDBHelper;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.Products;
import com.velvetcase.app.material.Models.WishListModel;

import com.velvetcase.app.material.adapters.ImageSliderListAdapter;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.wizrocket.android.sdk.WizRocketAPI;
import com.wizrocket.android.sdk.exceptions.WizRocketMetaDataNotFoundException;
import com.wizrocket.android.sdk.exceptions.WizRocketPermissionsNotSatisfied;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Akash on 4/7/2015.
 */

public class ActivityDetails extends ActionBarActivity implements View.OnClickListener,GetResponse {

    ViewPager viewPager;
    ImageSliderListAdapter adapter;

    ImageView backArrow,heart_icon,closed_icon;
    RelativeLayout bag_wrapper;
    private ScaleGestureDetector SGD;
    private float scale = 1f;
    private Matrix matrix = new Matrix();
    FrameLayout parant_wrapper;
    LinearLayout shop_layout;
    Boolean heartFlag = false;
    MaterialDialog mMaterialDialog ;
    TextView product_range,title,designer,product_specification_one,product_specification_two,product_story_detail;
    ImageView bag_icon;
    Boolean menuFlag = true;
    RelativeLayout FilterMenuWrapper;
    ImageView img_price,img_metals;
    TextView txt_price,txt_metals;
    TextView txt_shopnow,txt_discover_similar,txt_discover_new;
    JSONArray ImageList ;
    ArrayList<Products> productList = null;
    Products mainProduct = null;
    CirclePageIndicator indicator;
    SessionManager session;
    LinearLayout dynamiclayout;
    Button ShopNowBtn;
    boolean islike=false;
    AsyncReuse asyncReuse;
    WishListDBHelper wlDBHelper;
    TextView price_text;
    ImageView flag_icon;

    private GoogleAnalytics analytics;
    private Tracker tracker;
    private WizRocketAPI wr;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        SGD = new ScaleGestureDetector(this,new ScaleListener());
        session = new SessionManager(this);
        wlDBHelper=new WishListDBHelper(ActivityDetails.this);

        // Locate the ViewPager in viewpager_main.xml
        title  = (TextView) findViewById(R.id.title);
        flag_icon=(ImageView)findViewById(R.id.flag_icon);
        designer  = (TextView) findViewById(R.id.designer);
        txt_price  = (TextView) findViewById(R.id.prise);
        price_text=(TextView)findViewById(R.id.txt_price);
        product_range = (TextView) findViewById(R.id.toolbar_spinner);
        product_specification_one = (TextView) findViewById(R.id.product_specification_one);
//        product_specification_two = (TextView) findViewById(R.id.product_specification_two);
        product_story_detail =  (TextView) findViewById(R.id.product_story_detail);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        bag_wrapper = (RelativeLayout) findViewById(R.id.bag_wrapper);
        backArrow = (ImageView) findViewById(R.id.arrow_btn);
        bag_icon = (ImageView) findViewById(R.id.bag_icon);
        dynamiclayout = (LinearLayout) findViewById(R.id.dynamic_layout);

        heart_icon = (ImageView) findViewById(R.id.heart_icon);
        closed_icon = (ImageView) findViewById(R.id.detailclose_icon);
        shop_layout = (LinearLayout) findViewById(R.id.shop_layout);

        FilterMenuWrapper = (RelativeLayout) findViewById(R.id.filter_menu);
        img_price = (ImageView) findViewById(R.id.dollar_image);
        img_metals = (ImageView) findViewById(R.id.metal_image);
        txt_metals  = (TextView) findViewById(R.id.txt_metals);
        ShopNowBtn = (Button) findViewById(R.id.button3);
        price_text.setOnClickListener(this);
        img_price.setOnClickListener(this);
        img_metals.setOnClickListener(this);
        txt_metals.setOnClickListener(this);
        txt_price.setOnClickListener(this);

        backArrow.setOnClickListener(this);
        bag_icon.setOnClickListener(this);
        heart_icon.setOnClickListener(this);
        closed_icon.setOnClickListener(this);
        FilterMenuWrapper.setOnClickListener(this);
        ShopNowBtn.setOnClickListener(this);

        txt_shopnow = (TextView) findViewById(R.id.txt_shopnow);
        txt_discover_similar = (TextView) findViewById(R.id.txt_discoversimilar);
        txt_discover_new = (TextView) findViewById(R.id.txt_discovernew);

        txt_shopnow.setOnClickListener(this);
        txt_discover_similar.setOnClickListener(this);
        txt_discover_new.setOnClickListener(this);

        parant_wrapper = (FrameLayout) findViewById(R.id.parent_wrapper);
        parant_wrapper.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                SGD.onTouchEvent(event);
                return true;
            }
        });

        SetUPUI();

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

    private void SetUPUI() {

        String ProductSelectionFlag = AllProductsModelBase.getInstance().getProductSelectionFlag();
        if (ProductSelectionFlag.equalsIgnoreCase("Product")) {
            productList = AllProductsModelBase.getInstance().getProductsList();
        }else if (ProductSelectionFlag.equalsIgnoreCase("Designer")) {
            productList = AllProductsModelBase.getInstance().getSingleDesignerList();
        }else if (ProductSelectionFlag.equalsIgnoreCase("Looks")){
            productList = AllProductsModelBase.getInstance().getSingleProductData();
        }
        else if (ProductSelectionFlag.equalsIgnoreCase("SavedLooks")){
            productList = AllProductsModelBase.getInstance().getSingleProductData();
        }
        else if (ProductSelectionFlag.equalsIgnoreCase("Discover")){
            productList = AllProductsModelBase.getInstance().getDiscoverSimilarProductData();
        }
        else if (ProductSelectionFlag.equalsIgnoreCase("DiscoverNew")){
            productList = AllProductsModelBase.getInstance().getDiscoverNewProductData();
        }
        if (productList.size() > 0){
            for (int i=0; i<productList.size(); i++){
                Products pi = productList.get(i);
                if (pi.getproduct_id() == AllProductsModelBase.getInstance().getProductID()){
                    mainProduct = pi;
                }
            }
            if (mainProduct!= null){
                if(mainProduct.get_wish_list_flag()){
                    heart_icon.setImageResource(R.drawable.greenheart);
                    shop_layout.setVisibility(View.VISIBLE);
                    islike=true;
                    //  heart_icon.setImageResource(R.drawable.greenheart);
                    heartFlag = false;
                }else {
                    islike=false;
                    heart_icon.setImageResource(R.drawable.grey_heart);
                    shop_layout.setVisibility(View.GONE);
                }
                title.setText(mainProduct.getname());

                try {
                    Picasso.with(ActivityDetails.this).load(mainProduct.getflag_url()).into(flag_icon);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                designer.setText(mainProduct.getdesigner_name());
                product_range.setText("Available from Rs "+mainProduct.getstart_price()+" to Rs "+mainProduct.getend_price());
                JSONObject metalobj = mainProduct.getmetal_details();
                try {
                    JSONArray stoneArray = metalobj.getJSONArray("stone");
                    JSONArray shapeArray = metalobj.getJSONArray("shape");
                    JSONArray piecesArray = metalobj.getJSONArray("pieces");
                    TextView SPEC1;
                    TextView SPEC2;
                    for (int k=0; k<stoneArray.length(); k++){
                        SPEC1 = new TextView(ActivityDetails.this);
                        SPEC2 = new TextView(ActivityDetails.this);
                        SPEC1.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        SPEC2.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                        SPEC1.setTextSize(14);
                        SPEC1.setTextColor(Color.BLACK);
                        SPEC1.setGravity(Gravity.LEFT);

                        SPEC1.setText("Stone - "+stoneArray.getString(k)+" | Shape - "+shapeArray.getString(k)+" | Pieces - "+
                                piecesArray.getString(k));
                        dynamiclayout.addView(SPEC1);

                    }
                    product_specification_one.setText("Metal - "+metalobj.getString("metal")+" "+metalobj.getString("purity")+" | Weight - "+
                            metalobj.getString("weight")+"gms");
//                    product_specification_two.setText("Stone - "+stoneArray.getString(0)+" | Shape - "+shapeArray.getString(0)+" | Pieces - "+
//                            piecesArray.getString(0));
                    product_story_detail.setText(mainProduct.getstory());
                    txt_price.setText("\u20B9 "+metalobj.getString("price"));

                    ImageList = mainProduct.getphotos();
                    adapter = new ImageSliderListAdapter(ActivityDetails.this, ImageList);
                    // Binds the Adapter to the ViewPager
                    viewPager.setAdapter(adapter);
                    indicator = (CirclePageIndicator) findViewById(R.id.indicator);
                    indicator.setViewPager(viewPager);
                    indicator.setFillColor(getResources().getColor(R.color.light_grey));
                    indicator.setHovered(true);
                    indicator.setRadius(7);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void OpenBrowser(String url){
        if(url!=null){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(2000);
        getWindow().setEnterTransition(explode);

        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setReturnTransition(fade);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        if (ShopNowBtn == v){
            Log.e("id ",mainProduct.getshop_url());
            OpenBrowser(mainProduct.getshop_url());
        }
        if(backArrow == v){
            onBackPressed();
        }
        if(bag_icon == v){
            if (menuFlag){
                FilterMenuWrapper.setVisibility(View.VISIBLE);
                menuFlag = false;
                applyScaleAnimation(bag_icon);
            }else{
                FilterMenuWrapper.setVisibility(View.GONE);
                menuFlag = true;
                applyScaleAnimation(bag_icon);
            }
        }
        if(heart_icon == v){
            if(mainProduct!=null){
                mainProduct.getproduct_id();
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(heart_icon, "scaleX", 1.5f);
                scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleXAnimator.setRepeatCount(1);
                scaleXAnimator.setDuration(300);

                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(heart_icon, "scaleY", 1.5f);
                scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleYAnimator.setRepeatCount(1);
                scaleYAnimator.setDuration(300);

                AnimatorSet set = new AnimatorSet();
                set.playTogether(scaleXAnimator, scaleYAnimator);
                set.start();

                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if(mainProduct.get_wish_list_flag()){

                            String ProductSelectionFlag = AllProductsModelBase.getInstance().getProductSelectionFlag();
                            if (ProductSelectionFlag.equalsIgnoreCase("Product")) {
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),false,"Product");
                            }else if (ProductSelectionFlag.equalsIgnoreCase("Designer")) {
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),false,"Designer");
                            }else if (ProductSelectionFlag.equalsIgnoreCase("Looks")){
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),false,"Looks");
                            }
                            else if (ProductSelectionFlag.equalsIgnoreCase("SavedLooks")){
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),false,"SavedLooks");
                            }

                            ExecuteServerRequest(+mainProduct.getproduct_id(),false);
                        }else{

                            String ProductSelectionFlag = AllProductsModelBase.getInstance().getProductSelectionFlag();
                            if (ProductSelectionFlag.equalsIgnoreCase("Product")) {
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),true,"Product");
                            }else if (ProductSelectionFlag.equalsIgnoreCase("Designer")) {
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),true,"Designer");
                            }else if (ProductSelectionFlag.equalsIgnoreCase("Looks")){
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),true,"Looks");
                            }
                            else if (ProductSelectionFlag.equalsIgnoreCase("SavedLooks")){
                                AllProductsModelBase.getInstance().UpdateListData(mainProduct.getproduct_id(),true,"SavedLooks");
                            }
                            ExecuteServerRequest(+mainProduct.getproduct_id(),true);
                        }
                    }
                });
            }

        }
        if(closed_icon == v){
            onBackPressed();
        }
        if(img_metals == v || txt_metals == v  ){
            FilterMenuWrapper.setVisibility(View.GONE);
            menuFlag = true;
            applyScaleAnimation(bag_icon);
            Intent i = new Intent(ActivityDetails.this,Shopdetail.class);
            startActivity(i);

            tracker.setScreenName("Details Filter");
             /*send parameter to tracking */
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Details Filter")
                    .setAction("Material")
                    .build());

               /*send parameter to Wizrocket tracking */
            HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
            prodViewedAction.put("Details Filter", "Material");
            wr.event.push("Details Filter (MO)", prodViewedAction);
        }
        if(img_price == v || price_text == v ){
            FilterMenuWrapper.setVisibility(View.GONE);
            menuFlag = true;
            applyScaleAnimation(bag_icon);
            Intent i = new Intent(ActivityDetails.this,PriceFilterActivity.class);
            startActivity(i);
            tracker.setScreenName("Details Filter");
             /*send parameter to tracking */
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Details Filter")
                    .setAction("Price")
                    .build());

                 /*send parameter to Wizrocket tracking */
            HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
            prodViewedAction.put("Details Filter", "Price");
            wr.event.push("Details Filter (MO)", prodViewedAction);
        }
        if (txt_shopnow == v){
            OpenBrowser(mainProduct.getshop_url());
            tracker.setScreenName("Shop Show");
             /*send parameter to tracking */
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Shop Show")
                    .setAction(""+mainProduct.getproduct_id())
                    .setLabel(""+mainProduct.getname())
                    .build());
        } else if (txt_discover_similar == v){
            Intent i = new Intent(ActivityDetails.this,DiscoverSimilarActivity.class);
            i.putExtra("ActionbarTitle","Discover Similar");
            startActivity(i);
        } else if (txt_discover_new == v){
            Intent i = new Intent(ActivityDetails.this,DiscoverNewActivtiy.class);
            i.putExtra("ActionbarTitle","Discover New");
            startActivity(i);
        }
    }


    public void ExecuteServerRequest(int p_id,Boolean flag){

        List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
        paramslist.add(new BasicNameValuePair("variant_id",""+p_id));
        paramslist.add(new BasicNameValuePair("wishlist_flag",""+flag));


        asyncReuse = new AsyncReuse(ActivityDetails.this, Constants.APP_MAINURL+"tokens/product_wishlist", Constants.POST, paramslist, false);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }
    public void applyScaleAnimation(View v){
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(v, "scaleX", 1.3f);
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleXAnimator.setRepeatCount(1);
        scaleXAnimator.setDuration(200);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(v, "scaleY", 1.3f);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnimator.setRepeatCount(1);
        scaleYAnimator.setDuration(200);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnimator, scaleYAnimator);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
    }

    public List<String> getSpinnerData() {
        List<String> list = new ArrayList<String>();
        list.add("Available from Rs3,828 to Rs8,161");
        return list;
    }

    @Override
    public void GetData(String response) {


        if (response!= null){
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equalsIgnoreCase("Success")){
                  //  Toast.makeText(ActivityDetails.this,obj.getString("message"),Toast.LENGTH_SHORT).show();
                    if(obj.getString("message").equalsIgnoreCase("Product Added in wishlist")) {
                        shop_layout.setVisibility(View.VISIBLE);
                        WishListModel model = new WishListModel();
                        model.setProduct_id(""+mainProduct.getproduct_id());
                        model.setProduct_json("" + mainProduct.getproductJsonobj());

                        if(wlDBHelper!=null) {
                            wlDBHelper.open();
                            wlDBHelper.insertWishListTable(model);
                            heart_icon.setImageResource(R.drawable.greenheart);
                        }
                    }else if(obj.getString("message").equalsIgnoreCase("Product removed from wishlist")){
                        shop_layout.setVisibility(View.GONE);
                        if(wlDBHelper!=null) {
                            wlDBHelper.delete_row(mainProduct.getproduct_id());
                        }
                        heart_icon.setImageResource(R.drawable.grey_heart);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

//            Toast.makeText(ActivityDetails.this,"pinch inside",Toast.LENGTH_SHORT).show();
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min( 5.0f,scale));

            matrix.setScale(scale, scale);
            // img.setImageMatrix(matrix);

            if (scale > 1.0f) {
                //   Toast.makeText(MyAct.this,"Zooming In",Toast.LENGTH_SHORT).show();
                // zoom(2f, 2f, new PointF(0, 0));
            }
            if(scale<1.0f){
//                Toast.makeText(ActivityDetails.this,"Zooming Out",Toast.LENGTH_SHORT).show();
                //zoom(1.0f, 1.0f, new PointF(0, 0));
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(parant_wrapper, "scaleX", 0.8f);
                scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleXAnimator.setRepeatCount(1);
                scaleXAnimator.setDuration(500);

                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(parant_wrapper, "scaleY", 0.8f);
                scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleYAnimator.setRepeatCount(1);
                scaleYAnimator.setDuration(500);

                AnimatorSet set = new AnimatorSet();
                set.playTogether(scaleXAnimator, scaleYAnimator);
                set.start();

                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                       finish();
                    }
                });
            }
            return true;
        }

    }

    public void openDialog() {
//        Dialog dialog = new Dialog(ActivityDetails.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_dialog_xml);
//        getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//
//        ViewPager viewPager1 = (ViewPager) dialog.findViewById(R.id.view_pager);
//        DialogImageViewAdapter adapter1 = new DialogImageViewAdapter(ActivityDetails.this, ImageList);
//        // Binds the Adapter to the ViewPager
//        viewPager1.setAdapter(adapter1);
//        dialog.show();
    }
}
