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
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.ShopsCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Shop;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 31/01/2017.
 */

public class GetShopsFromServer {

    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    public ShopsCallback shopsCallback;
    AppData data;
    String url = "";

    public GetShopsFromServer(Context context, ShopsCallback shopsCallback) {
        this.context = context;
        this.shopsCallback = shopsCallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        data = new AppData(context);
    }

    public void getShops(String shop_area, final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv, final TextView avail, final BootstrapButton btnOthers) {
        if (!shop_area.contentEquals("")) {
            url = AppConfig.WEB_URL + "shops.php?shop_area=" + shop_area;
        } else {
            String[] ca = data.getLocation();
            url = AppConfig.WEB_URL + "shops.php?shop_area=" + ca[1];
        }
        final ArrayList<Shop> shops = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null && avail != null && btnOthers != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            btnOthers.setVisibility(View.VISIBLE);
                            tv.setText("There is no shop available in your area.");
                            avail.setText("There is no shop available in your area.");
                        }
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("JsonArray", "length = " + jsonArray.length() + "");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String shop_name = jsonObject.getString("shop_name");
                            String shop_description = jsonObject.getString("shop_description");
                            String shop_logo = jsonObject.getString("shop_logo");
                            String shop_full_address = jsonObject.getString("shop_full_address");
                            String shop_city = jsonObject.getString("shop_city");
                            String shop_area = jsonObject.getString("shop_area");
                            String shop_inside_area = jsonObject.getString("shop_inside_area");
                            String phone_number = jsonObject.getString("phone_number");
                            String open_time = jsonObject.getString("open_time");
                            String close_time = jsonObject.getString("close_time");
                            String rating = jsonObject.getString("rating");
                            String ratingCount = jsonObject.getString("ratingCount");
                            shops.add(new Shop(id, shop_name, shop_description, shop_logo, shop_full_address, shop_city, shop_area, shop_inside_area, phone_number, open_time, close_time, rating, ratingCount));
                        }
                        if (shopsCallback != null) {
                            shopsCallback.onShopsLoaded(shops);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loading != null && iv != null && tv != null && avail != null && btnOthers != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    btnOthers.setVisibility(View.VISIBLE);
                    tv.setText("An error occurred. Check your internet connection.");
                    avail.setText("There is no shop available in your area.");
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getAllShops(final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv, final TextView avail, final BootstrapButton btnOthers) {

        String _url = AppConfig.WEB_URL + "all_shops.php";
        final ArrayList<Shop> shops = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null && avail != null && btnOthers != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            btnOthers.setVisibility(View.VISIBLE);
                            tv.setText("There is no shop available in your area.");
                            avail.setText("There is no shop available in your area.");
                        }
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("JsonArray", "length = " + jsonArray.length() + "");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String shop_name = jsonObject.getString("shop_name");
                            String shop_description = jsonObject.getString("shop_description");
                            String shop_logo = jsonObject.getString("shop_logo");
                            String shop_full_address = jsonObject.getString("shop_full_address");
                            String shop_city = jsonObject.getString("shop_city");
                            String shop_area = jsonObject.getString("shop_area");
                            String shop_inside_area = jsonObject.getString("shop_inside_area");
                            String phone_number = jsonObject.getString("phone_number");
                            String open_time = jsonObject.getString("open_time");
                            String close_time = jsonObject.getString("close_time");
                            String rating = jsonObject.getString("rating");
                            String ratingCount = jsonObject.getString("ratingCount");
                            shops.add(new Shop(id, shop_name, shop_description, shop_logo, shop_full_address, shop_city, shop_area, shop_inside_area, phone_number, open_time, close_time, rating, ratingCount));
                        }
                        if (shopsCallback != null) {
                            shopsCallback.onShopsLoaded(shops);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loading != null && iv != null && tv != null && avail != null && btnOthers != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    btnOthers.setVisibility(View.VISIBLE);
                    tv.setText("An error occurred. Check your internet connection.");
                    avail.setText("There is no shop available in your area.");
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getInsideAreaShops(String shop_inside_area) {
        String url = AppConfig.WEB_URL + "shop_inside_area.php?shop_inside_area=" + shop_inside_area;
        String _url = url.replace(" ", "%20");
        final ArrayList<Shop> shops = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {

                        return;
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("JsonArray", "length = " + jsonArray.length() + "");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String shop_name = jsonObject.getString("shop_name");
                            String shop_description = jsonObject.getString("shop_description");
                            String shop_logo = jsonObject.getString("shop_logo");
                            String shop_full_address = jsonObject.getString("shop_full_address");
                            String shop_city = jsonObject.getString("shop_city");
                            String shop_area = jsonObject.getString("shop_area");
                            String shop_inside_area = jsonObject.getString("shop_inside_area");
                            String phone_number = jsonObject.getString("phone_number");
                            String open_time = jsonObject.getString("open_time");
                            String close_time = jsonObject.getString("close_time");
                            String rating = jsonObject.getString("rating");
                            String ratingCount = jsonObject.getString("ratingCount");
                            shops.add(new Shop(id, shop_name, shop_description, shop_logo, shop_full_address, shop_city, shop_area, shop_inside_area, phone_number, open_time, close_time, rating, ratingCount));
                        }
                        if (shopsCallback != null) {
                            shopsCallback.onShopsLoaded(shops);
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
