package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.velvetcase.app.material.ActivityDetails;
import com.velvetcase.app.material.AsyncReuse;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;
import com.velvetcase.app.material.Models.Template;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.StaticTemplateScreenAdapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.velvetcase.app.material.util.SweetAlertDialogManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant Patil on 06-06-2015.
 */
public class StaticTemplateScreen  extends Fragment  implements View.OnClickListener,GetResponse {
    RecyclerView recList;
    StaticTemplateScreenAdapter ca;
    Context context;
    Button select_template;

    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    SweetAlertDialogManager sweet_alert;
    String Request_type;
    SessionManager session;
    TextView templatepagecount;
    String total_count;
    Template template;

    public static final String TAG = StaticTemplateScreen.class
            .getSimpleName();
    private String counter;
   LinearLayoutManager llm;
    JSONArray lookslist;
     Handler handler;
    TextView select_template_txt;

    public static StaticTemplateScreen newInstance() {
        return new StaticTemplateScreen();
    }
    public StaticTemplateScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new AlertDialogManager();
        sweet_alert=new SweetAlertDialogManager();
        icd = new InternetConnectionDetector(getActivity());
        session = new SessionManager(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_template_screen, container, false);
        SetupView(v);
        return v;
    }

    private void SetupView(View v) {
        recList = (RecyclerView) v.findViewById(R.id.TemplatecardList);
        recList.setHasFixedSize(true);
        templatepagecount = (TextView) v.findViewById(R.id.templatepagecount);
        select_template_txt=(TextView)v.findViewById(R.id.textView12);
        alert=new AlertDialogManager();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        if(AllProductsModelBase.getInstance().getLooksList()!= null){
            if(AllProductsModelBase.getInstance().getLooksList().size() > 0){
               template = AllProductsModelBase.getInstance().getLooksList().get(0);
                select_template_txt.setVisibility(View.VISIBLE);
                lookslist = template.gettemplatelist();
                total_count=""+lookslist.length();
                GetProductId get_pid=new GetProductId();
                ca = new StaticTemplateScreenAdapter(lookslist,getActivity(),get_pid);
                recList.setAdapter(ca);




            }else{
                select_template_txt.setVisibility(View.INVISIBLE);
                icd = new InternetConnectionDetector(getActivity());
                if(icd.isConnectingToInternet()){
                    ExecuteServerRequest();
//                    threadMsg("done");
                }else{
                    alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
                }
            }
        }else{

            if(icd.isConnectingToInternet()){
                ExecuteServerRequest();
            }else{
                alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
            }
        }


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
                            templatepagecount.setText(""+counter);
                        }else {
                            templatepagecount.setVisibility(View.VISIBLE);
                            templatepagecount.setText("" + (llm.findLastCompletelyVisibleItemPosition() + 1) + " of "+total_count);
                            counter=""+(llm.findLastCompletelyVisibleItemPosition() + 1)+" of "+total_count;

                        }
                    }
                });
            }
        }, 900);

//        handler = new Handler() {
//
//            public void handleMessage(Message msg) {
//
//                String aResponse = msg.getData().getString("message");
//
//                if ((null != aResponse)) {
//                    total_count=""+lookslist.length();
//                }
//                else
//                {
//
//                    // ALERT MESSAGE
//
//                }
//
//            }
//        };


    }


//    public void threadMsg(String msg) {
//        if (!msg.equals(null) && !msg.equals("")) {
//            Message msgObj = handler.obtainMessage();
//            Bundle b = new Bundle();
//            b.putString("message", msg);
//            msgObj.setData(b);
//            handler.sendMessage(msgObj);
//        }
//    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void GetData(String response) {

        if (response != null) {
            if (Request_type == "All_Looks") {
                Log.e("Response:",""+response);
                AllProductsModelBase.getInstance().GetLooksDataFromServer(response);
                if (AllProductsModelBase.getInstance().getLooksList()!=null) {
                    Template template = AllProductsModelBase.getInstance().getLooksList().get(0);
                    JSONArray lookslist = template.gettemplatelist();
                    GetProductId get_pid = new GetProductId();
                    ca = new StaticTemplateScreenAdapter(lookslist, getActivity(), get_pid);
                    recList.setAdapter(ca);
                    templatepagecount.setText("1 of " + lookslist.length());
                    total_count=""+lookslist.length();


                } else {
                    alert.showAlertDialog(getActivity(), "Alert", "You dont have internet connection", false);
                }
            } else {
                AllProductsModelBase.getInstance().GetSingleProductDataFromServer(response);
                if (AllProductsModelBase.getInstance().getSingleProductData().size() > 0) {
                    AllProductsModelBase.getInstance().setProductSelectionFlag("Looks");
                    Intent intent = new Intent(getActivity(), ActivityDetails.class);
                    startActivity(intent);
                }
            }
        }
    }

    public  class GetProductId implements StaticTemplateScreenAdapter.getproduct_id {
        @Override
        public void product_id_provider(String product_id) {
            ExecuteServerRequest(product_id);
        }
    }

    @Override
    public void onClick(View v) {

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void ExecuteServerRequest(){
        Request_type="All_Looks";
        List<NameValuePair> paramslist = null;
        asyncReuse = new AsyncReuse(getActivity(), Constants.APP_MAINURL + "productjsons/all_looks_api.json", Constants.GET, paramslist, true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }
    public void ExecuteServerRequest(String p_id){
        Request_type="single_product_detail";
        AllProductsModelBase.getInstance().setProductID(Integer.parseInt(p_id));
        List<NameValuePair> paramslist= new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("product_id",""+p_id));
        asyncReuse = new AsyncReuse(getActivity(),
                Constants.APP_MAINURL+"productjsons/single_product.json", Constants.GET,paramslist,true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

}
