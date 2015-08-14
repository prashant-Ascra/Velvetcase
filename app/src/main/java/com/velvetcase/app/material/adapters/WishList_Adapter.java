package com.velvetcase.app.material.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

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
import com.velvetcase.app.material.Favorite_Designer;
import com.velvetcase.app.material.Models.AllProductsModelBase;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.WishList;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.squareup.picasso.Picasso;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abc on 6/5/2015.
 */
public class WishList_Adapter extends RecyclerView.Adapter<WishList_Adapter.ContactViewHolder> {

    static ArrayList<HashMap<String, String>> wishlist;
    private int lastPosition = -1;
    Activity context;
    Activity activity;
    static OnItemClickListener mItemClickListener;
    static int zoomcount = 0;
     boolean islike=false;
    SessionManager session;
    AsyncReuse asyncReuse;
    WishListDBHelper wlDBHelper;
    int product_id;
    int[]ImageList = new int[] {R.drawable.detail_icon, R.drawable.detail_icon,
            R.drawable.detail_icon, R.drawable.detail_icon};



    public WishList_Adapter(ArrayList<HashMap<String, String>> wishlist,Activity context) {
        this.wishlist = wishlist;
        this.context = context;
        session=new SessionManager(context);
        wlDBHelper = new WishListDBHelper(context);
    }

    public WishList_Adapter(Favorite_Designer Act) {
        this.activity = Act;
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ViewPagerAdapterslider adapter;
        HashMap<String,String> ci = wishlist.get(i);
        setAnimation(contactViewHolder.main_layout, i);
        String jsonstring = ci.get("product_json");
        try {
            JSONObject jobj = new JSONObject(jsonstring);

            contactViewHolder.productname.setText(jobj.getString("name"));
            contactViewHolder.designername.setText(jobj.getString("designer_name"));
            contactViewHolder.prize.setText("\u20B9 "+jobj.getString("end_price"));

            JSONArray photos = jobj.getJSONArray("photos");
            try {
                Picasso.with(context).load(photos.getString(0)).into(contactViewHolder.mImageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Picasso.with(context).load(jobj.getString("designer_country_flag")).into(contactViewHolder.flag_icon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.wishlist_row, viewGroup, false);

        return new ContactViewHolder(itemView,context);
    }

    public  class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,GetResponse {

        protected TextView productname;
        protected TextView designername;
        protected TextView prize;
        ImageView mImageView,heart_icon,flag_icon;
        protected RelativeLayout main_layout;
        CardView card_view;
        Activity holdercontext;
        WishListDBHelper WLdbHelper;


        public ContactViewHolder(View v, final Activity context) {
            super(v);

            this.holdercontext = context;
            productname =  (TextView) v.findViewById(R.id.title_detail);
            designername =  (TextView) v.findViewById(R.id.designer_detail);
            prize =  (TextView) v.findViewById(R.id.prise_detail);
            mImageView = (ImageView)v.findViewById(R.id.main_image);
            heart_icon = (ImageView)v.findViewById(R.id.heart_icon);
            flag_icon = (ImageView)v.findViewById(R.id.flag_icon);
            main_layout = (RelativeLayout) v.findViewById(R.id.main_layout);
            card_view = (CardView) v.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
            heart_icon.setOnClickListener(this);
            WLdbHelper = new WishListDBHelper(context);

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {


            if (v == heart_icon) {

                try {
                    HashMap<String, String> ci = wishlist.get(getPosition());
                    product_id = Integer.parseInt(ci.get("product_id"));
                    String product_json = ci.get("product_json");
                    boolean flag_val = false;
                    try {
                        JSONObject jobj = new JSONObject(product_json);
                        flag_val = jobj.getBoolean("wishlist_flag");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ((WishList) holdercontext).NotifyAdapeter(product_id);

                    if (flag_val) {
                        AllProductsModelBase.getInstance().UpdateListData(product_id, false,"Product");
                        ExecuteServerRequest(product_id, islike);

                    }
                }catch (ArrayIndexOutOfBoundsException e){
e.printStackTrace();
                }
            }
            if (v == card_view) {
//                HashMap<String,String> ci = wishlist.get(getPosition());
//                String product_id = ci.get("product_id");
//                ((WishList)holdercontext).RequestSingleProduct(product_id);

            }
        }
        public   void ExecuteServerRequest(int p_id,Boolean flag){
            AllProductsModelBase.getInstance().UpdateListData(getPosition(),!flag,"Product");
            List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
            paramslist.add(new BasicNameValuePair("variant_id",""+p_id));
            paramslist.add(new BasicNameValuePair("wishlist_flag",""+false));


            asyncReuse = new AsyncReuse(holdercontext, Constants.APP_MAINURL+"tokens/product_wishlist", Constants.POST, paramslist, false);
            asyncReuse.getResponse =this;
            asyncReuse.execute();
        }

        @Override
        public void GetData(String response) {

            if (response!= null){
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equalsIgnoreCase("Success")){
                    if(obj.getString("message").equalsIgnoreCase("Product removed from wishlist")){

                        wlDBHelper.delete_row(product_id);
                        notifyDataSetChanged();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    private void setAnimation(View viewToAnimate, int position)
    {

        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

