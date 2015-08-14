package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.AsyncReuse;
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.MainApplicationActivity;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.Create;
import com.velvetcase.app.material.Models.InternetConnectionDetector;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.Template_Adapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.velvetcase.app.material.util.Template_model;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.velvetcase.app.material.fragments.CreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.velvetcase.app.material.fragments.CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment implements GetResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GridView gridview;
    // TODO: Rename and change types of parameters
    ArrayList<Template_model>template_list;
    Template_Adapter temp_adpter;
    private String mParam1;
    private String mParam2;

    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    public static String Requestby = null;
    int Ocasion_id = 0;
    SessionManager sessionManager;


    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = CreateFragment.class
            .getSimpleName();

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }
    public CreateFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create, container, false);
        sessionManager=new SessionManager(getActivity());
        gridview=(GridView)v.findViewById(R.id.grid_view);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                icd = new InternetConnectionDetector(getActivity());
                if(icd.isConnectingToInternet()){
                    if(AllProductsModelBase.getInstance().getCreateList()!= null) {
                        Create c = AllProductsModelBase.getInstance().getCreateList().get(position);
                        Ocasion_id = c.getcreate_id();
                        String occasion_name=c.getcreate_name();
                        sessionManager.set_occasion_name(occasion_name);
                        Requestby = "TemplateRequest";
                        ExecuteServerRequest();
                    }
                }else{
                    alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
                }
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(AllProductsModelBase.getInstance().getCreateList()!= null){
            if(AllProductsModelBase.getInstance().getCreateList().size() > 0){
                temp_adpter = new Template_Adapter(getActivity(),AllProductsModelBase.getInstance().getCreateList());
                gridview.setAdapter(temp_adpter);
            }else{
                icd = new InternetConnectionDetector(getActivity());
                if(icd.isConnectingToInternet()){
                    Requestby = "CreateRequest";
                    ExecuteServerRequest();
                }else{
                    alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
                }
            }
        }else{

            if(icd.isConnectingToInternet()){
                Requestby = "CreateRequest";
                ExecuteServerRequest();
            }else{
                alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
            }
        }
        ((MainActivity)getActivity()).settabstrip(1);
    }

    public void ExecuteServerRequest(){
        List<NameValuePair> paramslist = null;
        if(Requestby.equalsIgnoreCase("CreateRequest")) {
            asyncReuse = new AsyncReuse(getActivity(), Constants.APP_MAINURL + "productjsons/createlist.json", Constants.GET, paramslist, false);
        }else{
            paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("occasion_id",""+Ocasion_id));
            asyncReuse = new AsyncReuse(getActivity(), Constants.APP_MAINURL + "productjsons/occasion_wise_template.json", Constants.GET, paramslist, true);
        }
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }



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
        if (response != null){
            if(Requestby.equalsIgnoreCase("CreateRequest")) {
                AllProductsModelBase.getInstance().GetCreateDataFromServer(response);
                if (AllProductsModelBase.getInstance().getCreateList().size() > 0) {
                    temp_adpter = new Template_Adapter(getActivity(), AllProductsModelBase.getInstance().getCreateList());
                    gridview.setAdapter(temp_adpter);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(getActivity(), "Alert", "You dont have internet connection", false);
                        }
                    });
                }
            }else{
                AllProductsModelBase.getInstance().GetTemplateDataFromServer(response);
                if (AllProductsModelBase.getInstance().getTemplateList().size() > 0) {
                    addFragmentInsideContainer(TemplateScreen.newInstance(),TemplateScreen.TAG);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(getActivity(), "Alert", "There is no template available", false);
                        }
                    });
                }
            }
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void addFragmentInsideContainer(Fragment fm, String tag) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.content, fm, tag).commit();
    }

}
