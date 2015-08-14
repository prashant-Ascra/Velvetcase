package com.velvetcase.app.material.Models;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Base64;


public class ServiceHandlerAuthentication {

	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;

	public ServiceHandlerAuthentication() {
	}
	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	public String makeServiceCall(String url, int method,String SessionToken) {
		return this.makeServiceCall(url, method, null, SessionToken);
	}
	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public String makeServiceCall(String url, int method,
			List<NameValuePair> params,String SessionToken) {
		try {
			final String basicAuth = "Basic " + Base64.encodeToString(("mea-api-user" + ":" + "MCMapi42015").getBytes(), Base64.NO_WRAP);
			basicAuth.replace("\n", "");
			DefaultHttpClient httpClient = new DefaultHttpClient();				
		
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			// Checking http request method type
			if (method == POST) {			
				HttpPost httpPost = new HttpPost(url);
				httpPost.setHeader("Authorization", basicAuth);
				if(SessionToken!=null){
					httpPost.setHeader("X-MC-Session-Token", SessionToken);
				}

				// adding post params
				if (params != null) {
					for(int i = 0; i < params.size() ; i++){

					}
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}else{
					
				}
				httpResponse = httpClient.execute(httpPost);
			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);
				httpGet.setHeader("Authorization", basicAuth);
				if(SessionToken!=null){
					httpGet.setHeader("X-MC-Session-Token", SessionToken);
				}
				httpResponse = httpClient.execute(httpGet);
			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return response;
	}
	
	

}
