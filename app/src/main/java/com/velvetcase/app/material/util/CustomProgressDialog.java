package com.velvetcase.app.material.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.velvetcase.app.material.R;


public class CustomProgressDialog extends AlertDialog {
	Context context;
	public CustomProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.swipe_dilog);
}
}
