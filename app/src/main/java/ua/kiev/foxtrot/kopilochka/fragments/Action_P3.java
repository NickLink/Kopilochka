package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Serials_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.data.Model;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.interfaces.Delete_Serial;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

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
    private int action_id, model_id;
    DB db;
    Model model;

    public static Action_P3 newInstance(int action_id, int model_id) {
        Action_P3 fragment = new Action_P3();
        Bundle args = new Bundle();
        args.putInt(Const.action_id, action_id);
        args.putInt(Const.model_id, model_id);
        fragment.setArguments(args);
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
        action_id = getArguments().getInt(Const.action_id, 0);
        model_id = getArguments().getInt(Const.model_id, 0);
        if(action_id == 0 || model_id == 0){
            //Error getting data
            Utils.ShowInputErrorDialog(getActivity(), "Error", "Action & model error", "OK");
            return null;
        }
        db = new DB(getActivity());
        model = db.getModelByIds(action_id, model_id);

        TextView action_name = (TextView)rootView.findViewById(R.id.action_name);
        TextView model_name = (TextView)rootView.findViewById(R.id.model_name);
        TextView bonus_points = (TextView)rootView.findViewById(R.id.action_p3_bonus_points);

        add_footer = inflater.inflate(R.layout.frag_action_p3_list_footer, null);
        Button add_new_serial = (Button)add_footer.findViewById(R.id.add_serial_field);
        Button action_register_model_button = (Button)add_footer.findViewById(R.id.action_register_model_button);

        serial_numbers_list = (ListView)rootView.findViewById(R.id.serial_numbers_list);
        serial_numbers_list.addFooterView(add_footer);

        adapter = new Serials_ListView_Adapter(getActivity(), new ArrayList<BBS_News>(), Action_P3.this, interfaces);
        //new ArrayList<BBS_News>() === serials_data
        serial_numbers_list.setAdapter(adapter);
        serial_numbers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowSerialDialog(i);
            }
        });
        action_name.setText(model.getModel_name());
        model_name.setText(model.getModel_name());
        bonus_points.setText(String.valueOf(model.getModel_points()));

        add_new_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Adding", Toast.LENGTH_SHORT).show();
                //serials_data.add(new BBS_News());
                adapter.getSerials_data().add(new BBS_News());
                ShowSerialDialog(adapter.getSerials_data().size()-1);

                //adapter.notifyDataSetChanged();
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

    void ShowSerialDialog(final int position){
        final Dialog dialog = new Dialog(getActivity(), R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_action_serial);

        final EditText serial = (EditText)dialog.findViewById(R.id.editText);
        Button ok = (Button) dialog.findViewById(R.id.action_ok);
        Button scan = (Button) dialog.findViewById(R.id.action_scan);
        Button delete = (Button) dialog.findViewById(R.id.action_delete);

        serial.setText(adapter.getSerials_data().get(position).getTitle());

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getSerials_data().get(position).setTitle(serial.getText().toString());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.ScannStart(position);
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getSerials_data().remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public void delete_serial(int i) {
        adapter.getSerials_data().remove(i);
        //serials_data.remove(i);
        adapter.notifyDataSetChanged();
    }

    public void updateScanCode(int id, String code){
        adapter.getSerials_data().get(id).setTitle(code);
        //serials_data.get(id).setTitle(code);
        adapter.notifyDataSetChanged();
        ShowSerialDialog(id);
    }
}
