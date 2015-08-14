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
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.Products;
import com.velvetcase.app.material.Models.WishListModel;

import com.velvetcase.app.material.R;
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
import java.util.List;

/**
 * Created by Prashant Patil on 20-06-2015.
 */
public class SingleDesignerCustomAdapter extends RecyclerView.Adapter<SingleDesignerCustomAdapter.ContactViewHolder> {
    static ArrayList<Products> productList;
    private int lastPosition = -1;
    Activity context;
    static OnItemClickListener mItemClickListener;
    JSONArray ImageList ;
    static SessionManager session;
    static boolean islike = false;
    static boolean wish_list_flag = false;
    static String  pro_json;
    static int pro_id;
    public SingleDesignerCustomAdapter( ArrayList<Products> productList,Activity context) {
        this.productList = productList;
        this.context = context;
        session = new SessionManager(context);
    }

    public SingleDesignerCustomAdapter(Activity context) {
        this.context = context;
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

        if (ci.get_wish_list_flag() == true) {
            wish_list_flag = true;
            contactViewHolder.heart_icon.setImageResource(R.drawable.greenheart);
        } else {
            wish_list_flag = false;
            contactViewHolder.heart_icon.setImageResource(R.drawable.grey_heart);
        }
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);
        return new ContactViewHolder(itemView,context);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,GetResponse {
        protected TextView productname;
        protected TextView designername;
        protected TextView prize;
        ImageView mImageView,heart_icon;
        protected RelativeLayout main_layout;
        CardView card_view;
        Activity holdercontext;
        WishListDBHelper wlDBHelper;
        AsyncReuse asyncReuse;
        ImageView flag_icon;

        public ContactViewHolder(View v, final Activity context) {
            super(v);
            this.holdercontext = context;
            productname =  (TextView) v.findViewById(R.id.title_detail);
            designername =  (TextView) v.findViewById(R.id.designer_detail);
            prize =  (TextView) v.findViewById(R.id.prise_detail);
            mImageView = (ImageView)v.findViewById(R.id.main_image);
            flag_icon=(ImageView)v.findViewById(R.id.flag_icon);
            main_layout = (RelativeLayout) v.findViewById(R.id.main_layout);
            heart_icon = (ImageView)v.findViewById(R.id.hrt_icon);
            card_view = (CardView) v.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
            heart_icon.setOnClickListener(this);
            wlDBHelper = new WishListDBHelper(context);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (v == card_view) {
                Products ci = productList.get(getPosition());
                AllProductsModelBase.getInstance().setProductSelectionFlag("Designer");
                AllProductsModelBase.getInstance().setProductID(ci.getproduct_id());
                ((MainActivity) holdercontext).TransferNextActivtiy();
            }
            if (v == heart_icon) {
                Products ci = productList.get(getPosition());
                if (ci.get_wish_list_flag()) {
                    islike = false;
                    pro_id = ci.getproduct_id();
                    pro_json = "" + ci.getproductJsonobj();
                    AllProductsModelBase.getInstance().setTemplateSelectionFlag("Designer");
                    AllProductsModelBase.getInstance().UpdateSingleDesignerListData(getPosition(),false);
                    ExecuteServerRequest(ci.getproduct_id(), false);

                } else {
                    islike = true;
                    pro_id =  ci.getproduct_id();
                    pro_json = "" + ci.getproductJsonobj();
                    AllProductsModelBase.getInstance().UpdateSingleDesignerListData(getPosition(),true);
                    ExecuteServerRequest(ci.getproduct_id(), true);
                }
            }
        }

        public void ExecuteServerRequest(int product_id, Boolean flag) {
            List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("user_id", session.getUserID()));
            paramslist.add(new BasicNameValuePair("variant_id", ""+product_id ));
            paramslist.add(new BasicNameValuePair("wishlist_flag","" + flag));



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
                            heart_icon.setImageResource(R.drawable.greenheart);
                            WishListModel model = new WishListModel();
                            model.setProduct_id(""+pro_id);
                            model.setProduct_json("" + pro_json);


                            wlDBHelper.open();
                            wlDBHelper.insertWishListTable(model);

                        }else if(obj.getString("message").equalsIgnoreCase("Product removed from wishlist")){
                            heart_icon.setImageResource(R.drawable.grey_heart);
                            wlDBHelper.delete_row(pro_id);
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
}

