package ua.kiev.foxtrot.kopilochka.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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

    public static void ShowInputErrorDialog(Context context, String title, String message, String button){
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_error);

        TextView Title = (TextView)dialog.findViewById(R.id.title);
        TextView Message = (TextView)dialog.findViewById(R.id.message);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);

        Title.setText(title);
        Message.setText(message);
        cancelBtn.setText(button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ShowExitDialog(final Context context, String title, String message, String cancel, String exit){
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);

        TextView Title = (TextView)dialog.findViewById(R.id.title);
        TextView Message = (TextView)dialog.findViewById(R.id.message);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);
        Button exitBtn = (Button) dialog.findViewById(R.id.exit_button);

        Title.setText(title);
        Message.setText(message);
        cancelBtn.setText(cancel);
        exitBtn.setText(exit);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
            }
        });
        dialog.show();
    }

    public static void ShowJSONErrorDialog(Context context){
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_error);

        String title = context.getString(R.string.JSON_title_error);
        String message = context.getString(R.string.JSON_text_error);
        String button = context.getString(R.string.JSON_button_error);

        TextView Title = (TextView)dialog.findViewById(R.id.title);
        TextView Message = (TextView)dialog.findViewById(R.id.message);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);

        Title.setText(title);
        Message.setText(message);
        cancelBtn.setText(button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ShowResponceDialog(Context context, String message){
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_not_a_member);

        String title = context.getString(R.string.incorrect_login_password);
        //String message = context.getString(R.string.JSON_text_error);
        String button = context.getString(R.string.JSON_button_error);

        TextView Title = (TextView)dialog.findViewById(R.id.title);
        TextView Message = (TextView)dialog.findViewById(R.id.message);
        Message.setVisibility(View.GONE);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);

        Title.setText(title);
        Message.setText(message);
        cancelBtn.setText(button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void Error_Dispencer(Context context, int metod, int error_code){
        switch (metod){
            case Const.getSession:
                if(error_code == 1){
                    //incorrect login/password
                    ShowResponceDialog(context, context.getString(R.string.incorrect_login_password));
                } else if (error_code == 2){
                    //not active
                    ShowResponceDialog(context, context.getString(R.string.not_active));
                } else {
                    ShowResponceDialog(context, context.getString(R.string.unknown_error));
                }
                break;
            case Const.getNotices:


                break;
            case Const.getActions:


                break;
            case Const.PostSN:



                break;
            case Const.GetFinInfo:


                break;
            case Const.PostQuestion:



                break;
        }

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
            millis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    public static boolean isDateInRange(String start_date, String end_date){
        long current_time = System.currentTimeMillis();
        long action_start_time = getMillisFromDate(start_date);
        long action_end_time = getMillisFromDate(end_date);
        if(current_time >= action_start_time && current_time <= action_end_time){
            return true;
        } else {
            return false;
        }
    }

}
