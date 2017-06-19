package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.kys.knowyourshop.Adapter.OthersAdapter;
import com.kys.knowyourshop.Callbacks.ShopsCallback;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Shop;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.Utility.Separation;
import com.kys.knowyourshop.network.GetShopsFromServer;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OthersActivity extends AppCompatActivity implements ShopsClickListener, View.OnClickListener, ShopsCallback {

    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv, avail;
    BootstrapButton button;
    OthersAdapter adapter;
    ArrayList<Shop> shopArrayList = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recylerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        avail = (TextView) findViewById(R.id.tv_info2);
        button = (BootstrapButton) findViewById(R.id.btn);
        iv.setOnClickListener(this);
        loading.smoothToShow();
        adapter = new OthersAdapter(OthersActivity.this, this);
        recylerView.setLayoutManager(new LinearLayoutManager(OthersActivity.this));
        recylerView.setAdapter(adapter);

        SyncAreas();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            //NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SyncAreas() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        final GetShopsFromServer getShopsFromServer = new GetShopsFromServer(OthersActivity.this, this);
        getShopsFromServer.getAllShops(loading, iv, tv, avail, button);
    }

    @Override
    public void onShopsClickListener(View view, int position) {
        Shop shop = shopArrayList.get(position);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("shop_area", shop.area);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        SyncAreas();
    }

    @Override
    public void onShopsLoaded(ArrayList<Shop> shops) {
        shopArrayList.clear();
        if (shops.isEmpty()) {
            loading.smoothToHide();
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("Ooops something went wrong");
        } else {
            loading.smoothToHide();
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            ArrayList<String> sp = Separation.separateShops(shops);
            for (int i = 0; i < sp.size(); i++) {
                Shop sh = new Shop(0, 0, "", "", "", "", "", sp.get(i),"", "", "", "", "", "");
                shopArrayList.add(sh);
            }
            adapter.LoadRecyclerView(shopArrayList);
        }
    }
}
