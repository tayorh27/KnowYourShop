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
import com.kys.knowyourshop.Callbacks.DealCallback;
import com.kys.knowyourshop.Callbacks.ShopsCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Deal;
import com.kys.knowyourshop.Information.Shop;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 31/03/2017.
 */

public class GetDealsFromServer {

    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    public DealCallback dealCallback;

    public GetDealsFromServer(Context context, DealCallback dealCallback) {
        this.context = context;
        this.dealCallback = dealCallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void getDeals(final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv) {
        String url = AppConfig.WEB_URL + "deals.php";
        final ArrayList<Deal> deals = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("There is no deal available yet.");
                        }
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("JsonArray", "length = " + jsonArray.length() + "");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String dealName = jsonObject.getString("deal_offer");
                            String expire = jsonObject.getString("expires");
                            String comment = jsonObject.getString("deal_comment");
                            String shop_name = jsonObject.getString("shop_name");
                            deals.add(new Deal(id, dealName, expire, comment, shop_name));
                        }
                        if (dealCallback != null) {
                            dealCallback.onDealsLoaded(deals);
                        }
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
                    tv.setText("An error occurred. Check your internet connection.");
                }
            }
        });
        requestQueue.add(stringRequest);
    }
}
