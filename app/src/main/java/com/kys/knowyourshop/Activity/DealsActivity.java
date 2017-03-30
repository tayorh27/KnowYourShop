package com.kys.knowyourshop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kys.knowyourshop.R;
import com.wang.avi.AVLoadingIndicatorView;

public class DealsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;

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

        //adapter = new RecommendAdapter(RecommendActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(DealsActivity.this));
        //recyclerView.setAdapter(adapter);

        SyncDeals();
    }

    private void SyncDeals() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        //GetNearByPlacesFromServer getNearByPlacesFromServer = new GetNearByPlacesFromServer(RecommendActivity.this, this);
        //getNearByPlacesFromServer.GetPlaces(radius, loading, iv, tv);
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
}
