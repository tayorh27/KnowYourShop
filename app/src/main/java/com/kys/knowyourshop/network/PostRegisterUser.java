package com.kys.knowyourshop.network;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.Activity.General;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanniAdewale on 26/03/2017.
 */

public class PostRegisterUser {
    Context context;
    General general;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String username, email, mobile, password, access_code;
    AppData data;

    public PostRegisterUser(Context context, String username, String email, String mobile, String password, String access_code) {
        this.context = context;
        general = new General(context);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.access_code = access_code;
        data = new AppData(context);
    }

    public void Register(final Activity activity) {
        String url = AppConfig.WEB_URL + "UserReg.php";
        general.displayDialog("Registering user");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int success;
                try {
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if (success == 1) {
                        general.dismissDialog();
                        Toast.makeText(context, "Registration successful. Login now.", Toast.LENGTH_LONG).show();
                        User user = new User(0, username, email, mobile, password);
                        data.setUser(user);
                        data.setLoggedIn(true);
                        activity.finish();
                    } else if (success == 0) {
                        general.dismissDialog();
                        general.error("An error occurred. Check your internet connection.");
                    } else if (success == 2) {
                        general.dismissDialog();
                        general.error("An error occurred. Email already exists.");
                    } else if (success == 3) {
                        general.dismissDialog();
                        general.error("An error occurred. Username already exists.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                general.dismissDialog();
                general.error("An error occurred. Check your internet connection.");
            }
        }) {
            @Override
            public String getPostBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                params.put("access_code", access_code);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
