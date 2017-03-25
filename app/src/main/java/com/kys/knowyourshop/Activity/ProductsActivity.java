package com.kys.knowyourshop.Activity;

import android.content.Context;
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
import com.kys.knowyourshop.Callbacks.ProductsCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Product;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetProductsFromServer;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductsActivity extends AppCompatActivity implements View.OnClickListener, ProductsCallback {

    AppData data;
    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;
    String name,cat;
    ProductAdapter adapter;

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
        adapter = new ProductAdapter(ProductsActivity.this);

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
        getProductsFromServer.getProducts(name,cat);
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
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
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
        }
    }
}
