package com.velvetcase.app.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Akash on 4/17/2015.
 */


public class SignUpActivtiy extends Activity implements GetResponse {
    Spinner spinner;
    Button signup;
    String[] itemsname = {"Mr","Mrs","Ms","Miss"};
    Boolean email_focus = false;
    Boolean fullname_focus = false;
    Boolean password_focus = false;
    Boolean mobile_focus = false;
    EditText email,fullname,password,mobile;
    LinearLayout errorWrapper;
    ImageView facebook_btn;

    AlertDialogManager alert;
    InternetConnectionDetector icd;
    AsyncReuse asyncReuse;
    String RequestType;
    String title="Mr";
    SessionManager session;
    List<String> list;
    WishListDBHelper dbhelper;
    FavouriteDatabaseHelper favoritedbHelper;
    TextView terms_and_cond_text;
    private GoogleAnalytics analytics;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        alert = new AlertDialogManager();
        session = new SessionManager(this);
        icd = new InternetConnectionDetector(this);
        email  = (EditText) findViewById(R.id.edt_emailfield);
        terms_and_cond_text=(TextView)findViewById(R.id.terms_and_cond_text);
        fullname  = (EditText) findViewById(R.id.edt_firstname);
        password  = (EditText) findViewById(R.id.edt_passwordfield);
        mobile = (EditText) findViewById(R.id.edt_mobilenumber);
        facebook_btn = (ImageView) findViewById(R.id.button);
        errorWrapper = (LinearLayout) findViewById(R.id.error_wrapper);
        terms_and_cond_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenBrowser("http://velvetcase.com/customer_support?q=terms");
            }
        });
        spinner = (Spinner) findViewById(R.id.spinner);
        dbhelper=new WishListDBHelper(SignUpActivtiy.this);
        favoritedbHelper=new FavouriteDatabaseHelper(SignUpActivtiy.this);
        spinner.setAdapter(new MyAdapter(this, R.layout.title_spinner_row,
                itemsname));


        signup = (Button) findViewById(R.id.button2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(email.getText().toString()!= null){
//                    errorWrapper.setVisibility(View.VISIBLE);
//                }else{
               email_focus = true;
                fullname_focus = true;
                 password_focus = true;
                mobile_focus=true;
//                if(mobile.getText().toString().equalsIgnoreCase("")){
//
//                }else{
//                    mobile.setError("mobile number can not be empty!");
//                }

                if(!email.getText().toString().equalsIgnoreCase("")) {

                }else{
                    email.setError("Email can not be empty!");

                }

                if (!password.getText().toString().equalsIgnoreCase("")) {

                }else{
                    password.setError("Password can not be empty!");
                }



                if(!fullname.getText().toString().equalsIgnoreCase("")) {
//                    if(fullname.getText().toString().contains(" ")) {
//                    }else{
//                        fullname.setError("Name  should contain space!");
//                    }

                }else{
                    fullname.setError("Name can not be empty!");
                }
                if(!email.getText().toString().equalsIgnoreCase("")) {
                    if (emailValidator(email.getText().toString())){
                        if (!fullname.getText().toString().equalsIgnoreCase("")) {

                                if (!password.getText().toString().equalsIgnoreCase("")) {
                                    if (!mobile.getText().toString().equalsIgnoreCase("")) {
                                        if (mobile.getText().toString().length() == 10){
                                            icd = new InternetConnectionDetector(SignUpActivtiy.this);
                                        if (icd.isConnectingToInternet()) {
                                            RequestType = "SignUp";
                                            ExecuteServerRequest();
                                        } else {
                                            alert.showAlertDialog(SignUpActivtiy.this, "Alert", "You dont have internet connection", false);
                                        }
                                    }else{
                                            mobile.setError("Please enter 10 digit mobile number!");
                                        }
                                    } else {
                                        mobile.setError("Please enter your mobile number!");
                                    }
                                } else {
                                    password.setError("Password can not be empty!");
                                }


                        } else {
                            fullname.setError("Name can not be empty!");
                        }
                }else{
                    email.setError("Please Enter Valid Email!");
                }
                }
                else{
                    email.setError("Email can not be empty!");

                }

                }
//           }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                try{
                    title=spinner.getItemAtPosition(position).toString();

                }catch (NullPointerException e){
                    e.printStackTrace();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        DisableAllHints();

        email.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Integer textlength1 = email.getText()
                        .length();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        fullname.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Integer textlength1 = fullname.getText()
                        .length();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Integer textlength1 = password.getText()
                        .length();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mobile.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Integer textlength1 = mobile.getText()
                        .length();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        facebook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivtiy.this,Fblogin.class);
                startActivity(i);
            }
        });


         /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y
        tracker.setScreenName("User Sign In");
        tracker.send(new HitBuilders.EventBuilder()
                .setAction("Sign UP")
                .build());
         /*analytic tracking code end*/

    }
    public void OpenBrowser(String url) {
        if (url != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }
    public boolean does_string_has_spl_ch(String name){
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        boolean b = m.find();
        if(b) {
            return true;
        }else{
            return false;
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
    public void DisableAllHints(){
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                if(hasFocus && email_focus){
                    email.setHint("");
                    email_focus = false;
                }else{
                    email.setHint("Email");
                    email_focus = true;
                }

            }
        });
        fullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && fullname_focus) {
                    fullname.setHint("");
                    fullname_focus = false;
                } else {
                    fullname.setHint("Name");
                    fullname_focus = true;
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && password_focus) {
                    password.setHint("");
                    password_focus = false;
                } else {
                    password.setHint("Password");
                    password_focus = true;
                }
            }
        });

        mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && mobile_focus) {
                    mobile.setHint("");
                    mobile_focus = false;
                } else {
                    mobile.setHint("Mobile Number");
                    mobile_focus = true;
                }
            }
        });

    }

    public List<String> getDiscoverSpinnerData() {
        list  = new ArrayList<String>();
        list.add("Mr");
        list.add("Miss");
        list.add("Mrs");
        return list;
    }

    public class MyAdapter extends ArrayAdapter<String> {
        String[] objects;
        public MyAdapter(Context ctx, int txtViewResourceId, String[] objects)
        {
            super(ctx, txtViewResourceId, objects);
            this.objects = objects;
        }
        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(position, cnvtView, prnt);
        }
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(pos, cnvtView, prnt);
        }
        public View getCustomView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.title_spinner_row, parent, false);
            TextView main_text = (TextView) mySpinner .findViewById(R.id.titleName);
            main_text.setText(objects[position]);
            return mySpinner;
        }
    }

    public void ExecuteServerRequest(){
        List<NameValuePair> paramslist = null;
        if(RequestType.equalsIgnoreCase("SignUP")){
            paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("title",""+title));
            paramslist.add(new BasicNameValuePair("full_name",fullname.getText().toString()));
            paramslist.add(new BasicNameValuePair("mobile_number",mobile.getText().toString()));
            paramslist.add(new BasicNameValuePair("email",email.getText().toString()));
            paramslist.add(new BasicNameValuePair("password",password.getText().toString()));


            asyncReuse = new AsyncReuse(SignUpActivtiy.this,Constants.APP_MAINURL+"tokens/user_sign_up", Constants.POST,paramslist,true);
        }else{
            paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("user_id",session.getUserID()));
            asyncReuse = new AsyncReuse(SignUpActivtiy.this, Constants.APP_MAINURL+"productjsons/product_list.json", Constants.GET,paramslist,true);
        }
        asyncReuse.getResponse = SignUpActivtiy.this;
        asyncReuse.execute();
    }

    @Override
    public void GetData(String response) {

        if (response != null) {
            if(RequestType.equalsIgnoreCase("Signup")){
                try {
                    final JSONObject jobj = new JSONObject(response);
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
                    }else if(jobj.getString("status").equalsIgnoreCase("Failure")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    alert.showAlertDialog(SignUpActivtiy.this,"Alert",jobj.getString("message").toString(),false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                AllProductsModelBase.getInstance().GetProductDataFromServer(response);
                if (AllProductsModelBase.getInstance().getProductsList().size() > 0) {
                    Intent i = new Intent(SignUpActivtiy.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(SignUpActivtiy.this, "Alert", "You dont have internet connection", false);
                        }
                    });

                }
            }
        }
    }

}
