package ua.f5.kopilochka.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.data.Action;
import ua.f5.kopilochka.data.Model;
import ua.f5.kopilochka.data.Notice;
import ua.f5.kopilochka.data.Post_SN;
import ua.f5.kopilochka.data.ProductGroup;
import ua.f5.kopilochka.utils.StringTools;
import ua.f5.kopilochka.utils.Utils;

/**
 * Created by NickNb on 30.09.2016.
 */
public class DB {
    private static String TAG = "DB";

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;

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
        mDB.execSQL("delete from "+ Tables.table_name_notices);
        mDB.execSQL("delete from "+ Tables.table_name_models);
        mDB.execSQL("delete from "+ Tables.table_name_actions);
        mDB.execSQL("delete from "+ Tables.table_name_postsn);
        mDB.execSQL("delete from "+ Tables.table_name_groups);
    }

    public SQLiteDatabase getDB (){
        return mDB;
    }



    //===========================NOTICES===================================
    public Cursor getNoticeData() {
        return mDB.query(Tables.table_name_notices, null, null, null, null, null, null);
    }

    public Cursor getNoticeByIdCursor(int id) {
        String selection = Const.notice_id + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return mDB.query(Tables.table_name_notices, null, selection, selectionArgs, null, null, null);
    }

    public long addNotice(Notice data) {
        ContentValues cv = new ContentValues();
        cv.put(Const.notice_id, data.getNotice_id());
        cv.put(Const.notice_name, data.getNotice_name());
        cv.put(Const.notice_text, data.getNotice_text());
        cv.put(Const.notice_type_id, data.getNotice_type_id());
        cv.put(Const.notice_type, data.getNotice_type());
        cv.put(Const.viewed, data.getViewed());
        cv.put(Const.notice_hash, data.getNotice_hash());
        return mDB.insert(Tables.table_name_notices, null, cv);
    }

    public boolean addNoticeArray(ArrayList<Notice> news){
        //this.open();
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
        //this.close();
        if(transaction_success){
            return true;
        } else {
            return false;
        }
    }

    public Notice getNotice(Cursor cursor){
        Notice item = new Notice();
        item.setNotice_id(cursor.getInt(cursor.getColumnIndex(Const.notice_id)));
        item.setNotice_name(cursor.getString(cursor.getColumnIndex(Const.notice_name)));
        item.setNotice_text(cursor.getString(cursor.getColumnIndex(Const.notice_text)));
        item.setNotice_type_id(cursor.getInt(cursor.getColumnIndex(Const.notice_type_id)));
        item.setNotice_type(cursor.getString(cursor.getColumnIndex(Const.notice_type)));
        item.setViewed(cursor.getInt(cursor.getColumnIndex(Const.viewed)));
        item.setNotice_hash(cursor.getString(cursor.getColumnIndex(Const.notice_hash)));
        return item;
    }

    public Notice getNoticeById(int id){
        Cursor cursor = this.getNoticeByIdCursor(id);
        if (cursor.moveToFirst()){
            Notice item = this.getNotice(cursor);
            return item;
        } else
            return null;
    }

    public ArrayList<Notice> getNoticeArray(){
        ArrayList<Notice> notif_data = new ArrayList<Notice>();
        //this.open();
        Cursor myCursor = this.getNoticeData();
        myCursor.moveToFirst();
        while (myCursor.isAfterLast() == false) {
            Notice item = getNotice(myCursor);
            notif_data.add(item);
            myCursor.moveToNext();
        }
        //Log.v("", "SSS Finish = " + notif_data.size());
        //this.close();
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
        //this.open();
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
            //Log.v("TAG", "SSSS groupName =" + groupName + " modelsCount =" + modelsCount + " group_id =" + group_id);
            myCursor.moveToNext();
        }
        //this.close();
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

    public Cursor getModelByGroupIdCursor(int group) {
        String table = Tables.table_name_models + " as model inner join " + Tables.table_name_actions
                + " as action on model.model_action = action.action_id";
        String selection = Const.model_group_id + " = ? and action.action_type_id = 1";
        String[] selectionArgs = {String.valueOf(group)};
        return mDB.query(table, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getModelByIdsCursor(int action, int model) {
        String selection = Const.model_action + " = ?"  +" AND " + Const.model_id + " = ?";
        String[] selectionArgs = {String.valueOf(action), String.valueOf(model)};
        return mDB.query(Tables.table_name_models, null, selection, selectionArgs, null, null, null);
    }

    public Cursor getActionsCursor() {
        return mDB.query(Tables.table_name_actions, null, null, null, null, null, null);
    }

    public Cursor getActionsCursor(int action_type) {
        String selection = Const.action_type_id + " = ?";
        String[] selectionArgs = {String.valueOf(action_type)};
        return mDB.query(Tables.table_name_actions, null, selection, selectionArgs, null, null, null);
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
            //Log.v("", "SSS addAction action_date_from= " + data.getAction_date_from());
            cv.put(Const.action_date_to, data.getAction_date_to());
            cv.put(Const.action_date_charge, data.getAction_date_charge());
            cv.put(Const.action_description, data.getAction_description());
            cv.put(Const.viewed, data.getViewed());
            cv.put(Const.action_hash, data.getAction_hash());
            return mDB.insert(Tables.table_name_actions, null, cv);
        } catch (Exception e){
            //Log.v("", "SSS Exception addAction= " + e.toString());
            return -1;
        }

    }

    public boolean addActionArray(ArrayList<Action> news){
        //this.open();
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
            //Log.v("", "SSS Exception = " + e.toString());
        } finally {
            this.getDB().endTransaction();
        }
        //this.close();
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
        //this.open();
        Cursor myCursor = this.getModelByIdsCursor(action, model);
        if(myCursor.moveToFirst()) {
            item = getModel(myCursor);
        } else item =  null;
        //this.close();
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
        //Log.v("", "SSS getModelsArray = " + models.size());
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
        //Log.v("", "SSS getModelsArray = " + models.size());
        return models;
    }

    public int getModelsInGroupCount(int group){
        Cursor cursor = this.getModelByGroupIdCursor(group);
        return cursor.getCount();
    }

    public Action getAction(Cursor myCursor){
        Action item = new Action();
        item.setAction_id(myCursor.getInt(myCursor.getColumnIndex(Const.action_id)));
        item.setAction_name(myCursor.getString(myCursor.getColumnIndex(Const.action_name)));
        item.setAction_type_id(myCursor.getInt(myCursor.getColumnIndex(Const.action_type_id)));
        item.setAction_type(myCursor.getString(myCursor.getColumnIndex(Const.action_type)));
        item.setAction_date_from((long) myCursor.getInt(myCursor.getColumnIndex(Const.action_date_from)));
        //Log.v("", "SSS getAction action_date_from = " + (long) myCursor.getInt(myCursor.getColumnIndex(Const.action_date_from)));
        item.setAction_date_to((long) myCursor.getInt(myCursor.getColumnIndex(Const.action_date_to)));
        item.setAction_date_charge((long) myCursor.getInt(myCursor.getColumnIndex(Const.action_date_charge)));
        item.setAction_description(myCursor.getString(myCursor.getColumnIndex(Const.action_description)));
        item.setAction_hash(myCursor.getString(myCursor.getColumnIndex(Const.action_hash)));
        item.setViewed(myCursor.getInt(myCursor.getColumnIndex(Const.viewed)));
        item.setModels(getModelsArray(item.getAction_id()));
        return item;
    }

    public Action getActionById(int id){
        Action item = new Action();
        //this.open();
        Cursor myCursor = this.getActionByIdCursor(id);
        if(myCursor.moveToFirst()) {
            item = getAction(myCursor);
        } else item =  null;
        //this.close();
        return item;
    }

    public ArrayList<Action> getActionByTypeArray(int action_type_id){
        ArrayList<Action> actions = new ArrayList<Action>();
        //this.open();
        Cursor myCursor = this.getActionByTypeCursor(action_type_id);
        myCursor.moveToFirst();
        //if(myCursor.moveToFirst()) {
            while (myCursor.isAfterLast() == false) {
                Action action = getAction(myCursor);
                if(Utils.daysLeft(action.getAction_date_to()) > 0){
                    actions.add(action);
                }
                myCursor.moveToNext();
            }
       // } else actions = null;
        //Log.v("", "SSS getActionByTypeArray = " + actions.size());
        //this.close();
        return actions;
    }

    public ArrayList<Action> getActionArray(){
        ArrayList<Action> actions = new ArrayList<Action>();
        //this.open();
        Cursor myCursor = this.getActionsCursor();
        myCursor.moveToFirst();
        //if(myCursor.moveToFirst()) {
        while (myCursor.isAfterLast() == false) {
            Action action = getAction(myCursor);
            if(Utils.daysLeft(action.getAction_date_to()) > 0){
                actions.add(action);
            }
            myCursor.moveToNext();
        }
        //} else actions = null;
        ////Log.v("", "SSS getActionArray = " + actions.size());
        //this.close();
        return actions;
    }

    public ArrayList<Action> getActionArray(int action_type){
        ArrayList<Action> actions = new ArrayList<Action>();
        //this.open();
        Cursor myCursor = this.getActionsCursor(action_type);
        myCursor.moveToFirst();
        //if(myCursor.moveToFirst()) {
        while (myCursor.isAfterLast() == false) {
            actions.add(getAction(myCursor));
            myCursor.moveToNext();
        }
        //} else actions = null;
        ////Log.v("", "SSS getActionArray = " + actions.size());
        //this.close();
        return actions;
    }

    //============================POST_SN===========================
    public Cursor getPostSNbyStatus(int status) {
        String selection = Const.reg_status + " = ?";
        String[] selectionArgs = {String.valueOf(status)};
        return mDB.query(Tables.table_name_postsn, null, selection, selectionArgs, null, null, Const.model_name);
    }

    private Post_SN getPost_SN(Cursor myCursor){
        Post_SN item = new Post_SN();
        item.setAction_id(myCursor.getInt(myCursor.getColumnIndex(Const.action_id)));
        item.setAction_name(myCursor.getString(myCursor.getColumnIndex(Const.action_name)));
        item.setModel_id(myCursor.getInt(myCursor.getColumnIndex(Const.model_id)));
        item.setModel_name(myCursor.getString(myCursor.getColumnIndex(Const.model_name)));
        item.setAction_date_to(myCursor.getLong(myCursor.getColumnIndex(Const.action_date_to)));
        item.setAction_type_id(myCursor.getInt(myCursor.getColumnIndex(Const.action_type_id)));
        item.setModel_points(myCursor.getInt(myCursor.getColumnIndex(Const.model_points)));
        item.setSerials(StringTools.ListFromString(myCursor.getString(myCursor.getColumnIndex(Const.serials))));
        item.setReg_date(myCursor.getLong(myCursor.getColumnIndex(Const.reg_date)));
        item.setReg_status(myCursor.getInt(myCursor.getColumnIndex(Const.reg_status)));
        item.setFail_reason(myCursor.getString(myCursor.getColumnIndex(Const.fail_reason)));
        return item;
    }


    public long addPostSN(Post_SN data){
        long code = -1;
        //this.open();
        this.getDB().beginTransaction();
        try {
            //Put POST_SN data
            ContentValues cv = new ContentValues();
            cv.put(Const.action_id, data.getAction_id());
            cv.put(Const.action_name, data.getAction_name());
            cv.put(Const.model_id, data.getModel_id());
            cv.put(Const.model_name, data.getModel_name());
            cv.put(Const.action_date_to, data.getAction_date_to());
            cv.put(Const.action_type_id, data.getAction_type_id());
            cv.put(Const.model_points, data.getModel_points());
            //====SERIALS====
            cv.put(Const.serials, StringTools.StringFromList(data.getSerials()));

            cv.put(Const.reg_date, data.getReg_date());
            cv.put(Const.reg_status, data.getReg_status());
            cv.put(Const.fail_reason, data.getFail_reason());

            for (String s: cv.keySet()
                 ) {
                Log.v("", "2121 CV key = " + s + " value = " + cv.get(s));
            }

            code = mDB.insert(Tables.table_name_postsn, null, cv);
            if (code != -1) {
                this.getDB().setTransactionSuccessful();
                Log.v("", "2121 addPostSN setTransactionSuccessful code = " + code);
            }
        } catch (Exception e){
            Log.v("", "2121 addPostSN Exception = " + e.toString());
            code = -1;
        }finally {
            this.getDB().endTransaction();
        }
        //this.close();
        return code;
    }

    public boolean deletePostSN(Post_SN data){
        boolean is_ok = false;
        //this.open();
        this.getDB().beginTransaction();
        try {
            String selection = Const.action_id + " = ? AND " + Const.model_id + " = ? AND " + Const.serials + " = ?";
            String[] selectionArgs = {
                    String.valueOf(data.getAction_id()),
                    String.valueOf(data.getModel_id()),
                    StringTools.StringFromList(data.getSerials())};
            if(mDB.delete(Tables.table_name_postsn, selection, selectionArgs) > 0){
                //All ok
                this.getDB().setTransactionSuccessful();
                is_ok = true;
            } else {
                //No record found
            }
        } catch (Exception e){
            //Log.v("", "SSS Exception deletePostSN= " + e.toString());
        }finally {
            this.getDB().endTransaction();
        }
        //this.close();
        return is_ok;
    }


    public ArrayList<Post_SN> getPost_SN_List(int status){
        //this.open();
        ArrayList<Post_SN> arrayList = new ArrayList<>();
        Cursor myCursor = this.getPostSNbyStatus(status);
        myCursor.moveToFirst();
        while (myCursor.isAfterLast() == false) {
            arrayList.add(getPost_SN(myCursor));
            myCursor.moveToNext();
        }
        //this.close();
        return arrayList;
    }

    private Cursor getPostSNbyDataCursor(int action_id, int model_id, String serials){
        String selection = Const.action_id + " = ? AND " + Const.model_id + " = ? AND " + Const.serials + " = ?";
        String[] selectionArgs = {
                String.valueOf(action_id),
                String.valueOf(model_id),
                serials};
        //Log.v("", "SSS getPostSNbyData selection= " + selection);
        //Log.v("", "SSS getPostSNbyData selectionArgs= " + Arrays.toString(selectionArgs));
        return mDB.query(Tables.table_name_postsn, null, selection, selectionArgs, null, null, null);
    }

    public Post_SN getPostSNbyData(int action_id, int model_id, String serials){
        //this.open();
        Cursor cursor = this.getPostSNbyDataCursor(action_id, model_id, serials);
        if(cursor != null && cursor.moveToFirst()){
            Post_SN item = getPost_SN(cursor);
            return item;
        }
        //this.close();
        return null;
    }

    public boolean setStatus_Post_SN_item(Post_SN data){
        //this.open();
        //Cursor cursor = this.getPostSNbyData(data);
        //if(cursor.moveToFirst()){
        String selection = Const.action_id + " = ? AND " + Const.model_id + " = ? AND " + Const.serials + " = ?";
        Log.v(TAG, "2121 ->> selection = " + selection);
        String[] selectionArgs = {
                String.valueOf(data.getAction_id()),
                String.valueOf(data.getModel_id()),
                StringTools.StringFromList(data.getSerials()).replace("\"", "")};
        for (String s: selectionArgs
             ) {
            Log.v(TAG, "2121 ->> selectionArgs = " + s);
        }
        ContentValues cv = new ContentValues();
        cv.put(Const.reg_status, data.getReg_status());
        cv.put(Const.fail_reason, data.getFail_reason());
        Log.v(TAG, "2121 ->> fail_reason = " + data.getFail_reason());
        this.getDB().beginTransaction();
        if(mDB.update(Tables.table_name_postsn, cv, selection, selectionArgs) > 0){
            //All ok
            Log.v(TAG, "2121 ->> All ok");
            this.getDB().setTransactionSuccessful();
            this.getDB().endTransaction();
            //this.close();
            return true;
        } else {
            //No record found
            Log.v(TAG, "2121 ->> No record found ");
            this.getDB().endTransaction();
            //this.close();
            return false;
        }
    }

    public boolean setSerials_Post_SN_item(Post_SN data, String old_serials){
        //this.open();
        //Cursor cursor = this.getPostSNbyData(data);
        //if(cursor.moveToFirst()){
        String selection = Const.action_id + " = ? AND " + Const.model_id + " = ? AND " + Const.serials + " = ?";
        String[] selectionArgs = {
                String.valueOf(data.getAction_id()),
                String.valueOf(data.getModel_id()),
                old_serials};
        ContentValues cv = new ContentValues();
        cv.put(Const.serials, StringTools.StringFromList(data.getSerials()));
        cv.put(Const.reg_status, String.valueOf(Const.reg_status_await));
        cv.put(Const.fail_reason, "");
        this.getDB().beginTransaction();
        if(mDB.update(Tables.table_name_postsn, cv, selection, selectionArgs) > 0){
            //All ok
            this.getDB().setTransactionSuccessful();
            this.getDB().endTransaction();
            //this.close();
            return true;
        } else {
            //No record found
            this.getDB().endTransaction();
            //this.close();
            return false;
        }
    }

    //====================================GROUPS=============================
    public Cursor getGroupsDataCursor() {
        return mDB.query(Tables.table_name_groups, null, null, null, null, null, null);
    }

    public Cursor getGroupByIdCursor(int id) {
        String selection = Const.group_id + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return mDB.query(Tables.table_name_groups, null, selection, selectionArgs, null, null, null);
    }

    public long addGroupItem(ProductGroup data){
        long code = -1;
        this.getDB().beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(Const.group_id, data.getGroup_id());
            cv.put(Const.group_name, data.getGroup_name());
            cv.put(Const.group_hash, data.getGroup_hash());
            cv.put(Const.viewed, data.getViewed());
            code = mDB.insert(Tables.table_name_groups, null, cv);
            if (code != -1) this.getDB().setTransactionSuccessful();
        } catch (Exception e){
            //Log.v("", "SSS Exception addGroupItem= " + e.toString());
            code = -1;
        }finally {
            this.getDB().endTransaction();
        }
        return code;
    }

    public boolean addGroupArray(ArrayList<ProductGroup> arrayList){
        mDB.execSQL("delete from "+ Tables.table_name_groups);
        try {
            for(int i=0;i<arrayList.size();i++) {
                if(this.addGroupItem(arrayList.get(i))==-1){
                    throw new Exception("FAIL on addGroupArray");
                };
            }
        } catch (Exception e){
            //Log.v("", "SSS addGroupArray Exception = " + e.toString());
            return false;
        }
        return true;
    }

    public ProductGroup getGroupItem(Cursor cursor){
        ProductGroup item = new ProductGroup();
        item.setGroup_id(cursor.getInt(cursor.getColumnIndex(Const.group_id)));
        item.setGroup_name(cursor.getString(cursor.getColumnIndex(Const.group_name)));
        item.setGroup_hash(cursor.getString(cursor.getColumnIndex(Const.group_hash)));
        item.setViewed(cursor.getInt(cursor.getColumnIndex(Const.viewed)));
        item.setModels_count(getModelsInGroupCount(item.getGroup_id()));
        return item;
    }

    public ArrayList<ProductGroup> getGroupArray(){
        ArrayList<ProductGroup> arrayList = new ArrayList<ProductGroup>();
        Cursor cursor = this.getGroupsDataCursor();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrayList.add(getGroupItem(cursor));
            cursor.moveToNext();
        }
        //Log.v("TAG", "SSS getGroupArray OK");
        return arrayList;
    }

    public ProductGroup getGroupById(int id){
        Cursor cursor = this.getGroupByIdCursor(id);
        if(cursor != null && cursor.moveToFirst()){
            ProductGroup item = getGroupItem(cursor);
            return item;
        }
        return null;
    }

    //==========================SET VIEWED STATUS=============================
    public boolean setActionViewed(int id){
        String selection = Const.action_id + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        ContentValues cv = new ContentValues();
        cv.put(Const.viewed, Const.viewed_yes);
        if(mDB.update(Tables.table_name_actions, cv, selection, selectionArgs) > 0){
            //Log.v("TAG", "SSS setActionViewed OK");
            return true;
        } else {
            //Log.v("TAG", "SSS setActionViewed NOT OK");
        }
        return false;
    }

    public boolean setGroupViewed(int id){
        String selection = Const.group_id + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        ContentValues cv = new ContentValues();
        cv.put(Const.viewed, Const.viewed_yes);
        if(mDB.update(Tables.table_name_groups, cv, selection, selectionArgs) > 0){
            return true;
        }
        return false;
    }

    public boolean setNotificationViewed(int id){
        String selection = Const.notice_id + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        ContentValues cv = new ContentValues();
        cv.put(Const.viewed, Const.viewed_yes);
        if(mDB.update(Tables.table_name_notices, cv, selection, selectionArgs) > 0){
            return true;
        }
        return false;
    }

}
