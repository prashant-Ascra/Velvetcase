package com.velvetcase.app.material;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.velvetcase.app.material.Models.AllProductsModelBase;

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
 * Created by Prashant Patil on 29-05-2015.
 */
public class ForgotPasswordActivty extends ActionBarActivity implements GetResponse {
    EditText email_edt_text;
    Button reset_email_btn;
    String RequestType="reset";
    AsyncReuse asyncReuse;
    SessionManager session;
    AlertDialogManager alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_screen);
        alert = new AlertDialogManager();
        setupUI();
    }

    public  void setupUI(){
        email_edt_text=(EditText)findViewById(R.id.edt_email);
        reset_email_btn=(Button)findViewById(R.id.btn_reset_email);

        reset_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailValidator(email_edt_text.getText().toString())){
                    ExecuteServerRequest(email_edt_text.getText().toString());
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(ForgotPasswordActivty.this,"Alert","Please confirm email",false);
                        }
                    });
                }
            }
        });

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

    public void ExecuteServerRequest(String email){
        List<NameValuePair> paramslist = null;
        if(RequestType.equalsIgnoreCase("reset")){
            paramslist = new ArrayList<NameValuePair>();
            paramslist.add(new BasicNameValuePair("email",email));
            asyncReuse = new AsyncReuse(ForgotPasswordActivty.this,Constants.APP_MAINURL+"tokens/forgot_password", Constants.POST,paramslist,true);
        }else{
            asyncReuse = new AsyncReuse(ForgotPasswordActivty.this,Constants.APP_MAINURL+"productjsons/product_list.json", Constants.GET,paramslist,true);
        }
        asyncReuse.getResponse = this;
        asyncReuse.execute();
    }

    @Override
    public void GetData(String response) {
        if (response != null){
            if(RequestType.equalsIgnoreCase("reset")){
                try {
                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getString("status").equalsIgnoreCase("Success")) {
                        //     session.createLoginSession(jobj.getString("email"),jobj.getString("user_id"),""+jobj.getString("full_name"));
                        Toast.makeText(ForgotPasswordActivty.this,"Password reset link has been sent on register mail",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ForgotPasswordActivty.this,SignActivity.class);
                        startActivity(i);
                        // RequestType ="AllProduct";
                        // ExecuteServerRequest(email_edt_text.getText().toString());
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ForgotPasswordActivty.this,"Please check email",Toast.LENGTH_SHORT).show();
                                //                       alert.showAlertDialog(ForgotPasswordActivty.this,"Alert","Please confirm email or passsword",false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                AllProductsModelBase.getInstance().GetProductDataFromServer(response);
                if(AllProductsModelBase.getInstance().getProductsList().size() > 0){
                    Intent i = new Intent(ForgotPasswordActivty.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgotPasswordActivty.this,"Please check email",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }

        }

    }
}
