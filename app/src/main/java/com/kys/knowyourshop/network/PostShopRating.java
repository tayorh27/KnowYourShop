package com.kys.knowyourshop.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kys.knowyourshop.Activity.General;
import com.kys.knowyourshop.Activity.HomeActivity;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Async.PostGeneralClicks;
import com.kys.knowyourshop.Callbacks.RatingCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Rating;
import com.kys.knowyourshop.Information.Shop;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by sanniAdewale on 24/03/2017.
 */

public class PostShopRating implements RatingCallback {
    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String shopName, username, title, comment, star, items, date;
    General general;
    AppData data;
    String[] ca;

    public PostShopRating(Context context, String shopName, String username, String title, String comment, String star, String items, String date) {
        this.context = context;
        this.shopName = shopName;
        this.username = username;
        this.title = title;
        this.comment = comment;
        this.star = star;
        this.items = items;
        this.date = date;
        general = new General(context);
        data = new AppData(context);
        ca = data.getLocation();
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void PostRating(final Set<String> shops, final Activity activity) {

        String url = AppConfig.WEB_URL + "post_ratings.php";
        general.displayDialog("Please wait...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int success;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success == 1) {
                        data.setRatingAvailable(false);
                        PostShopClicks();
                        new UpdateRatingTask().execute();
                        general.dismissDialog();
                        general.success("Shop successfully rated");
                        if (shops.size() > 1) {
                            askBeforeClosing(activity);
                        } else {
                            context.startActivity(new Intent(context, HomeActivity.class));
                            activity.finish();
                        }
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
                params.put("username", username);
                params.put("rating_title", title);
                params.put("rating_comment", comment);
                params.put("rating_star", star);
                params.put("items", items);
                params.put("rating_date", date);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void PostShopClicks() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Map<String, String> _params = new HashMap<>();
        _params.put("shop_name", shopName);
        _params.put("product_name", "");
        _params.put("category_name", "");
        _params.put("rating", star);
        _params.put("comment", "");
        _params.put("day", String.valueOf(day));
        _params.put("month", mths[month]);
        _params.put("year", String.valueOf(year));
        _params.put("user_location", ca[3]);
        _params.put("type", "Rating");
        PostGeneralClicks postGeneralClicks = new PostGeneralClicks(context, "log.php", _params);
        postGeneralClicks.execute();

    }

    private void askBeforeClosing(final Activity activity) {
        new MaterialDialog.Builder(context)
                .title("Notification")
                .content("Do you still want to rate other shops?")
                .positiveText("YES")
                .negativeText("NO")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        data.setRatingAvailable(false);
                        context.startActivity(new Intent(context, HomeActivity.class));
                        activity.finish();
                    }
                }).show();
    }

    private void GetRatingsFirst() {
        GetShopRatings getShopRatings = new GetShopRatings(context, this);
        getShopRatings.getRatings(shopName);
    }

    @Override
    public void onRatingLoaded(ArrayList<Rating> ratingArrayList) {

        String ratingCount = String.valueOf(ratingArrayList.size());
        int count = ratingArrayList.size();
        float rateStars = 0;

        for (int i = 0; i < ratingArrayList.size(); i++) {
            Rating rating = ratingArrayList.get(i);
            rateStars += Float.parseFloat(rating.star);
        }

        float avg_rate_star = rateStars / count;
        String avg = String.valueOf(avg_rate_star);
        UpdateShopRatings(ratingCount, avg);
    }

    private void UpdateShopRatings(String ratingCount, String avg_rate_star) {
        UpdateShopRating updateShopRating = new UpdateShopRating(context, shopName, ratingCount, avg_rate_star);
        updateShopRating.UpdateRating();
    }


    class UpdateRatingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            GetRatingsFirst();
            return null;
        }
    }
}
