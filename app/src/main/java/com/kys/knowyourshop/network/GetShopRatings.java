package com.kys.knowyourshop.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.RatingCallback;
import com.kys.knowyourshop.Information.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 24/03/2017.
 */

public class GetShopRatings {

    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    RatingCallback ratingCallback;

    public GetShopRatings(Context context, RatingCallback ratingCallback) {
        this.context = context;
        this.ratingCallback = ratingCallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void getRatings(String shop_name) {

        String url = AppConfig.WEB_URL + "ratings.php?shop_name=" + shop_name;
        final ArrayList<Rating> arrayList = new ArrayList<>();

        String _url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String shopName = jsonObject.getString("shop_name");
                            String username = jsonObject.getString("username");
                            String title = jsonObject.getString("rating_title");
                            String comment = jsonObject.getString("rating_comment");
                            String star = jsonObject.getString("rating_star");
                            String items = jsonObject.getString("items");
                            String date = jsonObject.getString("rating_date");
                            Rating rating = new Rating(shopName, username, title, comment, star, items, date);
                            arrayList.add(rating);
                        }
                        if (ratingCallback != null) {
                            ratingCallback.onRatingLoaded(arrayList);
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
