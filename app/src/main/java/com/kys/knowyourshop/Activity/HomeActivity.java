package com.kys.knowyourshop.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.kys.knowyourshop.Adapter.HomeAdapter;
import com.kys.knowyourshop.Async.PostGeneralClicks;
import com.kys.knowyourshop.Async.SyncShops;
import com.kys.knowyourshop.Callbacks.ShopsCallback;
import com.kys.knowyourshop.Callbacks.ShopsClickListener;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Shop;
import com.kys.knowyourshop.MainActivity;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetShopsFromServer;
import com.kys.knowyourshop.network.PostShopRecommend;
import com.wang.avi.AVLoadingIndicatorView;

import org.joda.time.YearMonth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ShopsCallback, View.OnClickListener, ShopsClickListener {

    AppData data;
    HomeAdapter adapter;
    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv, avail;
    BootstrapButton btnOthers;
    ArrayList<Shop> current = new ArrayList<>();
    String[] ca;
    private static final int CHECK_OTHERS_CODE = 27;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        data = new AppData(HomeActivity.this);
        ca = data.getLocation();
        getSupportActionBar().setTitle(ca[0]);
        toolbar.setSubtitle(ca[1] + ", " + ca[2]);

        Log.e("Locations", ca[0] + ", " + ca[1] + ", " + ca[2]);

        recylerView = (RecyclerView) findViewById(R.id.recyclerView);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        avail = (TextView) findViewById(R.id.nShops);
        btnOthers = (BootstrapButton) findViewById(R.id.check_others);
        iv.setOnClickListener(this);
        loading.smoothToShow();

        adapter = new HomeAdapter(HomeActivity.this, this);
        recylerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recylerView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView tvAreas = (TextView) navigationView.getHeaderView(0).findViewById(R.id.areas);
        ImageView icon = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        //int color2 = generator.getColor("user@gmail.com")
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .height(64)
                .width(64)
                .toUpperCase()
                .endConfig()
                .buildRoundRect("U", color1, 32);
        icon.setImageDrawable(textDrawable);
        tvAreas.setText(ca[1] + ", " + ca[2]);

        navigationView.setNavigationItemSelectedListener(this);

        SyncingShops("");

    }

    private void SyncingShops(String area) {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        btnOthers.setVisibility(View.GONE);
        final GetShopsFromServer getShopsFromServer = new GetShopsFromServer(HomeActivity.this, this);
        getShopsFromServer.getShops(area, loading, iv, tv, avail, btnOthers);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
        }
        if (id == R.id.action_nav) {
            Intent intent = new Intent(HomeActivity.this, OthersActivity.class);
            startActivityForResult(intent, CHECK_OTHERS_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recommend) {
            startActivity(new Intent(HomeActivity.this, RecommendActivity.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CheckOthers(View view) {
        Intent intent = new Intent(HomeActivity.this, OthersActivity.class);
        startActivityForResult(intent, CHECK_OTHERS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CHECK_OTHERS_CODE) {
            Bundle bundle = data.getExtras();
            String sa = bundle.getString("shop_area");
            SyncingShops(sa);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (data.isRatingAvailable()) {
            startActivity(new Intent(HomeActivity.this, RatingActivity.class));
        }
    }

    @Override
    public void onShopsLoaded(ArrayList<Shop> shops) {
        current.clear();
        if (shops.isEmpty()) {
            loading.smoothToHide();
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            btnOthers.setVisibility(View.VISIBLE);
            tv.setText("There is no shop available in your area.");
            avail.setText("There is no shop available in your area.");
        } else {
            current = shops;
            loading.smoothToHide();
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            btnOthers.setVisibility(View.GONE);
            int count = shops.size();
            if (count == 0) {
                avail.setText("There is no shop available in your area.");
            } else if (count == 1) {
                avail.setText(count + " Shop available in your area.");
            } else {
                avail.setText(count + " Shops available in your area.");
            }
            adapter.LoadRecyclerView(shops);
        }
    }

    @Override
    public void onClick(View view) {
        SyncingShops("");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onShopsClickListener(View view, int position) {
        Shop sh = current.get(position);
        PostShopClicks(sh);
        Bundle bundle = new Bundle();
        bundle.putString("shop_name", sh.name);
        bundle.putString("shop_description", sh.desc);
        bundle.putString("shop_logo", sh.logo);
        bundle.putString("shop_full_address", sh.full_add);
        bundle.putString("shop_city", sh.city);
        bundle.putString("shop_area", sh.area);
        bundle.putString("phone_number", sh.phone_number);
        bundle.putString("open_time", sh.open);
        bundle.putString("close_time", sh.close);
        bundle.putString("rating", sh.ratingStar);
        bundle.putString("ratingCount", sh.ratingCount);
        Intent intent = new Intent(HomeActivity.this, ShopActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void PostShopClicks(Shop shop) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Map<String, String> _params = new HashMap<>();
        _params.put("shop_name", shop.name);
        _params.put("product_name", "");
        _params.put("category_name", "");
        _params.put("rating", "");
        _params.put("comment", "");
        _params.put("day", String.valueOf(day));
        _params.put("month", mths[month]);
        _params.put("year", String.valueOf(year));
        _params.put("user_location", ca[3]);
        _params.put("type", "Shop");
        PostGeneralClicks postGeneralClicks = new PostGeneralClicks(HomeActivity.this, "log.php", _params);
        postGeneralClicks.execute();

    }
}
