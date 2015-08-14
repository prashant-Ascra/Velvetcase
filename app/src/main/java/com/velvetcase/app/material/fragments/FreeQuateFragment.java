package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.MainActivity;
import com.velvetcase.app.material.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FreeQuateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FreeQuateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FreeQuateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

 // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView upload_from_gallary,Take_a_photo;
    ImageView call_btn;


    public static FreeQuateFragment newInstance(String param1, String param2) {
        FreeQuateFragment fragment = new FreeQuateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = FreeQuateFragment.class
            .getSimpleName();

    public static FreeQuateFragment newInstance() {
        return new FreeQuateFragment();
    }
    public FreeQuateFragment() {

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
       View v = inflater.inflate(R.layout.fragment_free_quate, container, false);
       BuildView(v);
        ((MainActivity)getActivity()).settabstrip(2);
        return v;
    }

    public void BuildView(View v){
        upload_from_gallary = (TextView) v.findViewById(R.id.upload_from_gallary_text);
        Take_a_photo = (TextView) v.findViewById(R.id.take_a_photo_text);
        call_btn=(ImageView)v.findViewById(R.id.imageView4);

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+919029835838" ));
                startActivity(intent);
            }
        });
        upload_from_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPhotoFragment newFragment = new UploadPhotoFragment ();

                Bundle bundle = new Bundle();
                bundle.putString("key","gallary");
                newFragment.setArguments(bundle);

                ((MainActivity)getActivity()).addFragmentInsideContainer(newFragment,
                        UploadPhotoFragment.TAG);
            }
        });

        Take_a_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPhotoFragment newFragment = new UploadPhotoFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("key","camera");
                newFragment.setArguments(bundle);
                ((MainActivity)getActivity()).addFragmentInsideContainer(newFragment,
                        UploadPhotoFragment.TAG);
            }
        });
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
