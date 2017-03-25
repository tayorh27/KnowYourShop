package com.kys.knowyourshop.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kys.knowyourshop.Callbacks.ShopsCallback;
import com.kys.knowyourshop.Information.Shop;
import com.kys.knowyourshop.network.GetShopsFromServer;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 31/01/2017.
 */

public class SyncShops extends AsyncTask<Void,Void,ArrayList<Shop>>{

    public Context context;
    public ShopsCallback shopsCallback;
    public GetShopsFromServer getShopsFromServer;
    ArrayList<Shop> myList = new ArrayList<>();

    public SyncShops(Context context, ShopsCallback shopsCallback){
        this.context = context;
        this. shopsCallback = shopsCallback;
    }

    @Override
    protected ArrayList<Shop> doInBackground(Void... voids) {
        //Log.e("PostExecute", myList.get(0).name);
        return myList;
    }

    @Override
    protected void onPostExecute(ArrayList<Shop> shops) {
        Log.e("JsonArray", "length = "+shops.size()+"");
        if(shopsCallback != null){
            shopsCallback.onShopsLoaded(shops);
        }
    }
}
