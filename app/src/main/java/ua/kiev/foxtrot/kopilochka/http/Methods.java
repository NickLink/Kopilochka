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

    public static void PutActionInBase(Context context, String result){
        ArrayList<Action> actions = new ArrayList<>();
        actions = Parser.getActionsArray(result);
        if(actions != null) {
            DB db = new DB(context);
            if (db.addActionArray(actions)) {
                //Data to base added successfully
            } else {
                Log.v("Error", "SSS Methods PutActionsInDatabase error");
            }
        } else {
            Log.v("Error", "SSS Methods Parser.getActionsArray error");
        }

    }

    public static void GetNotificationList(Context context, HttpRequest request) {
        Requests notice_requests = new Requests(context, Const.getNotices, request);
        HashMap<String, String> notice_params = new HashMap<String, String>();
        notice_params.put(Const.method, Const.GetNotices);
        notice_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        notice_requests.getHTTP_Responce(notice_params);
    }

    public static void PutNotificationInBase(Context context, String result){
        ArrayList<Notice> notices = new ArrayList<>();
        notices = Parser.getNoticesArray(result);
        if(notices != null) {
            //Actions ok
            DB db = new DB(context);
            if (db.addNoticeArray(notices)) {
                //Data to base added successfully
            } else {
                Log.v("Error", "SSS Methods PutNotificationInBase error");
            }
        } else {
            Log.v("Error", "SSS Methods Parser.getNoticesArray error");
        }
    }

    public static void PutGroupsInBase(Context context, String result){
        ArrayList<ProductGroup> arrayList = Parser.getProductGroupArray(result);
        if(arrayList != null) {
            //Actions ok
            DB db = new DB(context);
            db.open();
            if (db.addGroupArray(arrayList)) {
                //Data to base added successfully
            } else {
                Log.v("Error", "SSS Methods PutGroupsInBase error");
            }
            db.close();
        } else {
            Log.v("Error", "SSS Methods Parser.PutGroupsInBase error");
        }
    }

}
