package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Serials_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.interfaces.Delete_Serial;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;

/**
 * Created by NickNb on 07.10.2016.
 */
public class Action_P3 extends Fragment implements Delete_Serial{

    Interfaces interfaces;
    OnBackPress onBackPress;
    //Delete_Serial delete_serial;

    TextView result_test;
    Button button2;

    ListView serial_numbers_list;
    Serials_ListView_Adapter adapter;
    View add_footer;
    private ArrayList<BBS_News> serials_data;

    public static Action_P3 newInstance() {
        Action_P3 fragment = new Action_P3();
        return fragment;
    }

    public Action_P3() {
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
        View rootView = inflater.inflate(R.layout.frag_action_p3, container,
                false);


//        Button scan_button = (Button)rootView.findViewById(R.id.scan_button);
//        scan_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                interfaces.ScannStart();
//            }
//        });
//        EditText scan_result = (EditText)rootView.findViewById(R.id.scan_result);

        serials_data = new ArrayList<BBS_News>();
        serials_data.add(new BBS_News());

        add_footer = inflater.inflate(R.layout.frag_action_p3_list_footer, null);
        serial_numbers_list = (ListView)rootView.findViewById(R.id.serial_numbers_list);
        serial_numbers_list.addFooterView(add_footer);

        adapter = new Serials_ListView_Adapter(getActivity(), serials_data, Action_P3.this, interfaces);
        serial_numbers_list.setAdapter(adapter);


        add_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Adding", Toast.LENGTH_SHORT).show();
                serials_data.add(new BBS_News());
                adapter.notifyDataSetChanged();
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
        menu_item_title.setText(getString(R.string.menu_action));
        return rootView;
    }

    @Override
    public void delete_serial(int i) {
        serials_data.remove(i);
        adapter.notifyDataSetChanged();
    }
}
