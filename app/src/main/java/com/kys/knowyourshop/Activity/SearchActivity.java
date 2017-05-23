package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kys.knowyourshop.Adapter.ProductAdapter;
import com.kys.knowyourshop.Adapter.SearchAdapter;
import com.kys.knowyourshop.Async.PostGeneralClicks;
import com.kys.knowyourshop.Callbacks.ProductsCallback;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Callbacks.SpecialProductCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Category;
import com.kys.knowyourshop.Information.Product;
import com.kys.knowyourshop.Information.SpecialProducts;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetProductsFromServer;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity implements ProductsCallback, ShopsClickListener, SpecialProductCallback {

    AppData data;
    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    TextView tv;
    EditText editText;
    SearchAdapter adapter;
    //ArrayList<Product> productArrayList = new ArrayList<>();
    ArrayList<SpecialProducts> specialProductsArrayList = new ArrayList<>();
    ArrayList<SpecialProducts> productArrayList = new ArrayList<>();

    String[] ca;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        data = new AppData(SearchActivity.this);
        ca = data.getLocation();
        adapter = new SearchAdapter(SearchActivity.this, this);

        recylerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        editText = (EditText) findViewById(R.id.edit_query);
        tv = (TextView) findViewById(R.id.tv_info);

        recylerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recylerView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence);
                if (text.length() > 0) {
                    loading.smoothToShow();
                    productArrayList.clear();
                    ArrayList<SpecialProducts> customList = new ArrayList<>();
                    for (int k = 0; k < specialProductsArrayList.size(); k++) {
                        String online_product = specialProductsArrayList.get(k).product_name.toLowerCase();
                        String offline_product = text.toLowerCase();
                        if (online_product.contains(offline_product)) {
                            customList.add(specialProductsArrayList.get(k));
                        }
                    }
                    adapter.LoadRecyclerView(customList);
                    productArrayList = customList;
                    loading.smoothToHide();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SyncProducts();
    }

    private void SyncProducts() {
        loading.smoothToShow();
        tv.setVisibility(View.GONE);
        GetProductsFromServer getProductsFromServer = new GetProductsFromServer(SearchActivity.this, this);
        getProductsFromServer.getAllProducts(this);
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

    }

    @Override
    public void onShopsClickListener(View view, int position) {
        SpecialProducts product = productArrayList.get(position);
        PostProductClicks(product);
        Bundle bundle = new Bundle();
        bundle.putString("shop_name", product.shop_name);
        bundle.putString("shop_description", product.desc);
        bundle.putString("shop_logo", product.logo);
        bundle.putString("shop_full_address", product.full_add);
        bundle.putString("shop_city", product.city);
        bundle.putString("shop_area", product.area);
        bundle.putString("phone_number", product.phone_number);
        bundle.putString("open_time", product.open);
        bundle.putString("close_time", product.close);
        bundle.putString("rating", product.ratingStar);
        bundle.putString("ratingCount", product.ratingCount);
        Intent intent = new Intent(SearchActivity.this, InfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void PostProductClicks(SpecialProducts product) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Map<String, String> _params = new HashMap<>();
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
        PostGeneralClicks postGeneralClicks = new PostGeneralClicks(SearchActivity.this, "log.php", _params);
        postGeneralClicks.execute();
    }

    @Override
    public void onProducts(ArrayList<SpecialProducts> specialProducts) {
        specialProductsArrayList.clear();
        if (specialProducts.isEmpty()) {
            loading.smoothToHide();
            tv.setVisibility(View.VISIBLE);
            tv.setText("There is no product available.");
        } else {
            loading.smoothToHide();
            tv.setVisibility(View.GONE);
            specialProductsArrayList = specialProducts;
            Toast.makeText(SearchActivity.this, "Search Now", Toast.LENGTH_SHORT).show();
        }
    }
}
