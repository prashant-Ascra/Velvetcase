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

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.AsyncReuse;
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.Designer;
import com.velvetcase.app.material.Models.FavouriteDesignerModel;

import com.velvetcase.app.material.DBHandlers.FavouriteDatabaseHelper;
import com.velvetcase.app.material.R;
import com.velvetcase.app.material.fragments.SingleDesigner;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.squareup.picasso.Picasso;
import com.wizrocket.android.sdk.WizRocketAPI;
import com.wizrocket.android.sdk.exceptions.WizRocketMetaDataNotFoundException;
import com.wizrocket.android.sdk.exceptions.WizRocketPermissionsNotSatisfied;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Prashant Patil on 06-06-2015.
 */
public class DesignersAdapters   extends RecyclerView.Adapter<DesignersAdapters.ContactViewHolder>  {

    public static ArrayList<Designer> designerList;

    private int lastPosition = -1;
    Activity context;

    static OnItemClickListener mItemClickListener;
    boolean islike=false;
    static boolean islike1=true;
    static String designer_name,designer_id_to_db,designer_img_url,designer_flag_url,designer_products,designer_city;
    static boolean wish_list_flag=false;
    String design_id,design_json;
    static SessionManager session;
    static String design_obj;
    static int designer_id;
    static FavouriteDatabaseHelper fdHelper;
    private static Tracker tracker;
    String product_name;
    private GoogleAnalytics analytics;
    static WizRocketAPI wr;
    public DesignersAdapters(ArrayList<Designer> designerList,Activity context) {
        this.designerList = designerList;
        this.context = context;
        session=new SessionManager(context);
        fdHelper = new FavouriteDatabaseHelper(context);
             /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(context);
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y

         /*analytic tracking code end*/
        try{
            wr = WizRocketAPI.getInstance(context.getApplicationContext());
            WizRocketAPI.setDebugLevel(1);
        } catch (WizRocketMetaDataNotFoundException e) {
            e.printStackTrace();
            // The WizRocketMetaDataNotFoundException is thrown when you haven�t specified your WizRocket Account ID and/or the Account Token in your AndroidManifest.xml
        } catch (WizRocketPermissionsNotSatisfied e) {
            e.printStackTrace();
            // WizRocketPermissionsNotSatisfiedException is thrown when you haven�t requested the required permissions in your AndroidManifest.xml
        }
    }

    public DesignersAdapters(Activity Act) {
        this.context = Act;
    }

    @Override
    public int getItemCount() {
        return designerList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Designer ci = designerList.get(i);
        setAnimation(contactViewHolder.main_layout, i);
        contactViewHolder.designername.setText(ci.getdesigner_name());
        contactViewHolder.designerproducts.setText(""+ci.getdesigner_products() +" Designs");
        try {
            Picasso.with(context).load(ci.getdesigner_img()).into(contactViewHolder.mImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Picasso.with(context).load(ci.getflag_url()).into(contactViewHolder.flag_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ci.get_wish_list_flag()==true){
            wish_list_flag=true;
            contactViewHolder.hrt_icon.setImageResource(R.drawable.greenheart);
            AddItemIntoDesignerList(ci.getdesigner_id(),ci.getdesigner_name(),ci.getdesigner_products(),ci.getdesigner_img(),ci.getflag_url());
        }else{
            wish_list_flag=false;
            contactViewHolder.hrt_icon.setImageResource(R.drawable.grey_heart);
        }

    }

    public void AddItemIntoDesignerList(int designer_id_to_db ,String designer_name,int designer_products,String designer_img_url,String designer_flag){
        FavouriteDesignerModel model = new FavouriteDesignerModel();
        model.setdesigner_id("" + designer_id_to_db);
        model.setDesigner_name("" + designer_name);
        model.setDesigner_products("" + designer_products);
        model.setDesigner_img("" + designer_img_url);
        model.setFlag_url("" + designer_flag);

        if(fdHelper!=null) {
            fdHelper.open();
            fdHelper.insertFavDesigner(model);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.favorite_designer_row, viewGroup, false);

        return new ContactViewHolder(itemView,context);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,GetResponse {
        protected TextView designername;
        protected TextView designerproducts;
        ImageView mImageView,hrt_icon;
        protected RelativeLayout main_layout;
        CardView card_view;
        Activity holdercontext;
        ImageView flag_icon;

        AsyncReuse asyncReuse;
        public ContactViewHolder(View v, final Activity context) {
            super(v);

            this.holdercontext = context;
            designername =  (TextView) v.findViewById(R.id.designer_detail);
            designerproducts =  (TextView) v.findViewById(R.id.prise_detail);
            mImageView = (ImageView)v.findViewById(R.id.main_image);
            hrt_icon = (ImageView)v.findViewById(R.id.hrt_icon);
            main_layout = (RelativeLayout) v.findViewById(R.id.main_layout);
            card_view = (CardView) v.findViewById(R.id.card_view);
            flag_icon=(ImageView)v.findViewById(R.id.flag_icon);
            card_view.setOnClickListener(this);
            hrt_icon.setOnClickListener(this);
            hrt_icon.setImageResource(R.drawable.grey_heart);

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            Designer ci = designerList.get(getPosition());

            if (v == card_view) {
                AllProductsModelBase.getInstance().setDesignerID(ci.getdesigner_id());
                ((MainActivity) holdercontext).addFragmentInsideContainer(SingleDesigner.newInstance(), SingleDesigner.TAG);
                tracker.setScreenName("Favourite");

             /*send parameter to tracking */
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Favourite")
                        .setAction(ci.getdesigner_name())
                        .build());

                   /*send parameter to Wizrocket tracking */
                HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                prodViewedAction.put("Designer Name", ci.getdesigner_name());
                wr.event.push("Favourite (MO)", prodViewedAction);
            }
            if (v == hrt_icon) {

                if(wish_list_flag){
                    wish_list_flag=false;
                    designer_id_to_db=""+ci.getdesigner_id();
                    design_obj=""+ci.getdesignerJsonObj();
                    designer_name=ci.getdesigner_name();
                    designer_img_url= ci.getdesigner_img();
                    designer_products=""+ci.getdesigner_products();
                    designer_flag_url=""+ci.getflag_url();
                    AllProductsModelBase.getInstance().UpdateDesignListData(getPosition(),false);

                    ExecuteServerRequest(+ci.getdesigner_id(),false);
                }else {
                    designer_id_to_db=""+ci.getdesigner_id();
                    design_obj=""+ci.getdesignerJsonObj();
                    designer_name=ci.getdesigner_name();
                    designer_img_url= ci.getdesigner_img();
                    designer_flag_url=""+ci.getflag_url();
                    designer_products=""+ci.getdesigner_products();
                    wish_list_flag=true;
                    designer_id=ci.getdesigner_id();
                    design_obj=""+ci.getdesignerJsonObj();
                    AllProductsModelBase.getInstance().UpdateDesignListData(getPosition(),true);
                    ExecuteServerRequest(+ci.getdesigner_id(),true);
                }

//                model.setDesigner_name("" + ci.getdesigner_name());
//                model.setDesigner_products("" + ci.getdesigner_products());
//               model.setDesigner_img("" + ci.getdesigner_img());
//               model.setDesigner_city("" + ci.getdesigner_city());

//                fdHelper.open();
//                fdHelper.insertFavDesigner(model);
//                hrt_icon.setImageResource(R.drawable.greenheart);
            }
        }


        public  void ExecuteServerRequest(int designer_id,Boolean flag){
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

                        if(obj.getString("message").equalsIgnoreCase("Designer Added in wishlist")) {
                            hrt_icon.setImageResource(R.drawable.greenheart);
                            FavouriteDesignerModel model = new FavouriteDesignerModel();
                            model.setdesigner_id("" + designer_id_to_db);
                            model.setDesigner_name("" + designer_name);
                            model.setDesigner_products("" + designer_products);
                            model.setDesigner_img("" + designer_img_url);
                            model.setFlag_url("" + designer_flag_url);
                            if(fdHelper!=null) {
                                fdHelper.open();
                                fdHelper.insertFavDesigner(model);
                            }

                        }else if(obj.getString("message").equalsIgnoreCase("Designer removed from wishlist")){
                            hrt_icon.setImageResource(R.drawable.grey_heart);
                            if(fdHelper!=null) {
                                fdHelper.delete_row(designer_id);
                            }
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
    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
