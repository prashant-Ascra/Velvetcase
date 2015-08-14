package com.velvetcase.app.material.Models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Prashant Patil on 17-06-2015.
 */
public class Template {
    private JSONObject TemplateJsonObj;

    public Template(JSONObject TemplateJsonObj) {
        this.TemplateJsonObj = TemplateJsonObj;
    }
    public JSONArray getringslist() {
        return (JSONArray) JSONUtil.getJSONProperty(TemplateJsonObj, "ringslist");
    }
    public JSONArray getearringslist() {
        return (JSONArray) JSONUtil.getJSONProperty(TemplateJsonObj, "earringslist");
    }
    public JSONArray getbangleslist() {
        return (JSONArray) JSONUtil.getJSONProperty(TemplateJsonObj, "bangleslist");
    }
    public JSONArray getpendantslist() {
        return (JSONArray) JSONUtil.getJSONProperty(TemplateJsonObj, "pendantslist");
    }
    public JSONArray gettemplatelist() {
        return (JSONArray) JSONUtil.getJSONProperty(TemplateJsonObj, "templatelist");
    }
    public JSONObject getTemplateJsonObj() {
        return TemplateJsonObj;
    }
}
