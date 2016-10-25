package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.FinInfo;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;
import ua.kiev.foxtrot.kopilochka.utils.Encryption;

/**
 * Created by NickNb on 25.10.2016.
 */
public class Data_P1_Extra extends Fragment implements HttpRequest {
    Interfaces interfaces;
    OnBackPress onBackPress;
    SwipeRefreshLayout swipeRefreshLayout;
    ExpandableListView payment_listview;

    FinInfo finInfo;

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
    public void onStop () {
        super.onStop();
        //Cancel HTTP requests
        if (AppContr.getInstance() != null) {
            AppContr.getInstance().cancelAllRequests();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_data_p1_extra, container,
                false);

        payment_listview = (ExpandableListView)rootView.findViewById(R.id.payment_listview);

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Get_From_Database();
                getFromInternet();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);





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
        return rootView;
    }

    private void getFromInternet(){
        //ACTIONS-----------------------------------------------
        Requests payment_requests = new Requests(getActivity(), Const.getFinInfo, this);
        HashMap<String, String> payment_params = new HashMap<String, String>();
        payment_params.put(Const.method, Const.GetFinInfo);
        payment_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
        payment_requests.getHTTP_Responce(payment_params);
    }


    @Override
    public void http_result(int type, String result) {

    }

    @Override
    public void http_error(int type, String error) {

    }
}
