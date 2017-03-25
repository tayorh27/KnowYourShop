package com.kys.knowyourshop.Callbacks;

import com.kys.knowyourshop.Information.Recommend;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 23/03/2017.
 */

public interface NearByCallback {
    void onGetNearByPlaces(ArrayList<Recommend> recommends);
}
