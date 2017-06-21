package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kys.knowyourshop.Adapter.ProductAdapter;
import com.kys.knowyourshop.Async.PostGeneralClicks;
import com.kys.knowyourshop.Callbacks.ProductsCallback;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Product;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetProductsFromServer;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductsActivity extends AppCompatActivity implements View.OnClickListener, ProductsCallback, ShopsClickListener {

    AppData data;
    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;
    String name, cat;
    ProductAdapter adapter;
    ArrayList<Product> customData = new ArrayList<>();
    String[] ca;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("shop_name");
        cat = bundle.getString("product_category");

        getSupportActionBar().setTitle(name);

        data = new AppData(ProductsActivity.this);
        adapter = new ProductAdapter(ProductsActivity.this, this);

        ca = data.getLocation();

        recylerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        iv.setOnClickListener(this);
        loading.smoothToShow();

        recylerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
        recylerView.setAdapter(adapter);

        SyncProducts();
    }

    private void SyncProducts() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        GetProductsFromServer getProductsFromServer = new GetProductsFromServer(ProductsActivity.this, this);
        getProductsFromServer.getProducts(name, cat);
    }

    @Override
    public void onClick(View view) {
        SyncProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductsLoaded(ArrayList<Product> products) {
        if (products.isEmpty()) {
            loading.smoothToHide();
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("There is no product available.");
        } else {
            loading.smoothToHide();
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            adapter.LoadRecyclerView(products);
            customData.clear();
            customData = products;
        }
    }

    @Override
    public void onShopsClickListener(View view, int position) {
        Product product = customData.get(position);
        PostProductClicks(product);
        Bundle bundle = new Bundle();
        bundle.putString("shop_name", product.shop_name);
        bundle.putString("product_description", product.product_description);
        bundle.putString("product_logo", product.product_logo);
        bundle.putString("product_category", product.product_category);
        bundle.putString("product_name", product.product_name);
        bundle.putString("product_price", product.product_price);
        bundle.putString("in_stock", product.in_stock);
        Intent intent = new Intent(ProductsActivity.this, ViewProductActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void PostProductClicks(Product product) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Map<String, String> _params = new HashMap<>();
        _params.put("user_id", product.user_id + "");
        _params.put("shop_name", product.shop_name);
        _params.put("product_name", product.product_name);
        _params.put("category_name", product.product_category);
        _params.put("rating", "");
        _params.put("comment", "");
        _params.put("day", String.valueOf(day));
        _params.put("month", mths[month]);
        _params.put("year", String.valueOf(year));
        _params.put("user_location", ca[3]);
        _params.put("type", "Product");
        PostGeneralClicks postGeneralClicks = new PostGeneralClicks(ProductsActivity.this, "log.php", _params);
        postGeneralClicks.execute();
    }
}
