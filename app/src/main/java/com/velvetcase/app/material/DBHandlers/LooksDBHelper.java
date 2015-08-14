package com.velvetcase.app.material.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


import com.velvetcase.app.material.Models.MyLooks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abc on 7/4/2015.
 */
public class LooksDBHelper {

    public static final String ID = "id";
    public static final String TEMPLATE_ID = "tmplate_id_id";
    public static final String TEMPLATE_JSon = "template_json";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "LookListDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String LOOKLIST_TABLE = "LookListTable";
    private static final String CREATE_LOOKLIST_TABLE = "create table "
            + LOOKLIST_TABLE + " (" + ID
            + " integer primary key autoincrement," + TEMPLATE_ID + " text not null,"+ TEMPLATE_JSon+ " text not null );";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_LOOKLIST_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + LOOKLIST_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public LooksDBHelper(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }

    public LooksDBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void deletetabledata() {
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("delete from "+ LOOKLIST_TABLE);
        mDb.close();
    }

    public void insertWishListTable(MyLooks notoffer) {
        ContentValues cv = new ContentValues();
        cv.put(TEMPLATE_ID,notoffer.getTemplate_id());
        cv.put(TEMPLATE_JSon, notoffer.getTemplate_object());

        int count = CheckOfferAvailableOrNot(notoffer.getTemplate_id());
        if(count == 0){
            mDb.insert(LOOKLIST_TABLE, null, cv);
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
                TEMPLATE_ID +","+
                TEMPLATE_JSon +" FROM "+LOOKLIST_TABLE;
        //Student student = new Student();
        ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor!=null){
            // looping through all rows and adding to list
            if(cursor.moveToFirst()) {
                do {
                    HashMap<String,String> voucher = new HashMap<String,String>();
                    voucher.put(ID, cursor.getString(cursor.getColumnIndex(ID)));
                    voucher.put(TEMPLATE_ID, cursor.getString(cursor.getColumnIndex(TEMPLATE_ID)));
                    voucher.put(TEMPLATE_JSon, cursor.getString(cursor.getColumnIndex(TEMPLATE_JSon)));
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

    public void delete_row(String id) {
        mDb=mDbHelper.getWritableDatabase();
        mDb.execSQL("DELETE FROM WishListTable WHERE product_id= '"+id+"'");

    }
}
