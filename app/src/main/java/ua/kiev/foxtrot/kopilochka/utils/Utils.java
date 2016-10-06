package ua.kiev.foxtrot.kopilochka.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.kiev.foxtrot.kopilochka.R;

/**
 * Created by NickNb on 04.10.2016.
 */
public class Utils {

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
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);

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
}
