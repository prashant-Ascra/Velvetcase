package com.velvetcase.app.material.util;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Prashant Patil on 19-06-2015.
 */
public class SweetAlertDialogManager {
    SweetAlertDialog pDialog;
    public void showAlertDialog(Context context) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    public void Hide(){
        pDialog.cancel();
    }
}
