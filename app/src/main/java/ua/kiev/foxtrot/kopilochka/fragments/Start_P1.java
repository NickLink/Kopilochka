package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Product_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.data.Model;
import ua.kiev.foxtrot.kopilochka.data.ProductGroup;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.http.Requests;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Start_P1 extends Fragment implements HttpRequest{
    Interfaces interfaces;

    Product_ListView_Adapter adapter;
    private ArrayList<BBS_News> news_data;
    DB db;
    Cursor myCursor;
    TextView counter;

    Button load_more_button;
    ProgressBar load_more_progress;

    //ACTIONS WORKING
    private ArrayList<Action> actionList;
    private ArrayList<Model> modelList;
    private ArrayList<ProductGroup> productList;
    private int cumulative_count = 0;
    ListView start_listview;
    View header;

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
        View rootView = inflater.inflate(R.layout.frag_start_p1, container,
                false);
        start_listview = (ListView)rootView.findViewById(R.id.listView);
        header = inflater.inflate(R.layout.frag_start_p1_list_item, null);
        TextView header_title = (TextView)header.findViewById(R.id.title);
        TextView header_count = (TextView)header.findViewById(R.id.count);

        db = new DB(getActivity());
        actionList = db.getActionArray();

        if(actionList != null){
            cumulative_count = LookForCumulativeActions(actionList);
            if(cumulative_count > 0){
                start_listview.addHeaderView(header);
                header_title.setText(getString(R.string.start_cumulative_points));
                header_count.setText(String.valueOf(cumulative_count));
            }

        } else {
            Utils.ShowInputErrorDialog(getActivity(), "Ашипка", "Старт П1", "Массив акций пуст/сломан");
        }

        db = new DB(getActivity());
        productList = db.getGroupsNamesAndCount();
        //modelList = db.getModelsArray();
//        if(modelList != null){
//
//            //SortByProductGroup(modelList);
//        } else {
//            Utils.ShowInputErrorDialog(getActivity(), "Ашипка", "Старт П1", "Массив моделей пуст/сломан");
//        }


        //counter.setText(String.valueOf(cumulative_count));

        //news_data = new ArrayList<BBS_News>();

        adapter = new Product_ListView_Adapter(getActivity(), productList);
        start_listview.setAdapter(adapter);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "header Selected", Toast.LENGTH_LONG).show();
            }
        });
        start_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "start_listview Selected pos = " + i, Toast.LENGTH_LONG).show();
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

    private int LookForCumulativeActions(ArrayList<Action> arrayList){
        int cumulative_count = 0;
        for(int i=0 ; i < arrayList.size() ; i++){
            Action item = arrayList.get(i);
            boolean isCumulative = item.getAction_type_id() == 0 ? true : false;
            boolean isDateValid = Utils.isDateInRange(item.getAction_date_from(), item.getAction_date_to());
            if(isCumulative && isDateValid){
                cumulative_count++;
            }
        }
        return cumulative_count;
    };

//    private void SortByProductGroup(ArrayList<Model> arrayList){
//        Map<Integer, ProductGroup> hashmap = new HashMap<Integer, ProductGroup>();
//        ArrayList<ProductGroup> productList = new ArrayList<>();
//        for (int i =0 ; i < arrayList.size(); i++){
//            if(!hashmap.isEmpty() && hashmap.containsKey(arrayList.get(i).getModel_group_id())){
//                //not empty & contains that key
//                hashmap.get(arrayList.get(i).getModel_group_id()).setModels_count();
//
//            } else if (!hashmap.isEmpty() && !hashmap.containsKey(arrayList.get(i).getModel_group_id())){
//                //not empty & DO NOT contains that key
//            } else {
//                //hash map is empty
//                ProductGroup item = new ProductGroup();
//                item.setGroup_id(arrayList.get(i).getModel_group_id());
//                item.setGroup_name(arrayList.get(i).getModel_group_name());
//                item.setModels_count(1);
//                hashmap.put(arrayList.get(i).getModel_group_id(), item);
//            }
//
//
//
//            //hashmap.put(arrayList.get(i).getModel_group_id(), arrayList.get(i).getModel_group_name());
//        }
//        List list = new LinkedList(hashmap.entrySet());
//        // Defined Custom Comparator here
//        Collections.sort(list, new Comparator() {
//            public int compare(Object o1, Object o2) {
//                return ((Comparable) ((Map.Entry) (o1)).getValue())
//                        .compareTo(((Map.Entry) (o2)).getValue());
//            }
//        });
//
//    }

    void loadMore(){
        Requests requests = new Requests(1, Start_P1.this);
        requests.getNewsData();
        load_more_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Get_From_Database();
    }

//    private void Get_From_Database() {
//        news_data.clear();
//        db = new DB(getActivity());
//        db.open();
//        myCursor = db.getAllData();
//        myCursor.moveToFirst();
//        while (myCursor.isAfterLast() == false) {
//            BBS_News item = new BBS_News();
//            item.setAuthor(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_author)));
//            item.setTitle(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_title)));
//            item.setDescription(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_description)));
//            item.setUrl(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_url)));
//            item.setUrlToImage(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_urlToImage)));
//            item.setPublishedAt(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_publishedAt)));
//            news_data.add(item);
//            myCursor.moveToNext();
//        }
//        adapter.notifyDataSetChanged();
//        counter.setText(" " + news_data.size());
//    }


    @Override
    public void http_result(int type, String result) {
        //load_more_progress.setVisibility(View.INVISIBLE);
        //PutDataInDatabase(Parser.getNewsArray(result));

    }

    @Override
    public void http_error(int type, String error) {
        Utils.ShowInputErrorDialog(getActivity(), "Error", "HTTP", "OK");

    }

//    private void PutDataInDatabase(ArrayList<BBS_News> news) {
//        db = new DB(getActivity());
//        db.open();
//        if(db.addNewsArray(news)){
//            //Data to base added successfully
//            Get_From_Database();
//        } else {
//            //CreateNotification("Error", "Database Transaction FAIL", "");
//            Utils.ShowInputErrorDialog(getActivity(), "Error", "Database Transaction FAIL", "OK");
//        }
//    }
}
