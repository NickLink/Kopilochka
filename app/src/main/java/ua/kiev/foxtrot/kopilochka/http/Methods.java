package ua.kiev.foxtrot.kopilochka.http;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.Notice;
import ua.kiev.foxtrot.kopilochka.data.ProductGroup;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.utils.Encryption;
import ua.kiev.foxtrot.kopilochka.utils.Parser;

/**
 * Created by NickNb on 07.11.2016.
 */
public class Methods {
    public static void GetActionList(Context context, HttpRequest request){
        Requests actions_requests = new Requests(context, Const.getActions, request);
        HashMap<String, String> actions_params = new HashMap<String, String>();
        actions_params.put(Const.method, Const.GetActions);
        actions_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        actions_requests.getHTTP_Responce(actions_params);
    }

    public static boolean PutActionInBase(Context context, String result){
        boolean new_actions = false;
        ArrayList<Action> actions = new ArrayList<>();
        actions = Parser.getActionsArray(result);
        if(actions != null) {
            DB db = AppContr.db;
            //Go to check existing base
            for(int i = 0 ; i < actions.size() ; i++){
                //Action item_new = actions.get(i);
                Action item_old = db.getActionById(actions.get(i).getAction_id());
                if(item_old != null && actions.get(i).getAction_hash().equals(item_old.getAction_hash())){
                    actions.get(i).setViewed(item_old.getViewed());
                } else {
                    new_actions = true;
                }

            }
            if (db.addActionArray(actions)) {
                //Data to base added successfully
            } else {
                Log.v("Error", "SSS Methods PutActionsInDatabase error");
            }
        } else {
            Log.v("Error", "SSS Methods Parser.getActionsArray error");
        }
        return new_actions;
    }

    public static void GetNotificationList(Context context, HttpRequest request) {
        Requests notice_requests = new Requests(context, Const.getNotices, request);
        HashMap<String, String> notice_params = new HashMap<String, String>();
        notice_params.put(Const.method, Const.GetNotices);
        notice_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        notice_requests.getHTTP_Responce(notice_params);
    }

    public static boolean PutNotificationInBase(Context context, String result){
        boolean new_notification = false;
        ArrayList<Notice> notices = new ArrayList<>();
        notices = Parser.getNoticesArray(result);
        if(notices != null) {
            //Notifications ok
            DB db = AppContr.db;
            //Check for new Notifications


            if (db.addNoticeArray(notices)) {
                //Data to base added successfully
            } else {
                Log.v("Error", "SSS Methods PutNotificationInBase error");
            }
        } else {
            Log.v("Error", "SSS Methods Parser.getNoticesArray error");
        }
        return new_notification;
    }

    public static boolean PutGroupsInBase(Context context, String result){
        boolean new_groups = false;
        ArrayList<ProductGroup> arrayList = Parser.getProductGroupArray(result);
        if(arrayList != null) {
            //Actions ok
            DB db = AppContr.db;
            //Go to check existing base
            for(int i = 0 ; i < arrayList.size() ; i++){
                //Action item_new = actions.get(i);
                ProductGroup item_old = db.getGroupById(arrayList.get(i).getGroup_id());
                if(item_old != null && arrayList.get(i).getGroup_hash().equals(item_old.getGroup_hash())){
                    arrayList.get(i).setViewed(item_old.getViewed());
                } else {
                    new_groups = true;
                }
            }

            if (db.addGroupArray(arrayList)) {
                //Data to base added successfully
            } else {
                Log.v("Error", "SSS Methods PutGroupsInBase error");
            }
        } else {
            Log.v("Error", "SSS Methods Parser.PutGroupsInBase error");
        }
        return new_groups;
    }

}
