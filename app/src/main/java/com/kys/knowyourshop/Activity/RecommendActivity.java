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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.kys.knowyourshop.Adapter.RecommendAdapter;
import com.kys.knowyourshop.Callbacks.NearByCallback;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Recommend;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetNearByPlacesFromServer;
import com.kys.knowyourshop.network.PostShopRecommend;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RecommendActivity extends AppCompatActivity implements View.OnClickListener, ShopsClickListener, NearByCallback {

    EditText sh, add, tel, cm;
    RecyclerView recyclerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;
    AppData data;
    String[] ca;
    RecommendAdapter adapter;
    SeekBar seekBar;
    TextView sTv;
    ArrayList<Recommend> recommendArrayList = new ArrayList<>();
    General general;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend2);

        sh = (EditText) findViewById(R.id.edit_shop_name);
        add = (EditText) findViewById(R.id.edit_shop_add);
        tel = (EditText) findViewById(R.id.edit_shop_phone);
        cm = (EditText) findViewById(R.id.edit_shop_comment);

        general = new General(RecommendActivity.this);
        data = new AppData(RecommendActivity.this);
        ca = data.getLocation();
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        sTv = (TextView) findViewById(R.id.seekText);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        iv.setOnClickListener(this);
        loading.smoothToShow();

        adapter = new RecommendAdapter(RecommendActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecommendActivity.this));
        recyclerView.setAdapter(adapter);

        int radius = seekBar.getProgress();
        SyncRecommended(radius);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sTv.setText("Radius: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int radius = seekBar.getProgress();
                SyncRecommended(radius);
            }
        });
    }

    private void SyncRecommended(int radius) {
        //recommendArrayList.clear();
        //adapter.LoadRecyclerView(recommendArrayList);
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        GetNearByPlacesFromServer getNearByPlacesFromServer = new GetNearByPlacesFromServer(RecommendActivity.this, this);
        getNearByPlacesFromServer.GetPlaces(radius, loading, iv, tv);
    }


    public void PostRecommend(View view) {
        String shop_name = sh.getText().toString();
        String shop_add = add.getText().toString();
        String shop_tel = tel.getText().toString();
        String shop_cm = cm.getText().toString();

        if (shop_name.isEmpty() && shop_add.isEmpty()) {
            general.error("Shop name and address must be filled");
            return;
        }
        PostShopRecommend postShopRecommend = new PostShopRecommend(RecommendActivity.this, shop_name, shop_add, shop_tel, shop_cm);
        postShopRecommend.PostShop(sh, add, tel, cm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int radius = seekBar.getProgress();
        SyncRecommended(radius);
    }

    @Override
    public void onShopsClickListener(View view, int position) {
        Recommend recommend = recommendArrayList.get(position);
        PostShopRecommend postShopRecommend = new PostShopRecommend(RecommendActivity.this, recommend.shop_name, recommend.shop_add, "", "");
        postShopRecommend.PostShop(sh, add, tel, cm);
    }

    @Override
    public void onGetNearByPlaces(ArrayList<Recommend> recommends) {
        recommendArrayList.clear();
        if (recommends.isEmpty()) {
            loading.smoothToHide();
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("There is no shop available within that radius.");
        } else {
            loading.smoothToHide();
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            recommendArrayList = recommends;
            adapter.LoadRecyclerView(recommends);
        }
    }
}
