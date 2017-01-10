package ua.f5.kopilochka.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.Interfaces;
import ua.f5.kopilochka.R;
import ua.f5.kopilochka.adapters.FinInfo_ExpList_Adapter;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.FinInfo;
import ua.f5.kopilochka.http.Requests;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.interfaces.OnBackPress;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.Encryption;
import ua.f5.kopilochka.utils.Parser;
import ua.f5.kopilochka.utils.Utils;

/**
 * Created by NickNb on 25.10.2016.
 */
public class Data_P1_Extra extends BaseFragment implements HttpRequest {
    Interfaces interfaces;
    OnBackPress onBackPress;

    //private Typeface calibri, calibri_bold;

    ExpandableListView payment_listview;
    FinInfo_ExpList_Adapter adapter;
    View header;
    TextView fininfo_name, fininfo_contact_e_mail, fininfo_contact_phone, fininfo_bonus;

    FinInfo finInfo;
    ProgressDialog load;

    public static Data_P1_Extra newInstance() {
        Data_P1_Extra fragment = new Data_P1_Extra();
        return fragment;
    }

    public Data_P1_Extra() {
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
        View rootView = inflater.inflate(R.layout.frag_data_p1_extra, container,
                false);

        //calibri = FontCache.get("fonts/calibri.ttf", getActivity());
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());
        //Header part
        header = inflater.inflate(R.layout.frag_data_p1_extra_header, null);
        fininfo_name = (TextView)header.findViewById(R.id.fininfo_name);
        fininfo_contact_e_mail = (TextView)header.findViewById(R.id.fininfo_contact_e_mail);
        TextView fininfo_contact_e_mail_title = (TextView)header.findViewById(R.id.fininfo_contact_e_mail_title);
        fininfo_contact_phone = (TextView)header.findViewById(R.id.fininfo_contact_phone);
        TextView fininfo_contact_phone_title = (TextView)header.findViewById(R.id.fininfo_contact_phone_title);
        fininfo_bonus = (TextView)header.findViewById(R.id.fininfo_bonus);
        TextView data_f5_ua_message = (TextView)rootView.findViewById(R.id.data_f5_ua_message);

        payment_listview = (ExpandableListView)rootView.findViewById(R.id.payment_listview);
        payment_listview.addHeaderView(header);

//        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //Get_From_Database();
//                getFromInternet();
//            }
//        });
//        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
//                R.color.holo_green_light,
//                R.color.holo_orange_light,
//                R.color.holo_red_light);
        fininfo_name.setTypeface(AppContr.calibri_bold);
        fininfo_contact_e_mail.setTypeface(AppContr.calibri);
        fininfo_contact_e_mail_title.setTypeface(AppContr.calibri);
        fininfo_contact_phone.setTypeface(AppContr.calibri);
        fininfo_contact_phone_title.setTypeface(AppContr.calibri);
        fininfo_bonus.setTypeface(AppContr.calibri_bold);
        data_f5_ua_message.setTypeface(AppContr.calibri);

        getFromInternet();

        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Pressed in Fragment", Toast.LENGTH_SHORT).show();
                onBackPress.onBackPressed();
            }
        });
        menu_item_title.setText(getString(R.string.menu_data));
        menu_item_title.setTypeface(AppContr.calibri_bold);
        return rootView;
    }

    private void getFromInternet(){
        //ACTIONS-----------------------------------------------
        Requests payment_requests = new Requests(getActivity(), Const.getFinInfo, this);
        HashMap<String, Object> payment_params = new HashMap<>();
        payment_params.put(Const.method, Const.GetFinInfo);
        payment_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        payment_requests.getHTTP_Responce(payment_params);
        load = new ProgressDialog(getActivity());
        load.setMessage(getString(R.string.data_loading));
        load.show();
    }


    @Override
    public void http_result(int type, String result) {
        if(load != null && load.isShowing()) load.dismiss();

        finInfo = new FinInfo();
        finInfo = Parser.getFinInfo(result);
        if(finInfo != null){
            fininfo_name.setText(getString(R.string.data_name_hello) + " " + finInfo.getUser_name());

            if(Utils.notNull_orEmpty(finInfo.getUser_email()))
                fininfo_contact_e_mail.setText(finInfo.getUser_email());
            else fininfo_contact_e_mail.setText(getString(R.string.data_email_text_empty));
            if(Utils.notNull_orEmpty(finInfo.getUser_phone()))
                fininfo_contact_phone.setText(finInfo.getUser_phone());
            else fininfo_contact_phone.setText(getString(R.string.data_phone_text_empty));

            fininfo_bonus.setText(getString(R.string.data_bonus_to) + " " + finInfo.getUser_payment());
            adapter = new FinInfo_ExpList_Adapter(getActivity(), finInfo);
            payment_listview.setAdapter(adapter);
            payment_listview.setGroupIndicator(null);
            //payment_listview.expandGroup(0, true);
            //payment_listview.expandGroup(1, true);
            //payment_listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
            //swipeRefreshLayout.setRefreshing(false);
        } else {
            Dialogs.ShowJSONErrorDialog(getActivity());
        }

    }

    @Override
    public void http_error(int type, String error) {
        if(load != null && load.isShowing()) load.dismiss();
    }
}
