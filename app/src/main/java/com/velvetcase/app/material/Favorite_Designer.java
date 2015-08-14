package com.velvetcase.app.material;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.velvetcase.app.material.adapters.Favorite_Designer_Adapter;
import com.velvetcase.app.material.DBHandlers.FavouriteDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Favorite_Designer extends ActionBarActivity {
    static TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    Favorite_Designer_Adapter ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    ImageView back_arrow;
    FavouriteDatabaseHelper hdHelper;
    ArrayList<HashMap<String, String>> DesignersList;
    TextView error_msg;
    String counter;
    int pageNumber;
    LinearLayoutManager llm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite__designer);

        FindIDS();
        hdHelper = new FavouriteDatabaseHelper(Favorite_Designer.this);
        if(hdHelper.getDesignersList() != null) {

            DesignersList =  hdHelper.getDesignersList();
            pagecount.setText("1 of "+DesignersList.size());
            SetupView();
        }else{
            recList.setVisibility(View.GONE);
            error_msg.setVisibility(View.VISIBLE);
        }

        hdHelper = new FavouriteDatabaseHelper(this);
        if(hdHelper.getDesignersList() != null) {
            DesignersList =  hdHelper.getDesignersList();
        }else{

        }
        SetupView();
    }

    private void FindIDS() {
        error_msg = (TextView) findViewById(R.id.empty_msg);
        reclyclerview_wrapper = (RelativeLayout)findViewById(R.id.reclyclerview_wrapper);
        pagecount = (TextView)findViewById(R.id.pagecount);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);
        animation_layout = (RelativeLayout)findViewById(R.id.animation_layout);
        recList = (RecyclerView)findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(Favorite_Designer.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
     //   pagecount.setText(""+DesignersList.size());
        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                if ((llm.findLastCompletelyVisibleItemPosition() + 1) == 0) {
                    pagecount.setText("" + counter);
                } else {


                            pagecount.setVisibility(View.VISIBLE);
//                            if (pageNumber == AllProductsModelBase.getInstance().getDesignerList().size()) {
                                pagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of " + DesignersList.size());
//                            } else {
//                                pagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition()) + " of " + AllProductsModelBase.getInstance().getDesignerList().size());
//                            }
                            counter = "" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of " + DesignersList.size();
                          //  pageNumber = llm.findLastCompletelyVisibleItemPosition() + 1;


                }
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
        ca = new Favorite_Designer_Adapter(DesignersList,Favorite_Designer.this);
        recList.setAdapter(ca);
    }
    public void NotifyAdapeter(int id){
        hdHelper.open();
        hdHelper.delete_row(id);
        if(hdHelper.getDesignersList() != null) {
            DesignersList =  hdHelper.getDesignersList();
            UpdateListView();
//            ca.notifyDataSetChanged();
        }else{
            recList.setVisibility(View.GONE);
            error_msg.setVisibility(View.VISIBLE);
        }


    }

    public void addFragmentInsideContainer(Fragment fm, String tag) {
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.content, fm, tag).commit();
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
}
