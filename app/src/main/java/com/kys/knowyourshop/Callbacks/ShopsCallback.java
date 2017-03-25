package com.kys.knowyourshop.Callbacks;

import com.kys.knowyourshop.Information.Shop;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 31/01/2017.
 */

public interface ShopsCallback {

    void onShopsLoaded(ArrayList<Shop> shops);
}
