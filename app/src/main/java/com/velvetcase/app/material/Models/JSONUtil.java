package com.velvetcase.app.material.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
	public static void setJSONProperty(JSONObject jsonObject, String key,
			Object value) {
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			// MobiculeLogger.showErrorLog("JSONUTIL: setJSONProperty",
			// e.toString());
		}
	}

	public static Object getJSONProperty(JSONObject jsonObject, String key) {
		try {
			return jsonObject.get(key);
		} catch (JSONException e) {
			// MobiculeLogger.showErrorLog("JSONUTIL: getJSONProperty",
			// e.toString());
		} 
		return null;
	}

	public static Object get(JSONArray jsonArray, int index) {
		try {
			return jsonArray.get(index);
		} catch (JSONException e) {
			// MobiculeLogger.showErrorLog("JSONUTIL:    get", e.toString());
		}
		return null;
	}

	public static boolean isValid(JSONObject jsonObject) {
		return ((jsonObject != null) && jsonObject.length() > 0) ? true : false;
	}

	public static boolean isValid(JSONArray jsonArray) {
		return ((jsonArray != null) && jsonArray.length() > 0) ? true : false;
	}

	public static Object removeJSONProperty(JSONObject jsonObject, String key) {
		try {
			if (jsonObject.has(key)) {
				return jsonObject.remove(key);
			}
		} catch (Exception e) {
//			MobiculeLogger.showErrorLog("JSONUTIL: removeJSONProperty",
//					e.toString());
		}
		return null;
	}
}
