package com.kys.knowyourshop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kys.knowyourshop.Adapter.DealAdapter;
import com.kys.knowyourshop.Callbacks.DealCallback;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Information.Deal;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetDealsFromServer;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class DealsActivity extends AppCompatActivity implements View.OnClickListener, ShopsClickListener, DealCallback {

    RecyclerView recyclerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;
    DealAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        iv.setOnClickListener(this);
        loading.smoothToShow();

        adapter = new DealAdapter(DealsActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(DealsActivity.this));
        recyclerView.setAdapter(adapter);

        SyncDeals();
    }

    private void SyncDeals() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        GetDealsFromServer getDealsFromServer = new GetDealsFromServer(DealsActivity.this, this);
        getDealsFromServer.getDeals(loading, iv, tv);
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
    public void onClick(View v) {
        SyncDeals();
    }

    @Override
    public void onShopsClickListener(View view, int position) {
        //later things
    }

    @Override
    public void onDealsLoaded(ArrayList<Deal> deals) {
        if (deals.isEmpty()) {
            loading.smoothToHide();
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("There is no deal available yet.");
            Log.e("IsEmpty", "Recommend is empty");
        } else {
            loading.smoothToHide();
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            adapter.LoadRecyclerView(deals);
        }
    }
}
