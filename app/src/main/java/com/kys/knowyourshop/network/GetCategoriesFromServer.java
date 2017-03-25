package com.kys.knowyourshop.network;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.CategoriesCallback;
import com.kys.knowyourshop.Information.Category;
import com.kys.knowyourshop.Utility.Separation;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 01/02/2017.
 */

public class GetCategoriesFromServer {

    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    public CategoriesCallback categoriesCallback;

    public GetCategoriesFromServer(Context context, CategoriesCallback categoriesCallback) {
        this.context = context;
        this.categoriesCallback = categoriesCallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void getCategories(String shop_name, final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv) {

        String url = AppConfig.WEB_URL + "categories.php?shop_name=" + shop_name;
        final ArrayList<Category> arrayList = new ArrayList<>();

        String _url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("There is no category available in this shop.");
                        }
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String category = jsonObject.getString("product_category");
                            arrayList.add(new Category(id, category));
                        }
                        ArrayList<String> stringArrayList = Separation.separate(arrayList);
                        if (categoriesCallback != null) {
                            categoriesCallback.onCategoriesLoaded(stringArrayList);
                        }
                    } else {
                        ArrayList<String> stringArrayList = new ArrayList<>();
                        if (categoriesCallback != null) {
                            categoriesCallback.onCategoriesLoaded(stringArrayList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ArrayList<String> stringArrayList = new ArrayList<>();
                //stringArrayList.add(error.getMessage());
                if (categoriesCallback != null) {
                    categoriesCallback.onCategoriesLoaded(stringArrayList);
                }
            }
        });

        requestQueue.add(stringRequest);
    }
}
