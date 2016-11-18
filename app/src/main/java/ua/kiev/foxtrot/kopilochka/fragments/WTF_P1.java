package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;
import ua.kiev.foxtrot.kopilochka.ui.FontCache;
import ua.kiev.foxtrot.kopilochka.utils.Dialogs;
import ua.kiev.foxtrot.kopilochka.utils.Encryption;
import ua.kiev.foxtrot.kopilochka.utils.Parser;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class WTF_P1 extends BaseFragment implements HttpRequest {
    private long mLastClickTime = 0;
    Interfaces interfaces;
    OnBackPress onBackPress;
    EditText wtf_name_edit, wtf_email_edit, wtf_text_edit;

    public static WTF_P1 newInstance() {
        return new WTF_P1();
    }

    public WTF_P1() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interfaces = (Interfaces) activity;
            onBackPress = (OnBackPress) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Interfaces");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_wtf_p1, container,
                false);
        Typeface calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());

        TextView wtf_name = (TextView) rootView.findViewById(R.id.wtf_name);
        TextView wtf_email = (TextView) rootView.findViewById(R.id.wtf_email);
        TextView wtf_text = (TextView) rootView.findViewById(R.id.wtf_text);
        wtf_name_edit = (EditText) rootView.findViewById(R.id.wtf_name_edit);
        wtf_email_edit = (EditText) rootView.findViewById(R.id.wtf_email_edit);
        wtf_text_edit = (EditText) rootView.findViewById(R.id.wtf_text_edit);
        Button send_button = (Button) rootView.findViewById(R.id.send_button);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Utils.isQuestionCorrect(
                        wtf_name_edit.getText().toString(),
                        wtf_email_edit.getText().toString(),
                        wtf_text_edit.getText().toString())) {
                    sendQuestion();
                } else {
                    Dialogs.ShowDialog(getActivity(),
                            getString(R.string.wtf_fields_title),
                            getString(R.string.wtf_fields_not_complete),
                            getString(R.string.wtf_ok));
                }
            }
        });

        wtf_name.setTypeface(calibri_bold);
        wtf_email.setTypeface(calibri_bold);
        wtf_text.setTypeface(calibri_bold);
        wtf_name_edit.setTypeface(calibri_bold);
        wtf_email_edit.setTypeface(calibri_bold);
        wtf_text_edit.setTypeface(calibri_bold);
        send_button.setTypeface(calibri_bold);

        ImageButton menu_item_icon = (ImageButton) rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView) rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_wtf));
        menu_item_title.setTypeface(calibri_bold);
        return rootView;
    }

    private void sendQuestion() {
        //sendQuestion-----------------------------------------------
        Requests question_requests = new Requests(getActivity(), Const.postQuestion, this);
        HashMap<String, String> question_params = new HashMap<>();
        question_params.put(Const.method, Const.PostQuestion);
        question_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        question_params.put(Const.fio, wtf_name_edit.getText().toString());
        question_params.put(Const.email, wtf_email_edit.getText().toString());
        question_params.put(Const.question, wtf_text_edit.getText().toString());
        question_requests.getHTTP_Responce(question_params);
    }

    @Override
    public void http_result(int type, String result) {

        switch (Parser.parseQuestionResponce(result)) {
            case -1:
                //Parser or unknown error
                Dialogs.ShowDialog(getActivity(),
                        getString(R.string.warning_title),
                        getString(R.string.unknown_error),
                        getString(R.string.wtf_ok));
                break;
            case 0:
                //All ok - message has been sent
                Dialogs.ShowDialog(getActivity(),
                        getString(R.string.warning_title),
                        getString(R.string.wtf_sent_success),
                        getString(R.string.wtf_ok));
                wtf_name_edit.setText("");
                wtf_email_edit.setText("");
                wtf_text_edit.setText("");
                break;
            case 1:
                //Session error - need relogin
                Dialogs.ShowDialog(getActivity(),
                        getString(R.string.warning_title),
                        getString(R.string.warning_session_expired),
                        getString(R.string.wtf_ok));

                break;
            case 2:
                //User is not active
                Dialogs.ShowDialog(getActivity(),
                        getString(R.string.warning_title),
                        getString(R.string.warning_user_not_active),
                        getString(R.string.wtf_ok));
                break;

        }

    }

    @Override
    public void http_error(int type, String error) {
        Dialogs.ShowDialog(getActivity(),
                getString(R.string.warning_title),
                getString(R.string.internet_error),
                getString(R.string.wtf_ok));
    }
}
