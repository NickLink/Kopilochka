package ua.f5.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.Interfaces;
import ua.f5.kopilochka.R;
import ua.f5.kopilochka.adapters.History_ExpList_Adapter;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Post_SN;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.StringTools;

/**
 * Created by NickNb on 29.09.2016.
 */
public class History_P1 extends BaseFragment {
    Interfaces interfaces;
    SwipeRefreshLayout swipeRefreshLayout;
    ExpandableListView history_listview;
    History_ExpList_Adapter adapter;
    DB db = AppContr.db;
    List<List<Post_SN>> fullArray;
    //private Typeface calibri_bold;

    public static History_P1 newInstance() {
        History_P1 fragment = new History_P1();
        return fragment;
    }

    public History_P1() {
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_history_p1, container,
                false);
        getAllData();
        history_listview = (ExpandableListView)rootView.findViewById(R.id.history_listview);
        adapter = new History_ExpList_Adapter(getActivity(), fullArray); //complete_list, error_list, await_list
        history_listview.setAdapter(adapter);
        history_listview.setGroupIndicator(null);
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());

        history_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group, int item, long l) {
                String text_to_dialog = fullArray.get(group).get(item).getAction_name() + " "
                        + fullArray.get(group).get(item).getModel_name() + " "
                        + StringTools.StringFromList(fullArray.get(group).get(item).getSerials());
                if(group == 0){
                    //Dialog to delete item only
                    Dialogs.ShowDialogDeleteEditItem(getActivity(),
                            interfaces,
                            getString(R.string.warning_title),
                            text_to_dialog,
                            fullArray.get(group).get(item), false);
                } else if(group == 1 || group ==2){
                    //Dialog to edit/delete item
                    Dialogs.ShowDialogDeleteEditItem(getActivity(),
                            interfaces,
                            getString(R.string.warning_title),
                            text_to_dialog,
                            fullArray.get(group).get(item), true);
                }
                return false;
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
        menu_item_title.setText(getString(R.string.menu_history));
        menu_item_title.setTypeface(AppContr.calibri_bold);
        return rootView;
    }

    public void getAllData(){
        //Toast.makeText(getActivity(), "getAllData event", Toast.LENGTH_SHORT).show();
        fullArray = new ArrayList<List<Post_SN>>();
        fullArray.add(db.getPost_SN_List(Const.reg_status_ok));
        fullArray.add(db.getPost_SN_List(Const.reg_status_await));
        fullArray.add(db.getPost_SN_List(Const.reg_status_error));
    }

    public void NotifyAdapter(){
        //Toast.makeText(getActivity(), "Notify event", Toast.LENGTH_SHORT).show();
        getAllData();
        adapter.setExpListData(fullArray);
        adapter.notifyDataSetChanged();
    }

}
