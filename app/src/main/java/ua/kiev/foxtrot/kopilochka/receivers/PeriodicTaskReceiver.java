package ua.kiev.foxtrot.kopilochka.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.MainActivity;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.data.Notice;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.utils.Encryption;
import ua.kiev.foxtrot.kopilochka.utils.Parser;

/**
 * Created by NickNb on 29.09.2016.
 */
public class PeriodicTaskReceiver extends BroadcastReceiver implements HttpRequest{

    private static final String TAG = "PeriodicTaskReceiver";
    private static final String INTENT_ACTION = "com.example.app.PERIODIC_TASK_HEART_BEAT";
    ArrayList<BBS_News> news;
    ArrayList<Notice> notices;
    ArrayList<Action> actions;
    Context context;
    DB db;
    private static int notif_id;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (!TextUtils.isEmpty(intent.getAction())) {
            AppContr appContr = (AppContr) context.getApplicationContext();
            SharedPreferences sharedPreferences = appContr.getSharPref();

            if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {
                sharedPreferences.edit().putBoolean(Const.BACKGROUND_SERVICE_BATTERY_CONTROL, false).apply();
                stopPeriodicTaskHeartBeat(context);
            } else if (intent.getAction().equals("android.intent.action.BATTERY_OKAY")) {
                sharedPreferences.edit().putBoolean(Const.BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
                restartPeriodicTaskHeartBeat(context, appContr);
            } else if (intent.getAction().equals(INTENT_ACTION)) {
                doPeriodicTask(context, appContr);
            }
        }
    }

    public void restartPeriodicTaskHeartBeat(Context context, AppContr appContr) {
        SharedPreferences sharedPreferences = appContr.getSharPref();
        boolean isBatteryOk = sharedPreferences.getBoolean(Const.BACKGROUND_SERVICE_BATTERY_CONTROL, true);
        Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
        boolean isAlarmUp = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null;

        if (isBatteryOk && !isAlarmUp) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent.setAction(INTENT_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 20000,
                    AlarmManager.INTERVAL_HOUR, pendingIntent);
        }
    }

    public void stopPeriodicTaskHeartBeat(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, PeriodicTaskReceiver.class);
        alarmIntent.setAction(INTENT_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private void doPeriodicTask(Context context, AppContr appContr) {
        // Periodic task(s) go here ...
        if(AppContr.getSharPref().getString(Const.SAVED_SES, null) != null) {
            Log.v("TAG", "SSS doPeriodicTask");

            //BBS-------------------------------------------------
//            Requests BBC_requests = new Requests(9, this);
//            BBC_requests.getNewsData();

            //NOTICES----------------------------------------------
            Requests notice_requests = new Requests(context, Const.getNotices, this);
            HashMap<String, String> notice_params = new HashMap<String, String>();
            notice_params.put(Const.method, Const.GetNotices);
            notice_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                    .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
            notice_requests.getHTTP_Responce(notice_params);

            //ACTIONS-----------------------------------------------
            Requests actions_requests = new Requests(context, Const.getActions, this);
            HashMap<String, String> actions_params = new HashMap<String, String>();
            actions_params.put(Const.method, Const.GetActions);
            actions_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                    .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
            actions_requests.getHTTP_Responce(actions_params);





        } else {
            //do nothing
        }

    }

    @Override
    public void http_result(int type, String result) {
        switch (type){
            case Const.getNotices:
                notices = Parser.getNoticesArray(result);
                if(notices != null){
                    //Notices ok
                    CreateNotification("getNotices", "Data ok", "");
                    PutNoticesInDatabase(notices);
                } else {
                    //Some error happened
                    CreateNotification("Error", "getNotices Data structure corrupted", "");
                }

                break;
            case Const.getActions:
                actions = Parser.getActionsArray(result);
                if(actions != null){
                    //Actions ok
                    CreateNotification("getActions", "Data ok", "");
                    PutActionsInDatabase(actions);
                } else {
                    //Some error happened
                    CreateNotification("Error", "getActions Data structure corrupted", "");
                }



                break;
            case 9:

                news = Parser.getNewsArray(result);
                if(news!=null){
                    CreateNotification("News", "Data ok", "");
                    PutNewsInDatabase(news);
                } else {
                    CreateNotification("Error", "News Data structure corrupted", "");
                }

                break;
        }


    }

    @Override
    public void http_error(int type, String error) {
        CreateNotification("Error", "Download data error", error);
    }


    private void PutNewsInDatabase(ArrayList<BBS_News> news) {
        db = new DB(context);
        //db.open();
        if(db.addNewsArray(news)){
            //Data to base added successfully
        } else {
            CreateNotification("Error", "Database Transaction FAIL", "");
        }
        //db.close();
    }

    private void PutNoticesInDatabase(ArrayList<Notice> notices) {
        db = new DB(context);
        if(db.addNoticeArray(notices)){
            //Data to base added successfully
        } else {
            CreateNotification("Error", "Database notices Transaction FAIL", "");
        }
    }

    private void PutActionsInDatabase(ArrayList<Action> actions) {
        db = new DB(context);
        if(db.addActionArray(actions)){
            //Data to base added successfully
        } else {
            CreateNotification("Error", "Database actions Transaction FAIL", "");
        }
    }


//      For Lolipop Notification http://stackoverflow.com/questions/32267626/how-to-display-proper-small-icon-for-notification-is-not-getting-displayed-in-lo
//    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//        Notification notification = new Notification.Builder(context)
//        ...
//    } else {
//        // Lollipop specific setColor method goes here.
//        Notification notification = new Notification.Builder(context)
//        ...
//        notification.setColor(your_color)
//        ...
//    }
//    if (entry.targetSdk >= Build.VERSION_CODES.LOLLIPOP) {
//        entry.icon.setColorFilter(mContext.getResources().getColor(android.R.color.white));
//    } else {
//        entry.icon.setColorFilter(null);
//    }
//    .setColor(your_color_resource_here)


    private void CreateNotification(String title, String text, String url) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Строим уведомление
        //Sound
        Uri ringURI =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Vibrate
        long[] vibrate = new long[] { 1000, 1000, 1000 };

        Notification builder = new Notification.Builder(context)
                .setTicker("New news")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.icon_remind).setContentIntent(pIntent)
                .addAction(R.drawable.icon_open, "Open", pIntent)
                .addAction(R.drawable.icon_later, "Later", pIntent)
                .setSound(ringURI)
                //.setVibrate(vibrate)
                .build();
        // убираем уведомление, когда его выбрали
        builder.flags |= Notification.FLAG_AUTO_CANCEL;
        notif_id ++;
        notificationManager.notify(notif_id, builder);
    }

}