package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;
import com.velvetcase.app.material.Models.Template;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.CustomTemplateAdapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.PageCountListener;
import com.velvetcase.app.material.util.SessionManager;

import org.json.JSONArray;


public class TemplateScreen extends Fragment implements PageCountListener {
    RecyclerView recList;
    CustomTemplateAdapter ca;
    Context context;
    Button select_template;
    AlertDialogManager alert;
    InternetConnectionDetector icd;
    TextView TemplateCount;
    TextView error_msg;
    LinearLayoutManager llm;
    String pagecount;
    TextView occasion_name;
    public static final String TAG = TemplateScreen.class
            .getSimpleName();
    private String counter;
     JSONArray templetelist;
    SessionManager sessionManager;

    public static TemplateScreen newInstance() {
        return new TemplateScreen();
    }
    public TemplateScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new AlertDialogManager();
        icd = new InternetConnectionDetector(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_template_screen, container, false);
            SetupView(v);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //  TemplateCount.setText("1 of " + templetelist.length());
                recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        //     Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                        if ((llm.findLastCompletelyVisibleItemPosition() + 1) == 0) {
                            TemplateCount.setText("" + (llm.findLastVisibleItemPosition()+1)+" of "+templetelist.length());
                        } else {
                            TemplateCount.setVisibility(View.VISIBLE);
                            TemplateCount.setText("" + (llm.findLastVisibleItemPosition()+1) + " of " + templetelist.length());
                            counter = "" +((llm.findLastVisibleItemPosition()+1) + " of " + templetelist.length()) ;

                        }
                    }
                });
            }
        }, 200);
            return v;
    }

    private void SetupView(View v) {
        sessionManager=new SessionManager(getActivity());
        occasion_name=(TextView)v.findViewById(R.id.occasion_name);
        recList = (RecyclerView) v.findViewById(R.id.TemplatecardList);
        recList.setHasFixedSize(true);
        TemplateCount = (TextView) v.findViewById(R.id.templatepagecount);

        error_msg = (TextView) v.findViewById(R.id.empty_msg);
        occasion_name.setText(""+sessionManager.get_occasion_name().toUpperCase()+"-"+"Select Template");




    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        if(AllProductsModelBase.getInstance().getTemplateList().size()>0){
            if(AllProductsModelBase.getInstance().getTemplateList().size() > 0){
                Template template = AllProductsModelBase.getInstance().getTemplateList().get(0);
              templetelist = template.gettemplatelist();




                if (templetelist.length() > 0) {
                    ca = new CustomTemplateAdapter(templetelist, getActivity());
                    ca.pageCountListener = this;
                    recList.setAdapter(ca);
                    TemplateCount.setText("1 of " + templetelist.length());
                    error_msg.setVisibility(View.GONE);



                }else{
                    error_msg.setVisibility(View.VISIBLE);
                }
            }else{
                 alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
            }
        }else{
            alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
        }

    }

    public void SetTemplateCount(int page,int count){
        TemplateCount.setText(page+" of "+count);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void SetPageCount(int page, int count) {
//        TemplateCount.setText(page+" of "+count);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
