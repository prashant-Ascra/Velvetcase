package com.velvetcase.app.material.adapters;

/**
 * Created by Akash on 3/14/2015.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.velvetcase.app.material.AsyncReuse;
import com.velvetcase.app.material.DBHandlers.WishListDBHelper;
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;
import com.velvetcase.app.material.Models.Products;
import com.velvetcase.app.material.Models.WishListModel;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.PageCountListener;
import com.velvetcase.app.material.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ContactViewHolder> {

    static ArrayList<Products> productList;
    private int lastPosition = -1;
    Activity context;

    static OnItemClickListener mItemClickListener;
    JSONArray ImageList ;
    static SessionManager session;
    public PageCountListener pageCountListener = null;
    static boolean islike=false;
    static boolean wish_list_flag=false;
    static String pro_json;
    static int pro_id;
    static WishListDBHelper wlDBHelper;


    public CustomListAdapter( ArrayList<Products> productList,Activity context) {
        this.productList = productList;
        this.context = context;
        session = new SessionManager(context);
        wlDBHelper = new WishListDBHelper(context);

    }

    public CustomListAdapter(Activity Act) {
        this.context = Act;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ViewPagerAdapterslider adapter;
        Products ci = productList.get(i);
        setAnimation(contactViewHolder.main_layout, i);
        contactViewHolder.productname.setText(ci.getname());
        try {
            contactViewHolder.designername.setText(ci.getdesigner_name());
        }catch (ClassCastException e){

        }
        JSONObject metalobj = ci.getmetal_details();
        try {
            contactViewHolder.prize.setText("\u20B9 "+metalobj.getString("price"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray photos = ci.getphotos();
        try {
            Picasso.with(context).load(photos.getString(0)).into(contactViewHolder.mImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Picasso.with(context).load(ci.getflag_url()).into(contactViewHolder.flag_icon);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if(ci.get_wish_list_flag()==true){
            wish_list_flag=true;
            contactViewHolder.heart_icon.setImageResource(R.drawable.greenheart);
            AddItemIntoWishList(ci.getproduct_id(),ci.getproductJsonobj());
        }else{
            wish_list_flag=false;
            contactViewHolder.heart_icon.setImageResource(R.drawable.grey_heart);
        }
        if (pageCountListener!=null) {
            pageCountListener.SetPageCount(i, productList.size());
        }

    }

    public void AddItemIntoWishList(int pro_id ,JSONObject pro_json){
        WishListModel model = new WishListModel();
        model.setProduct_id(""+pro_id);
        model.setProduct_json("" + pro_json);

        if(wlDBHelper!=null) {
            wlDBHelper.open();
            wlDBHelper.insertWishListTable(model);
        }
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);
        return new ContactViewHolder(itemView,context);
    }



    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,GetResponse  {
        protected TextView productname;
        protected TextView designername;
        protected TextView prize;
        ImageView mImageView,heart_icon;
        protected RelativeLayout main_layout;
        CardView card_view;
        Activity holdercontext;
        AlertDialogManager alert;
        InternetConnectionDetector icd;
        AsyncReuse asyncReuse;
        ImageView flag_icon;
        int mCurrentProductID;
        JSONObject mCurrentProductJson;

        public ContactViewHolder(View v, final Activity context) {
            super(v);
            this.holdercontext = context;
            productname =  (TextView) v.findViewById(R.id.title_detail);
            designername =  (TextView) v.findViewById(R.id.designer_detail);
            flag_icon=(ImageView)v.findViewById(R.id.flag_icon);
            prize =  (TextView) v.findViewById(R.id.prise_detail);
            mImageView = (ImageView)v.findViewById(R.id.main_image);
            main_layout = (RelativeLayout) v.findViewById(R.id.main_layout);
            heart_icon = (ImageView)v.findViewById(R.id.hrt_icon);
            card_view = (CardView) v.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
            heart_icon.setOnClickListener(this);
            alert = new AlertDialogManager();
            icd = new InternetConnectionDetector(context);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            Products ci = productList.get(getPosition());
                if (v == card_view) {
                    AllProductsModelBase.getInstance().setProductSelectionFlag("Product");
                    AllProductsModelBase.getInstance().setProductID(ci.getproduct_id());
                   session.setHeartSelected(ci.get_wish_list_flag());
                    ((MainActivity) holdercontext).TransferNextActivtiy();
                }
                if (v == heart_icon) {
                    Products c = productList.get(getPosition());
                    if(ci.get_wish_list_flag()){
                        islike=false;
                        pro_id = ci.getproduct_id();
                        pro_json=""+ci.getproductJsonobj();
                        AllProductsModelBase.getInstance().UpdateListData(c.getproduct_id(),false,"Product");
                        ExecuteServerRequest(+ci.getproduct_id(),islike);
                    }else {
                        islike=true;
                        pro_id = ci.getproduct_id();
                        pro_json=""+ci.getproductJsonobj();
                        ExecuteServerRequest(ci.getproduct_id(),islike);
                        AllProductsModelBase.getInstance().UpdateListData(c.getproduct_id(),true,"Product");

                    }

                }
            }


        public void changeWishListFlag(int Pos ,Boolean flag){

            productList.get(Pos).getproductJsonobj().optString("wishlist_flag",""+flag);

        }
        public void ExecuteServerRequest(int p_id,Boolean flag){
            List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
            paramslist.add(new BasicNameValuePair("variant_id",""+p_id));
            paramslist.add(new BasicNameValuePair("wishlist_flag",""+flag));


            asyncReuse = new AsyncReuse(holdercontext, Constants.APP_MAINURL+"tokens/product_wishlist", Constants.POST, paramslist, false);
            asyncReuse.getResponse = this;
            asyncReuse.execute();
        }
        @Override
        public void GetData(String response) {

            if (response!= null){
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equalsIgnoreCase("Success")){
                        if(obj.getString("message").equalsIgnoreCase("Product Added in wishlist")) {
                            WishListModel model = new WishListModel();
                            model.setProduct_id(""+pro_id);
                            model.setProduct_json("" + pro_json);

                            if(wlDBHelper!=null) {
                                wlDBHelper.open();
                                wlDBHelper.insertWishListTable(model);
                                heart_icon.setImageResource(R.drawable.greenheart);
                            }
                        }else if(obj.getString("message").equalsIgnoreCase("Product removed from wishlist")){
                            if(wlDBHelper!=null) {
                                wlDBHelper.delete_row(pro_id);
                            }
                            heart_icon.setImageResource(R.drawable.grey_heart);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

//    public interface PageCountListener{
//        public void SetPageCount(int page,int count);
//    }
}