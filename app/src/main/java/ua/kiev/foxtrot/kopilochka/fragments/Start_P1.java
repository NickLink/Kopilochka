package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.BBS_News_Adapter;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.database.Tables;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.utils.Parser;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Start_P1 extends Fragment implements HttpRequest{
    Interfaces interfaces;
    ListView bbs_news_listview;
    BBS_News_Adapter adapter;
    private ArrayList<BBS_News> news_data;
    DB db;
    Cursor myCursor;
    TextView counter;

    Button load_more_button;
    ProgressBar load_more_progress;

    public static Start_P1 newInstance() {
        Start_P1 fragment = new Start_P1();
        return fragment;
    }

    public Start_P1() {
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
        View rootView = inflater.inflate(R.layout.frag_start_p1, container,
                false);

        counter = (TextView)rootView.findViewById(R.id.counter);
        news_data = new ArrayList<BBS_News>();
        bbs_news_listview = (ListView)rootView.findViewById(R.id.listView);
        adapter = new BBS_News_Adapter(getActivity(), news_data);
        bbs_news_listview.setAdapter(adapter);


        load_more_progress = (ProgressBar)rootView.findViewById(R.id.load_more_progress);
        load_more_progress.setVisibility(View.INVISIBLE);
        load_more_button =(Button)rootView.findViewById(R.id.start_loadmore_button);
        load_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMore();
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
        menu_item_title.setText(getString(R.string.menu_start));
        return rootView;
    }

    void loadMore(){
        Requests requests = new Requests(1, Start_P1.this);
        requests.getNewsData();
        load_more_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Get_From_Database();
    }

    private void Get_From_Database() {
        news_data.clear();
        db = new DB(getActivity());
        db.open();
        myCursor = db.getAllData();
        myCursor.moveToFirst();
        while (myCursor.isAfterLast() == false) {
            BBS_News item = new BBS_News();
            item.setAuthor(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_author)));
            item.setTitle(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_title)));
            item.setDescription(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_description)));
            item.setUrl(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_url)));
            item.setUrlToImage(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_urlToImage)));
            item.setPublishedAt(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_publishedAt)));
            news_data.add(item);
            myCursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
        counter.setText(" " + news_data.size());
    }


    @Override
    public void http_result(int type, String result) {
        load_more_progress.setVisibility(View.INVISIBLE);
        PutDataInDatabase(Parser.getNewsArray(result));

    }

    @Override
    public void http_error(int type, String error) {
        Utils.ShowInputErrorDialog(getActivity(), "Error", "HTTP", "OK");

    }

    private void PutDataInDatabase(ArrayList<BBS_News> news) {
        db = new DB(getActivity());
        db.open();
        if(db.addNewsArray(news)){
            //Data to base added successfully
            Get_From_Database();
        } else {
            //CreateNotification("Error", "Database Transaction FAIL", "");
            Utils.ShowInputErrorDialog(getActivity(), "Error", "Database Transaction FAIL", "OK");
        }


    }
}
