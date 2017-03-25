package com.kys.knowyourshop.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sanniAdewale on 04/01/2017.
 */

public class AppData {

    Context context;
    SharedPreferences prefs;

    public AppData(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("app_data", 0);
    }

    public void setLocation(String city, String area, String inside_area, String formatted_address) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city", city);
        editor.putString("area", area);
        editor.putString("inside_area", inside_area);
        editor.putString("formatted_address", formatted_address);
        editor.apply();
    }

    public String[] getLocation() {
        String[] locations = {
                prefs.getString("city", ""),
                prefs.getString("area", ""),
                prefs.getString("inside_area", ""),
                prefs.getString("formatted_address", "")
        };
        return locations;
    }

    public void setLocationEnter(boolean loc) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("enter", loc);
        editor.apply();
    }

    public boolean getLocationEnter() {
        return prefs.getBoolean("enter", false);
    }

    public void setLatLng(String latitude, String longitude) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.apply();
    }

    public String getLatitude() {
        return prefs.getString("latitude", "0.0");
    }

    public String getLongitude() {
        return prefs.getString("longitude", "0.0");
    }

    public void setRatingAvailable(boolean rating) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("rating", rating);
        editor.apply();
    }

    public boolean isRatingAvailable() {
        return prefs.getBoolean("rating", false);
    }

    public void setShopsAvailable(Set<String> shops) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("shopsAvail", shops);
        editor.apply();
    }

    public Set<String> getShopsAvailable() {
        return prefs.getStringSet("shopsAvail", new HashSet<String>());
    }


    public void setTourShown(boolean tour) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("tour", tour);
        editor.apply();
    }

    public boolean getTourShown() {
        return prefs.getBoolean("tour", false);
    }
}