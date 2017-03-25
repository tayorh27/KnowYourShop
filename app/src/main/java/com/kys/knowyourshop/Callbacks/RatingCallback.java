package com.kys.knowyourshop.Callbacks;

import com.kys.knowyourshop.Information.Rating;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 24/03/2017.
 */

public interface RatingCallback {

    void onRatingLoaded(ArrayList<Rating> ratingArrayList);
}
