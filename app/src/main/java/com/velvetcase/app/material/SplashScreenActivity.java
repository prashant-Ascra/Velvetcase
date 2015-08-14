package com.velvetcase.app.material;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;
import com.velvetcase.app.material.Models.ServiceHandler;

import com.velvetcase.app.material.adapters.ViewPagerAdapterslider;
import com.velvetcase.app.material.fragments.TestFragment;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.RegistrationIntentService;
import com.velvetcase.app.material.util.SessionManager;
import com.velvetcase.app.material.util.SystemUiHider;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;
import com.wizrocket.android.sdk.WizRocketAPI;
import com.wizrocket.android.sdk.exceptions.WizRocketMetaDataNotFoundException;
import com.wizrocket.android.sdk.exceptions.WizRocketPermissionsNotSatisfied;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashScreenActivity extends FragmentActivity implements View.OnClickListener, GetResponse {
    TextView signintxt, signuptxt;
    ImageView middleimg;
    ViewPager viewpager;
    CirclePageIndicator indicator;
    ViewPagerAdapterslider adapter;
    ImageView facebook_btn;
    WizRocketAPI wr;
    AlertDialogManager alert;
    AsyncReuse asyncReuse;
    private CallbackManager callbackManager;
    InternetConnectionDetector icd;
    String URL_FEED;
    String Request_Type = "";
    ArrayList<String> array = new ArrayList<String>();
    SessionManager sessionManager;
    String user_email, auth_token, fb_id;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    protected final String[] CONTENT = new String[]{"This", "Is", "A", "Test",};
    protected final int[] ICONS = new int[]{
            R.drawable.product_one,
            R.drawable.product_two,
            R.drawable.product_three,
            R.drawable.product_one
    };
    Intent intent;
    int[] ImageList = {R.drawable.blueearring, R.drawable.blueearring,
            R.drawable.blueearring, R.drawable.blueearring};
    GoogleAnalytics analytics;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker("UA-37575096-2"); // Send hits to tracker id UA-XXXX-Y
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        setContentView(R.layout.activity_splash_screen);
        icd = new InternetConnectionDetector(SplashScreenActivity.this);
        try {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            wr = WizRocketAPI.getInstance(getApplicationContext());
            WizRocketAPI.setDebugLevel(1);
        } catch (WizRocketMetaDataNotFoundException e) {
            e.printStackTrace();
            // The WizRocketMetaDataNotFoundException is thrown when you haven�t specified your WizRocket Account ID and/or the Account Token in your AndroidManifest.xml
        } catch (WizRocketPermissionsNotSatisfied e) {
            e.printStackTrace();
            // WizRocketPermissionsNotSatisfiedException is thrown when you haven�t requested the required permissions in your AndroidManifest.xml
        }
        try {
            intent = getIntent();
            if(intent.getExtras()!=null) {
                if (intent.getStringExtra("flag").toString().equalsIgnoreCase("signin")) {
                    connect_with_fb();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);


        }
        sessionManager = new SessionManager(SplashScreenActivity.this);
        if (!sessionManager.isITFirstTime()) {
            createshrtcut();
        }
        sessionManager = new SessionManager(SplashScreenActivity.this);
        signintxt = (TextView) findViewById(R.id.signin_text);
        signuptxt = (TextView) findViewById(R.id.signup_text);
        facebook_btn = (ImageView) findViewById(R.id.imageView8);
        URL_FEED = "http://admin.velvetcase.com/productjsons/launch_banner_api.json";
        signintxt.setOnClickListener(this);
        signuptxt.setOnClickListener(this);
        facebook_btn.setOnClickListener(this);
        alert = new AlertDialogManager();
        icd = new InternetConnectionDetector(SplashScreenActivity.this);
        viewpager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapterslider(SplashScreenActivity.this, array);
        viewpager.setAdapter(adapter);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        indicator.setFillColor(getResources().getColor(R.color.light_grey));
        indicator.setRadius(7);

        if (icd.isConnectingToInternet()) {
            if (sessionManager.isLoggedIn()) {
                ExecuteServerRequest_For_Session();
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert.showAlertDialog(SplashScreenActivity.this, "Alert", "You dont have internet connection", false);
                }
            });

        }

        if (icd.isConnectingToInternet()) {
            new LoadingimgList().execute();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert.showAlertDialog(SplashScreenActivity.this, "Alert", "You dont have internet connection", false);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signintxt) {
            tracker.setScreenName("Sign In");  /// google analytic code
             /*send parameter to tracking */
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("User Sign In")
                    .setAction("Sign In")
                    .build());

              /*send parameter to Wizrocket tracking */
            HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
            prodViewedAction.put("User Sign In(MO)", "Sign in");
            wr.event.push("User Sign In", prodViewedAction);

            Intent i = new Intent(SplashScreenActivity.this, SignActivity.class);
            startActivity(i);
        }
        if (v == signuptxt) {
           tracker.setScreenName("New Registration");  /// google analytic code
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("User Sign In")
                    .setAction("New Registration")
                    .build());
            Intent i = new Intent(SplashScreenActivity.this, SignUpActivtiy.class);
            startActivity(i);

               /*send parameter to Wizrocket tracking */
            HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
            prodViewedAction.put("User Sign In(MO)", "New Registration");
            wr.event.push("User Sign In(MO)", prodViewedAction);

        }
        if (v == middleimg) {
//            Intent i = new Intent(SplashScreenActivity.this,MainActivity.class);
//            startActivity(i);
        }
        if (v == facebook_btn) {

            connect_with_fb();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                finish();
            }
            return false;
        }
        return true;
    }

    public void createshrtcut() {
        sessionManager.set_first_Time(true);
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutintent.putExtra("duplicate", false);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "VelvetCase");
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.velvetcase);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), SplashScreenActivity.class));
        sendBroadcast(shortcutintent);


    }

    private class LoadingimgList extends
            AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... v) {
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(URL_FEED, ServiceHandler.GET);
            if (jsonStr != null) {
                try {
                    final JSONObject mainObject = new JSONObject(jsonStr);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            parseJSONimg(mainObject);
                        }

                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            viewpager = (ViewPager) findViewById(R.id.pager);
            adapter = new ViewPagerAdapterslider(SplashScreenActivity.this, array);
            viewpager.setAdapter(adapter);
            indicator = (CirclePageIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(viewpager);
            indicator.setFillColor(getResources().getColor(R.color.light_grey));
            indicator.setRadius(7);
        }
    }


    public void ExecuteServerRequest_For_Session() {
        Request_Type = "session";
        List<NameValuePair> paramslist = null;
        paramslist = new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("user_id", sessionManager.getUserID()));
        asyncReuse = new AsyncReuse(SplashScreenActivity.this, Constants.APP_MAINURL + "productjsons/product_list.json", Constants.GET, paramslist, true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    private void parseJSONimg(JSONObject objMain) {
        //  inProcess.setVisibility(View.GONE);
        try {
            JSONArray BannerArray = null;

            BannerArray = objMain.getJSONArray("launch_banner");
            int p = 0;
            for (p = 0; p < BannerArray.length(); p++) {
                String banners_pic1 = BannerArray
                        .getJSONObject(p).getString("banner");
                array.add(banners_pic1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        super.onActivityResult(reqCode, resCode, i);
        callbackManager.onActivityResult(reqCode, resCode, i);
    }

    public void connect_with_fb() {

          /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y
        tracker.setScreenName("User Sign In");
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("User Sign In")
                .setAction("Facebook")
                .build());
         /*analytic tracking code end*/

             /*send parameter to Wizrocket tracking */
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put("User Sign In(MO)", "Facebook");
        wr.event.push("User Sign In", prodViewedAction);

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("token", "" + loginResult.getAccessToken().getToken());
                auth_token = "" + loginResult.getAccessToken().getToken();
                fb_id = "" + loginResult.getAccessToken().getUserId();
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    user_email = me.optString("name");
                                    Log.e("email", "" + user_email);

                                    ExecuteServerRequest_FB(me.optString("email"), me.optString("name"), auth_token, fb_id);

                                }
                            }
                        }).executeAsync();

                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                //   Toast.makeText(SplashScreenActivity.this, "Login Successful!" + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();

//                ExecuteServerRequest();
            }

            @Override
            public void onCancel() {

                Intent intent = new Intent(SplashScreenActivity.this, SplashScreenActivity.class);
                startActivity(intent);

                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {

                System.out.println("Facebook Login failed!!");
            }
        });
    }


    public void ExecuteServerRequest() {
        List<NameValuePair> paramslist = null;

        asyncReuse = new AsyncReuse(SplashScreenActivity.this, Constants.APP_MAINURL + "productjsons/product_list.json", Constants.GET, paramslist, true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    public void ExecuteServerRequest_FB(String email, String name, String auth_token, String fb_id) {
        Request_Type = "fb";
        List<NameValuePair> paramslist = null;
        paramslist = new ArrayList<NameValuePair>();
        paramslist.add(new BasicNameValuePair("user_email", email));
        paramslist.add(new BasicNameValuePair("user_name", name));
        paramslist.add(new BasicNameValuePair("auth_token", auth_token));
        paramslist.add(new BasicNameValuePair("facebook_uid", fb_id));
        asyncReuse = new AsyncReuse(SplashScreenActivity.this, "" +
                "http://admin.velvetcase.com:8020/tokens/facebook_authentication", Constants.POST, paramslist, true);
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    @Override
    public void GetData(String response) {
        if (response != null) {
            Log.e("amol", "" + response);
            Log.e("Request_Type", "" + Request_Type);
            if (Request_Type.equalsIgnoreCase("fb")) {
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getString("status").equalsIgnoreCase("Success")) {
                        sessionManager.createLoginSession(jobj.getString("email"), jobj.getString("user_id"), jobj.getString("full_name"));
                        Request_Type = "AllProduct";
                        ExecuteServerRequest();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.showAlertDialog(SplashScreenActivity.this, "Alert", "Something went wrong please try again!", false);
                            }
                        });
//                        Request_Type ="AllProduct";
//                        ExecuteServerRequest();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (Request_Type.equalsIgnoreCase("AllProduct") || Request_Type.equalsIgnoreCase("session")) {
                Log.e("amol", "hera");

                AllProductsModelBase.getInstance().GetProductDataFromServer(response);
                if (AllProductsModelBase.getInstance().getProductsList().size() > 0) {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(SplashScreenActivity.this, "Alert", "You dont have internet connection", false);
                        }
                    });

                }
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert.showAlertDialog(SplashScreenActivity.this, "Alert", "You dont have internet connection", false);
                }
            });
        }
    }

    class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private int mCount = CONTENT.length;

        public TestFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getIconResId(int index) {
            return ICONS[index % ICONS.length];
        }

        public void setCount(int count) {
            if (count > 0 && count <= 10) {
                mCount = count;
                notifyDataSetChanged();
            }
        }
    }
}
