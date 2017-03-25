package com.kys.knowyourshop.Information;

/**
 * Created by sanniAdewale on 04/02/2017.
 */

public class Product {

    public int id;
    public String shop_name, product_category, product_name, product_price, product_description, product_logo, in_stock;

    public Product(int id, String shop_name, String product_category, String product_name, String product_price, String product_description, String product_logo, String in_stock) {
        this.id = id;
        this.shop_name = shop_name;
        this.product_category = product_category;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_description = product_description;
        this.product_logo = product_logo;
        this.in_stock = in_stock;
    }
}
