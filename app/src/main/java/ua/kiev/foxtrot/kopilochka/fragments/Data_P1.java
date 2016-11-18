package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.http.Methods;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.ui.FontCache;
import ua.kiev.foxtrot.kopilochka.utils.Dialogs;
import ua.kiev.foxtrot.kopilochka.utils.Parser;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Data_P1 extends BaseFragment implements HttpRequest {
    private long mLastClickTime = 0;
    Interfaces interfaces;
    private LinearLayout login_layout, loged_layout;
    private boolean logged;

    private EditText data_email_edit, data_password_edit;
    private Button data_login_button;
    private String login = "", password = "";
    ProgressDialog load_data;
    private Typeface calibri, calibri_bold;

    public static Data_P1 newInstance() {
        Data_P1 fragment = new Data_P1();
        return fragment;
    }

    public Data_P1() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interfaces = (Interfaces) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Interfaces");
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.frag_data_p1, null);
        // This just inflates the view but doesn't add it to any thing.
        // You need to add it to the root view of the fragment
        ViewGroup rootView = (ViewGroup) getView();
        // Remove all the existing views from the root view.
        // This is also a good place to recycle any resources you won't need anymore
        rootView.removeAllViews();
        rootView.addView(newView);

//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View newView = inflater.inflate(R.layout.frag_data_p1, null);
//            // This just inflates the view but doesn't add it to any thing.
//            // You need to add it to the root view of the fragment
//            ViewGroup rootView = (ViewGroup) getView();
//            // Remove all the existing views from the root view.
//            // This is also a good place to recycle any resources you won't need anymore
//            rootView.removeAllViews();
//            rootView.addView(newView);
//        } else {
//            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View newView = inflater.inflate(R.layout.frag_data_p1, null);
//            // This just inflates the view but doesn't add it to any thing.
//            // You need to add it to the root view of the fragment
//            ViewGroup rootView = (ViewGroup) getView();
//            // Remove all the existing views from the root view.
//            // This is also a good place to recycle any resources you won't need anymore
//            rootView.removeAllViews();
//            rootView.addView(newView);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_data_p1, container,
                false);

        calibri = FontCache.get("fonts/calibri.ttf", getActivity());
        calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());

        TextView log_01_work_with = (TextView) rootView.findViewById(R.id.log_01_work_with);
        TextView log_01_to_manager = (TextView) rootView.findViewById(R.id.log_01_to_manager);
        TextView log_01_login_text = (TextView) rootView.findViewById(R.id.log_01_login_text);
        TextView log_01_password_text = (TextView) rootView.findViewById(R.id.log_01_password_text);

        data_email_edit = (EditText) rootView.findViewById(R.id.data_email_edit);
        data_password_edit = (EditText) rootView.findViewById(R.id.data_password_edit);
        data_login_button = (Button) rootView.findViewById(R.id.data_login_button);

        data_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Utils.hideKeyboard(getActivity(), view);
                login = data_email_edit.getText().toString();
                password = data_password_edit.getText().toString();
                if (!login.trim().isEmpty() && //Utils.email_Correct(data_email_edit.getText().toString())
                        !password.trim().isEmpty()) { //Utils.password_Correct(data_password_edit.getText().toString())
                    getToken(login, password);
                } else {
                    //Data not complete
                    Dialogs.ShowDialog(getActivity(), getString(R.string.input_error),
                            getString(R.string.fill_all_fields),
                            getString(R.string.input_ok));

                }
            }
        });

        log_01_work_with.setTypeface(calibri_bold);
        log_01_to_manager.setTypeface(calibri);
        log_01_login_text.setTypeface(calibri_bold);
        log_01_password_text.setTypeface(calibri_bold);

        data_email_edit.setTypeface(calibri_bold);
        data_password_edit.setTypeface(calibri_bold);
        data_login_button.setTypeface(calibri_bold);

//        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
//        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
//        menu_item_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                interfaces.OpenClose();
//            }
//        });
//        menu_item_title.setText(getString(R.string.menu_data));
        return rootView;
    }

    private void getToken(String login, String password) {
        load_data = new ProgressDialog(getActivity());
        load_data.setTitle(getString(R.string.data_loading));
        load_data.show();

        Requests requests = new Requests(getActivity(), Const.getSession, Data_P1.this);
        HashMap<String, String> params = new HashMap<>();
        params.put(Const.login, login);
        params.put(Const.password, password);
        params.put(Const.method, Const.GetSession);
        requests.getHTTP_Responce(params);
    }

    @Override
    public void http_result(int type, String result) {
        switch (type) {

            case Const.getSession:
                //Parse data
                AppContr.userData = Parser.getUserData(result);
                if (AppContr.userData != null) {
                    switch (AppContr.userData.getCode()) {
                        case Const.JSON_Ok:
                            //All ok
                            AppContr.userData.setLogin(login);
                            AppContr.userData.setPassword(password);
                            interfaces.SaveUser();
                            Load_All_data();

                            break;
                        case 1:
                            //No user found
                            Clear_Input_Fields();
                            Dialogs.ShowLoginDialog(getActivity(), 1);
                            break;
                        case 2:
                            //Token time expired
                            Clear_Input_Fields();
                            Dialogs.ShowLoginDialog(getActivity(), 2);
                            break;
                    }

                } else {
                    Clear_Input_Fields();
                    Dialogs.ShowJSONErrorDialog(getActivity());
                    return;
                }
                break;

            case Const.getActions:
                Methods.PutGroupsInBase(getActivity(), result);
                Methods.PutActionInBase(getActivity(), result);
                Methods.GetNotificationList(getActivity(), this);
                break;

            case Const.getNotices:
                Methods.PutNotificationInBase(getActivity(), result);
                if (load_data != null)
                    load_data.dismiss();
                interfaces.LoginSuccess();
                break;
        }

    }

    @Override
    public void http_error(int type, String error) {
        if (load_data != null)
            load_data.dismiss();
        switch (type) {
            case Const.getSession:

                break;

        }

    }

    private void Load_All_data() {
        //Set preferences
        Utils.setNewGroup();
        Utils.setNewNotice();
        Utils.setNewAction();

        Methods.GetActionList(getActivity(), this);
    }

    private void Clear_Input_Fields() {
        data_email_edit.setText("");
        data_password_edit.setText("");
        data_email_edit.requestFocus();
    }

}
