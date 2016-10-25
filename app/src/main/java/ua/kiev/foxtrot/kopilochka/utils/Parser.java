package ua.kiev.foxtrot.kopilochka.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.data.Charge;
import ua.kiev.foxtrot.kopilochka.data.FinInfo;
import ua.kiev.foxtrot.kopilochka.data.Model;
import ua.kiev.foxtrot.kopilochka.data.Notice;
import ua.kiev.foxtrot.kopilochka.data.Payment;
import ua.kiev.foxtrot.kopilochka.data.UserData;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Parser {
    private static String TAG = "Parser";

    public static ArrayList<BBS_News> getNewsArray(String response){
        ArrayList<BBS_News> arrayList = new ArrayList<BBS_News>();
        try{
            JSONObject data = new JSONObject(response);
            JSONArray articles = data.getJSONArray("articles");
            for (int i = 0;i<articles.length();i++){
                BBS_News item = get_News(articles.getJSONObject(i));
                arrayList.add(item);
            }
            return arrayList;
        }
        catch (Exception e){
            return null;
        }

    }

    public static BBS_News get_News(JSONObject jsonObject){
        BBS_News item = new BBS_News();
        item.setAuthor(jsonObject.optString("author"));
        item.setTitle(jsonObject.optString("title"));
        item.setDescription(jsonObject.optString("description"));
        item.setUrl(jsonObject.optString("url"));
        item.setUrlToImage(jsonObject.optString("urlToImage"));
        item.setPublishedAt(jsonObject.optString("publishedAt"));
        return item;
    }

    //================CHECK FOR VALID SESSION==============
    public static boolean IsSessionOk(String result){
        try {
            JSONObject data = new JSONObject(result);
            if(data.has(Const.JSON_Error)){
                switch (data.getJSONObject(Const.JSON_Error).getInt(Const.JSON_Code)){
                    case 0:
                        return false;

                    case 1:
                        return false;

                }

            }
        } catch (JSONException e){
            return false;
        }
        return true;
    }

    public static UserData getUserData(String result){
        UserData item = new UserData();
        try {
            JSONObject data = new JSONObject(result);
            if(data.has(Const.JSON_Error)){
                item.setCode(data.getJSONObject(Const.JSON_Error).getInt(Const.JSON_Code));
                item.setComment(data.getJSONObject(Const.JSON_Error).getString(Const.JSON_Comment));
                Log.v(TAG, "SSS JSON_Error=" + item.getCode());
            } else {
                item.setSession_id(data.getString(Const.session));
                item.setActive(data.getInt(Const.active));
                item.setUser_name(data.getString(Const.user_name));
                item.setUser_email(data.getString(Const.user_email));
                item.setUser_phone(data.getString(Const.user_phone));
                item.setCode(Const.JSON_Ok);
                Log.v(TAG, "SSS JSON_Ok");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v(TAG, "SSS JSONException=" + e);
            return null;
        }
        return item;
    }
    //=========================NOTICES===========================
    public static Notice get_Notice(JSONObject jsonObject){
        Notice item = new Notice();
        try {
            item.setNotice_id(jsonObject.getInt(Const.notice_id));
            item.setNotice_name(jsonObject.getString(Const.notice_name));
            item.setNotice_text(jsonObject.getString(Const.notice_text));
            item.setNotice_type_id(jsonObject.getInt(Const.notice_type_id));
            item.setNotice_type(jsonObject.getString(Const.notice_type));
        } catch (JSONException e) {
            Log.v("", "SSS get_Notice JSONException=" + e.toString());
            return null;
        }
        return item;
    }

    public static ArrayList<Notice> getNoticesArray(String response){
        ArrayList<Notice> arrayList = new ArrayList<Notice>();
        try{
            JSONObject data = new JSONObject(response);
            JSONArray array = data.getJSONArray(Const.notices);
            for (int i = 0;i<array.length();i++){
                Notice item = get_Notice(array.getJSONObject(i));
                arrayList.add(item);
            }
            return arrayList;
        }
        catch (Exception e){
            Log.v("", "SSS getNoticesArray Exception=" + e.toString());
            return null;
        }
    }
    //================================MODELS=====================================
    public static Model getModel(JSONObject jsonObject, int model_action){
        Model item = new Model();
        try {
            item.setModel_id(jsonObject.getInt(Const.model_id));
            item.setModel_name(jsonObject.getString(Const.model_name));
            item.setModel_points(jsonObject.getInt(Const.model_points));
            item.setModel_brand_id(jsonObject.getInt(Const.model_brand_id));
            item.setModel_group_id(jsonObject.getInt(Const.model_group_id));
            item.setModel_url(jsonObject.getString(Const.model_url));
            item.setModel_brand_name(jsonObject.getString(Const.model_brand_name));
            item.setModel_group_name(jsonObject.getString(Const.model_group_name));
            item.setModel_sn_count(jsonObject.getInt(Const.model_sn_count));
            item.setModel_action(model_action);
        } catch (JSONException e) {
            Log.v("", "SSS getModel JSONException=" + e.toString());
            return null;
        }
        return item;
    }

    //===============================ACTION=======================================
    public static Action getAction(JSONObject jsonObject){
        Action item = new Action();
        try {
            item.setAction_id(jsonObject.getInt(Const.action_id));
            item.setAction_name(jsonObject.getString(Const.action_name));
            item.setAction_type_id(jsonObject.getInt(Const.action_type_id));
            item.setAction_type(jsonObject.getString(Const.action_type));
            item.setAction_date_from(jsonObject.getString(Const.action_date_from));
            item.setAction_date_to(jsonObject.getString(Const.action_date_to));
            item.setAction_date_charge(jsonObject.getString(Const.action_date_charge));
            item.setAction_description(jsonObject.getString(Const.action_description));
            //Go for models
            JSONArray models_array = jsonObject.getJSONArray(Const.models);
            for( int i = 0 ; i < models_array.length() ; i++){
                item.getModels().add(getModel(models_array.getJSONObject(i), item.getAction_id()));
            }
        } catch (JSONException e) {
            Log.v("", "SSS getAction JSONException=" + e.toString());
            return null;
        }
        return item;
    }

    //===============================ACTIONS ARRAY==================================
    public static ArrayList<Action> getActionsArray(String response){
        ArrayList<Action> arrayList = new ArrayList<Action>();
        try{
            JSONObject data = new JSONObject(response);
            JSONArray array = data.getJSONArray(Const.actions);
            for (int i = 0;i<array.length();i++){
                Action item = getAction(array.getJSONObject(i));
                arrayList.add(item);
            }
            return arrayList;
        }
        catch (Exception e){
            Log.v("", "SSS getActionsArray Exception=" + e.toString());
            return null;
        }
    }

    //=========================FININFO DATA===========================================
    public static Charge getCharge(JSONObject jsonObject){
        Charge item = new Charge();
        try {
            item.setAction_charge(jsonObject.getString(Const.action_charge));
            item.setDate_charge(jsonObject.getString(Const.date_charge));
            item.setAmount_charges(jsonObject.getInt(Const.amount_charge));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return item;
    }

    public static Payment getPayment(JSONObject jsonObject){
        Payment item = new Payment();
        try {
            item.setAction_payment(jsonObject.getString(Const.action_payment));
            item.setDate_payment(jsonObject.getString(Const.date_payment));
            item.setAmount_payment(jsonObject.getInt(Const.amount_payment));
            item.setComment_payment(jsonObject.getString(Const.comment_payment));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return item;
    }


    public static FinInfo getFinInfo(String response){
        FinInfo finInfo = new FinInfo();
        try {






        } catch (Exception e){
            Log.v("", "SSS getActionsArray Exception=" + e.toString());
            return null;
        }
        return finInfo;
    }

    public static int parseQuestionResponce(String response){
        try{
            JSONObject data = new JSONObject(response);
            if (data.has(Const.JSON_Error)) {
                switch (data.getInt(Const.JSON_Code)){
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                }
            } else {
                return 0;
            }
        }
        catch (Exception e){
            return -1;
        }
        return 1;
    }



}
