package ua.kiev.foxtrot.kopilochka.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.data.BBS_News;

/**
 * Created by NickNb on 30.09.2016.
 */
public class DB {
    private static String TAG = "DB";

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = Tables.table_name_bbs_news;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_URLIMG = "urlToImage";
    public static final String COLUMN_PUBLISH = "publishedAt";


    private final Context context;
    boolean transaction_success = false;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        context = ctx;
        mDBHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void create(){
        //mDBHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void open() {
        //mDBHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public long addRec(BBS_News data) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AUTHOR, data.getAuthor());
        cv.put(COLUMN_TITLE, data.getTitle());
        cv.put(COLUMN_DESC, data.getDescription());
        cv.put(COLUMN_URL, data.getUrl());
        cv.put(COLUMN_URLIMG, data.getUrlToImage());
        cv.put(COLUMN_PUBLISH, data.getPublishedAt());
        return mDB.insert(DB_TABLE, null, cv);
    }

    public boolean addNewsArray(ArrayList<BBS_News> news){
        this.getDB().beginTransaction();
        try {
            for(int i=0;i<news.size();i++) {
                if(this.addRec(news.get(i))==-1){
                    throw new Exception("FAIL");
                };
            }
            this.getDB().setTransactionSuccessful();
            Log.v(TAG, "SSS setTransactionSuccessful");
        } catch (Exception e){
            transaction_success = false;
            Log.v(TAG, "SSS setTransaction Exception");
        } finally {
            this.getDB().endTransaction();
            transaction_success = true;
            Log.v(TAG, "SSS setTransaction Finally");
        }
        this.close();
        if(transaction_success){
            //Send ok to server
            return true;
        } else {
            //Try again later
            return false;
        }
    }

    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    public SQLiteDatabase getDB (){
        return mDB;
    }

}
