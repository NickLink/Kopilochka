package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Models_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;
import ua.kiev.foxtrot.kopilochka.ui.FontCache;
import ua.kiev.foxtrot.kopilochka.utils.Dialogs;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 06.10.2016.
 */
public class Action_P2 extends BaseFragment {

    private long mLastClickTime = 0;
    Interfaces interfaces;
    OnBackPress onBackPress;

    private int action_id;

    ListView models_list;
    Models_ListView_Adapter adapter;
    View action_header;
    DB db = AppContr.db;
    Action action;
    private Typeface calibri, calibri_bold;


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
            Dialogs.ShowDialog(getActivity(), "Error", "Action number error", "OK");
            return null;
        }
        //All ok
        calibri = FontCache.get("fonts/calibri.ttf", getActivity());
        calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());

        //Inflate header
        action_header = inflater.inflate(R.layout.frag_action_p2_list_header, null);

        TextView action_period_text = (TextView)action_header.findViewById(R.id.action_period_text);
        TextView action_type_text = (TextView)action_header.findViewById(R.id.action_type_text);
        TextView action_data_text = (TextView)action_header.findViewById(R.id.action_data_text);
        TextView action_days_left_text = (TextView)action_header.findViewById(R.id.action_days_left_text);

        TextView action_period_tv = (TextView)action_header.findViewById(R.id.action_period);
        TextView action_type_tv = (TextView)action_header.findViewById(R.id.action_type);
        TextView action_data_tv = (TextView)action_header.findViewById(R.id.action_data);
        TextView action_days_left_tv = (TextView)action_header.findViewById(R.id.action_days_left);
        TextView action_comment_tv = (TextView)action_header.findViewById(R.id.action_comment);


        //Inflate main viev
        TextView action_name = (TextView)rootView.findViewById(R.id.title);
        TextView action_models_count = (TextView)rootView.findViewById(R.id.count);
        models_list = (ListView)rootView.findViewById(R.id.models_list);
        models_list.addHeaderView(action_header, "Header", false);
        //Initializing

        action = db.getActionById(action_id);
        //models_data = new ArrayList<Model>();
        adapter = new Models_ListView_Adapter(getActivity(), action.getModels());
        models_list.setAdapter(adapter);
        models_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                i -= models_list.getHeaderViewsCount();
                interfaces.ModelSelected(action.getAction_id(), action.getModels().get(i).getModel_id());
            }
        });

        //Set info on action
        action_name.setText(action.getAction_name());
        action_models_count.setText(String.valueOf(action.getModels().size()));
        action_period_tv.setText("з " + Utils.getDateFromMillis(action.getAction_date_from())
                + " по " + Utils.getDateFromMillis(action.getAction_date_to()));
        action_type_tv.setText(action.getAction_type());
        action_data_tv.setText(Utils.getDateFromMillis(action.getAction_date_charge()));
        action_days_left_tv.setText(String.valueOf(Utils.daysLeft(action.getAction_date_to())));
        action_comment_tv.setText(action.getAction_description());

        action_name.setTypeface(calibri_bold);
        action_models_count.setTypeface(calibri_bold);
        action_period_tv.setTypeface(calibri_bold);
        action_type_tv.setTypeface(calibri_bold);
        action_data_tv.setTypeface(calibri_bold);
        action_days_left_tv.setTypeface(calibri_bold);
        action_comment_tv.setTypeface(calibri);
        action_period_text.setTypeface(calibri);
        action_type_text.setTypeface(calibri);
        action_data_text.setTypeface(calibri);
        action_days_left_text.setTypeface(calibri);

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
        menu_item_title.setTypeface(calibri_bold);
        return rootView;
    }

}
