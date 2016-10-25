package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Models_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.Model;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 25.10.2016.
 */
public class Start_P2 extends Fragment implements HttpRequest {
    Interfaces interfaces;
    OnBackPress onBackPress;
    private int model_group_id, action_type_id;
    private String model_group_name;
    ListView product_listview;
    ArrayList<Model> modelArrayList;
    Models_ListView_Adapter adapter;
    ArrayList<Action> actionArrayList;
    DB db;

    public static Start_P2 newInstance(int group_id, String group_name, int action_type_id) {
        Start_P2 fragment = new Start_P2();
        Bundle args = new Bundle();
        args.putInt(Const.model_group_id, group_id);
        args.putString(Const.model_group_name, group_name);
        args.putInt(Const.action_type_id, action_type_id);
        fragment.setArguments(args);
        return fragment;
    }

    public Start_P2() {
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
        View rootView = inflater.inflate(R.layout.frag_start_p2, container,
                false);
        model_group_id = getArguments().getInt(Const.model_group_id, -1);
        model_group_name = getArguments().getString(Const.model_group_name);
        action_type_id = getArguments().getInt(Const.action_type_id, -1);
        if(model_group_id == -1 || model_group_name.isEmpty() || action_type_id == -1){
            //Error getting data
            Utils.ShowInputErrorDialog(getActivity(), "Error", "Start P2  Model group error", "OK");
            return null;
        }
        //Go for Data
        db = new DB(getActivity());
        actionArrayList = db.getActionByTypeArray(action_type_id);
        modelArrayList = new ArrayList<>();

        for(int i = 0 ; i<actionArrayList.size() ; i++){
            for(int j = 0 ; j<actionArrayList.get(i).getModels().size(); j++){
                Model item = actionArrayList.get(i).getModels().get(j);
                boolean isDateValid = false;
                boolean isPGroupValid = false;
                isDateValid = Utils.isDateInRange(
                        actionArrayList.get(i).getAction_date_from(),
                        actionArrayList.get(i).getAction_date_to());
                if(action_type_id == 1) {
                    isPGroupValid = item.getModel_group_id() == model_group_id ? true : false;
                } else isPGroupValid = true;
                if(isDateValid && isPGroupValid){
                    item.setModel_days_left(Utils.daysLeft(actionArrayList.get(i).getAction_date_to()));
                    modelArrayList.add(item);
                }

            }

        }

        //All ok
        product_listview = (ListView)rootView.findViewById(R.id.product_listview);
        adapter = new Models_ListView_Adapter(getActivity(), modelArrayList);
        product_listview.setAdapter(adapter);




        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Pressed in Fragment", Toast.LENGTH_SHORT).show();
                onBackPress.onBackPressed();
            }
        });
        menu_item_title.setText(model_group_name + " " + action_type_id);
        return rootView;
    }

    @Override
    public void http_result(int type, String result) {

    }

    @Override
    public void http_error(int type, String error) {

    }
}
