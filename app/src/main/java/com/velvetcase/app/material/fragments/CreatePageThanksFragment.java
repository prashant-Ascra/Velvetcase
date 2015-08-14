package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.velvetcase.app.material.MainActivity;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.util.SessionManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class CreatePageThanksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView close_icon,call_btn;
    SessionManager sessionManager;
    TextView lookname;
    Button template_btn;
    String selection_media="fb";
    ImageView call;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePageThanksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePageThanksFragment newInstance(String param1, String param2) {
        CreatePageThanksFragment fragment = new CreatePageThanksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ImageView facebook,twitter,pintrest;
    public static final String TAG = CreatePageThanksFragment.class
            .getSimpleName();

    public static CreatePageThanksFragment newInstance() {
        return new CreatePageThanksFragment();
    }
    public CreatePageThanksFragment() {
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
        View v = inflater.inflate(R.layout.social_sharingpage, container, false);
        BuildView(v);
        return v;
    }





    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Log.wtf(TAG, "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }



    private void BuildView(View v) {
        template_btn=(Button)v.findViewById(R.id.template_btn);
        lookname=(TextView)v.findViewById(R.id.lookname);
        sessionManager=new SessionManager(getActivity());
        facebook=(ImageView)v.findViewById(R.id.imageView9);
        twitter=(ImageView)v.findViewById(R.id.twitter_icon);
        pintrest=(ImageView)v.findViewById(R.id.pintrest_icon);
        close_icon = (ImageView) v.findViewById(R.id.imageView5);
        call_btn=(ImageView)v.findViewById(R.id.imageView4);
        lookname.setText(sessionManager.get_occasion_name()+" "+"Template");
        template_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selection_media.toString().equalsIgnoreCase("fb")){
                    facebookclicked();
                }else {
                    if(selection_media.toString().equalsIgnoreCase("twitter")){
                    twitterclicked();
                    }else {
                        pintrestclicked();
                    }
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebook.setBackgroundColor(Color.parseColor("#d3d3d3"));
                twitter.setBackgroundColor(Color.parseColor("#ffffff"));
                twitter.setBackgroundColor(Color.parseColor("#ffffff"));
                selection_media="fb";

            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebook.setBackgroundColor(Color.parseColor("#ffffff"));
                twitter.setBackgroundColor(Color.parseColor("#d3d3d3"));
                pintrest.setBackgroundColor(Color.parseColor("#ffffff"));
                selection_media="twitter";
            }
        });


        pintrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebook.setBackgroundColor(Color.parseColor("#ffffff"));
                twitter.setBackgroundColor(Color.parseColor("#ffffff"));
                pintrest.setBackgroundColor(Color.parseColor("#d3d3d3"));
                selection_media="pin";
            }
        });

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+919029835838" ));
                startActivity(intent);
            }
        });

        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).addFragmentInsideContainer(CreateFragment.newInstance(),
                        CreateFragment.TAG);
            }
        });
    }

    public  void facebookclicked(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
        i.putExtra(Intent.EXTRA_TEXT, ""+sessionManager.get_share_url());
        startActivity(Intent.createChooser(i, "Share URL"));
    }

    public  void twitterclicked(){
        String tweetUrl = String.format(
                "https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode("Hey Followers follow this !" ),
                urlEncode(""+sessionManager.get_share_url()));
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(tweetUrl));
        intent.putExtra(
                "android.intent.extra.TEXT",
                "VelvetCase"
        );

        // Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = getActivity().getPackageManager()
                .queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase()
                    .startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }

    public  void pintrestclicked(){
        String shareUrl = ""+sessionManager.get_share_url();
        String mediaUrl = ""+sessionManager.get_share_url();
        String description = "VelvetCase Template";
        String url = String.format(
                "https://www.pinterest.com/pin/create/button/?url=%s&media=%s&description=%s",
                urlEncode(shareUrl), urlEncode(mediaUrl), description);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //   filterByPackageName(context, intent, "com.pinterest");
        getActivity().startActivity(intent);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
