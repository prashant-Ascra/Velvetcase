package com.velvetcase.app.material.Models;

import org.json.JSONObject;

/**
 * Created by Prashant Patil on 12-06-2015.
 */
public class Create {

    private JSONObject createJsonObj;

    public Create(JSONObject createJsonObj) {
        this.createJsonObj = createJsonObj;
    }

    public int getcreate_id() {
        return (int) JSONUtil.getJSONProperty(createJsonObj, "create_id");
    }
    public String getcreate_name() {
        return (String) JSONUtil.getJSONProperty(createJsonObj, "create_name");
    }
    public String getcreate_thumnail() {
        return (String) JSONUtil.getJSONProperty(createJsonObj, "create_thumnail");
    }
    public JSONObject getcreateJsonObj() {
        return createJsonObj;
    }

}
