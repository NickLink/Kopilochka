package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import ua.kiev.foxtrot.kopilochka.ui.FontCache;
import ua.kiev.foxtrot.kopilochka.utils.Dialogs;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Start_P1 extends BaseFragment implements HttpRequest{
    Interfaces interfaces;

    Product_ListView_Adapter adapter;
    private ArrayList<BBS_News> news_data;
    DB db = AppContr.db;
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
    RelativeLayout no_bonuses_layout;
    private Typeface calibri_bold;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_start_p1, container,
                false);

        calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());
        start_listview = (ListView)rootView.findViewById(R.id.listView);
        no_bonuses_layout = (RelativeLayout)rootView.findViewById(R.id.no_bonuses_layout);

        actionList = db.getActionArray();

        productList = new ArrayList<>();
        if(actionList != null){
            cumulative_count = LookForCumulativeActions(actionList);
            if(cumulative_count > 0){
                ProductGroup item = new ProductGroup();
                item.setGroup_name(getString(R.string.start_cumulative_points));
                item.setModels_count(cumulative_count);
                productList.add(item);
            }

        } else {
            //Бонусные акции отсутствуют
        }

        productList.addAll(db.getGroupArray()); //productList.addAll(db.getGroupsNamesAndCount());
        //

        if(productList != null && productList.size() != 0){
            adapter = new Product_ListView_Adapter(getActivity(), productList);
            start_listview.setAdapter(adapter);

            start_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(cumulative_count > 0 && i == 0) {
                        interfaces.ProductGroupSelected(
                                productList.get(i).getGroup_id(),
                                productList.get(i).getGroup_name(),
                                0);
                    } else {
                        adapter.setGroupViewed(i);
                        db.setGroupViewed(adapter.getProduct_data().get(i).getGroup_id());

                        interfaces.ProductGroupSelected(
                                productList.get(i).getGroup_id(),
                                productList.get(i).getGroup_name(),
                                1);
                    }
                }
            });

        } else {
            //Выводим что бонусирование не проводиться
            no_bonuses_layout.setVisibility(View.VISIBLE);
            start_listview.setVisibility(View.GONE);
        }

        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_start));
        menu_item_title.setTypeface(calibri_bold);
        return rootView;
    }

    private int LookForCumulativeActions(ArrayList<Action> arrayList){
        int cumulative_count = 0;
        for(int i=0 ; i < arrayList.size() ; i++){
            Action item = arrayList.get(i);
            boolean isCumulative = item.getAction_type_id() == 0 ? true : false;
            boolean isDateValid = Utils.isDateInRange(item.getAction_date_from(), item.getAction_date_to());
            if(isCumulative && isDateValid){
                cumulative_count+=item.getModels().size();
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
        Requests requests = new Requests(getActivity(), 1, Start_P1.this);
        requests.getNewsData();
        load_more_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }




    @Override
    public void http_result(int type, String result) {

    }

    @Override
    public void http_error(int type, String error) {
        Dialogs.ShowDialog(getActivity(), "Error", "HTTP", "OK");

    }

}
