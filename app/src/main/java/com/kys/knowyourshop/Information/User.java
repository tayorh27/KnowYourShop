package com.kys.knowyourshop.Information;

/**
 * Created by sanniAdewale on 26/03/2017.
 */

public class User {

    public int id;
    public String username, email, mobile, password;

    public User(int id, String username, String email, String mobile, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }
}
