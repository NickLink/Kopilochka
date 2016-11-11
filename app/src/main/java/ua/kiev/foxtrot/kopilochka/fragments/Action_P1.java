package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Action_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.http.Methods;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.ui.FontCache;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Action_P1 extends BaseFragment implements HttpRequest {
    private long mLastClickTime = 0;
    Interfaces interfaces;
    TextView result_test;
    Button button2;

    ListView action_listview;
    SwipeRefreshLayout swipeRefreshLayout;
    Action_ListView_Adapter adapter;
    //private ArrayList<BBS_News> action_data;
    DB db;
    private Typeface calibri_bold;

    public static Action_P1 newInstance() {
        Action_P1 fragment = new Action_P1();
        return fragment;
    }

    public Action_P1() {
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
        View rootView = inflater.inflate(R.layout.frag_action_p1, container,
                false);
        Log.v("", "SSS onCreateView ");

        calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());
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

        //action_data = new ArrayList<BBS_News>();
        db = new DB(getActivity());

        action_listview = (ListView)rootView.findViewById(R.id.action_listview);
        adapter = new Action_ListView_Adapter(getActivity(), db.getActionArray());
        action_listview.setAdapter(adapter);
        action_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                interfaces.ActionSelected(adapter.getAction_data().get(i).getAction_id());
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
        menu_item_title.setText(getString(R.string.menu_action));
        menu_item_title.setTypeface(calibri_bold);
        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }


    private void getFromInternet(){
        //ACTIONS-----------------------------------------------
        Methods.GetActionList(getActivity(), this);


//        Requests actions_requests = new Requests(getActivity(), Const.getActions, this);
//        HashMap<String, String> actions_params = new HashMap<String, String>();
//        actions_params.put(Const.method, Const.GetActions);
//        actions_params.put(Const.session, Encryption.getDefault("Key", "Disabled", new byte[16])
//                .decryptOrNull(AppContr.getSharPref().getString(Const.SAVED_SES, null)));
//        actions_requests.getHTTP_Responce(actions_params);
    }

    @Override
    public void http_result(int type, String result) {
        swipeRefreshLayout.setRefreshing(false);
        switch (type){
            case Const.getActions:
                Methods.PutActionInBase(getActivity(), result);
                db = new DB(getActivity());
                adapter.setAction_data(db.getActionArray());
                adapter.notifyDataSetChanged();
            break;
        }
    }

    @Override
    public void http_error(int type, String error) {
        //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void PutActionsInDatabase(ArrayList<Action> actions) {
        db = new DB(getActivity());
        if(db.addActionArray(actions)){
            //Data to base added successfully
        } else {
            Log.v("Error", "SSS ActionP1 PutActionsInDatabase error");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        result_test.setText(getScanCode());
//    }
//
//    private String getScanCode(){
//        MainActivity activity = (MainActivity) getActivity();
//        String code = activity.getScan_code();
//        return code;
//    }
}
