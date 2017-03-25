package com.kys.knowyourshop.Information;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by sanniAdewale on 02/02/2017.
 */

public class Route {

    public String distance,duration,endAddress,startAddress;
    public LatLng startLocation,endLocation;
    public List<LatLng> points;
}
