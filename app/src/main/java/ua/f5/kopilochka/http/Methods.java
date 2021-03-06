package ua.f5.kopilochka.http;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Action;
import ua.f5.kopilochka.data.Notice;
import ua.f5.kopilochka.data.Post_SN;
import ua.f5.kopilochka.data.ProductGroup;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.utils.Encryption;
import ua.f5.kopilochka.utils.Utils;

/**
 * Created by NickNb on 07.11.2016.
 */
public class Methods {

    public static void GetActionList(Context context, HttpRequest request) {
        Requests actions_requests = new Requests(context, Const.getActions, request);
        HashMap<String, Object> actions_params = new HashMap<>();
        actions_params.put(Const.method, Const.GetActions);
        actions_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        actions_requests.getHTTP_Responce(actions_params);
    }

    public static boolean PutActionInBase(Context context, ArrayList<Action> actions) {
        boolean new_actions = false;

        DB db = AppContr.db;
        //Go to check existing base
        for (int i = 0; i < actions.size(); i++) {
            //Action item_new = actions.get(i);
            Action item_old = db.getActionById(actions.get(i).getAction_id());
            if (item_old != null && actions.get(i).getAction_hash().equals(item_old.getAction_hash())) {
                actions.get(i).setViewed(item_old.getViewed());
            } else {
                new_actions = true;
                Utils.setNewAction();
            }

        }
        if (db.addActionArray(actions)) {
            //Data to base added successfully
        } else {
            //Log.v("Error", "SSS Methods PutActionsInDatabase error");
        }

        return new_actions;
    }

    public static void GetNotificationList(Context context, HttpRequest request) {
        Requests notice_requests = new Requests(context, Const.getNotices, request);
        HashMap<String, Object> notice_params = new HashMap<>();
        notice_params.put(Const.method, Const.GetNotices);
        notice_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        notice_requests.getHTTP_Responce(notice_params);
    }

    public static boolean PutNotificationInBase(Context context, ArrayList<Notice> notices) {
        boolean new_notification = false;

            //Notifications ok
            DB db = AppContr.db;
            //Check for new Notifications
            for (int i = 0; i < notices.size(); i++) {
                //Action item_new = actions.get(i);
                Notice item_old = db.getNoticeById(notices.get(i).getNotice_id());
                if (item_old != null && notices.get(i).getNotice_hash().equals(item_old.getNotice_hash())) {
                    notices.get(i).setViewed(item_old.getViewed());
                } else {
                    new_notification = true;
                    Utils.setNewNotice();
                }
            }
            if (db.addNoticeArray(notices)) {
                //Data to base added successfully
            } else {
                //Log.v("Error", "SSS Methods PutNotificationInBase error");
            }
        return new_notification;
    }

    public static boolean PutGroupsInBase(Context context, ArrayList<ProductGroup> arrayList) {
        boolean new_groups = false;
        //Actions ok
        DB db = AppContr.db;
        //Go to check existing base
        for (int i = 0; i < arrayList.size(); i++) {
            //Action item_new = actions.get(i);
            ProductGroup item_old = db.getGroupById(arrayList.get(i).getGroup_id());
            if (item_old != null && arrayList.get(i).getGroup_hash().equals(item_old.getGroup_hash())) {
                arrayList.get(i).setViewed(item_old.getViewed());
            } else {
                new_groups = true;
                Utils.setNewGroup();
            }
        }

        if (db.addGroupArray(arrayList)) {
            //Data to base added successfully
        } else {
            //Log.v("Error", "SSS Methods PutGroupsInBase error");
        }
        return new_groups;
    }

    public static void post_SN(Context context, Post_SN item, HttpRequest request) {
        Requests requests = new Requests(context, Const.postSN, request);
        HashMap<String, Object> post_params = new HashMap<>();
        post_params.put(Const.method, Const.PostSN);
        post_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        post_params.put(Const.action_id, String.valueOf(item.getAction_id()));
        post_params.put(Const.model_id, String.valueOf(item.getModel_id()));
        post_params.put(Const.date, Utils.getDateFromMillis(item.getReg_date()));
        post_params.put(Const.serials, item.getSerials()); //StringTools.StringFromList(item.getSerials())
        ////Log.v("", "2121 serials = " + item.getSerials().toString());
        for (Map.Entry<String, Object> entry : post_params.entrySet()) {
            //Log.v("", "2121 " + entry.getKey() + " = " + entry.getValue());
        }
        requests.getHTTP_Responce(post_params);
    }

    public static boolean RegisterReceive(Context context, String result, Post_SN item) {
        try {
            //Log.v("", "SSS http_result = " + result);
            JSONObject data = new JSONObject(result);
            DB db = AppContr.db;
            if (data.has(Const.JSON_Error)) {
                item.setReg_status(Const.reg_status_error);
                item.setFail_reason(data.getJSONObject(Const.JSON_Error).getString(Const.comment));
                db.setStatus_Post_SN_item(item);
                return false;
            } else {
                if (data.has(Const.ok) && data.getInt(Const.ok) == 1) {
//                    Post_SN received = new Post_SN();
//                    received.setAction_id(data.getInt(Const.action_id));
//                    received.setModel_id(data.getInt(Const.model_id));
                    item.setReg_date(Utils.getMillisFromDate(data.getString(Const.date)));
                    item.setReg_status(Const.reg_status_ok);
//                    received.setSerials(StringTools.ListFromString(data.getString(Const.serials)));
                    if (db.setStatus_Post_SN_item(item)) {
                        //model successfully registered
                        return true;
                    } else {
                        //something wrong with DB
                        return false;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void RegisterInBackground(Context context, HttpRequest request) {

    }

}
