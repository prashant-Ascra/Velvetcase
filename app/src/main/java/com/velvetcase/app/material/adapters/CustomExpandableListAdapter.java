package com.velvetcase.app.material.adapters;

/**
 * Created by Akash on 4/21/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.velvetcase.app.material.MyGridView;

import com.velvetcase.app.material.R;
import com.velvetcase.app.material.fragments.Template_detail;
import com.velvetcase.app.material.util.AlertDialogManager;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    public  get_user_selection  get_uer_selected_product;
    private LayoutInflater inflater;
    private Context _context;
    int positon_set_by_user;
    AlertDialogManager alert;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<JSONObject>> _listDataChild;
    public CustomExpandableListAdapter(LayoutInflater inflater,Context context,List<String> listDataHeader,
                                       HashMap<String, List<JSONObject>> listChildData,get_user_selection get_uer_selected_product ,int position_selected_by_ser) {
        this.inflater = inflater;
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.get_uer_selected_product=get_uer_selected_product;
        this.positon_set_by_user=position_selected_by_ser;
        alert=new AlertDialogManager();
    }

    //    @Override
//    public JSONObject getChild(int groupPosition, int childPosition) {
//        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                .get(childPosition);
//    }
    @Override
    public List<JSONObject>  getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final List<JSONObject>  childlist = (List<JSONObject> ) getChild(groupPosition, childPosition);
        MyGridView gridView =  null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        gridView = (MyGridView) convertView.findViewById(R.id.gallary_view);
        gridView.setAdapter(new ImageAdapter( this._context,childlist,groupPosition));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(_context,""+position,Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private boolean[] thumbnailsselection;
        List<JSONObject> list;
        int group_pos;
        public ImageAdapter(Context c,List<JSONObject> list,int group_pos){
            mContext = c;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.thumbnailsselection = new boolean[mThumbIds.length];
            this.list = list;
            this.group_pos = group_pos;
        }

        public int getCount()
        {
            return list.size();
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent){
            final ViewHolder holder;
            final ImageView imageView;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.gridview_custom_row, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.imageView9);
                holder.border_view=(View)convertView.findViewById(R.id.back_view);
                holder.checkbox = (CheckBox)convertView.findViewById(R.id.custom_checkbox);
                convertView.setTag(holder);

            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            final JSONObject singleobj = list.get(position);
            try {
                Picasso.with(mContext).load(singleobj.getString("thumbnail")).into(holder.imageview);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i<Template_detail.selectedvariantsIDS.size(); i++){
                try {
                    if(singleobj.getString("p_id").equalsIgnoreCase(Template_detail.selectedvariantsIDS.get(i))){
                        holder.border_view.setVisibility(View.VISIBLE);
                        holder.checkbox.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            holder.imageview.setId(position);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkbox.isChecked()){
                        if(Template_detail.selectedvariants.size() >=3){
                            alert.showAlertDialog(mContext,"You can select only 3!"," Please uncheck one to select another",false);
                        }else {
                            holder.border_view.setVisibility(View.VISIBLE);
                            holder.checkbox.setChecked(false);
                            BitmapDrawable drawable = (BitmapDrawable) holder.imageview.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            try {
                                get_uer_selected_product.select_product(bitmap, positon_set_by_user, singleobj.getString("p_id"), true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                            holder.border_view.setVisibility(View.GONE);
                            holder.checkbox.setChecked(true);
                            BitmapDrawable drawable = (BitmapDrawable) holder.imageview .getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            try {
                                get_uer_selected_product.select_product(bitmap,positon_set_by_user,singleobj.getString("p_id"),false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            });
            return convertView;
        }
        private Integer[] mThumbIds = {R.drawable.one, R.drawable.one,
                R.drawable.one, R.drawable.one,
                R.drawable.one, R.drawable.one,
                R.drawable.one, R.drawable.one,
                R.drawable.one, R.drawable.one};
    }

    public Bitmap  get_bitmap(ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return  bitmap;
    }
    public  interface  get_user_selection{
        public void  select_product(Bitmap img, int pos, String productID, Boolean flag);
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        View border_view;
        int id;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
//        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                .size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header_view, null);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView updownarrow = (ImageView) convertView
                .findViewById(R.id.imageView10);
        if (isExpanded){
            updownarrow.setImageResource(R.drawable.uparrow);
        }else{
            updownarrow.setImageResource(R.drawable.downarrow);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
