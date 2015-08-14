package com.velvetcase.app.material.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Prashant Patil on 09-06-2015.
 */
public class Products {

    private JSONObject productJsonobj;

    public Products(JSONObject productJsonobj) {

        this.productJsonobj = productJsonobj;
    }
    public int getproduct_id() {
        return (int) JSONUtil.getJSONProperty(productJsonobj, "product_id");
    }
    public String getshop_url() {
        return (String) JSONUtil.getJSONProperty(productJsonobj, "shop_url");
    }

    public String getflag_url() {
        return (String) JSONUtil.getJSONProperty(productJsonobj, "designer_country_flag");
    }
    public String getname() {
        return (String) JSONUtil.getJSONProperty(productJsonobj, "name");
    }
    public String getdesigner_name() {
        return (String) JSONUtil.getJSONProperty(productJsonobj, "designer_name");
    }
    public String getstory() {
        return (String) JSONUtil.getJSONProperty(productJsonobj, "story");
    }
    public JSONArray getphotos() {
        return (JSONArray) JSONUtil.getJSONProperty(productJsonobj, "photos");
    }
    public String getstart_price() {
        return (String) JSONUtil.getJSONProperty(productJsonobj, "start_price");
    }
    public String getend_price() {
        return (String) JSONUtil.getJSONProperty(productJsonobj, "end_price");
    }
    public JSONArray getvariations() {
        return (JSONArray) JSONUtil.getJSONProperty(productJsonobj, "variations");
    }
    public boolean get_wish_list_flag() {
        return (boolean) JSONUtil.getJSONProperty(productJsonobj, "wishlist_flag");
    }
    public void set_wish_list_flag(boolean hrt_val,JSONObject proJson ) {

        try {
            proJson.put("wishlist_flag", "" + hrt_val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getmetal_details() {
        return (JSONObject) JSONUtil.getJSONProperty(productJsonobj, "metal_details");
    }

    public JSONObject getproductJsonobj() {
        return productJsonobj;
    }

    public  JSONObject function(JSONObject obj, String keyMain, String newValue) throws Exception {
        // We need to know keys of Jsonobject
        JSONObject json = new JSONObject();
        Iterator iterator = obj.keys();
        String key = "wishlist_flag";
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            // if object is just string we change value in key
//            if ((obj.optJSONArray(key)==null) && (obj.optJSONObject(key)==null)) {
//                if ((key.equals(keyMain)) && (obj.get(key).toString().equals(valueMain))) {
//                    // put new value
//                    obj.put(key, newValue);
//                    return obj;
//                }
//            }

            // if it's jsonobject
            if (obj.optJSONObject(key) != null) {
                function(obj.getJSONObject(key), keyMain, newValue);
            }

            // if it's jsonarray
//            if (obj.optJSONArray(key) != null) {
//                JSONArray jArray = obj.getJSONArray(key);
//                for (int i=0;i<jArray.length();i++) {
//                    function(jArray.getJSONObject(i), keyMain, valueMain, newValue);
//                }
//            }
        }
        return obj;
    }

}
