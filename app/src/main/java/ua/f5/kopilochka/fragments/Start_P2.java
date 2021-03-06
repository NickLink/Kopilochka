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
import android.widget.TextView;

import java.util.ArrayList;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.Interfaces;
import ua.f5.kopilochka.R;
import ua.f5.kopilochka.adapters.GroupModels_ListView_Adapter;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Action;
import ua.f5.kopilochka.data.Model;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.interfaces.OnBackPress;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.Utils;

/**
 * Created by NickNb on 25.10.2016.
 */
public class Start_P2 extends BaseFragment implements HttpRequest {
    private long mLastClickTime = 0;
    Interfaces interfaces;
    OnBackPress onBackPress;

    ListView product_listview;
    View header;
    ArrayList<Model> modelArrayList;
    GroupModels_ListView_Adapter adapter;
    ArrayList<Action> actionArrayList;
    DB db = AppContr.db;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_start_p2, container,
                false);

        //Typeface calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());
        int model_group_id = getArguments().getInt(Const.model_group_id, -2);
        String model_group_name = getArguments().getString(Const.model_group_name, "");
        int action_type_id = getArguments().getInt(Const.action_type_id, -2);
        if(model_group_id == -2 || model_group_name.isEmpty() || action_type_id == -2){
            //Error getting data
            Dialogs.ShowDialog(getActivity(), getString(R.string.internal_base_error_title),
                    getString(R.string.internal_base_error_text),
                    getString(R.string.ok));
            return null;
        }
        //Go for Data
        actionArrayList = db.getActionByTypeArray(action_type_id);
        modelArrayList = new ArrayList<>();

        for(int i = 0 ; i<actionArrayList.size() ; i++){
            for(int j = 0 ; j<actionArrayList.get(i).getModels().size(); j++){
                Model item = actionArrayList.get(i).getModels().get(j);
                boolean isDateValid;
                boolean isPGroupValid;
                isDateValid = Utils.isDateInRange(
                        actionArrayList.get(i).getAction_date_from(),
                        actionArrayList.get(i).getAction_date_to());
                if(action_type_id == 1) {
                    isPGroupValid = item.getModel_group_id() == model_group_id;
                } else isPGroupValid = true;
                if(isDateValid && isPGroupValid){
                    item.setModel_days_left(Utils.daysLeft(actionArrayList.get(i).getAction_date_to()));
                    modelArrayList.add(item);
                }

            }

        }

        //All ok

        //Inflate header
        product_listview = (ListView)rootView.findViewById(R.id.product_listview);
        header = inflater.inflate(R.layout.frag_start_p2_list_item, null);
        product_listview.addHeaderView(header, "Header", false);
        adapter = new GroupModels_ListView_Adapter(getActivity(), modelArrayList);
        product_listview.setAdapter(adapter);

        product_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                i -= product_listview.getHeaderViewsCount();
                interfaces.ModelSelected(modelArrayList.get(i).getModel_action(), modelArrayList.get(i).getModel_id());
            }
        });


        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Pressed in Fragment", Toast.LENGTH_SHORT).show();
                onBackPress.onBackPressed();
            }
        });
        menu_item_title.setText(model_group_name);
        menu_item_title.setTypeface(AppContr.calibri_bold);
        return rootView;
    }

    @Override
    public void http_result(int type, String result) {

    }

    @Override
    public void http_error(int type, String error) {

    }
}
