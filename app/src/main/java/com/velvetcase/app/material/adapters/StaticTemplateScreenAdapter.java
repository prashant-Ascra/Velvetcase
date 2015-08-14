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

import com.velvetcase.app.material.AsyncReuse;
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.Models.AllProductsModelBase;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.util.GetResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prashant Patil on 06-06-2015.
 */
public class StaticTemplateScreenAdapter  extends RecyclerView.Adapter<StaticTemplateScreenAdapter.CntViewHolder> implements GetResponse {

    public static JSONArray lookslist;

    Activity context;
    Activity activity;
    String product_id;
    ArrayList<String> prduct_id_list;
    AsyncReuse asyncReuse;
    getproduct_id get_id;
    String Request_type="single_product_detail";


    public StaticTemplateScreenAdapter(JSONArray lookslist,Activity context, getproduct_id get_id) {
        this.lookslist = lookslist;
        this.context = context;
        this.get_id=get_id;
    }

    public StaticTemplateScreenAdapter(MainActivity Act) {
        this.activity = Act;
    }

    @Override
    public int getItemCount() {
        return lookslist.length();
    }

    @Override
    public void onBindViewHolder(final CntViewHolder contactViewHolder, int pos) {

        try {
            prduct_id_list = new ArrayList<>();
            JSONObject templateobj = lookslist.getJSONObject(pos);
            JSONArray templatejsonarray = templateobj.getJSONArray("template");
            for (int i = 0; i<templatejsonarray.length(); i++){
                try {
                    JSONObject variantobj = templatejsonarray.getJSONObject(i);
                    if (i == 0){
//                        prduct_id_list.add(variantobj.getString("pid"));
                        contactViewHolder.variationImg1.setTag(variantobj.getString("pid"));
                        Picasso.with(activity).load(variantobj.getString("pthumbnail")).into(contactViewHolder.variationImg1);
                    }else if (i == 1){
//                        prduct_id_list.add(variantobj.getString("pid"));
                        contactViewHolder.variationImg2.setTag(variantobj.getString("pid"));
                        Picasso.with(activity).load(variantobj.getString("pthumbnail")).into(contactViewHolder.variationImg2);
                    }else if (i == 2){
//                        prduct_id_list.add(variantobj.getString("pid"));
                        contactViewHolder.variationImg3.setTag(variantobj.getString("pid"));
                        Picasso.with(activity).load(variantobj.getString("pthumbnail")).into(contactViewHolder.variationImg3);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Picasso.with(activity).load(templateobj.getString("template_bgthumbanil")).into(contactViewHolder.Bg_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        contactViewHolder.variationImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        AllProductsModelBase.getInstance().setTemplateSelectionFlag("Looks");
//                product_id = prduct_id_list.get(0);
                get_id.product_id_provider(contactViewHolder.variationImg1.getTag().toString());
            }
        });

        contactViewHolder.variationImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  product_id = prduct_id_list.get(1);
                AllProductsModelBase.getInstance().setTemplateSelectionFlag("Looks");
                get_id.product_id_provider(contactViewHolder.variationImg2.getTag().toString());

            }
        });

        contactViewHolder.variationImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                product_id = prduct_id_list.get(2);
                AllProductsModelBase.getInstance().setTemplateSelectionFlag("Looks");
                get_id.product_id_provider(contactViewHolder.variationImg3.getTag().toString());
            }
        });

    }


    public  interface  getproduct_id{
        public  void product_id_provider(String  product_id);
    }
    public void ExecuteServerRequest(){


    }
    @Override
    public CntViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.template_cardview, viewGroup, false);
        return new CntViewHolder(itemView,context);
    }

    @Override
    public void GetData(String response) {


    }

    public static class CntViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Button select_template,template_btn;
        ImageView Bg_image;
        protected RelativeLayout main_layout;
        Context holdercontext;
        ImageView variationImg1,variationImg2,variationImg3;


        public CntViewHolder(View v, final Context context) {
            super(v);
            this.holdercontext = context;
            select_template = (Button) v.findViewById(R.id.template_btn);
            main_layout = (RelativeLayout) v.findViewById(R.id.main_layout);
            Bg_image = (ImageView) v.findViewById(R.id.template_image);
            template_btn = (Button) v.findViewById(R.id.template_btn);

            variationImg1 = (ImageView) v.findViewById(R.id.variation_img1);
            variationImg2 = (ImageView) v.findViewById(R.id.variation_img2);
            variationImg3 = (ImageView) v.findViewById(R.id.variation_img3);
            template_btn.setVisibility(View.GONE);
            main_layout.setOnClickListener(this);


        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if(main_layout == v){
                try {
                    JSONObject templateobj = lookslist.getJSONObject(getPosition());
                    AllProductsModelBase.getInstance().setTemplateSelectionFlag("LooksFlag");
                    AllProductsModelBase.getInstance().setProductBGImg(templateobj.getString("template_bgthumbanil"));
                    AllProductsModelBase.getInstance().setTemplateID(templateobj.getString("template_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                ((MainActivity)holdercontext).addFragmentInsideContainer(Template_detail.newInstance(),
//                        Template_detail.TAG);
            }
        }
    }
}