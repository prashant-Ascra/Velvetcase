package com.velvetcase.app.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;

import com.velvetcase.app.material.adapters.DiscoverNewAdapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant Patil on 05-06-2015.
 */
public class DiscoverNewActivtiy extends Activity implements View.OnClickListener,GetResponse {
    static TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    DiscoverNewAdapter ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    ImageView back_arrow;
    TextView ActionbarTitle;
    String actionbartitle;
    String counter;
    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    LinearLayoutManager llm;
SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite__designer);
        try {
            Bundle b = getIntent().getExtras();
            actionbartitle = b.getString("ActionbarTitle");
        }catch (NullPointerException e){

        }

        alert = new AlertDialogManager();
        icd = new InternetConnectionDetector(DiscoverNewActivtiy.this);
        SetupView();
    }
    private void SetupView() {
        session=new SessionManager(DiscoverNewActivtiy.this);
        ActionbarTitle  = (TextView) findViewById(R.id.favorite_designer_text);
        reclyclerview_wrapper = (RelativeLayout)findViewById(R.id.reclyclerview_wrapper);
        pagecount = (TextView)findViewById(R.id.pagecount);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);
        animation_layout = (RelativeLayout)findViewById(R.id.animation_layout);
        recList = (RecyclerView)findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(DiscoverNewActivtiy.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        pagecount.setText("");

        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //     Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                if((llm.findLastCompletelyVisibleItemPosition()+1)==0){
                    pagecount.setText(""+counter);
                }else {
                    pagecount.setVisibility(View.VISIBLE);
                    pagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of " + AllProductsModelBase.getInstance().getDiscoverNewProductData().size());
                    counter=""+(llm.findLastCompletelyVisibleItemPosition() + 1)+" of "+AllProductsModelBase.getInstance().getDiscoverNewProductData().size();

                }
            }
        });
        if(AllProductsModelBase.getInstance().getDiscoverNewProductData()!= null){
            if(AllProductsModelBase.getInstance().getDiscoverNewProductData().size() > 0){
                ca = new DiscoverNewAdapter(AllProductsModelBase.getInstance().getDiscoverNewProductData(),DiscoverNewActivtiy.this);
                recList.setAdapter(ca);
             //   pagecount.setText("1 of "+AllProductsModelBase.getInstance().getDiscoverNewProductData().size());

            }else{
                icd = new InternetConnectionDetector(DiscoverNewActivtiy.this);
                if(icd.isConnectingToInternet()){
                    ExecuteServerRequest();
                }else{
                    alert.showAlertDialog(DiscoverNewActivtiy.this,"Alert","You dont have internet connection",false);
                }
            }


        }else{

            if(icd.isConnectingToInternet()){
                ExecuteServerRequest();
            }else{
                alert.showAlertDialog(DiscoverNewActivtiy.this,"Alert","You dont have internet connection",false);
            }
        }

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(actionbartitle!=null){
            ActionbarTitle.setText(actionbartitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ExecuteServerRequest(){
        List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
        asyncReuse = new AsyncReuse(DiscoverNewActivtiy.this, Constants.APP_MAINURL + "productjsons/discover_new.json", Constants.GET, paramslist, true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    @Override
    public void onClick(View v) {

    }

    public void TransferNextActivtiy(){
        Intent i = new Intent(DiscoverNewActivtiy.this,ActivityDetails.class);
        startActivity(i);
    }


    @Override
    public void GetData(String response) {
        if (response!=null){
            AllProductsModelBase.getInstance().GetDiscoverNewDataFromServer(response);
            if(AllProductsModelBase.getInstance().getDiscoverNewProductData().size() > 0){
                ca = new DiscoverNewAdapter(AllProductsModelBase.getInstance().getDiscoverNewProductData(),DiscoverNewActivtiy.this);
                recList.setAdapter(ca);
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert.showAlertDialog(DiscoverNewActivtiy.this,"Alert","You dont have internet connection",false);
                    }
                });
            }
        }
    }
}
