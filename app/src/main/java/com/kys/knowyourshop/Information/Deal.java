package com.kys.knowyourshop.Information;

/**
 * Created by sanniAdewale on 31/03/2017.
 */

public class Deal {

    public int id;
    public String deal, expire, comment, shop;

    public Deal(int id, String deal, String expire, String comment, String shop) {
        this.id = id;
        this.deal = deal;
        this.expire = expire;
        this.comment = comment;
        this.shop = shop;
    }
}
