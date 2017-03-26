package com.kys.knowyourshop.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.Adscallback;
import com.kys.knowyourshop.Information.Ads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 26/03/2017.
 */

public class GetAdsFromServer {
    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    Adscallback adscallback;

    public GetAdsFromServer(Context context, Adscallback adscallback) {
        this.context = context;
        this.adscallback = adscallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void getAds() {

        String url = AppConfig.WEB_URL + "ads.php";
        final ArrayList<Ads> arrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String adImage = jsonObject.getString("ad_image");
                            String adLink = jsonObject.getString("ad_link");
                            arrayList.add(new Ads(id, adImage, adLink));
                        }
                        if (adscallback != null) {
                            adscallback.onAdsLoaded(arrayList);
                        }
                    }
                } catch (JSONException e) {
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
