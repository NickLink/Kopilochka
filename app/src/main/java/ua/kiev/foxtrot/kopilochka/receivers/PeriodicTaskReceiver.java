package ua.kiev.foxtrot.kopilochka.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.MyLifecycleHandler;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.Notice;
import ua.kiev.foxtrot.kopilochka.data.Post_SN;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.http.Connect;
import ua.kiev.foxtrot.kopilochka.http.Methods;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;

/**
 * Created by NickNb on 29.09.2016.
 */
public class PeriodicTaskReceiver extends BroadcastReceiver implements HttpRequest{

    private static final String TAG = "PeriodicTaskReceiver";
    private static final String INTENT_ACTION = "ua.kiev.foxtrot.kopilochka.app.PERIODIC_TASK_HEART_BEAT";
    List<Post_SN> arrayList;
//    ArrayList<BBS_News> news;
//    ArrayList<Notice> notices;
//    ArrayList<Action> actions;
    Context context;
    DB db = AppContr.db;
    private static int notif_id;
    private Post_SN register_item;

    private static int succes_count = 0, error_count = 0;


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
        if(AppContr.getSharPref().getString(Const.SAVED_SES, null) != null
                && Connect.isOnline(context)
                && !MyLifecycleHandler.isApplicationInForeground()) {
            Log.v("TAG", "SSS doPeriodicTask");
            //Start from notices & continue after it
            //NOTICES----------------------------------------------
            Methods.GetNotificationList(context, this);

        } else {
            //do nothing
        }

    }

    @Override
    public void http_result(int type, String result) {
        switch (type){
            case Const.getNotices:
                if(Methods.PutNotificationInBase(context, result)){
                    //Create notification for new Action
                    CreateNotification("Повідомлення", "Отримано нові повідомлення", "");
                }
                //ACTIONS-----------------------------------------------
                Methods.GetActionList(context, this);
                break;

            case Const.getActions:
                if(Methods.PutActionInBase(context, result)){
                    //Create notification for new Action
                    CreateNotification("Повідомлення", "Отримано нові акції", "");
                }

                if(Methods.PutGroupsInBase(context, result)){
                    //Create notofication for new Group
                    CreateNotification("Повідомлення", "Отримано нові групи товарів", "");
                }
                //POST_SN------------------------------------------------
                arrayList = db.getPost_SN_List(Const.reg_status_await);
                doSerialsRegister();
                break;
            case Const.postSN:
                if(Methods.RegisterReceive(context, result, register_item)){
                    succes_count++;
                } else {
                    error_count++;
                }
                arrayList.remove(arrayList.size()-1);
                if(arrayList !=null && arrayList.size() > 0) {
                    doSerialsRegister();
                } else {
                    if(arrayList !=null && arrayList.size() == 0 && (succes_count > 0 || error_count > 0)){
                        CreateNotification(context.getString(R.string.notification_ticker),
                                context.getString(R.string.notification_good) + String.valueOf(succes_count)
                                + System.getProperty("line.separator") + context.getString(R.string.notification_bad)
                                + String.valueOf(error_count) , "");
                        succes_count = 0;
                        error_count = 0;
                    }
                }
                break;
        }
    }

    void doSerialsRegister(){
        if(arrayList !=null && arrayList.size() > 0) {
            register_item = arrayList.get(arrayList.size()-1);
            Methods.post_SN(context, register_item, this);
            Log.v("TAG", "DDD Send " + "new " + " item");
        }
    }

    @Override
    public void http_error(int type, String error) {
        //CreateNotification("Повідомлення", "Помилка інтернет з\'єднання", error);
    }

    private void PutNoticesInDatabase(ArrayList<Notice> notices) {
        if(db.addNoticeArray(notices)){
            //Data to base added successfully
        } else {
            //CreateNotification("Повідомлення", "Помилка запису данних у базу.", "");
        }
    }

    private void PutActionsInDatabase(ArrayList<Action> actions) {
        if(db.addActionArray(actions)){
            //Data to base added successfully
        } else {
            //CreateNotification("Повідомлення", "Помилка запису данних у базу.", "");
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

        //Intent intent = new Intent(context, MainActivity.class);
        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage("ua.kiev.foxtrot.kopilochka");
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
        //PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Строим уведомление
        //Sound
        Uri ringURI =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Vibrate
        long[] vibrate = new long[] { 1000, 1000, 1000 };

        Notification builder = new Notification.Builder(context)
                .setTicker(context.getString(R.string.notification_ticker))
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pIntent)
                .setStyle(new Notification.BigTextStyle().bigText(text))
                //.addAction(R.drawable.pencil, "Open", pIntent)
                //.addAction(R.drawable.del, "Later", pIntent)
                .setSound(ringURI)
                //.setVibrate(vibrate)
                .build();
        // убираем уведомление, когда его выбрали
        builder.flags |= Notification.FLAG_AUTO_CANCEL;
        notif_id ++;
        notificationManager.notify(notif_id, builder);
    }

}