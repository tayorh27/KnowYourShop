package com.kys.knowyourshop.Callbacks;

import com.kys.knowyourshop.Information.Route;

import java.util.List;

/**
 * Created by sanniAdewale on 02/02/2017.
 */

public interface DirectionFinderCallback {
    void LoadDistanceDuration(List<Route> routes);
    void LoadDistanceDuration();
}
