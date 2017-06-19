package com.kys.knowyourshop.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.kys.knowyourshop.Information.User;

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

    public void setUser(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("id", user.id);
        editor.putString("username", user.username);
        editor.putString("email", user.email);
        editor.putString("mobile", user.mobile);
        editor.putString("password", user.password);
        editor.apply();
    }

    public User getUser() {
        int id = prefs.getInt("id", 0);
        String username = prefs.getString("username", "");
        String email = prefs.getString("email", "");
        String mobile = prefs.getString("mobile", "");
        String password = prefs.getString("password", "");
        User user = new User(id, username, email, mobile, password);
        return user;
    }

    public void setLoggedIn(boolean logged) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("logged", logged);
        editor.apply();
    }

    public boolean getLoggedIn() {
        return prefs.getBoolean("logged", false);
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

    public void setShopsUserIdAvailable(Set<String> shops) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("shopsIDAvail", shops);
        editor.apply();
    }

    public Set<String> getShopsUserIdAvailable() {
        return prefs.getStringSet("shopsIDAvail", new HashSet<String>());
    }

    public void setVisited(String visited) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("visited", visited);
        editor.apply();
    }

    public String getVisited() {
        return prefs.getString("visited", "");
    }


    public void setTourShown(boolean tour) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("tour", tour);
        editor.apply();
    }

    public boolean getTourShown() {
        return prefs.getBoolean("tour", false);
    }

    public void setPlacement(boolean stay) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("stay", stay);
        editor.apply();
    }

    public boolean getPlacement() {
        return prefs.getBoolean("stay", false);
    }

    public void deleteAll() {
        setLoggedIn(false);
    }
}