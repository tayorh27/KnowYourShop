package com.kys.knowyourshop.network;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.NearByCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Recommend;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 23/03/2017.
 */

public class GetNearByPlacesFromServer {

    Context context;
    NearByCallback nearByCallback;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    AppData data;

    public GetNearByPlacesFromServer(Context context, NearByCallback nearByCallback) {
        this.context = context;
        this.nearByCallback = nearByCallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        data = new AppData(context);
    }

    public void GetPlaces(int radius, final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv) {
        final ArrayList<Recommend> recommendArrayList = new ArrayList<>();
        String latitude = data.getLatitude();
        String longitude = data.getLongitude();
        String url = AppConfig.GET_NEARBY_PLACES + latitude + "," + longitude + "&radius=" + radius + "&sensor=true&key=" + AppConfig.API_KEY_FOR_DISTANCE_DURATION + "&types=jewelry_store,bicycle_store,book_store,meal_delivery,car_dealer,city_hall,clothing_store,department_store,electronics_store,shoe_store,shopping_mall,furniture_store,hardware_store,home_goods_store";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponse", "Response = " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("results");
                    if (array.length() < 0) {
                        if (loading != null && iv != null && tv != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("There is no shop available within that radius.");
                        }
                        return;
                    }
                    Log.e("onResponseArray", "ArraySize = " + array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Log.e("onArrayObject", "ArrayObject = " + jsonObject.toString());
                        String icon = jsonObject.getString("icon");
                        String name = jsonObject.getString("name");
                        String add = jsonObject.getString("vicinity");
                        String rating = "";
                        if (jsonObject.getString("rating") != null) {
                            rating = jsonObject.getString("rating");
                        } else {
                            rating = "0";
                        }
                        Log.e("name" + i, "Name of place = " + name);
                        Recommend recommend = new Recommend(icon, rating, name, add);
                        recommendArrayList.add(recommend);
                    }
                    if (nearByCallback != null) {
                        nearByCallback.onGetNearByPlaces(recommendArrayList);
                        Log.e("after", "recommendArrayList = " + recommendArrayList.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loading != null && iv != null && tv != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("There is no shop available within that radius.");
                }
            }
        });
        requestQueue.add(stringRequest);

    }
}
