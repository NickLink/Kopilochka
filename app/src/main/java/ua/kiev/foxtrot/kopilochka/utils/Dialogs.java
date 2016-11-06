package ua.kiev.foxtrot.kopilochka.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.data.Post_SN;
import ua.kiev.foxtrot.kopilochka.database.DB;

/**
 * Created by NickNb on 02.11.2016.
 */
public class Dialogs {

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

    public static void Dialog_For_Restart(final Context context, String title, String message, String button){
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
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
                Utils.Clear_User();
                Utils.doRestart(context);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ShowDialog(Context context, String title, String message, String button){
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
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

    public static void ShowDialogDeleteEditItem(final Context context, final Interfaces interfaces,
                                            String title, String message, final Post_SN item, boolean edit){
        final int action_id = item.getAction_id();
        final int model_id = item.getModel_id();
        final String serials = StringTools.StringFromList(item.getSerials());

        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_delete_item);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView Title = (TextView)dialog.findViewById(R.id.dialog_title);
        TextView Message = (TextView)dialog.findViewById(R.id.dialog_text);
        Button deleteButoon = (Button) dialog.findViewById(R.id.action_delete);
        Button editButton = (Button) dialog.findViewById(R.id.action_edit);
        Button cancel_Button = (Button) dialog.findViewById(R.id.action_cancel);

        Title.setText(title);
        Message.setText(message);
        deleteButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB db = new DB(context);
                db.deletePostSN(item);
                interfaces.DeleteExistPostSN();
                dialog.dismiss();
            }
        });

        if(edit)
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.EditExistPostSN(action_id, model_id, serials);
                dialog.dismiss();
            }
        }); else editButton.setVisibility(View.GONE);

        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
