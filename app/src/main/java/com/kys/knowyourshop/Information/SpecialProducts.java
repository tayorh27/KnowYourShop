package com.kys.knowyourshop.Information;

/**
 * Created by sanniAdewale on 24/03/2017.
 */

public class SpecialProducts {

    public int id;
    public String shop_name, product_category, product_name, product_price, product_description, product_logo, in_stock;
    public String logo, ratingStar, ratingCount, name, desc, full_add, city, area, inside_area, phone_number, open, close;

    public SpecialProducts(int id, String shop_name, String product_category, String product_name, String product_price, String product_description, String product_logo, String in_stock,
                           String desc, String logo, String fullAddress, String city, String area, String inside_area, String phone_number, String open, String close, String ratingStar, String ratingCount) {
        this.id = id;
        this.shop_name = shop_name;
        this.product_category = product_category;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_description = product_description;
        this.product_logo = product_logo;
        this.in_stock = in_stock;
        this.logo = logo;
        this.desc = desc;
        this.full_add = fullAddress;
        this.city = city;
        this.area = area;
        this.inside_area = inside_area;
        this.phone_number = phone_number;
        this.open = open;
        this.close = close;
        this.ratingStar = ratingStar;
        this.ratingCount = ratingCount;
    }
}
