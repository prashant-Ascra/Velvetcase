package com.velvetcase.app.material.Models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Prashant Patil on 16-06-2015.
 */
public class Looks {

    private JSONObject LooksjsonObj;

    public Looks(JSONObject LooksjsonObj) {
        this.LooksjsonObj = LooksjsonObj;
    }


    public JSONArray gettemplatelist() {
        return (JSONArray) JSONUtil.getJSONProperty(LooksjsonObj, "templatelist");
    }
    public JSONArray getringslist() {
        return (JSONArray) JSONUtil.getJSONProperty(LooksjsonObj, "ringslist");
    }
    public JSONArray getearringslist() {
        return (JSONArray) JSONUtil.getJSONProperty(LooksjsonObj, "earringslist");
    }
    public JSONArray getbangleslist() {
        return (JSONArray) JSONUtil.getJSONProperty(LooksjsonObj, "bangleslist");
    }
    public JSONArray getpendantslist() {
        return (JSONArray) JSONUtil.getJSONProperty(LooksjsonObj, "pendantslist");
    }
    public JSONObject getLooksjsonObj() {
        return LooksjsonObj;
    }
}
