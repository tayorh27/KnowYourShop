package com.kys.knowyourshop;

import android.*;
import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kys.knowyourshop.Callbacks.LocationCallback;
import com.kys.knowyourshop.Callbacks.ShopsCallback;
import com.kys.knowyourshop.Database.AppData;
import com.kys.knowyourshop.Information.Shop;
import com.kys.knowyourshop.Information.User;
import com.kys.knowyourshop.network.GetLocationFromServer;
import com.kys.knowyourshop.network.GetShopsFromServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * Created by sanniAdewale on 23/03/2017.
 */

public class UserLocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, LocationCallback, ShopsCallback {

    Context context = MyApplication.getAppContext();
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager mLocationManager;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10; //METERS

    boolean isGPSEnabled = false;
    AppData data;

    public UserLocationService() {
        data = new AppData(context);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        if (isGPSEnabled) {
            displayLocation();
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            GetLocationFromServer getLocationFromServer = new GetLocationFromServer(context, latitude, longitude, this);
            getLocationFromServer.Locate();
            Log.e("Location Update", "LatLng = " + latitude + " / " + longitude);
        }
    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Toast.makeText(LocationUserActivity.this, "This device is not supported", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && android.support.v4.app.ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    public void setLocation(String city, String area, String inside_area, String formatted_address) {
        Log.e("After LatLng", "Inside Area = " + inside_area);
        GetShopsFromServer getShopsFromServer = new GetShopsFromServer(context, this);
        getShopsFromServer.getInsideAreaShops(inside_area);
    }

    @Override
    public void onShopsLoaded(ArrayList<Shop> shops) {
        if (!shops.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String[] mths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String date = day + "/" + mths[month] + "/" + year;
            Log.e("After Inside Area", "Inside Area Size = " + shops.size());
            data.setRatingAvailable(true);
            Set<String> myShops = new HashSet<>();
            for (int i = 0; i < shops.size(); i++) {
                myShops.add(shops.get(i).name);
            }
            data.setShopsAvailable(myShops);
            String username = "";
            String inside_area = shops.get(0).inside_area;
            String area = shops.get(0).area;
            String city = shops.get(0).city;
            User user = data.getUser();
            if (user.username.isEmpty()) {
                username = "user";
            } else {
                username = user.username;
            }
            data.setVisited("Hello " + username + ", you were located at " + inside_area + ", " + area + ", " + city + " on " + date + ". Did you visit any shop around there?");
            PugNotification.with(MyApplication.getAppContext())
                    .load()
                    .title("KnowYourShop")
                    .message("Hello " + username + ", you were on " + inside_area + ", " + area + ", " + city + " today. \nDid you visit any shop around there? Click to rate that shop.")
                    .bigTextStyle(Notification.EXTRA_BIG_TEXT)

                    .smallIcon(R.drawable.pugnotification_ic_launcher)
                    .largeIcon(R.drawable.pugnotification_ic_launcher)
                    .flags(Notification.DEFAULT_ALL)
                    .simple()
                    .build();
        }
    }
}
