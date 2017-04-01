package com.kys.knowyourshop.Callbacks;

import com.kys.knowyourshop.Information.Deal;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 31/03/2017.
 */

public interface DealCallback {
    void onDealsLoaded(ArrayList<Deal> deals);
}
