package ua.kiev.foxtrot.kopilochka.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;

/**
 * Created by NickNb on 04.10.2016.
 */
public class Utils {
    private static String TAG = "Utils";

    public static void Save_User(Context context, Encryption encrypt){
        //Log.v(TAG, "SSSS IN userData = " + AppContr.userData.getLogin() + " out = " + encrypt.encryptOrNull(AppContr.userData.getLogin()));
        AppContr.getSharPref().edit()
                .putString(Const.SAVED_LOG, encrypt.encryptOrNull(AppContr.userData.getLogin()))
                .putString(Const.SAVED_PAS, encrypt.encryptOrNull(AppContr.userData.getPassword()))
                .putString(Const.SAVED_SES, encrypt.encryptOrNull(AppContr.userData.getSession_id()))
                .putString(Const.SAVED_NAME, encrypt.encryptOrNull(AppContr.userData.getUser_name()))
                .putString(Const.SAVED_EMAIL, encrypt.encryptOrNull(AppContr.userData.getUser_email()))
                .putString(Const.SAVED_PHONE, encrypt.encryptOrNull(AppContr.userData.getUser_phone()))
                .apply();
    }

    public static void Restore_User(Context context, Encryption encrypt){
        AppContr.userData.setSession_id(encrypt.decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        AppContr.userData.setLogin(encrypt.decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_LOG, null)));
        AppContr.userData.setPassword(encrypt.decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_PAS, null)));
        AppContr.userData.setUser_name(encrypt.decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_NAME, null)));
        AppContr.userData.setUser_email(encrypt.decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_EMAIL, null)));
        AppContr.userData.setUser_phone(encrypt.decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_PHONE, null)));
    }

    public static void Clear_User(){
        AppContr.getSharPref().edit()
                .remove(Const.SAVED_LOG)
                .remove(Const.SAVED_PAS)
                .remove(Const.SAVED_SES)
                .remove(Const.SAVED_NAME)
                .remove(Const.SAVED_EMAIL)
                .remove(Const.SAVED_PHONE)
                .apply();
    }

    public static boolean Correct_User(){
        if(notNull_orEmpty(AppContr.userData.getLogin())
                && notNull_orEmpty(AppContr.userData.getPassword())
                && notNull_orEmpty(AppContr.userData.getSession_id())
                && notNull_orEmpty(AppContr.userData.getUser_name())){
            return true;
        } else return false;
    }

    public static String getSession_Id(Encryption encrypt){
        return encrypt.decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null));
    }

    public static boolean email_Correct(String email_string) {
        boolean isValid = false;
        if (email_string == null || email_string == "null") {
            return false;
        }
        try{
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = email_string;

            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                isValid = true;
            }

        } catch (Exception e){
            isValid = false;
        }
        return isValid;
    }

    public static boolean password_Correct(String password_string) {
        if (password_string !=null && password_string.trim().length()>5)
            return true;
        else
            return false;
    }

    public static String CheckError_1_2(Context context, String result){
        try {
            JSONObject data = new JSONObject(result);
            if(data.has(Const.JSON_Error)
                    && data.getString(Const.methodresponse).equals(Const.GetSession)) {
                return result;
            } else if(data.has(Const.JSON_Error)){
                if(data.getJSONObject(Const.JSON_Error).getInt(Const.code) == 1) {
                    Dialogs.Dialog_For_Restart(context,
                            context.getString(R.string.warning_title),
                            context.getString(R.string.warning_session_expired),
                            context.getString(R.string.warning_ok));
                } else if (data.getJSONObject(Const.JSON_Error).getInt(Const.code) == 2){
                    Dialogs.Dialog_For_Restart(context,
                            context.getString(R.string.warning_title),
                            context.getString(R.string.warning_user_not_active),
                            context.getString(R.string.warning_ok));
                }
            }
        } catch (JSONException e){
            return null;
        }
        return result;
    }

    public static boolean notNull_orEmpty(String in){
        if(in != null && !in.trim().isEmpty() && !in.equals("null"))
            return true;
        else
            return false;
    }

    public static long getMillisFromDate(String date){
        long millis = 0;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = f.parse(date);
            millis = d.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    public static String getDateFromMillis(long millis){
        String date_string = "";
        try{
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (new Date(millis * 1000));
            date_string = f.format(date);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return date_string;
    }

//    public static boolean isDateInRange(String start_date, String end_date){
//        long current_time = System.currentTimeMillis();
//        long action_start_time = getMillisFromDate(start_date);
//        long action_end_time = getMillisFromDate(end_date);
//        if(current_time >= action_start_time && current_time <= action_end_time){
//            return true;
//        } else {
//            return false;
//        }
//    }

    public static boolean isDateInRange(long start_date, long end_date){
        long current_time = System.currentTimeMillis() / 1000;
        if(current_time >= start_date && current_time <= end_date){
            return true;
        } else {
            return false;
        }
    }

//    public static int daysLeft(String end_date){
//        int days = 0;
//        long action_end_time = getMillisFromDate(end_date);
//        days = (int)((action_end_time - (System.currentTimeMillis()/1000)) / (24 * 60 * 60));
//        return days < 0 ? -days: days;
//    }

    public static int daysLeft(long end_date){
        int days = 0;
        days = (int)((end_date - (System.currentTimeMillis()/1000)) / (24 * 60 * 60));
        return days < 0 ? -days: days;
    }

    public static boolean isQuestionCorrect(String name, String email, String question){
        if(notNull_orEmpty(name) && email_Correct(email) && notNull_orEmpty(question)) {
            return true;
        } else {
            return false;
        }
    }

    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }

}
