package com.kys.knowyourshop.Information;

/**
 * Created by sanniAdewale on 24/03/2017.
 */

public class Rating {

    public String shopName, username, title, comment, star, items, date;

    public Rating(String shopName, String username, String title, String comment, String star, String items, String date) {
        this.shopName = shopName;
        this.username = username;
        this.title = title;
        this.comment = comment;
        this.star = star;
        this.items = items;
        this.date = date;
    }
}
