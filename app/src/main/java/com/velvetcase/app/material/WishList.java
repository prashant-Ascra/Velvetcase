package com.velvetcase.app.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.velvetcase.app.material.DBHandlers.WishListDBHelper;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;

import com.velvetcase.app.material.adapters.WishList_Adapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.velvetcase.app.material.util.SweetAlertDialogManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abc on 6/5/2015.
 */
public class WishList extends Activity implements GetResponse {
    static TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    WishList_Adapter ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    ImageView back_arrow;

    WishListDBHelper wlDbHelper;
    ArrayList<HashMap<String, String>> wishlist;
    TextView error_msg;

    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    SweetAlertDialogManager sweet_alert;
    String Request_type;
    SessionManager session;
    LinearLayoutManager llm;
    String counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.wishlist);
        alert = new AlertDialogManager();
        sweet_alert=new SweetAlertDialogManager();
        icd = new InternetConnectionDetector(this);
        session = new SessionManager(this);
        FindIDS();
        wlDbHelper = new WishListDBHelper(this);
        if(wlDbHelper.getWishList() != null) {
            wishlist =  wlDbHelper.getWishList();
            SetupView();
        }else{
            recList.setVisibility(View.GONE);
            error_msg.setVisibility(View.VISIBLE);
        }

    }
    private void FindIDS() {
        error_msg = (TextView) findViewById(R.id.empty_msg);
        reclyclerview_wrapper = (RelativeLayout)findViewById(R.id.reclyclerview_wrapper);
        pagecount = (TextView)findViewById(R.id.pagecount);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);
        animation_layout = (RelativeLayout)findViewById(R.id.animation_layout);
        recList = (RecyclerView)findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
         llm= new LinearLayoutManager(WishList.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void SetupView() {
        UpdateListView();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void UpdateListView(){
        ca = new WishList_Adapter(wishlist,WishList.this);
        recList.setAdapter(ca);

if(wishlist.size()>0) {
    pagecount.setText("1 of " + wishlist.size());
}else{
    pagecount.setVisibility(View.GONE);
}
        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //     Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                if((llm.findLastCompletelyVisibleItemPosition()+1)==0){
                    pagecount.setText(""+counter);
                }else {
                    pagecount.setVisibility(View.VISIBLE);
                    pagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of " + wishlist.size());
                    counter=""+(llm.findLastCompletelyVisibleItemPosition() + 1)+" of "+wishlist.size();

                }
            }
        });

    }
    public void NotifyAdapeter(int id){
                wlDbHelper.open();
                wlDbHelper.delete_row(id);
            if(wlDbHelper.getWishList() != null) {
                wishlist =  wlDbHelper.getWishList();
                UpdateListView();
                ca.notifyDataSetChanged();
            }else{
                recList.setVisibility(View.GONE);
                error_msg.setVisibility(View.VISIBLE);
            }
    }

    public void RequestSingleProduct(String p_id){
        if(icd.isConnectingToInternet()){
            ExecuteServerRequest(p_id);
        }else{
            alert.showAlertDialog(WishList.this,"Alert","You dont have internet connection",false);
        }
    }

    public void ExecuteServerRequest(String p_id){
        AllProductsModelBase.getInstance().setProductID(Integer.parseInt(p_id));
        List<NameValuePair> paramslist= new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("product_id",""+p_id));
        asyncReuse = new AsyncReuse(WishList.this,
                Constants.APP_MAINURL+"productjsons/single_product.json", Constants.GET,paramslist,true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }
    @Override
    public void GetData(String response) {

        if (response!=null){
            AllProductsModelBase.getInstance().GetSingleProductDataFromServer(response);
            if (AllProductsModelBase.getInstance().getSingleProductData().size() > 0) {
                AllProductsModelBase.getInstance().setProductSelectionFlag("SavedLooks");
                Intent intent = new Intent(WishList.this, ActivityDetails.class);
                startActivity(intent);
            }
        }
    }
}
