package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Data_P1 extends Fragment implements HttpRequest{
    Interfaces interfaces;
    private LinearLayout login_layout, loged_layout;
    private boolean logged;

    private EditText data_email_edit, data_password_edit;
    private Button data_login_button, data_show_extra_button;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_data_p1, container,
                false);
        login_layout = (LinearLayout)rootView.findViewById(R.id.login_layout);
        loged_layout = (LinearLayout)rootView.findViewById(R.id.loged_layout);

        data_email_edit = (EditText)rootView.findViewById(R.id.data_email_edit);
        data_password_edit = (EditText)rootView.findViewById(R.id.data_password_edit);
        data_login_button = (Button)rootView.findViewById(R.id.data_login_button);
        data_show_extra_button = (Button)rootView.findViewById(R.id.data_show_extra_button);

        if(logged){
            login_layout.setVisibility(View.GONE);
            loged_layout.setVisibility(View.VISIBLE);
            doShowData();
        } else {
            login_layout.setVisibility(View.VISIBLE);
            loged_layout.setVisibility(View.GONE);
            doLogin();
        }




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

    private void doLogin() {
        data_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.email_Correct(data_email_edit.getText().toString()) &&
                        Utils.password_Correct(data_password_edit.getText().toString())){
                    getToken(data_email_edit.getText().toString(),
                            data_password_edit.getText().toString());
                } else {
                    //Data not complete
                    Utils.ShowInputErrorDialog(getActivity(), getString(R.string.input_error),
                            getString(R.string.fill_all_fields),
                            getString(R.string.input_ok));

                }
            }
        });
    }

    private void getToken(String user, String password) {
        Requests requests = new Requests(Const.GetToken, Data_P1.this);
        HashMap<String, String> params = new HashMap<>();
        params.put(Const.user, user);
        params.put(Const.password, password);
        requests.getToken(params);
    }

    private void RefreshView() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    @Override
    public void http_result(int type, String result) {
        switch (type){
            case Const.GetToken:
                //Parse data

                //Encrypt & Save token

                //Reload fragment
                logged = true;
                RefreshView();
                break;

        }

    }

    @Override
    public void http_error(int type, String error) {
        switch (type){
            case Const.GetToken:

                break;

        }

    }

//    Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
//    String encrypted = encryption.encryptOrNull("top secret string");
//    String decrypted = encryption.decryptOrNull(encrypted);
}
