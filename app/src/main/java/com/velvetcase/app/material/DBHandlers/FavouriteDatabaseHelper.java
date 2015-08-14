package com.velvetcase.app.material.DBHandlers;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.velvetcase.app.material.Models.FavouriteDesignerModel;

public class FavouriteDatabaseHelper {
    public static final String ID = "id";
    public static final String DesignerID = "designer_id";
    public static final String DesignerName = "Designer_name";
    public static final String DesignerProducts = "Designer_product";
    public static final String DesignerImg = "Designer_img";
    public static final String DesignerFlag = "Designer_flag";
//    public static final String DesignerCity = "Designer_city";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "FavouriteDesigner.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DESIGNER_TABLE = "FavouriteDesignerTable";
    private static final String CREATE_DESIGNER_TABLE = "create table "
            + DESIGNER_TABLE + " (" + ID
            + " integer primary key autoincrement," + DesignerID + " text not null,"+ DesignerName + " text not null,"+DesignerFlag+" text not null," + DesignerProducts + " text not null,"  + DesignerImg+ " text not null );";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_DESIGNER_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DESIGNER_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public FavouriteDatabaseHelper(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }

    public FavouriteDatabaseHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public void deletetabledata() {
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("delete from "+ DESIGNER_TABLE);
        mDb.close();
    }

    public void insertFavDesigner(FavouriteDesignerModel notoffer) {
        ContentValues cv = new ContentValues();
        cv.put(DesignerID,notoffer.getdesigner_id());
        cv.put(DesignerName, notoffer.getDesigner_name());
        cv.put(DesignerImg, notoffer.getDesigner_img());
        cv.put(DesignerProducts, notoffer.getDesigner_products());
        cv.put(DesignerFlag,notoffer.getFlag_url());

//        cv.put(DesignerCity, notoffer.getDesigner_city());

        int count = CheckOfferAvailableOrNot(notoffer.getdesigner_id());
        if(count == 0){
            mDb.insert(DESIGNER_TABLE, null, cv);
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

    public ArrayList<HashMap<String,String>> getDesignersList(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selectQuery = "SELECT "+
                ID+ ","+
                DesignerID +","+
                DesignerName +","+
                DesignerProducts +","+
                DesignerFlag +","+
                DesignerImg +" FROM "+DESIGNER_TABLE;
        //Student student = new Student();
        ArrayList<HashMap<String, String>> designList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor!=null){
            // looping through all rows and adding to list
            if(cursor.moveToFirst()) {
                do {
                    HashMap<String,String> voucher = new HashMap<String,String>();
                    voucher.put(ID, cursor.getString(cursor.getColumnIndex(ID)));
                    voucher.put(DesignerID, cursor.getString(cursor.getColumnIndex(DesignerID)));
                    voucher.put(DesignerName, cursor.getString(cursor.getColumnIndex(DesignerName)));
                    voucher.put(DesignerProducts, cursor.getString(cursor.getColumnIndex(DesignerProducts)));
                    voucher.put(DesignerImg, cursor.getString(cursor.getColumnIndex(DesignerImg)));
                    voucher.put(DesignerFlag, cursor.getString(cursor.getColumnIndex(DesignerFlag)));
//                    voucher.put(DesignerCity, cursor.getString(cursor.getColumnIndex(DesignerCity)));

                    designList.add(voucher);
                }while (cursor.moveToNext());
            }else{
                return null;
            }
            cursor.close();
            db.close();
        }
        return designList;
    }
//    public int GetNotificationCount(){
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String selectQuery = "SELECT * FROM FavouriteDesignerTable  WHERE notification_read = 'No'";
//        Cursor cursor = db.rawQuery(selectQuery,null);
//        return cursor.getCount();
//    }
    public int CheckOfferAvailableOrNot(String designer_id){
        Cursor cursor = null;
        try{
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String selectQuery = "SELECT * FROM FavouriteDesignerTable  WHERE designer_id = '"+designer_id+"';";
            cursor = db.rawQuery(selectQuery,null);
        }catch(SQLiteException e){
            e.printStackTrace();
        }
        return cursor.getCount();
    }

    public void delete_row(int id) {
        mDb=mDbHelper.getWritableDatabase();
        mDb.execSQL("DELETE FROM FavouriteDesignerTable WHERE designer_id= '"+id+"'");

    }
}

