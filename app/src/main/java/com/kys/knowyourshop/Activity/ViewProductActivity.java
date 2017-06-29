package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.R;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewProductActivity extends AppCompatActivity {

    String shop_name, product_description, product_logo, product_category, product_name, product_price, in_stock;

    TextView tvDesc, tvCat, tvPrice, tvStock, tvShopName;
    ImageView imageView;
    AppBarLayout appBarLayout;
    String mUrl;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        tvDesc = (TextView) findViewById(R.id.p_desc);
        tvCat = (TextView) findViewById(R.id.p_cat);
        tvPrice = (TextView) findViewById(R.id.p_price);
        tvStock = (TextView) findViewById(R.id.p_stock);
        tvShopName = (TextView) findViewById(R.id.p_shop_name);
        imageView = (ImageView) findViewById(R.id.p_image);


        Bundle bundle = getIntent().getExtras();
        shop_name = bundle.getString("shop_name");
        product_description = bundle.getString("product_description");
        product_logo = bundle.getString("product_logo");
        product_category = bundle.getString("product_category");
        product_name = bundle.getString("product_name");
        product_price = bundle.getString("product_price");
        in_stock = bundle.getString("in_stock");

        getSupportActionBar().setTitle(product_name);

        tvDesc.setText(product_description);
        tvCat.setText(product_category);
        tvPrice.setText("₦" + product_price);
        tvStock.setText(in_stock);
        tvShopName.setText(shop_name);

        String url = AppConfig.WEB_URL + "images/" + product_logo;
        if (product_logo.contains("http") || product_logo.contains("www.")) {
            url = product_logo;
        }
        String _url = url.replace(" ", "%20");
        mUrl = _url;
        Glide.with(ViewProductActivity.this).load(_url).fitCenter().centerCrop().placeholder(R.drawable.no_logo).crossFade().error(R.drawable.no_logo).into(imageView);
        // Picasso.with(ViewProductActivity.this).load(_url).centerCrop().placeholder(R.drawable.no_logo).fit().into(imageView);
    }

    public void ImageClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("img", mUrl);
        bundle.putString("name", product_name);
        Intent intent = new Intent(ViewProductActivity.this, ImageActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
