package ua.f5.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ua.f5.kopilochka.Interfaces;
import ua.f5.kopilochka.R;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.Utils;

/**
 * Created by NickNb on 13.10.2016.
 */
public class Data_P1_Logged extends BaseFragment implements HttpRequest {

    private long mLastClickTime = 0;
    Interfaces interfaces;

    private Button data_show_extra_button; //data_show_logout_button;
    private ImageView logout_button;
    //private Typeface calibri, calibri_bold;

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

        //calibri = FontCache.get("fonts/calibri.ttf", getActivity());
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());

        TextView data_name_text = (TextView)rootView.findViewById(R.id.data_name_text);
        TextView data_email_email = (TextView)rootView.findViewById(R.id.data_email_email);
        TextView data_email_text = (TextView)rootView.findViewById(R.id.data_email_text);

        TextView data_phone_phone = (TextView)rootView.findViewById(R.id.data_phone_phone);
        TextView data_phone_text = (TextView)rootView.findViewById(R.id.data_phone_text);
        TextView data_f5_ua_message = (TextView)rootView.findViewById(R.id.data_f5_ua_message);

        data_show_extra_button = (Button)rootView.findViewById(R.id.data_show_extra_button);
        logout_button = (ImageView)rootView.findViewById(R.id.logout_button);
        data_name_text.setText(getString(R.string.data_name_hello) + " " + AppContr.userData.getUser_name());

        if(Utils.notNull_orEmpty(AppContr.userData.getUser_email()))
            data_email_email.setText(AppContr.userData.getUser_email());
        else data_email_email.setText(getString(R.string.data_email_text_empty));
        if(Utils.notNull_orEmpty(AppContr.userData.getUser_phone()))
            data_phone_phone.setText(AppContr.userData.getUser_phone());
        else data_phone_phone.setText(getString(R.string.data_phone_text_empty));

        //Fonts
        data_name_text.setTypeface(AppContr.calibri_bold);
        data_email_email.setTypeface(AppContr.calibri);
        data_email_text.setTypeface(AppContr.calibri);
        data_phone_phone.setTypeface(AppContr.calibri);
        data_phone_text.setTypeface(AppContr.calibri);
        data_show_extra_button.setTypeface(AppContr.calibri_bold);
        data_f5_ua_message.setTypeface(AppContr.calibri);

        data_show_extra_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                interfaces.ShowDataExtra();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Dialogs.ShowLogoutDialog(getActivity(), interfaces);
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
        menu_item_title.setTypeface(AppContr.calibri_bold);
        return rootView;
    }

    @Override
    public void http_result(int type, String result) {
        switch (type){

        }

    }

    @Override
    public void http_error(int type, String error) {

    }


}
