package com.velvetcase.app.material.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.velvetcase.app.material.Models.WishListModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant Patil on 12-06-2015.
 */
public class WishListDBHelper {

    public static final String ID = "id";
    public static final String ProductID = "product_id";
    public static final String ProductJSon = "product_json";
   
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "WishListDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String WISHLIST_TABLE = "WishListTable";
    private static final String CREATE_WISHLIST_TABLE = "create table "
            + WISHLIST_TABLE + " (" + ID
            + " integer primary key autoincrement," + ProductID + " text not null,"+ ProductJSon+ " text not null );";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_WISHLIST_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + WISHLIST_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public WishListDBHelper(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }

    public WishListDBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void deletetabledata() {
        try {

            mDb = mDbHelper.getWritableDatabase();
            mDb.delete(WISHLIST_TABLE, null, null);
            mDb.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void insertWishListTable(WishListModel notoffer) {

        ContentValues cv = new ContentValues();
        cv.put(ProductID,notoffer.getProduct_id());
        cv.put(ProductJSon, notoffer.getProduct_json());

        int count = CheckOfferAvailableOrNot(notoffer.getProduct_id());

        if(count == 0){
            mDb.insert(WISHLIST_TABLE, null, cv);
        }else{

        }


    }

    public void UpdateSeenFlag(String Flag,String offercode) {
//        mDb = mDbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(NOT_SEEN_FLAG,Flag);
//        String updateQuery = "UPDATE NotificationVouchers SET not_seen_flag ='"+Flag+"' WHERE offer_code ='"+offercode+"';";
//        Log.e("Quesry", updateQuery);
//        mDb.rawQuery(updateQuery, null).moveToFirst();
    }

    public void UpdateNotificationCountFlag(String Flag) {
//        mDb = mDbHelper.getWritableDatabase();
//        String updateQuery = "UPDATE NotificationVouchers SET notification_read ='"+Flag+"' WHERE notification_read ='No';";
//        Log.e("Quesry1", updateQuery);
//        mDb.rawQuery(updateQuery, null).moveToFirst();
    }

    public ArrayList<HashMap<String,String>> getWishList(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selectQuery = "SELECT "+
                ID+ ","+
                ProductID +","+
                ProductJSon +" FROM "+WISHLIST_TABLE;
        //Student student = new Student();
        ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor!=null){
            // looping through all rows and adding to list
            if(cursor.moveToFirst()) {
                do {
                    HashMap<String,String> voucher = new HashMap<String,String>();
                    voucher.put(ID, cursor.getString(cursor.getColumnIndex(ID)));
                    voucher.put(ProductID, cursor.getString(cursor.getColumnIndex(ProductID)));
                    voucher.put(ProductJSon, cursor.getString(cursor.getColumnIndex(ProductJSon)));
                    productList.add(voucher);
                }while (cursor.moveToNext());
            }else{
                return null;
            }
            cursor.close();
            db.close();
        }
        return productList;
    }
    //    public int GetNotificationCount(){
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String selectQuery = "SELECT * FROM FavouriteDesignerTable  WHERE notification_read = 'No'";
//        Cursor cursor = db.rawQuery(selectQuery,null);
//        return cursor.getCount();
//    }
    public int CheckOfferAvailableOrNot(String product_id){
        Cursor cursor = null;
        try{
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String selectQuery = "SELECT * FROM WishListTable  WHERE product_id = '"+product_id+"';";
            cursor = db.rawQuery(selectQuery,null);
        }catch(SQLiteException e){
            e.printStackTrace();
        }
        return cursor.getCount();
    }

    public void delete_row(int id) {
        mDb=mDbHelper.getWritableDatabase();
        mDb.execSQL("DELETE FROM WishListTable WHERE product_id= '"+id+"'");

    }
}



