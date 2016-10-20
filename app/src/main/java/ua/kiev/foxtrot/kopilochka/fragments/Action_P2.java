package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Models_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.database.Tables;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 06.10.2016.
 */
public class Action_P2 extends Fragment {
    private long mLastClickTime = 0;
    Interfaces interfaces;
    OnBackPress onBackPress;
    private int action_id;
    private String title;
    ImageLoader imageLoader = AppContr.getInstance().getImageLoader();
    ListView models_list;
    Models_ListView_Adapter adapter;
    View action_header;
    ArrayList<BBS_News> models_data;


    public static Action_P2 newInstance(int action_id) { //
        Action_P2 fragment = new Action_P2();
        Bundle args = new Bundle();
        args.putInt(Const.action_id, action_id);
        //args.putString(Const.action_id, title); //--------no need----
        fragment.setArguments(args);
        return fragment;
    }

    public Action_P2() {
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
        View rootView = inflater.inflate(R.layout.frag_action_p2, container,
                false);
        action_id = getArguments().getInt(Const.action_id, 0);
        if(action_id == 0){
            //Error getting data
            Utils.ShowInputErrorDialog(getActivity(), "Error", "Action number error", "OK");
            return null;
        }
        //All ok

        //Inflate header
        action_header = inflater.inflate(R.layout.frag_action_p2_list_header, null);

        TextView action_period = (TextView)action_header.findViewById(R.id.action_period);
        TextView action_type = (TextView)action_header.findViewById(R.id.action_type);
        TextView action_data = (TextView)action_header.findViewById(R.id.action_data);
        TextView action_comment = (TextView)action_header.findViewById(R.id.action_comment);


        //Inflate main viev
        TextView action_name = (TextView)rootView.findViewById(R.id.action_name);
        TextView action_models_count = (TextView)rootView.findViewById(R.id.action_models_count);
        models_list = (ListView)rootView.findViewById(R.id.models_list);
        models_list.addHeaderView(action_header);
        //Initializing
        models_data = new ArrayList<BBS_News>();
        adapter = new Models_ListView_Adapter(getActivity(), models_data);
        models_list.setAdapter(adapter);
        models_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                interfaces.ModelSelected(i);
            }
        });

        //Get info on action
        action_name.setText(getItem_fromBase(action_id).getTitle());
        action_models_count.setText(String.valueOf(action_id));

        //Send request
        Get_From_Database();


        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Pressed in Fragment", Toast.LENGTH_SHORT).show();
                onBackPress.onBackPressed();
            }
        });
        menu_item_title.setText(getString(R.string.menu_action));
        return rootView;
    }

    private BBS_News getItem_fromBase(int id){
        DB db = new DB(getActivity());
        db.open();
        Cursor myCursor = db.getData_forId(id);
        if (myCursor.moveToFirst()){
            BBS_News item = new BBS_News();
            item.setAuthor(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_author)));
            item.setTitle(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_title)));
            item.setDescription(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_description)));
            item.setUrl(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_url)));
            item.setUrlToImage(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_urlToImage)));
            item.setPublishedAt(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_publishedAt)));
            db.close();
            return item;
        } else
            return null;
    }

    private void Get_From_Database() {
        Log.v("", "SSS Start = " + models_data.size());
        models_data.clear();
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
            models_data.add(item);
            myCursor.moveToNext();
        }
        Log.v("", "SSS Finish = " + models_data.size());
        db.close();
        adapter.notifyDataSetChanged();
    }
}
