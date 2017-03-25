package com.kys.knowyourshop.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kys.knowyourshop.Adapter.RatingAdapter;
import com.kys.knowyourshop.AppConfig;
import com.kys.knowyourshop.Callbacks.DirectionFinderCallback;
import com.kys.knowyourshop.Callbacks.MapDetailsCallback;
import com.kys.knowyourshop.Callbacks.RatingCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Rating;
import com.kys.knowyourshop.Information.Route;
import com.kys.knowyourshop.R;
import com.kys.knowyourshop.network.GetDirectionsFromMap;
import com.kys.knowyourshop.network.GetDistanceDuration;
import com.kys.knowyourshop.network.GetShopRatings;
import com.kys.knowyourshop.network.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InfoActivity extends AppCompatActivity implements MapDetailsCallback, OnMapReadyCallback, DirectionFinderCallback, RatingCallback {

    String logo, ratingStar, ratingCount, name, desc, full_add, city, area, phone_number, open, close;
    ImageView ivv;
    RatingBar ratingBar, ratingBar2;
    TextView ratingCountt;
    TextView sName, sDesc;
    VolleySingleton volleySingleton;
    ImageLoader imageLoader;
    TextView times, number, rateCount, rateV, address;
    RecyclerView recyclerView;

    GetDistanceDuration getDistanceDuration;
    AppData data;
    TextView tvDis, tvDur;
    AVLoadingIndicatorView loadingIndicatorView;

    private GoogleMap mMap;
    GetDirectionsFromMap getDirectionsFromMap;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    RatingAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
        data = new AppData(InfoActivity.this);
        adapter = new RatingAdapter(InfoActivity.this);
        String[] ca = data.getLocation();


        ivv = (ImageView) findViewById(R.id.shop_logo);
        ratingBar = (RatingBar) findViewById(R.id.shop_rating);
        ratingCountt = (TextView) findViewById(R.id.shop_rating_number);
        sName = (TextView) findViewById(R.id.shop_name);
        sDesc = (TextView) findViewById(R.id.shop_desc);
        recyclerView = (RecyclerView) findViewById(R.id.infoRecyclerView);

        times = (TextView) findViewById(R.id.day2);
        number = (TextView) findViewById(R.id.ph);
        rateCount = (TextView) findViewById(R.id.rate);
        rateV = (TextView) findViewById(R.id.rateValue);
        ratingBar2 = (RatingBar) findViewById(R.id.rateStar);
        address = (TextView) findViewById(R.id.address);

        tvDis = (TextView) findViewById(R.id.tvDistance);
        tvDur = (TextView) findViewById(R.id.tvDuration);
        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loading);
        loadingIndicatorView.smoothToShow();


        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("shop_name") != null) {
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
        }

        String origin = ca[3];
        getDistanceDuration = new GetDistanceDuration(InfoActivity.this, origin, full_add, this);
        //getDistanceDuration.getDetails();

        getDirectionsFromMap = new GetDirectionsFromMap(InfoActivity.this, origin, full_add, this);
        getDirectionsFromMap.getDirections();

        getSupportActionBar().setTitle(name);

        String url = AppConfig.WEB_URL + "images/" + logo;
        String _url = url.replace(" ", "%20");
        imageLoader.get(_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                ivv.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        sName.setText(name);
        sDesc.setText(desc);
        ratingBar.setRating(Float.parseFloat(ratingStar));
        ratingCountt.setText("(" + ratingCount + ")");

        times.setText(open + " - " + close);
        number.setText(phone_number);
        if (Integer.parseInt(ratingCount) > 1) {
            rateCount.setText(ratingCount + " Ratings");
        } else {
            rateCount.setText(ratingCount + " Rating");
        }
        rateV.setText(ratingStar);
        ratingBar2.setRating(Float.parseFloat(ratingStar));
        address.setText(full_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
        recyclerView.setAdapter(adapter);
        SyncRatings();
    }

    private void SyncRatings() {
        GetShopRatings getShopRatings = new GetShopRatings(InfoActivity.this, this);
        getShopRatings.getRatings(name);
    }

    public void CallNumber(View view) {
        String getNumber = number.getText().toString();
        if (!getNumber.contains(",")) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel: " + getNumber));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
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
    public void onDetailsCallback(String distance, String duration, int distance_value, int duration_value) {

        tvDis.setText(distance);
        tvDur.setText(duration);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void LoadDistanceDuration(List<Route> routes) {
        loadingIndicatorView.smoothToHide();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText("ETA: " + route.duration);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Your current location")
                    .snippet(route.startAddress)//"Your current Location"+"-"+
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(name)
                    .snippet(route.endAddress)//"Driver Location"+"-"+
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(20);

            for (int i = 0; i < route.points.size(); i++) {
                polylineOptions.add(route.points.get(i));
                Log.e("adding polyline", route.points.get(i).toString());
            }

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }

    }

    @Override
    public void LoadDistanceDuration() {

    }

    @Override
    public void onRatingLoaded(ArrayList<Rating> ratingArrayList) {
        if (!ratingArrayList.isEmpty()) {
            adapter.LoadRecyclerView(ratingArrayList);
        }
    }
}
