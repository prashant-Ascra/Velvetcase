package com.velvetcase.app.material.Models;

import org.json.JSONObject;

/**
 * Created by Prashant Patil on 11-06-2015.
 */
public class Designer {

    private JSONObject designerJsonObj;

    public Designer(JSONObject designerJsonObj) {
        this.designerJsonObj = designerJsonObj;
    }

    public int getdesigner_id() {
        return (int) JSONUtil.getJSONProperty(designerJsonObj, "designer_id");
    }

    public String getflag_url() {
        return (String) JSONUtil.getJSONProperty(designerJsonObj, "designer_country_flag");
    }
    public String getdesigner_name() {
        return (String) JSONUtil.getJSONProperty(designerJsonObj, "designer_name");
    }
    public int getdesigner_products() {
        return (int) JSONUtil.getJSONProperty(designerJsonObj, "designer_products");
    }
    public String getdesigner_img() {
        return (String) JSONUtil.getJSONProperty(designerJsonObj, "designer_img");
    }
    public boolean get_wish_list_flag() {
        return (boolean) JSONUtil.getJSONProperty(designerJsonObj, "wishlist_flag");
    }
    public JSONObject getdesignerJsonObj() {
        return designerJsonObj;
    }

}
