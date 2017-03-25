package com.kys.knowyourshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kys.knowyourshop.Activity.HomeActivity;
import com.kys.knowyourshop.Adapter.PlaceArrayAdapter;
import com.kys.knowyourshop.Callbacks.LocationCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.network.GetLocationFromServer;
import com.wang.avi.AVLoadingIndicatorView;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener, LocationCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    //    TextView tv;
//    ImageView iv;
    GPSService gps;
    AVLoadingIndicatorView ref;
    AppData data;

    private static final String LOG_TAG = "LocationActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(0, -0), new LatLng(0, -0));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        data = new AppData(LocationActivity.this);

//        tv = (TextView) findViewById(R.id.tv_info);
//        iv = (ImageView) findViewById(R.id.click_refresh);
//        iv.setOnClickListener(this);

        ref = (AVLoadingIndicatorView) findViewById(R.id.loading);
        ref.smoothToHide();

        mGoogleApiClient = new GoogleApiClient.Builder(LocationActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        mAutocompleteTextView.setOnItemClickListener(mAutoCompleteClickListener);
        mAutocompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ref.smoothToShow();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //ref.smoothToHide();
            }
        });
        //LoadLocation();


    }

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String location = mAutocompleteTextView.getText().toString();
            //data.setLocation(location);
            data.setLocationEnter(true);
            startActivity(new Intent(LocationActivity.this, HomeActivity.class));
            finish();

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            //}
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

            //distanceDuration();
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

//            String text = Html.fromHtml(place.getName() + "\n").toString();
//            text += Html.fromHtml(place.getAddress() + "\n").toString();
//            text += Html.fromHtml(place.getId() + "\n").toString();
//            text += Html.fromHtml(place.getPhoneNumber() + "\n").toString();
//            text += place.getWebsiteUri() + "\n";
//            if (attributions != null) {
//                text += Html.fromHtml(attributions.toString());
//            }
//
//            book_now.setText(text);
        }
    };

    @Override
    public void onClick(View view) {
        LoadLocation();
    }

    public void LoadLocation() {
        ref.smoothToShow();
        //iv.setVisibility(View.GONE);
        //tv.setText("Getting your location...");
        gps = new GPSService(LocationActivity.this);
        gps.getLocation();

        if (!gps.isLocationAvailable) {
            ref.smoothToHide();
            // iv.setVisibility(View.VISIBLE);
            // tv.setText("Your location is not available, please try again.");
            Log.e("Location", "Your location is not available");
            return;
        } else {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            GetLocationFromServer getLocationFromServer = new GetLocationFromServer(LocationActivity.this, latitude, longitude, this);
            getLocationFromServer.Locate();
        }
        gps.closeGPS();
    }

    @Override
    public void setLocation(String city, String area, String inside_area, String fo) {
        if (city.isEmpty()) {
            ref.smoothToHide();
            // iv.setVisibility(View.VISIBLE);
            // tv.setText("Your location is not available, please try again.");
            return;
        }
        //data.setLocation(city, area, inside_area);
        //data.setLocationEnter(true);
        startActivity(new Intent(LocationActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
        Toast.makeText(LocationActivity.this, "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }
}
