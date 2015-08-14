package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.AsyncReuse;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;
import com.velvetcase.app.material.Models.Products;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.SingleDesignerCustomAdapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link SingleDesigner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleDesigner extends Fragment implements GetResponse{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String counter;

    static TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    SingleDesignerCustomAdapter ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    LinearLayoutManager llm;
    ArrayList<Products> productList = null;
    AsyncReuse asyncReuse;
    AlertDialogManager alert;
    InternetConnectionDetector icd;
    SessionManager sessionManager;
    int page_count = 0;
    private GoogleAnalytics analytics;
    private Tracker tracker;

    // TODO: Rename and change types and number of parameters
    public static SingleDesigner newInstance(String param1, String param2) {
        SingleDesigner fragment = new SingleDesigner();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = SingleDesigner.class
            .getSimpleName();

    public static SingleDesigner newInstance() {
        return new SingleDesigner();
    }
    public SingleDesigner() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        alert = new AlertDialogManager();
        icd = new InternetConnectionDetector(getActivity());
                  /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(getActivity());
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
         /*analytic tracking code end*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single_designer, container, false);
        SetupView(v);
        return v;
    }

    private void SetupView(View v) {
        reclyclerview_wrapper = (RelativeLayout) v.findViewById(R.id.reclyclerview_wrapper);
        pagecount = (TextView) v.findViewById(R.id.pagecount);
        animation_layout = (RelativeLayout) v.findViewById(R.id.animation_layout);
        recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm= new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        sessionManager=new SessionManager(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if(AllProductsModelBase.getInstance().getSingleDesignerList()!= null){
//            if(AllProductsModelBase.getInstance().getSingleDesignerList().size() > 0){
//                ca = new SingleDesignerCustomAdapter(AllProductsModelBase.getInstance().getSingleDesignerList(),getActivity());
//                recList.setAdapter(ca);
//            }else{
                icd = new InternetConnectionDetector(getActivity());
                if(icd.isConnectingToInternet()){
                    ExecuteServerRequest();
                }else{
                    alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
                }
//            }
//        }else{
//            if(icd.isConnectingToInternet()){
//                ExecuteServerRequest();
//            }else{
//                alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
//            }
//        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              //  pagecount.setText("1 of "+productList.size());
                recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        //     Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                        if((llm.findLastCompletelyVisibleItemPosition()+1)==0){
                            pagecount.setText(""+counter);
                        }else {
                            pagecount.setVisibility(View.VISIBLE);
                            if(page_count == productList.size()){
                                pagecount.setText("" + (llm.findLastVisibleItemPosition() + 1) + " of " + productList.size());
                            }else{
                                pagecount.setText("" + (llm.findLastVisibleItemPosition()) + " of " + productList.size());
                            }
                            counter=""+(llm.findLastVisibleItemPosition() + 1)+" of "+productList.size();
                            page_count = llm.findLastVisibleItemPosition() + 1 ;
                        }
                    }
                });
            }
        }, 200);


    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public  void update(){
        if(AllProductsModelBase.getInstance().getSingleDesignerList()!=null) {
            if(AllProductsModelBase.getInstance().getSingleDesignerList().size()>0) {
                try {
                    ca = new SingleDesignerCustomAdapter(AllProductsModelBase.getInstance().getSingleDesignerList(), getActivity());
                    recList.setAdapter(ca);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void ExecuteServerRequest(){
        List<NameValuePair> paramslist = new ArrayList<NameValuePair>();

        int designer_id = AllProductsModelBase.getInstance().getDesignerID();
        paramslist.add(new BasicNameValuePair("designer_id",""+designer_id));
        paramslist.add(new BasicNameValuePair("user_id",""+sessionManager.getUserID()));


        asyncReuse = new AsyncReuse(getActivity(), Constants.APP_MAINURL+"productjsons/single_designer.json", Constants.GET,paramslist,true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }
    @Override
    public void GetData(String response) {

        if (response!=null){
            AllProductsModelBase.getInstance().GetSingleDesignerDataFromServer(response);
            if(AllProductsModelBase.getInstance().getSingleDesignerList().size() > 0){
                ca = new SingleDesignerCustomAdapter(AllProductsModelBase.getInstance().getSingleDesignerList(),getActivity());
                productList=AllProductsModelBase.getInstance().getSingleDesignerList();
                recList.setAdapter(ca);
                pagecount.setVisibility(View.VISIBLE);
                pagecount.setText("1 of " + productList.size());


            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert.showAlertDialog(getActivity(), "Alert", "You dont have internet connection", false);
                    }
                });

            }
        }
    }
}
