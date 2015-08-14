package com.velvetcase.app.material;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by Akash on 3/24/2015.
 */
public class LeftDrawerAdapter  extends RecyclerView.Adapter<LeftDrawerAdapter.ViewHolder> {

    private static final int TYPE_LINE = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    static Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        TextView textView;
        int itemPos;

        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                Holderid = 1;                             // setting holder id as 1 as the object being populated are of type item row
            }
            else{
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
            itemView.setOnClickListener(this);
        }
        public void SetItemPos(int pos){
            this.itemPos = pos;
        }
        public int getItemPos(){
            return itemPos;
        }

        @Override
        public void onClick(View v) {
            if (itemView!=null){
                if (itemView == v){
                    ((MainActivity)activity).ReplaceFragment(getItemPos());
                }
            }
        }
    }

    LeftDrawerAdapter(String Titles[],Activity activity){ // MyAdapter Constructor with titles and icons parameter
        mNavTitles = Titles;
        this.activity = activity;
    }
    @Override
    public LeftDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_drawer_row,parent,false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object


        }
        else if (viewType == TYPE_LINE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_line,parent,false); //Inflating the layout
            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(LeftDrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position]); // Setting the Text with the array of our Titles
        }
        holder.SetItemPos(position);

    }
    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return (mNavTitles.length); // the number of items in the list will be +1 the titles including the header view.
    }
    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_LINE;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 5;
    }

}