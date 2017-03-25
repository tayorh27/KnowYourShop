package com.kys.knowyourshop.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.ProductsCallback;
import com.kys.knowyourshop.Callbacks.SpecialProductCallback;
import com.kys.knowyourshop.Information.Category;
import com.kys.knowyourshop.Information.Product;
import com.kys.knowyourshop.Information.SpecialProducts;
import com.kys.knowyourshop.Utility.Separation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 04/02/2017.
 */

public class GetProductsFromServer {

    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    public ProductsCallback productsCallback;

    public GetProductsFromServer(Context context, ProductsCallback productsCallback) {
        this.context = context;
        this.productsCallback = productsCallback;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void getProducts(String shop_name, String category) {
        String url = AppConfig.WEB_URL + "products.php?shop_name=" + shop_name + "&product_category=" + category;
        final ArrayList<Product> arrayList = new ArrayList<>();

        String _url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String shop_name = jsonObject.getString("shop_name");
                            String product_category = jsonObject.getString("product_category");
                            String product_name = jsonObject.getString("product_name");
                            String product_price = jsonObject.getString("product_price");
                            String product_description = jsonObject.getString("product_description");
                            String product_logo = jsonObject.getString("product_logo");
                            String inStock = jsonObject.getString("inStock");
                            arrayList.add(new Product(id, shop_name, product_category, product_name, product_price, product_description, product_logo, inStock));
                        }
                        if (productsCallback != null) {
                            productsCallback.onProductsLoaded(arrayList);
                        }
                    } else {
                        if (productsCallback != null) {
                            productsCallback.onProductsLoaded(arrayList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (productsCallback != null) {
                    productsCallback.onProductsLoaded(arrayList);
                }
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getAllProducts(final SpecialProductCallback specialProductCallback) {
        String url = AppConfig.WEB_URL + "allproducts.php";
        final ArrayList<SpecialProducts> arrayList = new ArrayList<>();

        String _url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String shop_name = jsonObject.getString("shop_name");
                            String product_category = jsonObject.getString("product_category");
                            String product_name = jsonObject.getString("product_name");
                            String product_price = jsonObject.getString("product_price");
                            String product_description = jsonObject.getString("product_description");
                            String product_logo = jsonObject.getString("product_logo");
                            String inStock = jsonObject.getString("inStock");
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
                            arrayList.add(new SpecialProducts(id, shop_name, product_category, product_name, product_price, product_description, product_logo, inStock, shop_description, shop_logo, shop_full_address, shop_city, shop_area, shop_inside_area, phone_number, open_time, close_time, rating, ratingCount));
                        }
                        if (specialProductCallback != null) {
                            specialProductCallback.onProducts(arrayList);
                        }
                    } else {
                        if (specialProductCallback != null) {
                            specialProductCallback.onProducts(arrayList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (specialProductCallback != null) {
                    specialProductCallback.onProducts(arrayList);
                }
            }
        });
        requestQueue.add(stringRequest);
    }
}
