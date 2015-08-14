package com.velvetcase.app.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.velvetcase.app.material.adapters.DiscoverSimilarAdapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant Patil on 29-06-2015.
 */
public class DiscoverSimilarActivity extends Activity implements View.OnClickListener,GetResponse {
    static TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    DiscoverSimilarAdapter ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    ImageView back_arrow;
    TextView ActionbarTitle;
    String actionbartitle;

    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    LinearLayoutManager llm;
    String counter;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite__designer);
        try {
            Bundle b = getIntent().getExtras();
            actionbartitle = b.getString("ActionbarTitle");
        }catch (NullPointerException e){

        }
        sessionManager=  new SessionManager(DiscoverSimilarActivity.this);
        alert = new AlertDialogManager();
        icd = new InternetConnectionDetector(DiscoverSimilarActivity.this);
        SetupView();
    }
    private void SetupView() {

        ActionbarTitle  = (TextView) findViewById(R.id.favorite_designer_text);
        reclyclerview_wrapper = (RelativeLayout)findViewById(R.id.reclyclerview_wrapper);
        pagecount = (TextView)findViewById(R.id.pagecount);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);
        animation_layout = (RelativeLayout)findViewById(R.id.animation_layout);
        recList = (RecyclerView)findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm= new LinearLayoutManager(DiscoverSimilarActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


//        if(AllProductsModelBase.getInstance().getDiscoverSimilarProductData()!= null){
//            if(AllProductsModelBase.getInstance().getDiscoverSimilarProductData().size() > 0){
//                ca = new DiscoverSimilarAdapter(AllProductsModelBase.getInstance().getDiscoverSimilarProductData(),DiscoverSimilarActivity.this);
//                recList.setAdapter(ca);
//            }else{
//                icd = new InternetConnectionDetector(DiscoverSimilarActivity.this);
//                if(icd.isConnectingToInternet()){
//                    ExecuteServerRequest();
//                }else{
//                    alert.showAlertDialog(DiscoverSimilarActivity.this,"Alert","You dont have internet connection",false);
//                }
//            }
//        }else{

            if(icd.isConnectingToInternet()){
                ExecuteServerRequest();
            }else{
                alert.showAlertDialog(DiscoverSimilarActivity.this,"Alert","You dont have internet connection",false);
            }
//        }

                //  TemplateCount.setText("1 of " + templetelist.length());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  pagecount.setText("1 of "+productList.size());
                recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                      //       Toast.makeText(DiscoverSimilarActivity.this, "pos:" + llm.findLastCompletelyVisibleItemPosition(), Toast.LENGTH_SHORT).show();
                        try {

                            if((llm.findLastCompletelyVisibleItemPosition()+1)==0){
                                pagecount.setText(""+(counter));
                            }else {

                                    pagecount.setVisibility(View.VISIBLE);
                                    pagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of " + AllProductsModelBase.getInstance().getDiscoverSimilarProductData().size());
                                    counter = "" + (llm.findLastCompletelyVisibleItemPosition()+1) + " of " + AllProductsModelBase.getInstance().getDiscoverSimilarProductData().size();


                            }
                        }catch (NullPointerException e){

                        }

                    }
                });
            }
        }, 200);

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
        paramslist.add(new BasicNameValuePair("user_id",sessionManager.getUserID()));
        asyncReuse = new AsyncReuse(DiscoverSimilarActivity.this, Constants.APP_MAINURL +"productjsons/discover_simmilar.json", Constants.GET, paramslist, true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    @Override
    public void onClick(View v) {

    }

    public void TransferNextActivtiy(){
        Intent i = new Intent(DiscoverSimilarActivity.this,ActivityDetails.class);
        startActivity(i);
    }


    @Override
    public void GetData(String response) {
        if (response!=null){
            AllProductsModelBase.getInstance().GetDiscoverSimilarDataFromServer(response);
            if(AllProductsModelBase.getInstance().getDiscoverSimilarProductData().size() > 0){
                ca = new DiscoverSimilarAdapter(AllProductsModelBase.getInstance().getDiscoverSimilarProductData(),DiscoverSimilarActivity.this);
                recList.setAdapter(ca);
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert.showAlertDialog(DiscoverSimilarActivity.this,"Alert","You dont have internet connection",false);
                    }
                });
            }
        }
    }
}
