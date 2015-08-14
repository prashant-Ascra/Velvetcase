package com.velvetcase.app.material.fragments;

import android.app.Activity;
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

import com.velvetcase.app.material.DBHandlers.WishListDBHelper;
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.Models.WishListModel;
import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.CustomListAdapter;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.Products;

import com.velvetcase.app.material.util.PageCountListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Discover#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Discover extends Fragment implements View.OnClickListener,PageCountListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    CustomListAdapter ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    int page_count = 0;
     WishListDBHelper wlDBHelper;


    ArrayList<Products> productList = null;
    private String counter;

    public static Discover newInstance(String param1, String param2) {
        Discover fragment = new Discover();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = Discover.class
            .getSimpleName();

    public static Discover newInstance() {
        return new Discover();
    }
    public Discover() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        productList = AllProductsModelBase.getInstance().getProductsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        SetupView(v);
        return v;
    }

    private void SetupView(View v) {
        reclyclerview_wrapper = (RelativeLayout) v.findViewById(R.id.reclyclerview_wrapper);
        wlDBHelper=new WishListDBHelper(getActivity());
        pagecount = (TextView) v.findViewById(R.id.pagecount);
        win_btn = (ImageButton) v.findViewById(R.id.Win_btn);
        win_btn.setOnClickListener(this);
        animation_layout = (RelativeLayout) v.findViewById(R.id.animation_layout);
        recList = (RecyclerView) v.findViewById(R.id.cardList);
        ((MainActivity)getActivity()).settabstrip(0);
        for (final Products product : productList) {
            if(product.get_wish_list_flag()==true){

                WishListModel model = new WishListModel();
                model.setProduct_id(""+product.getproduct_id());
                model.setProduct_json("" + product.getproductJsonobj());

                if(wlDBHelper!=null) {
                    wlDBHelper.open();
                    wlDBHelper.insertWishListTable(model);
                }
            }
        }
       
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recList.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(layoutManager);

            pagecount.setText("1 of "+productList.size());
            ca = new CustomListAdapter(productList,getActivity());
            ca.pageCountListener = this;
            recList.setAdapter(ca);



        //  TemplateCount.setText("1 of " + templetelist.length());
        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //     Toast.makeText(getActivity(),"pos:"+layoutManager.findLastCompletelyVisibleItemPosition(),Toast.LENGTH_SHORT).show();
                if ((layoutManager.findLastVisibleItemPosition() + 1) == 0) {
                    pagecount.setText("" + (counter ) );
                } else {
                    pagecount.setVisibility(View.VISIBLE);

                    if(page_count == productList.size() ) {
                        pagecount.setText("" + (layoutManager.findLastVisibleItemPosition() + 1) + " of " + productList.size());
                    }
                    else {
                        pagecount.setText("" + (layoutManager.findLastVisibleItemPosition()) + " of " + productList.size());
                    }
                    counter = "" + ((layoutManager.findLastVisibleItemPosition() + 1) + " of " + productList.size());
                    page_count = layoutManager.findLastVisibleItemPosition() + 1 ;

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        try{
         //   pagecount.setText("1 of "+productList.size());
            ca = new CustomListAdapter(productList,getActivity());
            ca.pageCountListener = this;
            recList.setAdapter(ca);
        }catch (NullPointerException e){

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

    @Override
    public void onClick(View v) {

    }
    @Override
    public void SetPageCount(int page, int count) {
//        pagecount.setText(page +" of "+count);
    }
}
