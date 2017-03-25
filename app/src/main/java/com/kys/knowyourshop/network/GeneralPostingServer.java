package com.kys.knowyourshop.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanniAdewale on 22/03/2017.
 */

public class GeneralPostingServer {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String url_name;
    Map<String, String> params = new HashMap<>();
    Map<String, String> myParams = new HashMap<>();

    public GeneralPostingServer(Context context, String url_name, Map<String, String> params) {
        this.context = context;
        this.url_name = url_name;
        this.params = params;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void PostMe() {
        String url = AppConfig.WEB_URL + url_name;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int success;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success == 1) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public String getPostBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                myParams = params;
                //Log.e("Params", "myParams = " + myParams.get("shop_name"));
                return myParams;
            }
        };
        requestQueue.add(stringRequest);
    }
}
