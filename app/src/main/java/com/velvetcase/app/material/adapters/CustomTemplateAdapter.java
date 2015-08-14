package com.velvetcase.app.material.adapters;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.Models.AllProductsModelBase;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.fragments.Template_detail;
import com.velvetcase.app.material.util.PageCountListener;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomTemplateAdapter extends RecyclerView.Adapter<CustomTemplateAdapter.CntViewHolder> {
    static JSONArray templetelist;
    Activity activity;
    public PageCountListener pageCountListener;
    public CustomTemplateAdapter(JSONArray templetelist,Activity activity) {
        this.templetelist = templetelist;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return templetelist.length();
    }

    @Override
    public void onBindViewHolder(CntViewHolder contactViewHolder, int pos) {
        try {
            JSONObject templateobj = templetelist.getJSONObject(pos);
            Picasso.with(activity).load(templateobj.getString("template_bgthumbanil")).into(contactViewHolder.BackagroundImageView);
            JSONArray templatejsonarray = templateobj.getJSONArray("template");
            for (int i = 0; i<templatejsonarray.length(); i++){
                try {
                    JSONObject variantobj = templatejsonarray.getJSONObject(i);
//                    if (i == 0){
//                        Picasso.with(activity).load(variantobj.getString("pthumbnail")).into(contactViewHolder.variationImg1);
//                    }else if (i == 1){
//                        Picasso.with(activity).load(variantobj.getString("pthumbnail")).into(contactViewHolder.variationImg2);
//                    }else if (i == 2){
//                        Picasso.with(activity).load(variantobj.getString("pthumbnail")).into(contactViewHolder.variationImg3);
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            pageCountListener.SetPageCount(pos,templetelist.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public CntViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.template_cardview, viewGroup, false);
        return new CntViewHolder(itemView,activity);
    }

    public static class CntViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected Button select_template;
        ImageView BackagroundImageView;
        protected RelativeLayout main_layout;
        Context holdercontext;
        ImageView variationImg1,variationImg2,variationImg3;

        public CntViewHolder(View v, final Context context) {
            super(v);
            this.holdercontext = context;
            select_template = (Button) v.findViewById(R.id.template_btn);
            main_layout = (RelativeLayout) v.findViewById(R.id.main_layout);
            BackagroundImageView = (ImageView) v.findViewById(R.id.template_image);
            variationImg1 = (ImageView) v.findViewById(R.id.variation_img1);
            variationImg2 = (ImageView) v.findViewById(R.id.variation_img2);
            variationImg3 = (ImageView) v.findViewById(R.id.variation_img3);
            main_layout.setOnClickListener(this);
            select_template.setOnClickListener(this);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(main_layout == v || select_template == v ){
                try {
                    JSONObject templateobj = templetelist.getJSONObject(getPosition());
                    AllProductsModelBase.getInstance().setTemplateSelectionFlag("TemplateFlag");
                    AllProductsModelBase.getInstance().setProductBGImg(templateobj.getString("template_bgthumbanil"));
                    AllProductsModelBase.getInstance().setTemplateID(templateobj.getString("template_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((MainActivity)holdercontext).addFragmentInsideContainer(Template_detail.newInstance(),
                        Template_detail.TAG);
            }
        }
        public List<String> getSpinnerData() {
            List<String> list = new ArrayList<String>();
            list.add("Available from Rs3,828 to Rs8,161");
            return list;
        }
    }
}