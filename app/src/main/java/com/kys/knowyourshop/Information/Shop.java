package com.kys.knowyourshop.Information;

/**
 * Created by sanniAdewale on 31/01/2017.
 */

public class Shop {

    public int id;
    public String logo, ratingStar, ratingCount, name, desc, full_add, city, area, inside_area, phone_number, open, close;

    public Shop(int id, String name, String desc, String logo, String fullAddress, String city, String area, String inside_area, String phone_number, String open, String close, String ratingStar, String ratingCount) {
        this.id = id;
        this.logo = logo;
        this.ratingStar = ratingStar;
        this.ratingCount = ratingCount;
        this.name = name;
        this.desc = desc;
        this.full_add = fullAddress;
        this.city = city;
        this.area = area;
        this.inside_area = inside_area;
        this.phone_number = phone_number;
        this.open = open;
        this.close = close;
    }
}
