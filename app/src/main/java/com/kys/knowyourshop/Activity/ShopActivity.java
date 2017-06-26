package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.kys.knowyourshop.Adapter.CategoryAdapter;
import com.kys.knowyourshop.Adapter.HomeAdapter;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Async.PostGeneralClicks;
import com.kys.knowyourshop.Callbacks.CategoriesCallback;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Category;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetCategoriesFromServer;
import com.kys.knowyourshop.network.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener, ShopsClickListener, CategoriesCallback {

    AppData data;
    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;
    String logo, ratingStar, ratingCount, name, desc, full_add, city, area, phone_number, open, close;
    ImageView ivv;
    RatingBar ratingBar;
    TextView ratingCountt;
    TextView sName, sDesc;
    VolleySingleton volleySingleton;
    ImageLoader imageLoader;
    CategoryAdapter adapter;
    ArrayList<Category> categoryArrayList = new ArrayList<>();
    String[] ca;
    int user_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();

        ivv = (ImageView) findViewById(R.id.shop_logo);
        ratingBar = (RatingBar) findViewById(R.id.shop_rating);
        ratingCountt = (TextView) findViewById(R.id.shop_rating_number);
        sName = (TextView) findViewById(R.id.shop_name);
        sDesc = (TextView) findViewById(R.id.shop_desc);

        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getInt("user_id", 0);
        name = bundle.getString("shop_name");
        desc = bundle.getString("shop_description");
        logo = bundle.getString("shop_logo");
        full_add = bundle.getString("shop_full_address");
        city = bundle.getString("shop_city");
        area = bundle.getString("shop_area");
        phone_number = bundle.getString("phone_number");
        open = bundle.getString("open_time");
        close = bundle.getString("close_time");
        ratingStar = bundle.getString("rating");
        ratingCount = bundle.getString("ratingCount");

        getSupportActionBar().setTitle(name);
        String url = AppConfig.WEB_URL + "images/" + logo;
        String _url = url.replace(" ", "%20");
        Glide.with(ShopActivity.this).load(_url).fitCenter().centerCrop().placeholder(R.drawable.no_logo).crossFade().error(R.drawable.no_logo).into(ivv);
//        imageLoader.get(_url, new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                ivv.setImageBitmap(response.getBitmap());
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
        sName.setText(name);
        sDesc.setText(desc);
        ratingBar.setRating(Float.parseFloat(ratingStar));
        ratingCountt.setText("(" + ratingCount + ")");

        data = new AppData(ShopActivity.this);
        ca = data.getLocation();
        recylerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        iv.setOnClickListener(this);
        loading.smoothToShow();

        adapter = new CategoryAdapter(ShopActivity.this, this);
        recylerView.setLayoutManager(new LinearLayoutManager(ShopActivity.this));
        recylerView.setAdapter(adapter);

        SyncCategories();
    }

    @Override
    public void onClick(View view) {
        SyncCategories();
    }

    private void SyncCategories() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        final GetCategoriesFromServer getCategoriesFromServer = new GetCategoriesFromServer(ShopActivity.this, this);
        getCategoriesFromServer.getCategories(name, loading, iv, tv);
    }

    @Override
    public void onShopsClickListener(View view, int position) {
        Category category = categoryArrayList.get(position);
        PostCategoryClicks(category);
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", user_id);
        bundle.putString("shop_name", name);
        bundle.putString("product_category", category.category);
        Intent intent = new Intent(ShopActivity.this, ProductsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void PostCategoryClicks(Category category) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Map<String, String> _params = new HashMap<>();
        _params.put("user_id", user_id + "");
        _params.put("shop_name", name);
        _params.put("product_name", "");
        _params.put("category_name", category.category);
        _params.put("rating", "");
        _params.put("comment", "");
        _params.put("day", String.valueOf(day));
        _params.put("month", mths[month]);
        _params.put("year", String.valueOf(year));
        _params.put("user_location", ca[3]);
        _params.put("type", "Category");
        PostGeneralClicks postGeneralClicks = new PostGeneralClicks(ShopActivity.this, "log.php", _params);
        postGeneralClicks.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Bundle bundle = new Bundle();
            bundle.putString("shop_name", name);
            bundle.putString("shop_description", desc);
            bundle.putString("shop_logo", logo);
            bundle.putString("shop_full_address", full_add);
            bundle.putString("shop_city", city);
            bundle.putString("shop_area", area);
            bundle.putString("phone_number", phone_number);
            bundle.putString("open_time", open);
            bundle.putString("close_time", close);
            bundle.putString("rating", ratingStar);
            bundle.putString("ratingCount", ratingCount);
            Intent intent = new Intent(ShopActivity.this, InfoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_rate) {
            Bundle bundle = new Bundle();
            bundle.putString("shop_name", name);
            Intent intent = new Intent(ShopActivity.this, RatingActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SyncCategories();
    }

    @Override
    public void onCategoriesLoaded(ArrayList<String> categories) {
        categoryArrayList.clear();
        if (categories.isEmpty()) {
            loading.smoothToHide();
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("There is no category available in this shop.");
        } else {
            for (int i = 0; i < categories.size(); i++) {
                categoryArrayList.add(new Category((i + 1), categories.get(i)));
            }
            loading.smoothToHide();
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            adapter.LoadRecyclerView(categoryArrayList);
        }
    }
}
