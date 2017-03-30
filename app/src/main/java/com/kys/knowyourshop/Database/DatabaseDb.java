package com.kys.knowyourshop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kys.knowyourshop.Information.Shop;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 29/03/2017.
 */

public class DatabaseDb {

    ShopHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public DatabaseDb(Context context) {
        helper = new ShopHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insertMyShop(ArrayList<Shop> lists, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        String sql = "INSERT INTO " + ShopHelper.TABLE_NAME_MYPOST + " VALUES(?,?,?,?,?,?);";
        //compile statement and start a transaction
        SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < lists.size(); i++) {
            Shop current = lists.get(i);
            statement.clearBindings();

            statement.bindString(2, current.logo);
            statement.bindString(3, current.ratingStar);
            statement.bindString(4, current.ratingCount);
            statement.bindString(5, current.name);
            statement.bindString(6, current.desc);
            statement.execute();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    public ArrayList<Shop> getAllMyShop() {
        ArrayList<Shop> currentData = new ArrayList<>();

        String[] columns = {
                ShopHelper.COLUMN_ID,
                ShopHelper.COLUMN_LOGO,
                ShopHelper.COLUMN_RATE,
                ShopHelper.COLUMN_RATE_COUNT,
                ShopHelper.COLUMN_NAME,
                ShopHelper.COLUMN_DESC
        };
        Cursor cursor = sqLiteDatabase.query(ShopHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Shop current = new Shop();
                current.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ShopHelper.COLUMN_ID)));
                current.logo = cursor.getString(cursor.getColumnIndex(ShopHelper.COLUMN_LOGO));
                current.ratingStar = cursor.getString(cursor.getColumnIndex(ShopHelper.COLUMN_RATE));
                current.ratingCount = cursor.getString(cursor.getColumnIndex(ShopHelper.COLUMN_RATE_COUNT));
                current.name = cursor.getString(cursor.getColumnIndex(ShopHelper.COLUMN_NAME));
                current.desc = cursor.getString(cursor.getColumnIndex(ShopHelper.COLUMN_DESC));
                currentData.add(current);
            }
            cursor.close();
        }

        return currentData;
    }

    public int getLastId() {
        int id = 0;
        String[] columns = {
                ShopHelper.COLUMN_ID,
                ShopHelper.COLUMN_LOGO,
                ShopHelper.COLUMN_RATE,
                ShopHelper.COLUMN_RATE_COUNT,
                ShopHelper.COLUMN_NAME,
                ShopHelper.COLUMN_DESC
        };
        Cursor cursor = sqLiteDatabase.query(ShopHelper.TABLE_NAME_MYPOST, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToLast();
            id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ShopHelper.COLUMN_ID)));
            //cursor.close();
        }
        Log.e("gotID", "my id is " + id);
        return id;
    }

    public void deleteAll() {
        sqLiteDatabase.delete(ShopHelper.TABLE_NAME_MYPOST, null, null);
    }

    public void updateDatabase(int foreignKey, String what_to_update, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(what_to_update, status);//ShopHelper.COLUMN_STATUS
        sqLiteDatabase.update(ShopHelper.TABLE_NAME_MYPOST, contentValues, ShopHelper.COLUMN_ID + "=" + foreignKey, null);//
        Log.e("UPDATE", "database updated to " + status);
    }

    public void deleteDatabase(int id) {
        sqLiteDatabase.delete(ShopHelper.TABLE_NAME_MYPOST, ShopHelper.COLUMN_ID + "=" + id, null);
    }


    public class ShopHelper extends SQLiteOpenHelper {

        private Context mContext;
        private static final String DB_NAME = "Shop_db";
        private static final int DB_VERSION = 3;

        public static final String TABLE_NAME_MYPOST = "Shop101";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_LOGO = "logo";
        public static final String COLUMN_RATE = "ratings";
        public static final String COLUMN_RATE_COUNT = "ratingsCount";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "desc";

        private static final String CREATE_TABLE_MYPOST = "CREATE TABLE " + TABLE_NAME_MYPOST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LOGO + " TEXT," +
                COLUMN_RATE + " TEXT," +
                COLUMN_RATE_COUNT + " TEXT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_DESC + " TEXT" +
                ");";


        public ShopHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE_MYPOST);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(" DROP TABLE " + TABLE_NAME_MYPOST + " IF EXISTS;");
                onCreate(sqLiteDatabase);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
    }

}
