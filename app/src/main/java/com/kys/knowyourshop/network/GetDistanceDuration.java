package com.kys.knowyourshop.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.LocationCallback;
import com.kys.knowyourshop.Callbacks.MapDetailsCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sanniAdewale on 02/02/2017.
 */

public class GetDistanceDuration {

    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    public MapDetailsCallback mapDetailsCallback;
    public String origin, dest;

    public GetDistanceDuration(Context context, String origin, String dest, MapDetailsCallback mapDetailsCallback) {
        this.context = context;
        this.origin = origin;
        this.dest = dest;
        this.mapDetailsCallback = mapDetailsCallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void getDetails(){

        String _url = AppConfig.GET_DISTANCE_AND_DURATION + origin + "&destinations=" + dest + "&key=" + AppConfig.API_KEY_FOR_DISTANCE_DURATION;
        String nUrl = _url.replace(" ","%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, nUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("OK")){
                        JSONArray array = object.getJSONArray("rows");
                        JSONObject object1 = array.getJSONObject(0);
                        JSONArray array1 = object1.getJSONArray("elements");
                        JSONObject object2 = array1.getJSONObject(0);
                        JSONObject object3 = object2.getJSONObject("distance");
                        JSONObject object4 = object2.getJSONObject("duration");

                        String g_distance = object3.getString("text");
                        int g_distance_value = object3.getInt("value");
                        String g_duration = object4.getString("text");
                        int g_duration_value = object4.getInt("value");

                        if(mapDetailsCallback != null){
                            mapDetailsCallback.onDetailsCallback(g_distance,g_duration,g_distance_value,g_duration_value);
                        }

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

    }
}
