package com.velvetcase.app.material.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.velvetcase.app.material.Models.AllProductsModelBase;
import com.velvetcase.app.material.R;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.velvetcase.app.material.util.Constants;
import com.velvetcase.app.material.util.Image_selection_dialog;
import com.velvetcase.app.material.util.SweetAlertDialogManager;
import com.wizrocket.android.sdk.WizRocketAPI;
import com.wizrocket.android.sdk.exceptions.WizRocketMetaDataNotFoundException;
import com.wizrocket.android.sdk.exceptions.WizRocketPermissionsNotSatisfied;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class UploadPhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static int RESULT_LOAD_IMG = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Uri mImageCaptureUri;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE1 = 2;
    Uri imageUri = null;
    Button submit, cancel;
    String imgDecodableString;
    ImageView product_img1, product_img2, product_img3, product_img4;
    int selection;
    ArrayList<String>path_list;
    private static final int CAMERA_REQUEST = 1888;
    TextView image1_title, image2_title, image3_title, image4_title;
    boolean showdialog = false;
    int image_rank = 1;
    RelativeLayout wrap_one, wrap_two, wrap_three, wrap_four;
    EditText edt_email;
    AlertDialogManager alert;
    int photoURLCount=0;
    Bitmap bmp;
    String image_path="";
    int image_count=0;
    String image_path_to_upload;
    ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String EMAIL_MSG = "invalid email";
    HttpEntity resEntity;
    ImageView PhoneButton;
    String user_email_id;
    SweetAlertDialogManager dialog;
    boolean add_count1=false,add_count2=false,add_count3=false,add_count4=false;
    private static String url_path1 = "http://admin.velvetcase.com:8020/tokens/upload_json_design.json";
    private GoogleAnalytics analytics;
    private Tracker tracker;
    static WizRocketAPI wr;

    // TODO: Rename and change types and number of parameters
    public static UploadPhotoFragment newInstance(String param1, String param2) {
        UploadPhotoFragment fragment = new UploadPhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String TAG = UploadPhotoFragment.class
            .getSimpleName();

    public static UploadPhotoFragment newInstance() {
        return new UploadPhotoFragment();
    }

    public UploadPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try {
            String i = getArguments().getString("key");
            if (i.equalsIgnoreCase("gallary")) {
                showdialog = false;
                open_gallary();
            } else {
                showdialog = false;
                open_camera();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        alert = new AlertDialogManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.freequaote_upload_photo_screen, container, false);
        BuildView(v);
               /*analytic tracking code start*/
        analytics = GoogleAnalytics.getInstance(getActivity());
        tracker = analytics.newTracker(Constants.Google_analytic_id); // Send hits to tracker id UA-XXXX-Y
        tracker.setScreenName("Free Quote");
             /*send parameter to tracking */
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Free Quote")
                .setAction("Free Quote Form Initiated")
                .build());
         /*analytic tracking code end*/

        try{
            wr = WizRocketAPI.getInstance(getActivity().getApplicationContext());
            WizRocketAPI.setDebugLevel(1);
               /*send parameter to Wizrocket tracking */
            HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
            prodViewedAction.put("Free Quote", "Free Quote Form Initiated");
            wr.event.push("Free Quote (MO)", prodViewedAction);
        } catch (WizRocketMetaDataNotFoundException e) {
            e.printStackTrace();
            // The WizRocketMetaDataNotFoundException is thrown when you haven�t specified your WizRocket Account ID and/or the Account Token in your AndroidManifest.xml
        } catch (WizRocketPermissionsNotSatisfied e) {
            e.printStackTrace();
            // WizRocketPermissionsNotSatisfiedException is thrown when you haven�t requested the required permissions in your AndroidManifest.xml
        }
        return v;
    }

    public void BuildView(View v) {
        submit = (Button) v.findViewById(R.id.button4);
        cancel = (Button) v.findViewById(R.id.button5);
        edt_email = (EditText) v.findViewById(R.id.editText);
        product_img1 = (ImageView) v.findViewById(R.id.imageV1);
        product_img2 = (ImageView) v.findViewById(R.id.imageV2);
        image1_title = (TextView) v.findViewById(R.id.imageV1_title);
        image2_title = (TextView) v.findViewById(R.id.textView5);
        product_img3 = (ImageView) v.findViewById(R.id.imageV3);
        product_img4 = (ImageView) v.findViewById(R.id.imageV4);
        image3_title = (TextView) v.findViewById(R.id.textView_three);
        image4_title = (TextView) v.findViewById(R.id.textView_four);
        wrap_one = (RelativeLayout) v.findViewById(R.id.wrap_one);
        wrap_two = (RelativeLayout) v.findViewById(R.id.wrap_two);
        wrap_three = (RelativeLayout) v.findViewById(R.id.wrap_three);
        wrap_four = (RelativeLayout) v.findViewById(R.id.wrap_four);
        PhoneButton =(ImageView) v.findViewById(R.id.imageView4);
        path_list=new ArrayList<>();

        User_Image_Selection user_selection = new User_Image_Selection();
        final Image_selection_dialog selection_dialog = new Image_selection_dialog(getActivity(), user_selection);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_email.getText().toString().equalsIgnoreCase("") || edt_email.getText().toString()== null ) {
                    alert.showAlertDialog(getActivity(),"Alert","Please enter your email",false);
                }else{
                    if (isValidEmail(edt_email.getText().toString()) ) {
                        user_email_id=edt_email.getText().toString();
                        edt_email.setText("");

                        doMultipleFileUpload(user_email_id);
//                        ((MainActivity) getActivity()).addFragmentInsideContainer(PhotoUploadThanksFragment.newInstance(),
//                                PhotoUploadThanksFragment.TAG);
                    }else{
                        edt_email.setText("");
                        alert.showAlertDialog(getActivity(),"Alert","Invalid Email",false);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        wrap_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_rank = 1;
                selection_dialog.show();

            }
        });

        wrap_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_dialog.show();
                image_rank = 2;
            }
        });

        wrap_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_dialog.show();
                image_rank = 3;
            }
        });

        wrap_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection_dialog.show();
                image_rank = 4;
            }
        });

        PhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = "1234567890";
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + strName));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
    }

    private void doMultipleFileUpload(String email){


        Loadimage load_image_to_server=new Loadimage(email);
        load_image_to_server.execute();


    }



    public  class  Loadimage extends AsyncTask<Void,Void,Void>{
        String email;
        public Loadimage(String user_email){
            this.email=user_email;
            dialog =new SweetAlertDialogManager();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showAlertDialog(getActivity());
        }

        @Override
        protected Void doInBackground(Void... params) {
            //   File image=new File(image_path_toservcer);
            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url_path1);
                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("email", new StringBody(email));
                int i=0;
                FileBody filebody;
                File file;
                for(String image_path :path_list){
                    file =new File(image_path);
                    filebody = new FileBody(file);
                    reqEntity.addPart("photos[]",filebody);
                    i++;
                }

                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                resEntity = response.getEntity();
                final String response_str = EntityUtils.toString(resEntity);


            }
            catch (Exception ex){
                ex.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.Hide();

            addFragmentInsideContainer(PhotoUploadThanksFragment.newInstance(),
                    PhotoUploadThanksFragment.TAG);
        }
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }


    public void addFragmentInsideContainer(Fragment fm, String tag) {

        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.content, fm, tag).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();

                image_path= getPath(selectedImage);
                Toast.makeText(getActivity(),"image path:"+image_path,Toast.LENGTH_SHORT).show();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                if (image_rank == 1) {
                    if(add_count1==false){
                        add_count1=true;

                        image_path_to_upload=image_path;
                        path_list.add(image_path_to_upload);
                        image_count++;

                    }

                    image_path_to_upload=image_path;

                    Toast.makeText(getActivity(), "Size:"+path_list.size(), Toast.LENGTH_LONG)
                            .show();

                    Uri imageUri = data.getData();
                    Bitmap b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    product_img1.setImageBitmap(b);
                    product_img1.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                    image1_title.setText("" + getFileName(selectedImage));

                    wrap_two.setVisibility(View.VISIBLE);
                }
                if (image_rank == 2) {

                    if(add_count2==false){
                        add_count2=true;
                        image_path_to_upload=image_path;
                        path_list.add(image_path_to_upload);
                        image_count++;

                    }
                    image_path_to_upload=image_path;

                    Toast.makeText(getActivity(), "Size:"+path_list.size(), Toast.LENGTH_LONG)
                            .show();

                    Uri imageUri = data.getData();
                    Bitmap b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    product_img2.setImageBitmap(b);
                    product_img2.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                    image2_title.setText("" + getFileName(selectedImage));
                    wrap_three.setVisibility(View.VISIBLE);
                }
                if (image_rank == 3) {

                    if(add_count3==false){
                        add_count3=true;
                        image_path_to_upload=image_path;
                        path_list.add(image_path_to_upload);
                        image_count++;

                    }
                    image_path_to_upload=image_path;

                    Toast.makeText(getActivity(), "Size:"+path_list.size(), Toast.LENGTH_LONG)
                            .show();

                    Uri imageUri = data.getData();
                    Bitmap b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    product_img3.setImageBitmap(b);
                    product_img3.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                    image3_title.setText("" + getFileName(selectedImage));
                    wrap_four.setVisibility(View.VISIBLE);
                }
                if (image_rank == 4) {

                    if(add_count4==false){
                        add_count4=true;
                        image_path_to_upload=image_path;
                        path_list.add(image_path_to_upload);
                        image_count++;

                    }
                    image_path_to_upload=image_path;

                    Toast.makeText(getActivity(), "Size:"+path_list.size(), Toast.LENGTH_LONG)
                            .show();

                    Uri imageUri = data.getData();
                    Bitmap b = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    product_img4.setImageBitmap(b);
                    product_img4.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                    image4_title.setText("" + getFileName(selectedImage));
                }

            } else {
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                    if (resultCode == getActivity().RESULT_OK) {
                        String imageId = convertImageUriToFile(imageUri,
                                getActivity());
                        new LoadImagesFromSDCard().execute("" + imageId);
                    }
                }
            }

        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
        }
    }


    public void open_gallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public byte[] get_image_array(Bitmap image){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    public void open_camera() {
        String fileName = "Camera_Example.jpg";
        // Create parameters for Intent with filename
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION,
                "Image capture by camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent,
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(getActivity());
        Bitmap mBitmap;
        Uri uri;
        protected void onPreExecute() {
            Dialog.setMessage("Loading image from Sdcard..");
            Dialog.show();
        }
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            uri = null;
            try {
                uri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ""
                                + urls[0]);
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                        .openInputStream(uri));
                if (bitmap != null) {
                    newBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500,
                            true);
                    bitmap.recycle();
                    if (newBitmap != null) {
                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {
                cancel(true);
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            if (mBitmap != null)
                if (image_rank == 1) {

                    if(add_count1==false){
                        add_count1=true;

                        image_path_to_upload= getPath(uri);
                        path_list.add(image_path_to_upload);
                        image_count++;

                        Toast.makeText(getActivity(),"list_count:"+path_list.size(),Toast.LENGTH_SHORT).show();
                        wrap_two.setVisibility(View.VISIBLE);
                    }

                    image_path_to_upload=image_path;




                    product_img1.setImageBitmap(mBitmap);
                    product_img1.setScaleType(ImageView.ScaleType.FIT_XY);
                    image1_title.setText("" + getFileName(uri));
                    wrap_two.setVisibility(View.VISIBLE);
                }
            if (image_rank == 2) {
                if(add_count2==false){
                    add_count2=true;

                    image_path_to_upload= getPath(uri);
                    path_list.add(image_path_to_upload);
                    image_count++;
                    Toast.makeText(getActivity(),"list_count:"+path_list.size(),Toast.LENGTH_SHORT).show();

                    wrap_three.setVisibility(View.VISIBLE);
                }

                image_path_to_upload=image_path;




                product_img2.setImageBitmap(mBitmap);
                product_img2.setScaleType(ImageView.ScaleType.FIT_XY);
                image1_title.setText("" + getFileName(uri));
                wrap_three.setVisibility(View.VISIBLE);
            }
            if (image_rank == 3) {
                if(add_count3==false){
                    add_count3=true;

                    image_path_to_upload= getPath(uri);
                    path_list.add(image_path_to_upload);
                    image_count++;
                    wrap_four.setVisibility(View.VISIBLE);

                }

                image_path_to_upload=image_path;




                product_img3.setImageBitmap(mBitmap);
                product_img3.setScaleType(ImageView.ScaleType.FIT_XY);
                image1_title.setText("" + getFileName(uri));
                wrap_four.setVisibility(View.VISIBLE);
            }
            if (image_rank == 4) {
                if(add_count4==false){
                    add_count4=true;

                    image_path_to_upload= getPath(uri);
                    path_list.add(image_path_to_upload);
                    image_count++;

                }

                image_path_to_upload=image_path;




                product_img4.setImageBitmap(mBitmap);
                product_img4.setScaleType(ImageView.ScaleType.FIT_XY);
                image1_title.setText("" + getFileName(uri));
            }
        }
    }


    public static String convertImageUriToFile(Uri imageUri, Activity activity) {
        Cursor cursor = null;
        int imageID = 0;
        try {
            /*********** Which columns values want to get *******/
            String[] proj = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION};
            cursor = activity.managedQuery(

                    imageUri, // Get data for specific image URI
                    proj, // Which columns to return
                    null, // WHERE clause; which rows to return (all rows)
                    null, // WHERE clause selection arguments (none)
                    null // Order-by clause (ascending by name)

            );

            int columnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // int orientation_ColumnIndex =
            // cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            int size = cursor.getCount();

            /******* If size is 0, there are no images on the SD Card. *****/

            if (size == 0) {
                //    imageDetails.setText("No Image");
            } else {

                int thumbID = 0;
                if (cursor.moveToFirst()) {

                    imageID = cursor.getInt(columnIndex);

                    thumbID = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                }
            }
        } finally {
            if (cursor != null) {

            } else {


            }
        }
        // cursor.close();
        return "" + imageID;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }


    public class User_Image_Selection implements Image_selection_dialog.image_category {

        @Override
        public void pass_category(String category) {
            if (category.equalsIgnoreCase("gallary")) {
                open_gallary();
            }
            if (category.equalsIgnoreCase("camera")) {
                open_camera();
            }
        }
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

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
