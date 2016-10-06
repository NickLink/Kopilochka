package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Action_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.database.Tables;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Action_P1 extends Fragment {
    Interfaces interfaces;
    TextView result_test;
    Button button2;

    ListView action_listview;
    SwipeRefreshLayout swipeRefreshLayout;
    Action_ListView_Adapter adapter;
    private ArrayList<BBS_News> action_data;

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

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Get_From_Database();
                Log.v("", "SSS Refresh Started ");
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        //Run refresh querry
                        //
                        swipeRefreshLayout.setRefreshing(false);
                        Log.v("", "SSS Refresh finished ");
                    }
                }, 1000);

            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);

        action_data = new ArrayList<BBS_News>();
        action_listview = (ListView)rootView.findViewById(R.id.action_listview);
        adapter = new Action_ListView_Adapter(getActivity(), action_data);
        action_listview.setAdapter(adapter);
        action_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                interfaces.ActionSelected(i);
            }
        });

//        button2 = (Button)rootView.findViewById(R.id.button2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                interfaces.ScannStart();
//            }
//        });
//        result_test = (TextView)rootView.findViewById(R.id.result_test);



        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_action));
        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }

    private void Get_From_Database() {
        Log.v("", "SSS Start = " + action_data.size());
        action_data.clear();
        DB db = new DB(getActivity());
        db.open();
        Cursor myCursor = db.getAllData();
        myCursor.moveToFirst();
        while (myCursor.isAfterLast() == false) {
            BBS_News item = new BBS_News();
            item.setAuthor(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_author)));
            item.setTitle(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_title)));
            item.setDescription(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_description)));
            item.setUrl(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_url)));
            item.setUrlToImage(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_urlToImage)));
            item.setPublishedAt(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_publishedAt)));
            action_data.add(item);
            myCursor.moveToNext();
        }
        Log.v("", "SSS Finish = " + action_data.size());
        db.close();
        adapter.notifyDataSetChanged();
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
