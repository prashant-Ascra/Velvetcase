package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import com.velvetcase.app.material.adapters.SavedLooksAdapter;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavesLooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavesLooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavesLooksFragment extends Fragment implements View.OnClickListener,GetResponse {
    RecyclerView recList;
    SavedLooksAdapter ca;
    Context context;
    Button select_template;

    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    SweetAlertDialogManager sweet_alert;
    String Request_type;
    SessionManager session;
    TextView template_count;
    String counter;
    int page_count = 0 ;
    int list_size = 0;
    TextView emptry_msg,select_template_txt;

    public static final String TAG = SavesLooksFragment.class
            .getSimpleName();

    public static SavesLooksFragment newInstance() {
        return new SavesLooksFragment();
    }
    public SavesLooksFragment() {
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
        select_template_txt=(TextView)v.findViewById(R.id.textView12);
        recList = (RecyclerView) v.findViewById(R.id.TemplatecardList);
        template_count=(TextView)v.findViewById(R.id.template_count);
        emptry_msg=(TextView)v.findViewById(R.id.empty_msg);
        recList.setHasFixedSize(true);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
                icd = new InternetConnectionDetector(getActivity());
                if(icd.isConnectingToInternet()){
                    ExecuteServerRequest();
                }else{
                    alert.showAlertDialog(getActivity(),"Alert","You dont have internet connection",false);
                }
        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //     Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                if ((llm.findLastVisibleItemPosition() + 1) == 0) {
                    template_count.setText("" + counter);
                } else {
                    template_count.setVisibility(View.VISIBLE);
                    if (page_count == list_size) {
                        template_count.setText("" + (llm.findLastVisibleItemPosition() + 1) + " of " + list_size);
                    } else {
                        template_count.setText("" + (llm.findLastVisibleItemPosition()) + " of " + list_size);
                    }
                    counter = "" + (llm.findLastVisibleItemPosition() + 1) + " of " + list_size;
                    page_count = llm.findLastVisibleItemPosition() + 1;
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void GetData(String response) {
        if (response != null) {
            if(Request_type.equalsIgnoreCase("All_Looks")) {
                AllProductsModelBase.getInstance().GetSavedLooksDataFromServer(response);
                if (AllProductsModelBase.getInstance().getSavedLooksProductData().size() > 0) {
                 //   select_template_txt.setVisibility(View.VISIBLE);
                    Template template = AllProductsModelBase.getInstance().getSavedLooksProductData().get(0);
                    JSONArray lookslist = template.gettemplatelist();
                    GetProductId get_pid = new GetProductId();

                    list_size= lookslist.length();
                    ca = new SavedLooksAdapter(lookslist, getActivity(), get_pid);
                    recList.setAdapter(ca);
                    if(list_size>0){
                        template_count.setVisibility(View.VISIBLE);
                        emptry_msg.setVisibility(View.GONE);
                        template_count.setText("1 of " + lookslist.length());
                        select_template_txt.setVisibility(View.VISIBLE);
                    }else{
                        template_count.setVisibility(View.GONE);
                        emptry_msg.setVisibility(View.VISIBLE);
                        select_template_txt.setVisibility(View.INVISIBLE);
                    }

                } else {
                    alert.showAlertDialog(getActivity(), "Alert", "You dont have internet connection", false);
                }
            }
            else if(Request_type.equalsIgnoreCase("single_product_detail")) {
                AllProductsModelBase.getInstance().GetSingleProductDataFromServer(response);
                if (AllProductsModelBase.getInstance().getSingleProductData().size() > 0) {
                    AllProductsModelBase.getInstance().setProductSelectionFlag("SavedLooks");
                    Intent intent = new Intent(getActivity(), ActivityDetails.class);
                    startActivity(intent);
                }
            }
        }
    }

    public  class GetProductId implements SavedLooksAdapter.getproduct_id {
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
        List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
        asyncReuse = new AsyncReuse(getActivity(), Constants.APP_MAINURL + "productjsons/user_looks_api.json", Constants.GET, paramslist, true);
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
