package com.kys.knowyourshop.Utility;

import com.kys.knowyourshop.Information.Category;
import com.kys.knowyourshop.Information.Shop;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 01/02/2017.
 */

public class Separation {

    private static ArrayList<String> done = new ArrayList<>();

    public static ArrayList<String> separate(ArrayList<Category> getCategories){
        done.clear();
        for (int i = 0; i < getCategories.size(); i++){
            if(!done.contains(getCategories.get(i).category)){
                done.add(getCategories.get(i).category);
            }
        }
        return done;
    }

    private static ArrayList<String> doneShops = new ArrayList<>();

    public static ArrayList<String> separateShops(ArrayList<Shop> shops){
        doneShops.clear();
        for (int i = 0; i < shops.size(); i++){
            if(!doneShops.contains(shops.get(i).area)){
                doneShops.add(shops.get(i).area);
            }
        }
        return doneShops;
    }
}
