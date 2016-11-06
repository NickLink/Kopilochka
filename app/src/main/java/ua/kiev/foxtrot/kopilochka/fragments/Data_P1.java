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

import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.utils.Dialogs;
import ua.kiev.foxtrot.kopilochka.utils.Parser;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Data_P1 extends Fragment implements HttpRequest{
    Interfaces interfaces;
    private LinearLayout login_layout, loged_layout;
    private boolean logged;

    private EditText data_email_edit, data_password_edit;
    private Button data_login_button;
    private String login, password;

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
//        login_layout = (LinearLayout)rootView.findViewById(R.id.login_layout);
//        loged_layout = (LinearLayout)rootView.findViewById(R.id.loged_layout);

        data_email_edit = (EditText)rootView.findViewById(R.id.data_email_edit);
        data_password_edit = (EditText)rootView.findViewById(R.id.data_password_edit);
        data_login_button = (Button)rootView.findViewById(R.id.data_login_button);

        data_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = data_email_edit.getText().toString();
                password = data_password_edit.getText().toString();
                if(!login.trim().isEmpty() && //Utils.email_Correct(data_email_edit.getText().toString())
                        !password.trim().isEmpty()  ){ //Utils.password_Correct(data_password_edit.getText().toString())
                    getToken(login, password);
                } else {
                    //Data not complete
                    Dialogs.ShowDialog(getActivity(), getString(R.string.input_error),
                            getString(R.string.fill_all_fields),
                            getString(R.string.input_ok));

                }
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

    private void getToken(String login, String password) {
        Requests requests = new Requests(getActivity(), Const.getSession, Data_P1.this);
        HashMap<String, String> params = new HashMap<>();
        params.put(Const.login, login);
        params.put(Const.password, password);
        params.put(Const.method, Const.GetSession);
        requests.getHTTP_Responce(params);
    }

//    private void RefreshView() {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();
//    }


    @Override
    public void http_result(int type, String result) {
        switch (type){
            case Const.getSession:
                //Parse data
                AppContr.userData = Parser.getUserData(result);
                if(AppContr.userData != null){
                    //HTTP responce OK, now check for internal data
                    if(AppContr.userData.getCode() == Const.JSON_Ok){
                        //Session is ok, saving Login&Password
                        AppContr.userData.setLogin(login);
                        AppContr.userData.setPassword(password);
                        interfaces.LoginSuccess();
                    } else {
                        //Error in session
                        //Dialogs.Error_Dispencer(getActivity(), type, AppContr.userData.getCode());
                    }

                } else {
                    Dialogs.ShowJSONErrorDialog(getActivity());
                    return;
                }

                break;

        }

    }

    @Override
    public void http_error(int type, String error) {
        switch (type){
            case Const.getSession:

                break;

        }

    }

}
