package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;

/**
 * Created by NickNb on 13.10.2016.
 */
public class Data_P1_Logged extends Fragment implements HttpRequest {
    Interfaces interfaces;
    private LinearLayout login_layout, loged_layout;
    private boolean logged;

    private EditText data_email_edit, data_password_edit;
    private Button data_show_extra_button, data_show_logout_button;

    public static Data_P1_Logged newInstance() {
        Data_P1_Logged fragment = new Data_P1_Logged();
        return fragment;
    }

    public Data_P1_Logged() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_data_p1_logged, container,
                false);
        TextView data_name_name = (TextView)rootView.findViewById(R.id.data_name_name);
        TextView data_email_email = (TextView)rootView.findViewById(R.id.data_email_email);
        TextView data_phone_phone = (TextView)rootView.findViewById(R.id.data_phone_phone);
        data_show_extra_button = (Button)rootView.findViewById(R.id.data_show_extra_button);
        data_show_logout_button = (Button)rootView.findViewById(R.id.data_show_logout_button);
        data_name_name.setText(AppContr.userData.getUser_name());
        data_email_email.setText(AppContr.userData.getUser_email());
        data_phone_phone.setText(AppContr.userData.getUser_phone());

        data_show_logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.LogOut();
            }
        });

        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_data));
        return rootView;
    }

    private void doShowData() {
        data_show_extra_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void http_result(int type, String result) {

    }

    @Override
    public void http_error(int type, String error) {

    }
}