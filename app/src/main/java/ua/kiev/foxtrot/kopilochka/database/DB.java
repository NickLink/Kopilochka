package ua.kiev.foxtrot.kopilochka.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.data.Model;
import ua.kiev.foxtrot.kopilochka.data.Notice;
import ua.kiev.foxtrot.kopilochka.data.Post_SN;
import ua.kiev.foxtrot.kopilochka.data.ProductGroup;

/**
 * Created by NickNb on 30.09.2016.
 */
public class DB {
    private static String TAG = "DB";

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_BBS = Tables.table_name_bbs_news;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_URLIMG = "urlToImage";
    public static final String COLUMN_PUBLISH = "publishedAt";
    public static final String TIME_NOW  = " time('now') ";


    private Context context;
    boolean transaction_success = false;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        this.context = ctx;
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

    public void erase(){
        this.open();
        mDB.execSQL("delete from "+ Tables.table_name_bbs_news);
        mDB.execSQL("delete from "+ Tables.table_name_notices);
        mDB.execSQL("delete from "+ Tables.table_name_models);
        mDB.execSQL("delete from "+ Tables.table_name_actions);
        mDB.execSQL("delete from "+ Tables.table_name_postsn);
        this.close();
    }

    //========================================================================

    public Cursor getAllData() {
        return mDB.query(DB_TABLE_BBS, null, null, null, null, null, null);
    }

    public Cursor getData_forTitle(String title) {
        String selection = COLUMN_TITLE + " = " + title;
        String[] columns = new String[] { selection };
        return mDB.query(DB_TABLE_BBS, columns, null, null, null, null, null);
    }

    public Cursor getData_forId(int id) {
        long row_id = id + 1;
        String selection = COLUMN_ID + " = " + row_id;
        return mDB.query(DB_TABLE_BBS, null, selection, null, null, null, null);
    }

    public long addRec(BBS_News data) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_AUTHOR, data.getAuthor());
        cv.put(COLUMN_TITLE, data.getTitle());
        cv.put(COLUMN_DESC, data.getDescription());
        cv.put(COLUMN_URL, data.getUrl());
        cv.put(COLUMN_URLIMG, data.getUrlToImage());
        cv.put(COLUMN_PUBLISH, data.getPublishedAt());
        return mDB.insert(DB_TABLE_BBS, null, cv);
    }

    public boolean addNewsArray(ArrayList<BBS_News> news){
        this.open();
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
        mDB.delete(DB_TABLE_BBS, COLUMN_ID + " = " + id, null);
    }

    public SQLiteDatabase getDB (){
        return mDB;
    }



    //===========================NOTICES===================================
    public Cursor getNoticeData() {
        return mDB.query(Tables.table_name_notices, null, null, null, null, null, null);
    }

    public long addNotice(Notice data) {
        ContentValues cv = new ContentValues();
        cv.put(Const.notice_id, data.getNotice_id());
        cv.put(Const.notice_name, data.getNotice_name());
        cv.put(Const.notice_text, data.getNotice_text());
        cv.put(Const.notice_type_id, data.getNotice_type_id());
        cv.put(Const.notice_type, data.getNotice_type());
        return mDB.insert(Tables.table_name_notices, null, cv);
    }

    public boolean addNoticeArray(ArrayList<Notice> news){
        this.open();
        this.getDB().beginTransaction();
        try {
            mDB.execSQL("delete from "+ Tables.table_name_notices);
            for(int i=0;i<news.size();i++) {
                if(this.addNotice(news.get(i))==-1){
                    throw new Exception("FAIL");
                };
            }
            this.getDB().setTransactionSuccessful();
            transaction_success = true;
        } catch (Exception e){
            transaction_success = false;
        } finally {
            this.getDB().endTransaction();
        }
        this.close();
        if(transaction_success){
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Notice> getNoticeArray(){
        ArrayList<Notice> notif_data = new ArrayList<Notice>();
        this.open();
        Cursor myCursor = this.getNoticeData();
        myCursor.moveToFirst();
        while (myCursor.isAfterLast() == false) {
            Notice item = new Notice();
            item.setNotice_id(myCursor.getInt(myCursor.getColumnIndex(Const.notice_id)));
            item.setNotice_name(myCursor.getString(myCursor.getColumnIndex(Const.notice_name)));
            item.setNotice_text(myCursor.getString(myCursor.getColumnIndex(Const.notice_text)));
            item.setNotice_type_id(myCursor.getInt(myCursor.getColumnIndex(Const.notice_type_id)));
            item.setNotice_type(myCursor.getString(myCursor.getColumnIndex(Const.notice_type)));
            notif_data.add(item);
            myCursor.moveToNext();
        }
        Log.v("", "SSS Finish = " + notif_data.size());
        this.close();
        return notif_data;
    }
    //==================================MODELS DATA====================================
//    public Cursor getModelsForPGroup_id(int group_id, int action_type_id){
//        String table = Tables.table_name_models + " as MD inner join "
//                + Tables.table_name_actions + " as AC on MD." + Const.model_action + " = AC." + Const.action_id;
//
//
//        String selection = "";
//        String[] selection_args = {};
//
//        return mDB.query(Tables.table_name_models, columns, null, null, Const.model_group_name, null, Const.model_group_name);
//    }

    public Cursor getModelsForPGroup(){
        String[] columns = new String[] { Const.model_group_name, "COUNT(*) AS " + Const.models_count, Const.model_group_id };
        return mDB.query(Tables.table_name_models, columns, null, null, Const.model_group_name, null, Const.model_group_name);
    }

    public ArrayList<ProductGroup> getGroupsNamesAndCount(){
        ArrayList<ProductGroup> arrayList = new ArrayList<ProductGroup>();
        this.open();
        Cursor myCursor = getModelsForPGroup();
        myCursor.moveToFirst();
        while (!myCursor.isAfterLast()){
            ProductGroup item = new ProductGroup();
            String groupName = myCursor.getString(myCursor.getColumnIndex(Const.model_group_name));
            int modelsCount = myCursor.getInt(myCursor.getColumnIndex(Const.models_count));
            int group_id = myCursor.getInt(myCursor.getColumnIndex(Const.model_group_id));
            item.setGroup_name(groupName);
            item.setModels_count(modelsCount);
            item.setGroup_id(group_id);
            arrayList.add(item);
            Log.v("TAG", "SSSS groupName =" + groupName + " modelsCount =" + modelsCount + " group_id =" + group_id);
            myCursor.moveToNext();
        }
        this.close();
        return arrayList;
    }


    public Cursor getModelsCursor() {
        return mDB.query(Tables.table_name_models, null, null, null, null, null, null);
    }

    public Cursor getModelByActionIdCursor(int action) {
        String selection = Const.model_action + " = ?";
        String[] selectionArgs = {String.valueOf(action)};
        return mDB.query(Tables.table_name_models, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getModelByIdsCursor(int action, int model) {
        String selection = Const.model_action + " = ?"  +" AND " + Const.model_id + " = ?";
        String[] selectionArgs = {String.valueOf(action), String.valueOf(model)};
        return mDB.query(Tables.table_name_models, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getActionsCursor() {
        return mDB.query(Tables.table_name_actions, null, null, null, null, null, null);
    }

    public Cursor getActionByIdCursor(int action) {
        String selection = Const.action_id + " = ?";
        String[] selectionArgs = {String.valueOf(action)};
        return mDB.query(Tables.table_name_actions, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getActionByTypeCursor(int action_type_id) {
        String selection = Const.action_type_id + " = ?";
        String[] selectionArgs = {String.valueOf(action_type_id)};
        return mDB.query(Tables.table_name_actions, null, selection, selectionArgs, null, null, null);
    }

    public long addModel(Model data) {
        ContentValues cv = new ContentValues();
        cv.put(Const.model_id, String.valueOf(data.getModel_id()));
        cv.put(Const.model_name, data.getModel_name());
        cv.put(Const.model_points, data.getModel_points());
        cv.put(Const.model_brand_id, data.getModel_brand_id());
        cv.put(Const.model_group_id, data.getModel_group_id());
        cv.put(Const.model_url, data.getModel_url());
        cv.put(Const.model_brand_name, data.getModel_brand_name());
        cv.put(Const.model_group_name, data.getModel_group_name());
        cv.put(Const.model_sn_count, data.getModel_sn_count());
        cv.put(Const.model_action, data.getModel_action());
        return mDB.insert(Tables.table_name_models, null, cv);
    }

    public long addAction(Action data) {
        try {
            //Put models data for this action
            for(int i=0;i<data.getModels().size();i++) {
                if(addModel(data.getModels().get(i))==-1){
                    throw new Exception("FAIL on Add Model");
                };
            }
            //Put action data
            ContentValues cv = new ContentValues();
            cv.put(Const.action_id, data.getAction_id());
            cv.put(Const.action_name, data.getAction_name());
            cv.put(Const.action_type_id, data.getAction_type_id());
            cv.put(Const.action_type, data.getAction_type());
            cv.put(Const.action_date_from, data.getAction_date_from());
            cv.put(Const.action_date_to, data.getAction_date_to());
            cv.put(Const.action_date_charge, data.getAction_date_charge());
            cv.put(Const.action_description, data.getAction_description());
            return mDB.insert(Tables.table_name_actions, null, cv);
        } catch (Exception e){
            Log.v("", "SSS Exception addAction= " + e.toString());
            return -1;
        }

    }

    public boolean addActionArray(ArrayList<Action> news){
        this.open();
        this.getDB().beginTransaction();
        try {
            mDB.execSQL("delete from "+ Tables.table_name_models);
            mDB.execSQL("delete from "+ Tables.table_name_actions);
            for(int i=0;i<news.size();i++) {
                if(this.addAction(news.get(i))==-1){
                    throw new Exception("FAIL on Add Action");
                };
            }
            this.getDB().setTransactionSuccessful();
            transaction_success = true;
        } catch (Exception e){
            transaction_success = false;
            Log.v("", "SSS Exception = " + e.toString());
        } finally {
            this.getDB().endTransaction();
        }
        this.close();
        if(transaction_success){
            return true;
        } else {
            return false;
        }
    }

    public Model getModel(Cursor myCursor){
        Model item = new Model();
        item.setModel_id(myCursor.getInt(myCursor.getColumnIndex(Const.model_id)));
        item.setModel_name(myCursor.getString(myCursor.getColumnIndex(Const.model_name)));
        item.setModel_points(myCursor.getInt(myCursor.getColumnIndex(Const.model_points)));
        item.setModel_brand_id(myCursor.getInt(myCursor.getColumnIndex(Const.model_brand_id)));
        item.setModel_group_id(myCursor.getInt(myCursor.getColumnIndex(Const.model_group_id)));
        item.setModel_url(myCursor.getString(myCursor.getColumnIndex(Const.model_url)));
        item.setModel_brand_name(myCursor.getString(myCursor.getColumnIndex(Const.model_brand_name)));
        item.setModel_group_name(myCursor.getString(myCursor.getColumnIndex(Const.model_group_name)));
        item.setModel_sn_count(myCursor.getInt(myCursor.getColumnIndex(Const.model_sn_count)));
        item.setModel_action(myCursor.getInt(myCursor.getColumnIndex(Const.model_action)));
        return item;
    }

    public Model getModelByIds(int action, int model){
        Model item = new Model();
        this.open();
        Cursor myCursor = this.getModelByIdsCursor(action, model);
        if(myCursor.moveToFirst()) {
            item = getModel(myCursor);
        } else item =  null;
        this.close();
        return item;
    }

    public ArrayList<Model> getModelsArray(){
        ArrayList<Model> models = new ArrayList<Model>();
        Cursor myCursor = this.getModelsCursor();
        myCursor.moveToFirst();
        while (myCursor.isAfterLast() == false) {
            models.add(getModel(myCursor));
            myCursor.moveToNext();
        }
        Log.v("", "SSS getModelsArray = " + models.size());
        return models;
    }

    public ArrayList<Model> getModelsArray(int action){
        ArrayList<Model> models = new ArrayList<Model>();
        Cursor myCursor = this.getModelByActionIdCursor(action);
        myCursor.moveToFirst();
        while (myCursor.isAfterLast() == false) {
            models.add(getModel(myCursor));
            myCursor.moveToNext();
        }
        Log.v("", "SSS getModelsArray = " + models.size());
        return models;
    }

    public Action getAction(Cursor myCursor){
        Action item = new Action();
        item.setAction_id(myCursor.getInt(myCursor.getColumnIndex(Const.action_id)));
        item.setAction_name(myCursor.getString(myCursor.getColumnIndex(Const.action_name)));
        item.setAction_type_id(myCursor.getInt(myCursor.getColumnIndex(Const.action_type_id)));
        item.setAction_type(myCursor.getString(myCursor.getColumnIndex(Const.action_type)));
        item.setAction_date_from(myCursor.getString(myCursor.getColumnIndex(Const.action_date_from)));
        item.setAction_date_to(myCursor.getString(myCursor.getColumnIndex(Const.action_date_to)));
        item.setAction_date_charge(myCursor.getString(myCursor.getColumnIndex(Const.action_date_charge)));
        item.setAction_description(myCursor.getString(myCursor.getColumnIndex(Const.action_description)));
        item.setModels(getModelsArray(item.getAction_id()));
        return item;
    }

    public Action getActionById(int id){
        Action item = new Action();
        this.open();
        Cursor myCursor = this.getActionByIdCursor(id);
        if(myCursor.moveToFirst()) {
            item = getAction(myCursor);
        } else item =  null;
        this.close();
        return item;
    }

    public ArrayList<Action> getActionByTypeArray(int action_type_id){
        ArrayList<Action> actions = new ArrayList<Action>();
        this.open();
        Cursor myCursor = this.getActionByTypeCursor(action_type_id);
        myCursor.moveToFirst();
        //if(myCursor.moveToFirst()) {
            while (myCursor.isAfterLast() == false) {
                actions.add(getAction(myCursor));
                myCursor.moveToNext();
            }
       // } else actions = null;
        Log.v("", "SSS getActionByTypeArray = " + actions.size());
        this.close();
        return actions;
    }

    public ArrayList<Action> getActionArray(){
        ArrayList<Action> actions = new ArrayList<Action>();
        this.open();
        Cursor myCursor = this.getActionsCursor();
        myCursor.moveToFirst();
        //if(myCursor.moveToFirst()) {
            while (myCursor.isAfterLast() == false) {
                actions.add(getAction(myCursor));
                myCursor.moveToNext();
            }
        //} else actions = null;
        //Log.v("", "SSS getActionArray = " + actions.size());
        this.close();
        return actions;
    }

    //============================POST_SN===========================
    public long addPostSN(Post_SN data){
        try {
            //Put POST_SN data
            ContentValues cv = new ContentValues();
            cv.put(Const.action_id, data.getAction_id());
            cv.put(Const.action_name, data.getAction_name());
            cv.put(Const.model_id, data.getModel_id());
            cv.put(Const.model_name, data.getModel_name());
            cv.put(Const.action_date_to, data.getAction_date_to());
            cv.put(Const.action_type, data.getAction_type());
            cv.put(Const.model_points, data.getModel_points());
            //====SERIALS====
            cv.put(Const.serials, String.valueOf(data.getSerials().toArray(new String[data.getSerials().size()])));

            cv.put(Const.reg_date, data.getReg_date());
            cv.put(Const.reg_status, data.getReg_status());
            cv.put(Const.fail_reason, data.getFail_reason());
            return mDB.insert(Tables.table_name_postsn, null, cv);
        } catch (Exception e){
            Log.v("", "SSS Exception addPostSN= " + e.toString());
            return -1;
        }
    }


}
