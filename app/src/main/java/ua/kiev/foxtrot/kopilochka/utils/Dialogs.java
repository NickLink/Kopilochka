package ua.kiev.foxtrot.kopilochka.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Post_SN;
import ua.kiev.foxtrot.kopilochka.database.DB;

/**
 * Created by NickNb on 02.11.2016.
 */
public class Dialogs {

    public static void ShowInternetDialog(Context context, String title) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_not_a_member);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Typeface calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
        TextView Title = (TextView) dialog.findViewById(R.id.title);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);
        cancelBtn.setTypeface(AppContr.calibri_bold);
        Title.setText(title);
        Title.setTypeface(AppContr.calibri_bold);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ShowCallSyncDialog(final Context context, final Interfaces interfaces) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);
        Button exitBtn = (Button) dialog.findViewById(R.id.exit_button);

        Title.setText(context.getString(R.string.call_sync_title));
        Message.setText(context.getString(R.string.call_sync_text));
        cancelBtn.setText(context.getString(R.string.menu_exit_no));
        exitBtn.setText(context.getString(R.string.menu_exit_yes));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.DoSync();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void ShowLoginDialog(Context context, int code) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_not_a_member);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String title = "", message = "";
        switch (code) {
            case 1:
                title = context.getString(R.string.log_01_sorry);
                message = context.getString(R.string.log_01_sorry_expl);
                break;

            case 2:
                title = context.getString(R.string.log_01_not_active);
                message = context.getString(R.string.log_01_not_active_expl);
                break;
        }
        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);

        Title.setText(title);
        Message.setText(message);
        //cancelBtn.setText(button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ShowLogoutDialog(Context context, final Interfaces interfaces) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String title = context.getString(R.string.data_exit_title);
        String message = context.getString(R.string.data_exit_text);

        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
        Button exit_button = (Button) dialog.findViewById(R.id.exit_button);
        Button cancel_button = (Button) dialog.findViewById(R.id.cancel_button);

        Title.setText(title);
        Message.setText(message);
        //cancelBtn.setText(button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.LogOut();
                dialog.dismiss();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ShowJSONErrorDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_error);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String title = context.getString(R.string.JSON_title_error);
        String message = context.getString(R.string.JSON_text_error);
        String button = context.getString(R.string.JSON_button_error);

        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
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

    public static void ShowExitDialog(final Context context, String title, String message, String cancel, String exit) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
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

    public static void ShowRegDialog(final Context context, boolean success) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success_reg);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView icon = (ImageView) dialog.findViewById(R.id.reg_icon);
        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);

        if (success) {
            icon.setImageResource(R.drawable.book_opened);
            Title.setText(context.getString(R.string.act_reg_success_title));
            Message.setText(context.getString(R.string.act_reg_success_text));
        } else {
            icon.setImageResource(R.drawable.bug);
            Title.setText(context.getString(R.string.act_reg_failed_title));
            Message.setText(context.getString(R.string.act_reg_failed_text));
        }

        //Typeface calibri = FontCache.get("fonts/calibri.ttf", context);
        //Typeface calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
        Title.setTypeface(AppContr.calibri_bold);
        Message.setTypeface(AppContr.calibri);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void ShowSyncDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success_reg);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView icon = (ImageView) dialog.findViewById(R.id.reg_icon);
        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);

        icon.setImageResource(R.drawable.book_opened);
        Title.setText(context.getString(R.string.call_sync_title));
        Message.setText(context.getString(R.string.call_sync_text2));

        //Typeface calibri = FontCache.get("fonts/calibri.ttf", context);
        //Typeface calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
        Title.setTypeface(AppContr.calibri_bold);
        Message.setTypeface(AppContr.calibri);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void Dialog_For_Restart(final Context context, String title, String message, String button) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_input_error);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
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

    public static void ShowDialog(Context context, String title, String message, String button) {
        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_input_error);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView Title = (TextView) dialog.findViewById(R.id.title);
        TextView Message = (TextView) dialog.findViewById(R.id.message);
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
                                                String title, String message, final Post_SN item, boolean edit) {
        final int action_id = item.getAction_id();
        final int model_id = item.getModel_id();
        final String serials = StringTools.StringFromList(item.getSerials());

        final Dialog dialog = new Dialog(context, R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_delete_item);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //TextView Title = (TextView)dialog.findViewById(R.id.dialog_title);
        //TextView Message = (TextView)dialog.findViewById(R.id.dialog_text);

        TextView action_name = (TextView) dialog.findViewById(R.id.action_name);
        TextView model_name = (TextView) dialog.findViewById(R.id.model_name);
        TextView serial_numbers_list = (TextView) dialog.findViewById(R.id.serial_numbers_list);
        TextView error_title = (TextView) dialog.findViewById(R.id.error_title);
        TextView error_text = (TextView) dialog.findViewById(R.id.error_text);
        Button deleteButoon = (Button) dialog.findViewById(R.id.action_delete);
        Button editButton = (Button) dialog.findViewById(R.id.action_edit);
        Button cancel_Button = (Button) dialog.findViewById(R.id.action_cancel);

        //Title.setText(title);
        //Message.setText(message);
        action_name.setText(item.getAction_name());
        model_name.setText(item.getModel_name());
        serial_numbers_list.setText(StringTools.StringFromList(item.getSerials()));

        deleteButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB db = AppContr.db;
                db.deletePostSN(item);
                interfaces.DeleteExistPostSN();
                dialog.dismiss();
            }
        });


        if (edit) {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interfaces.EditExistPostSN(action_id, model_id, serials);
                    dialog.dismiss();
                }
            });
        } else {
            editButton.setVisibility(View.GONE);
        }

        if (item.getReg_status() == Const.reg_status_error) {
            error_title.setVisibility(View.VISIBLE);
            error_text.setVisibility(View.VISIBLE);
            error_text.setText(item.getFail_reason());
        }
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Typeface calibri = FontCache.get("fonts/calibri.ttf", context);
        //Typeface calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);

        action_name.setTypeface(AppContr.calibri_bold);
        model_name.setTypeface(AppContr.calibri_bold);
        serial_numbers_list.setTypeface(AppContr.calibri_bold);
        dialog.show();
    }

}
