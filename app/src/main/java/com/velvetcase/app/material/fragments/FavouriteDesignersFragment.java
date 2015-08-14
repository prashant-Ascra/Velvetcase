package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.velvetcase.app.material.R;
import com.velvetcase.app.material.adapters.Favorite_Designer_Adapter;

/**
 * Created by Prashant Patil on 06-06-2015.
 */
public class FavouriteDesignersFragment  extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static TextView pagecount;
    RelativeLayout animation_layout;
    ImageButton win_btn;
    Boolean winflag = true;
    Boolean Layoutflag = true;
    RecyclerView recList;
    Favorite_Designer_Adapter ca;
    Context context;
    RelativeLayout reclyclerview_wrapper;
    OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static FavouriteDesignersFragment newInstance(String param1, String param2) {
        FavouriteDesignersFragment fragment = new FavouriteDesignersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = FavouriteDesignersFragment.class
            .getSimpleName();

    public static FavouriteDesignersFragment newInstance() {
        return new FavouriteDesignersFragment();
    }
    public FavouriteDesignersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        pagecount = (TextView) v.findViewById(R.id.pagecount);
        win_btn = (ImageButton) v.findViewById(R.id.Win_btn);
        win_btn.setOnClickListener(this);
        animation_layout = (RelativeLayout) v.findViewById(R.id.animation_layout);


        recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

//        ca = new Favorite_Designer_Adapter(createList(30),getActivity());
//        recList.setAdapter(ca);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
    public void onClick(View v) {

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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
