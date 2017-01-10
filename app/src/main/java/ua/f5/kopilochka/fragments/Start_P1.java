package ua.f5.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ua.f5.kopilochka.Interfaces;
import ua.f5.kopilochka.R;
import ua.f5.kopilochka.adapters.Product_ListView_Adapter;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Action;
import ua.f5.kopilochka.data.ProductGroup;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Start_P1 extends BaseFragment{
    private long mLastClickTime = 0;
    Interfaces interfaces;

    Product_ListView_Adapter adapter;
    DB db = AppContr.db;

    private ArrayList<ProductGroup> productList;
    private int cumulative_count = 0;
    ListView start_listview;
    RelativeLayout no_bonuses_layout;

    public static Start_P1 newInstance() {
        return new Start_P1();
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
    public void onResume(){
        super.onResume();
        //Refresh data adapter
        getProductList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_start_p1, container,
                false);

        //Typeface calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());
        start_listview = (ListView)rootView.findViewById(R.id.listView);
        no_bonuses_layout = (RelativeLayout)rootView.findViewById(R.id.no_bonuses_layout);

        if(getProductList()) {
            adapter = new Product_ListView_Adapter(getActivity(), productList);
            start_listview.setAdapter(adapter);

            start_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    adapter.setGroupViewed(i);
                    db.setGroupViewed(adapter.getProduct_data().get(i).getGroup_id());
                    if(cumulative_count > 0 && i == 0) {
                        interfaces.ProductGroupSelected(
                                productList.get(i).getGroup_id(),
                                productList.get(i).getGroup_name(),
                                0);
                    } else {
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
        menu_item_title.setTypeface(AppContr.calibri_bold);
        return rootView;
    }

    private int LookForCumulativeActions(ArrayList<Action> arrayList){
        int cumulative_count = 0;
        for(int i=0 ; i < arrayList.size() ; i++){
            Action item = arrayList.get(i);
            boolean isCumulative = item.getAction_type_id() == 0;
            boolean isDateValid = Utils.isDateInRange(item.getAction_date_from(), item.getAction_date_to());
            if(isCumulative && isDateValid){
                cumulative_count+=item.getModels().size();
            }
        }
        return cumulative_count;
    }

    private boolean getProductList(){
        productList = db.getGroupArray();
        ArrayList<Action> actionList = db.getActionArray();
        if(actionList != null && actionList.size() > 0) {
            cumulative_count = LookForCumulativeActions(actionList);
            if (cumulative_count != 0) {
                productList.get(0).setModels_count(cumulative_count);
            } else {
                productList.remove(0);
            }
            return true;
        } else {
            return false;
        }

    }

}
