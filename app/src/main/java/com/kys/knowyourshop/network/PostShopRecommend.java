package com.kys.knowyourshop.network;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.Activity.General;
import com.kys.knowyourshop.AppConfig;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanniAdewale on 03/03/2017.
 */

public class PostShopRecommend {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String shopName, shopAdd, tel, comment;
    General general;

    public PostShopRecommend(Context context, String shopName, String shopAdd, String tel, String comment) {
        this.context = context;
        this.shopAdd = shopAdd;
        this.shopName = shopName;
        this.tel = tel;
        this.comment = comment;
        general = new General(context);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void PostShop(final EditText sh, final EditText add, final EditText tel2, final EditText cm) {

        String url = AppConfig.WEB_URL + "post_recommend.php";
        general.displayDialog("Please wait...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int success;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success == 1) {
                        general.dismissDialog();
                        general.success("Shop successfully recommended");
                        sh.setText("");
                        add.setText("");
                        tel2.setText("");
                        cm.setText("");
                    } else {
                        general.dismissDialog();
                        general.error("Something went wrong. Please try again.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                general.dismissDialog();
                general.error("Something went wrong. Please try again.");
            }
        }) {
            @Override
            public String getPostBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shop_name", shopName);
                params.put("street_name", shopAdd);
                params.put("telephone", tel);
                params.put("comment", comment);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
