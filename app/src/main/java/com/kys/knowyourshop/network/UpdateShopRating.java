package com.kys.knowyourshop.network;

import android.content.Context;

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
 * Created by sanniAdewale on 24/03/2017.
 */

public class UpdateShopRating {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String shopName, ratingCount, ratingStar;

    public UpdateShopRating(Context context, String shopName, String ratingCount, String ratingStar) {
        this.context = context;
        this.shopName = shopName;
        this.ratingCount = ratingCount;
        this.ratingStar = ratingStar;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void UpdateRating() {
        String url = AppConfig.WEB_URL + "update_shop_rating.php?shop_name=" + shopName + "&rating=" + ratingStar + "&ratingCount=" + ratingCount;
        String _url = url.replace(" ", "%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int success;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success == 1) {
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UpdateRating();
            }
        });
        requestQueue.add(stringRequest);
    }
}
