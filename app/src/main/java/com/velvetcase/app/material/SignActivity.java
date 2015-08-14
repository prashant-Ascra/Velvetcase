package com.velvetcase.app.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.DBHandlers.FavouriteDatabaseHelper;
import com.velvetcase.app.material.DBHandlers.WishListDBHelper;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.Models.InternetConnectionDetector;

import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.GetResponse;
import com.velvetcase.app.material.util.SessionManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Akash on 4/17/2015.
 */
public class SignActivity extends Activity implements View.OnClickListener,GetResponse{

    Button signin;
    TextView ResetPassword;
    TextView join;
    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    ImageView facebook_btn;
    EditText email_box,password_box;
    String RequestType;
    SessionManager session;
    private CallbackManager callbackManager;
    WishListDBHelper dbhelper;
    FavouriteDatabaseHelper favoritedbHelper;
    private GoogleAnalytics analytics;
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        alert=new AlertDialogManager();
        session = new SessionManager(this);
        favoritedbHelper=new FavouriteDatabaseHelper(SignActivity.this);
        signin = (Button) findViewById(R.id.button1);
        dbhelper=new WishListDBHelper(SignActivity.this);
        ResetPassword = (TextView) findViewById(R.id.forgot_pwd);
        join = (TextView) findViewById(R.id.join);
        facebook_btn = (ImageView) findViewById(R.id.facebook_btn);
        email_box=(EditText)findViewById(R.id.email_box);
        password_box=(EditText)findViewById(R.id.pwd_box);
        signin.setOnClickListener(this);
        ResetPassword.setOnClickListener(this);
        join.setOnClickListener(this);
        facebook_btn.setOnClickListener(this);


         /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y
        tracker.setScreenName("User Sign In");
        tracker.send(new HitBuilders.EventBuilder()
                .setAction("Sign in")
                .build());
         /*analytic tracking code end*/



    }

    @Override
    public void onClick(View v) {
        if(v == signin){
            icd = new InternetConnectionDetector(SignActivity.this);
            if(icd.isConnectingToInternet()){
                if(!email_box.getText().toString().equalsIgnoreCase("")){
                    if(emailValidator(email_box.getText().toString())) {

                        if (!password_box.getText().toString().equalsIgnoreCase("")) {
                            RequestType ="SignIN";
                            ExecuteServerRequest();
                        } else {

                            password_box.setError("Please enter a password");
                        }
                    }else{
                        email_box.setError("Please enter a valid email!");
                    }
                }else{

                    email_box.setError("Incorrect email/password.Please check and try again");
                }

            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert.showAlertDialog(SignActivity.this, "Alert", "You dont have internet connection", false);
                    }
                });

            }

        }
        if(v == ResetPassword){
            Intent i = new Intent(SignActivity.this,ForgotPasswordActivty.class);
            startActivity(i);
        }
        if(v == join){
            Intent i = new Intent(SignActivity.this,SignUpActivtiy.class);
            startActivity(i);
            finish();
        }
        if (v == facebook_btn){
            Intent i = new Intent(SignActivity.this,SplashScreenActivity.class);
            i.putExtra("flag","signin");
            startActivity(i);

        }
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void ExecuteServerRequest(){
        List<NameValuePair> paramslist = null;
        if(RequestType.equalsIgnoreCase("SignIN")){
            paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("email",email_box.getText().toString()));
            paramslist.add(new BasicNameValuePair("password",password_box.getText().toString()));
            asyncReuse = new AsyncReuse(SignActivity.this,Constants.APP_MAINURL+"tokens/get_key", Constants.POST,paramslist,true);

        }else{
            paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
            asyncReuse = new AsyncReuse(SignActivity.this,Constants.APP_MAINURL+"productjsons/product_list.json", Constants.GET,paramslist,true);
        }
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    @Override
    public void GetData(String response) {

        if (response != null){
            session=new SessionManager(SignActivity.this);
            if(RequestType.equalsIgnoreCase("SignIN")){
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getString("status").equalsIgnoreCase("Success")) {
                        try{
                            if(!session.getUserID().toString().equalsIgnoreCase(jobj.getString("user_id"))){
                                session.cleanUID();
                                dbhelper.deletetabledata();
                                favoritedbHelper.deletetabledata();
//                                if(dbhelper!=null) {
//                                    dbhelper.deletetabledata();
//
//                                }
                            }
                        }catch (NullPointerException e){

                        }

                        session.createLoginSession(jobj.getString("email"),jobj.getString("user_id"),jobj.getString("full_name"));
                        RequestType ="AllProduct";
                        ExecuteServerRequest();
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert.showAlertDialog(SignActivity.this,"Alert","Please confirm email or passsword",false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                AllProductsModelBase.getInstance().GetProductDataFromServer(response);
                if(AllProductsModelBase.getInstance().getProductsList().size() > 0){
                    Intent i = new Intent(SignActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(SignActivity.this,"Alert","You dont have internet connection", false);
                        }
                    });

                }
            }

        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        super.onActivityResult(reqCode, resCode, i);
        callbackManager.onActivityResult(reqCode, resCode, i);
    }

    public  void connect_with_fb(){


        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {



                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                //   Toast.makeText(SplashScreenActivity.this, "Login Successful!" + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();

//                ExecuteServerRequest();



            }

            @Override
            public void onCancel() {

                Intent intent = new Intent(SignActivity.this, SplashScreenActivity.class);
                startActivity(intent);

                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {

                System.out.println("Facebook Login failed!!");
            }
        });

    }

}
