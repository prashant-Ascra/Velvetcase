package com.velvetcase.app.material.fragments;

import android.content.Context;
import android.os.Bundle;
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

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.DesignersAdapters;
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
public class DesignerFragment extends Fragment implements View.OnClickListener,GetResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static TextView txt_pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    DesignersAdapters ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    SessionManager session;
    LinearLayoutManager llm;
    private String counter;
    int pageNumber = 0;


    public static DesignerFragment newInstance(String param1, String param2) {
        DesignerFragment fragment = new DesignerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = DesignerFragment.class
            .getSimpleName();

    public static DesignerFragment newInstance() {
        return new DesignerFragment();
    }
    public DesignerFragment() {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        SetupView(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(AllProductsModelBase.getInstance().getDesignerList()!= null){
            if(AllProductsModelBase.getInstance().getDesignerList().size() > 0){
                ca = new DesignersAdapters(AllProductsModelBase.getInstance().getDesignerList(),getActivity());
                recList.setAdapter(ca);
                txt_pagecount.setText("1 of " + AllProductsModelBase.getInstance().getDesignerList().size());
            }else{
                icd = new InternetConnectionDetector(getActivity());
                if(icd.isConnectingToInternet()){
                    ExecuteServerRequest();
                }else{
                    alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
                }
            }
        }else{

            if(icd.isConnectingToInternet()){
                session=new SessionManager(getActivity());
                ExecuteServerRequest();
            }else{
                alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
            }
        }

        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                if ((llm.findLastCompletelyVisibleItemPosition() + 1) == 0) {
                    txt_pagecount.setText("" + counter);
                } else {
                    txt_pagecount.setVisibility(View.VISIBLE);
//                            if (pageNumber == AllProductsModelBase.getInstance().getDesignerList().size()) {
                    txt_pagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of " +  AllProductsModelBase.getInstance().getDesignerList().size());
//                            } else {
//                                pagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition()) + " of " + AllProductsModelBase.getInstance().getDesignerList().size());
//                            }
                    counter = "" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of " + AllProductsModelBase.getInstance().getDesignerList().size();
                    //  pageNumber = llm.findLastCompletelyVisibleItemPosition() + 1;


                }
            }
        });

    }
    private void SetupView(View v) {
        reclyclerview_wrapper = (RelativeLayout) v.findViewById(R.id.reclyclerview_wrapper);
        txt_pagecount = (TextView) v.findViewById(R.id.pagecount);
        win_btn = (ImageButton) v.findViewById(R.id.Win_btn);
        win_btn.setOnClickListener(this);
        animation_layout = (RelativeLayout) v.findViewById(R.id.animation_layout);
        recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if(AllProductsModelBase.getInstance().getDesignerList().size() > 0){
                ca = new DesignersAdapters(AllProductsModelBase.getInstance().getDesignerList(),getActivity());
                recList.setAdapter(ca);
                txt_pagecount.setText("1 of " + AllProductsModelBase.getInstance().getDesignerList().size());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

//        ca = new DesignersAdapters(AllProductsModelBase.getInstance().getDesignerList(),getActivity());
//        recList.setAdapter(ca);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void GetData(String response) {
        if (response != null){
            AllProductsModelBase.getInstance().GetDesignerDataFromServer(response);
            if(AllProductsModelBase.getInstance().getDesignerList().size() > 0){
                ca = new DesignersAdapters(AllProductsModelBase.getInstance().getDesignerList(),getActivity());
                recList.setAdapter(ca);
                txt_pagecount.setText("1 of " + AllProductsModelBase.getInstance().getDesignerList().size());
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

    public void ExecuteServerRequest(){
        List<NameValuePair> paramslist = null;
        paramslist = new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
        asyncReuse = new AsyncReuse(getActivity(), Constants.APP_MAINURL+"productjsons/designer_list.json", Constants.GET,paramslist,true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }
}
