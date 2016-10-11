package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Notif_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.database.Tables;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Notif_P1 extends Fragment {
    private long mLastClickTime = 0;
    Interfaces interfaces;

    ListView notif_listview;
    Notif_ListView_Adapter adapter;
    ArrayList<BBS_News> notif_data;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_notif_p1, container,
                false);

        notif_data = new ArrayList<BBS_News>();
        notif_listview = (ListView) rootView.findViewById(R.id.notif_listview);
        adapter = new Notif_ListView_Adapter(getActivity(), notif_data);
        notif_listview.setAdapter(adapter);

        Get_From_Database();


        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_notification));
        return rootView;
    }

    private void Get_From_Database() {
        Log.v("", "SSS Start = " + notif_data.size());
        notif_data.clear();
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
            notif_data.add(item);
            myCursor.moveToNext();
        }
        Log.v("", "SSS Finish = " + notif_data.size());
        db.close();
        adapter.notifyDataSetChanged();
    }
}
