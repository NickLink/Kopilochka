package ua.f5.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.Interfaces;
import ua.f5.kopilochka.R;
import ua.f5.kopilochka.adapters.Action_ListView_Adapter;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Action;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.http.Methods;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.Parser;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Action_P1 extends BaseFragment implements HttpRequest {
    private long mLastClickTime = 0;
    Interfaces interfaces;

    ListView action_listview;
    SwipeRefreshLayout swipeRefreshLayout;
    Action_ListView_Adapter adapter;
    DB db = AppContr.db;
    //private Typeface calibri_bold;

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
    public void onResume() {
        super.onResume();
        //Refresh data adapter
        adapter.setAction_data(db.getActionArray());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_action_p1, container,
                false);
        //Log.v("", "SSS onCreateView ");

        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
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

        action_listview = (ListView) rootView.findViewById(R.id.action_listview);
        adapter = new Action_ListView_Adapter(getActivity(), db.getActionArray());
        action_listview.setAdapter(adapter);
        action_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                adapter.setActionViewed(i);
                db.setActionViewed(adapter.getAction_data().get(i).getAction_id());
                interfaces.ActionSelected(adapter.getAction_data().get(i).getAction_id());
            }
        });

        ImageButton menu_item_icon = (ImageButton) rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView) rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_action));
        menu_item_title.setTypeface(AppContr.calibri_bold);
        return rootView;
    }

    private void getFromInternet() {
        //ACTIONS-----------------------------------------------
        Methods.GetActionList(getActivity(), this);

    }

    @Override
    public void http_result(int type, String result) {
        swipeRefreshLayout.setRefreshing(false);

        ArrayList<Action> actions = new ArrayList<>();
        actions = Parser.getActionsArray(result);
        if (actions != null) {
            switch (type) {
                case Const.getActions:
                    Methods.PutActionInBase(getActivity(), actions);
                    adapter.setAction_data(db.getActionArray());
                    adapter.notifyDataSetChanged();
                    break;
            }
        } else {
            Dialogs.ShowJSONErrorDialog(getActivity());
        }

    }

    @Override
    public void http_error(int type, String error) {
        //Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    private ArrayList<Action> getActionArray(ArrayList<Action> actionArrayList){
        ArrayList<Action> list = new ArrayList<>();




        return list;
    }

}
