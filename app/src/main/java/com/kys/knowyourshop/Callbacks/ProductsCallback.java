package com.kys.knowyourshop.Callbacks;

import com.kys.knowyourshop.Information.Product;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 04/02/2017.
 */

public interface ProductsCallback {

    void onProductsLoaded(ArrayList<Product> products);
}
