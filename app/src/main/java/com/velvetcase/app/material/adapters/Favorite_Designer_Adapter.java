package com.velvetcase.app.material.adapters;

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
import com.velvetcase.app.material.Favorite_Designer;
import com.velvetcase.app.material.Models.AllProductsModelBase;

import com.velvetcase.app.material.DBHandlers.FavouriteDatabaseHelper;
import com.velvetcase.app.material.R;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.squareup.picasso.Picasso;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abc on 6/5/2015.
 */
public class Favorite_Designer_Adapter extends RecyclerView.Adapter<Favorite_Designer_Adapter.ContactViewHolder> {

    static ArrayList<HashMap<String, String>> DesignersList;
    private int lastPosition = -1;
    Activity context;
    Activity activity;
    static OnItemClickListener mItemClickListener;
    SessionManager session;
    AsyncReuse asyncReuse;
    int designer_id;

    public Favorite_Designer_Adapter(ArrayList<HashMap<String, String>> DesignersList,Activity context) {
        this.DesignersList = DesignersList;
        this.context = context;
        session=new SessionManager(context);
    }

    public Favorite_Designer_Adapter(Favorite_Designer Act) {
        this.activity = Act;
    }

    @Override
    public int getItemCount() {
        return DesignersList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ViewPagerAdapterslider adapter;
        HashMap ci = DesignersList.get(i);
//        setAnimation(contactViewHolder.main_layout, i);
        contactViewHolder.designername.setText(""+ci.get("Designer_name"));
        contactViewHolder.designerproducts.setText("" + ci.get("Designer_product") + " Designs");
        contactViewHolder.hrt_icon.setImageResource(R.drawable.greenheart);

        try {
            Picasso.with(context).load(ci.get("Designer_img").toString()).into(contactViewHolder.mImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Picasso.with(context).load(ci.get("Designer_flag").toString()).into(contactViewHolder.flag_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.favorite_designer_row, viewGroup, false);

        return new ContactViewHolder(itemView,context);
    }

    public  class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,GetResponse {
        protected TextView designername;
        protected TextView designerproducts;
        ImageView mImageView,hrt_icon,flag_icon;
        protected RelativeLayout main_layout;
        CardView card_view;
        Activity holdercontext;
        FavouriteDatabaseHelper fdHelper;
        public ContactViewHolder(View v, final Activity context) {
            super(v);

            this.holdercontext = context;
            designername =  (TextView) v.findViewById(R.id.designer_detail);
            designerproducts =  (TextView) v.findViewById(R.id.prise_detail);
            mImageView = (ImageView)v.findViewById(R.id.main_image);
            hrt_icon = (ImageView)v.findViewById(R.id.hrt_icon);
            flag_icon= (ImageView)v.findViewById(R.id.flag_icon);
            main_layout = (RelativeLayout) v.findViewById(R.id.main_layout);
            card_view = (CardView) v.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
            hrt_icon.setOnClickListener(this);
            hrt_icon.setImageResource(R.drawable.grey_heart);
            fdHelper = new FavouriteDatabaseHelper(context);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (v == card_view) {
                HashMap ci = DesignersList.get(getPosition());
//                AllProductsModelBase.getInstance().setDesignerID(Integer.parseInt(ci.get("designer_id").toString()));
//                ((Favorite_Designer) holdercontext).addFragmentInsideContainer(SingleDesigner.newInstance(), SingleDesigner.TAG);
            }
            if (v == hrt_icon) {
                HashMap<String,String> ci = DesignersList.get(getPosition());
                designer_id = Integer.parseInt(ci.get("designer_id"));
                ((Favorite_Designer)holdercontext).NotifyAdapeter(designer_id);
                AllProductsModelBase.getInstance().UpdateDesignListData(designer_id,false);
                ExecuteServerRequest(designer_id,false);

            }
        }

        public  void ExecuteServerRequest(int designer_id,Boolean flag){
            AllProductsModelBase.getInstance().UpdateDesignListData(getPosition(),false);
            List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
            paramslist.add(new BasicNameValuePair("designer_id",""+designer_id));
            paramslist.add(new BasicNameValuePair("wishlist_flag",""+flag));


            asyncReuse = new AsyncReuse(holdercontext,"http://admin.velvetcase.com/tokens/designer_wishlist", Constants.POST, paramslist, false);
            asyncReuse.getResponse =this;
            asyncReuse.execute();
        }

        @Override
        public void GetData(String response) {


            if (response!= null){
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("status").equalsIgnoreCase("Success")){
                        if(obj.getString("message").equalsIgnoreCase("Designer removed from wishlist")){

                            fdHelper.delete_row(designer_id);
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
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
