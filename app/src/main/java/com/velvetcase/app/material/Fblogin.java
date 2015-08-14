package com.velvetcase.app.material;

import android.app.Activity;
import android.content.Intent;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.util.Base64;

import android.widget.Toast;



import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Code2care.org
 *
 * Author : Code2care
 *
 * Date : 20150506
 *
 * Note : All you need to do is configure your
 * Facebook APP ID in String XML before running this
 * APP
 *
 *
 */

public class Fblogin extends Activity {

    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.ascra.com.fblogindemo",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        //You need this method to be used only once to configure
        //your key hash in your App Console at
        // developers.facebook.com/apps

        // getFbKeyHash("org.code2care.fbloginwithandroidsdk");

        setContentView(R.layout.fb_login);
        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);


        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {



                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
              //  Toast.makeText(Fblogin.this, "Login Successful!" + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
            Intent intent=new Intent(Fblogin.this,MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancel() {

                Intent intent=new Intent(Fblogin.this,SplashScreenActivity.class);
                startActivity(intent);
                Toast.makeText(Fblogin.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(Fblogin.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }
        });
    }

    public void getFbKeyHash(String packageName) {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                System.out.println("YourKeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        super.onActivityResult(reqCode, resCode, i);
        callbackManager.onActivityResult(reqCode, resCode, i);
    }
}