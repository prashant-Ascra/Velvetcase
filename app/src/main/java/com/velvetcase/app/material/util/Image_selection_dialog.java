package com.velvetcase.app.material.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.velvetcase.app.material.R;


/**
 * Created by abc on 6/16/2015.
 */
public class Image_selection_dialog extends AlertDialog implements View.OnClickListener {
    image_category image_cat;
    Context con;
    ImageView gallary, camera;
    TextView Txt_gallary, Txt_camera;

    public Image_selection_dialog(Context context, image_category image_cat) {
        super(context);
        this.con = context;
        this.image_cat = image_cat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selection_dialog);
        gallary = (ImageView) findViewById(R.id.gallary_icon);
        Txt_gallary = (TextView) findViewById(R.id.textView13);
        camera = (ImageView) findViewById(R.id.camera_icon);
        Txt_camera = (TextView) findViewById(R.id.textView14);

        gallary.setOnClickListener(this);
        camera.setOnClickListener(this);
        Txt_gallary.setOnClickListener(this);
        Txt_camera.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == gallary || v == Txt_gallary) {
            image_cat.pass_category("gallary");
            dismiss();
        }
        if (v == camera || v == Txt_camera) {
            image_cat.pass_category("camera");
            dismiss();
        }
    }


    public interface image_category {

        public void pass_category(String category);

    }
}
