package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.AsyncReuse;
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;
import com.velvetcase.app.material.Models.Template;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.CustomExpandableListAdapter;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.nirhart.parallaxscroll.views.ParallaxExpandableListView;
import com.squareup.picasso.Picasso;
import com.wizrocket.android.sdk.WizRocketAPI;
import com.wizrocket.android.sdk.exceptions.WizRocketMetaDataNotFoundException;
import com.wizrocket.android.sdk.exceptions.WizRocketPermissionsNotSatisfied;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Template_detail extends Fragment implements GetResponse{
    public static ArrayList<Bitmap> selectedvariants = new ArrayList<Bitmap>();
    public static ArrayList<String> selectedvariantsIDS = new ArrayList<String>();
    ExpandableListView expandablelist;
    List<String> listDataHeader;
    HashMap<String, List<JSONObject>> listDataChild;
    ParallaxExpandableListView listView;
    ImageView img_save,selection_image1,selection_image2,selection_image3;
    int item=1;
    View back_green1,back_green2,back_green3;
    AlertDialogManager alertmanager;
    ImageView backgroundIMG;
    public static final String TAG = Template_detail.class
            .getSimpleName();
    int FlagCount = 0;
    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    SessionManager session;

    private GoogleAnalytics analytics;
    private Tracker tracker;
    static WizRocketAPI wr;

    public static Template_detail newInstance() {
        return new Template_detail();
    }
    public Template_detail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertmanager = new AlertDialogManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_template_detail, container, false);
        listView = (ParallaxExpandableListView) v.findViewById(R.id.list_view);
        View v1 = inflater.inflate(R.layout.custom_template_detail_header, null, false);

        session = new SessionManager(getActivity());
        img_save = (ImageView) v1.findViewById(R.id.img_save);
        img_save.setVisibility(View.GONE);
        backgroundIMG = (ImageView) v1.findViewById(R.id.template_image);
        back_green1 = (View)v1.findViewById(R.id.back_green1);
        back_green2 = (View)v1.findViewById(R.id.back_green2);
        back_green3 = (View)v1.findViewById(R.id.back_green3);
        selection_image1 = ((ImageView)v1.findViewById(R.id.variation_img1));
        selection_image2 = (ImageView)v1.findViewById(R.id.variation_img2);
        selection_image3 = (ImageView)v1.findViewById(R.id.variation_img3);

        listView.addParallaxedHeaderView(v1);  // add header in listview
        prepareListData();
        User_Selection  user_selection = new User_Selection();
        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(LayoutInflater.from(getActivity()),getActivity(),listDataHeader, listDataChild,user_selection,item);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity(),
                        groupPosition
                                + " : "
                                + childPosition, Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedvariants.size()>= 3){
                    ExecuteServerRequest();
                    /*send parameter to tracking */
                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Create Look")
                            .setAction(session.get_occasion_name())
                            .setLabel(AllProductsModelBase.getInstance().getTemplateID())
                            .build());

                      /*send parameter to Wizrocket tracking */
                    HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
                    prodViewedAction.put("Occasion", session.get_occasion_name());
                    prodViewedAction.put("TemplateName", session.get_occasion_name()+" Template");
                    wr.event.push("Create Look (MO)", prodViewedAction);
                }else {
                    alertmanager.showAlertDialog(getActivity(),"Alert","Please select three variants",false);
                }
            }
        });
        String Bgimgpath =  AllProductsModelBase.getInstance().getProductBGImg();
        Picasso.with(getActivity()).load(Bgimgpath).into(backgroundIMG);

            /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(getActivity());
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        tracker.setScreenName("Create Look");
         /*analytic tracking code end*/

        try{
            wr = WizRocketAPI.getInstance(getActivity().getApplicationContext());
            WizRocketAPI.setDebugLevel(1);
        } catch (WizRocketMetaDataNotFoundException e) {
            e.printStackTrace();
            // The WizRocketMetaDataNotFoundException is thrown when you haven�t specified your WizRocket Account ID and/or the Account Token in your AndroidManifest.xml
        } catch (WizRocketPermissionsNotSatisfied e) {
            e.printStackTrace();
            // WizRocketPermissionsNotSatisfiedException is thrown when you haven�t requested the required permissions in your AndroidManifest.xml
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearList();
    }

    public void clearList(){
        Template_detail.selectedvariants.clear();
        Template_detail.selectedvariantsIDS.clear();
    }
    public void ExecuteServerRequest(){
        List<NameValuePair> paramslist = new ArrayList<NameValuePair>();
        StringBuilder sb = new StringBuilder();
        if (selectedvariantsIDS.size()>= 3){
            for (String str : selectedvariantsIDS){
                sb.append(str.toString());
                sb.append(",");
            }
        }
        paramslist.add(new BasicNameValuePair("product_id",sb.toString()));
        paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
        paramslist.add(new BasicNameValuePair("template_id",AllProductsModelBase.getInstance().getTemplateID()));
        asyncReuse = new AsyncReuse(getActivity(), Constants.APP_MAINURL +"tokens/save_looks", Constants.POST, paramslist, true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    @Override
    public void GetData(String response) {
        if (response!=null){
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equalsIgnoreCase("Success")){
                    clearList();
                    session.set_share_url(obj.getString("save_look_image"));
                    ((MainActivity)getActivity()).addFragmentInsideContainer(CreatePageThanksFragment.newInstance(),
                            CreatePageThanksFragment.TAG);
                }else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertmanager.showAlertDialog(getActivity(),"Alert","Something went wrong",false);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public  class User_Selection implements CustomExpandableListAdapter.get_user_selection {
        @Override
        public void select_product(Bitmap img,int posi,String pid,Boolean flag) {
            if(flag) {
                selectedvariants.add(img);
                selectedvariantsIDS.add(pid);
                if(selectedvariants!=null){
                    for (int i = 0; i <selectedvariants.size(); i++) {
                        if(i == 0)
                            selection_image1.setImageBitmap(selectedvariants.get(i));
                        if(i == 1)
                            selection_image2.setImageBitmap(selectedvariants.get(i));
                        if(i == 2)
                            selection_image3.setImageBitmap(selectedvariants.get(i));
                    }
                        img_save.setVisibility(View.VISIBLE);
                }
            }else{
                int var_pos = selectedvariants.indexOf(img);
                if(var_pos == 0) {
                    selectedvariants.remove(var_pos);
                    selectedvariantsIDS.remove(var_pos);
                    selection_image1.setImageResource(R.drawable.transferent_box);
                }
                if(var_pos == 1) {
                    selectedvariants.remove(var_pos);
                    selectedvariantsIDS.remove(var_pos);
                    selection_image2.setImageResource(R.drawable.transferent_box);
                }
                if(var_pos == 2) {
                    selectedvariants.remove(var_pos);
                    selectedvariantsIDS.remove(var_pos);
                    selection_image3.setImageResource(R.drawable.transferent_box);
                }

                if(selectedvariants.size() == 0){
                    selection_image1.setImageResource(R.drawable.transferent_box);
                    selection_image2.setImageResource(R.drawable.transferent_box);
                    selection_image3.setImageResource(R.drawable.transferent_box);
                }
            }
        }
    }

    private void SetupUI(View v) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void prepareListData() {
        ArrayList<Template> Templatelist = new ArrayList<Template>();
        if (AllProductsModelBase.getInstance().getTemplateSelectionFlag().equalsIgnoreCase("LooksFlag")){
            Templatelist = AllProductsModelBase.getInstance().getLooksList();
        }else if(AllProductsModelBase.getInstance().getTemplateSelectionFlag().equalsIgnoreCase("TemplateFlag")){
            Templatelist = AllProductsModelBase.getInstance().getTemplateList();
        }else if(AllProductsModelBase.getInstance().getTemplateSelectionFlag().equalsIgnoreCase("SavedLooksFlag")){
            Templatelist = AllProductsModelBase.getInstance().getSavedLooksProductData();
        }

        if(Templatelist!= null){
            if(Templatelist.size() > 0){
                Template template = Templatelist.get(0);
                JSONArray pendantslist = template.getpendantslist();
                JSONArray ringslist = template.getringslist();
                JSONArray earringslist = template.getearringslist();
                JSONArray bangleslist = template.getbangleslist();
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<JSONObject>>();
                // Adding child data
                listDataHeader.add("ADD RINGS");
                listDataHeader.add("ADD EARRINGS");
                listDataHeader.add("ADD PENDANTS & NECKLACES");
                listDataHeader.add("ADD BANGLES& BRACELETS");

                // Adding child data
                List<JSONObject> rings_list = new ArrayList<JSONObject>();
                for (int i = 0; i<ringslist.length(); i++ ){
                    try {
                        JSONObject ringsobj = ringslist.getJSONObject(i);
                        rings_list.add(ringsobj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Adding child data
                List<JSONObject> earrings_list = new ArrayList<JSONObject>();
                for (int i = 0; i<earringslist.length(); i++ ){
                    try {
                        JSONObject earringsobj = earringslist.getJSONObject(i);
                        earrings_list.add(earringsobj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Adding child data
                List<JSONObject> bangles_list = new ArrayList<JSONObject>();
                for (int i = 0; i<bangleslist.length(); i++ ){
                    try {
                        JSONObject banglessobj = bangleslist.getJSONObject(i);
                        bangles_list.add(banglessobj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Adding child data
                List<JSONObject> pendants_list = new ArrayList<JSONObject>();
                for (int i = 0; i<pendantslist.length(); i++ ){
                    try {
                        JSONObject pendantsobj = pendantslist.getJSONObject(i);
                        pendants_list.add(pendantsobj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listDataChild.put(listDataHeader.get(0), rings_list); // Header, Child data
                listDataChild.put(listDataHeader.get(1), earrings_list);
                listDataChild.put(listDataHeader.get(2), bangles_list);
                listDataChild.put(listDataHeader.get(3), pendants_list);
            }
        }
    }
}
