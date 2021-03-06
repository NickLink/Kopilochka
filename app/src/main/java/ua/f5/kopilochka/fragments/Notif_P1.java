package ua.f5.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.Interfaces;
import ua.f5.kopilochka.R;
import ua.f5.kopilochka.adapters.Notif_ListView_Adapter;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Notice;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.http.Methods;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.Parser;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Notif_P1 extends BaseFragment implements HttpRequest {
    private long mLastClickTime = 0;
    Interfaces interfaces;
    SwipeRefreshLayout swipeRefreshLayout;

    ListView notif_listview;
    Notif_ListView_Adapter adapter;
    //ArrayList<Notice> notif_data;
    DB db = AppContr.db;
    //private Typeface calibri_bold;

    public static Notif_P1 newInstance() {
        Notif_P1 fragment = new Notif_P1();
        return fragment;
    }

    public Notif_P1() {
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
    public void onResume(){
        super.onResume();
        //Refresh data adapter
        adapter.setNotice_data(db.getNoticeArray());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_notif_p1, container,
                false);

        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());

        notif_listview = (ListView) rootView.findViewById(R.id.notif_listview);
        adapter = new Notif_ListView_Adapter(getActivity(), db.getNoticeArray());
        notif_listview.setAdapter(adapter);


        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFromInternet();
//                //Log.v("", "SSS Refresh Started ");
//                new Handler().postDelayed(new Runnable() {
//                    @Override public void run() {
//                        //Run refresh querry
//                        adapter.setNotice_data(db.getNoticeArray());
//
//                        //Log.v("", "SSS Refresh finished ");
//                    }
//                }, 1000);

            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);




        //Get_From_Database();


        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_notification));
        menu_item_title.setTypeface(AppContr.calibri_bold);
        return rootView;
    }

    private void getFromInternet(){
        //NOTICES----------------------------------------------

        Methods.GetNotificationList(getActivity(), this);
//        Requests notice_requests = new Requests(getActivity(), Const.getNotices, this);
//        HashMap<String, String> notice_params = new HashMap<String, String>();
//        notice_params.put(Const.method, Const.GetNotices);
//        notice_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
//                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
//        notice_requests.getHTTP_Responce(notice_params);
    }

    @Override
    public void http_result(int type, String result) {
        swipeRefreshLayout.setRefreshing(false);
        switch (type){
            case Const.getNotices:
                ArrayList<Notice> notices = Parser.getNoticesArray(result);
                if(notices != null){
                    Methods.PutNotificationInBase(getActivity(), notices);
                    adapter.setNotice_data(db.getNoticeArray());
                    adapter.notifyDataSetChanged();
                } else {
                    Dialogs.ShowJSONErrorDialog(getActivity());
                }

        }
    }

    @Override
    public void http_error(int type, String error) {
        //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean PutNoticesInDatabase(ArrayList<Notice> notices) {
        if(db.addNoticeArray(notices)){
            //Data to base added successfully
            return true;
        } else {
            //Log.v("Error", "SSS ActionP1 PutActionsInDatabase error");
            return false;
        }
    }
}
