package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.adapters.Serials_ListView_Adapter;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;
import ua.kiev.foxtrot.kopilochka.data.Model;
import ua.kiev.foxtrot.kopilochka.data.Post_SN;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.http.Connect;
import ua.kiev.foxtrot.kopilochka.http.Methods;
import ua.kiev.foxtrot.kopilochka.interfaces.Delete_Serial;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;
import ua.kiev.foxtrot.kopilochka.ui.FontCache;
import ua.kiev.foxtrot.kopilochka.utils.Dialogs;
import ua.kiev.foxtrot.kopilochka.utils.StringTools;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 07.10.2016.
 */
public class Action_P3 extends BaseFragment implements Delete_Serial, HttpRequest {
    private long mLastClickTime = 0;
    Interfaces interfaces;
    OnBackPress onBackPress;

    ListView serial_numbers_list;
    Serials_ListView_Adapter adapter;
    View add_footer;
    private int action_id, model_id;
    private String serials, title, edited_serials;
    DB db = AppContr.db;
    Model model;
    Action action;
    Button action_register_model_button;
    ProgressDialog pDialog;
    boolean edit_mode = false;
    private Typeface calibri, calibri_bold;

    public static Action_P3 newInstance(int action_id, int model_id) {
        Action_P3 fragment = new Action_P3();
        Bundle args = new Bundle();
        args.putInt(Const.action_id, action_id);
        args.putInt(Const.model_id, model_id);
        fragment.setArguments(args);
        return fragment;
    }

    public static Action_P3 newInstance(int action_id, int model_id, String serials) {
        Action_P3 fragment = new Action_P3();
        Bundle args = new Bundle();
        args.putInt(Const.action_id, action_id);
        args.putInt(Const.model_id, model_id);
        args.putString(Const.serials, serials);
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
            Dialogs.ShowDialog(getActivity(), "Error", "Action & model error", "OK");
            return null;
        }

        calibri = FontCache.get("fonts/calibri.ttf", getActivity());
        calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getActivity());

        serials = getArguments().getString(Const.serials, null);
        if(serials != null){
            edited_serials = serials;
            Post_SN edit = db.getPostSNbyData(action_id, model_id, serials);
            model = new Model();
            model.setModel_name(edit.getModel_name());
            model.setModel_id(edit.getModel_id());
            model.setModel_action(edit.getAction_id());
            model.setModel_points(edit.getModel_points());
            model.setModel_sn_count(edit.getSerials().size());

            action = new Action();
            action.setAction_name(edit.getAction_name());
            action.setAction_type_id(edit.getAction_type_id());
            action.setAction_date_to(edit.getAction_date_to());
            adapter = new Serials_ListView_Adapter(getActivity(), edit.getSerials(), Action_P3.this, interfaces);
            edit_mode = true;
            title = getString(R.string.edit_title);

        } else {
            action = db.getActionById(action_id);
            model = db.getModelByIds(action_id, model_id);
            adapter = new Serials_ListView_Adapter(getActivity(), model.getModel_sn_count(), Action_P3.this, interfaces);
            title = getString(R.string.menu_action);
        }


        TextView action_name_tv = (TextView)rootView.findViewById(R.id.action_name);
        TextView model_name_tv = (TextView)rootView.findViewById(R.id.model_name);
        TextView bonus_points_tv = (TextView)rootView.findViewById(R.id.action_p3_bonus_points);
        TextView action_p3_info_text = (TextView)rootView.findViewById(R.id.action_p3_info_text);

        add_footer = inflater.inflate(R.layout.frag_action_p3_list_footer, null);
        //Button add_new_serial = (Button)add_footer.findViewById(R.id.add_serial_field);
        action_register_model_button = (Button)add_footer.findViewById(R.id.action_register_model_button);
        action_register_model_button.setEnabled(false);

        serial_numbers_list = (ListView)rootView.findViewById(R.id.serial_numbers_list);
        serial_numbers_list.addFooterView(add_footer);

        Log.v("", "S1 Count = " + model.getModel_sn_count());

        //new ArrayList<BBS_News>() === serials_data
        serial_numbers_list.setAdapter(adapter);
        serial_numbers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                ShowSerialDialog(i);
            }
        });
        action_name_tv.setText(action.getAction_name());
        model_name_tv.setText(model.getModel_name());
        bonus_points_tv.setText(String.valueOf(model.getModel_points()));

        action_name_tv.setTypeface(calibri_bold);
        model_name_tv.setTypeface(calibri_bold);
        bonus_points_tv.setTypeface(calibri_bold);
        action_p3_info_text.setTypeface(calibri);
        action_register_model_button.setTypeface(calibri_bold);

        action_register_model_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                goRegister();
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
        menu_item_title.setText(title);
        menu_item_title.setTypeface(calibri_bold);
        return rootView;
    }

    void ShowSerialDialog(final int position){
        final Dialog dialog = new Dialog(getActivity(), R.style.Error_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_action_serial);

        TextView dialog_title = (TextView)dialog.findViewById(R.id.dialog_title);
        final EditText serial = (EditText)dialog.findViewById(R.id.editText);
        final Button ok = (Button) dialog.findViewById(R.id.action_ok);
        final Button scan = (Button) dialog.findViewById(R.id.action_scan);
        final Button delete = (Button) dialog.findViewById(R.id.action_delete);

        serial.setText(adapter.getSerials_data().get(position));
        if(serial.getText().toString().length() != 0) {
            ok.setEnabled(true);
            delete.setEnabled(true);
        } else {
            ok.setEnabled(false);
            delete.setEnabled(false);
        }

        serial.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if(serial.getText().toString().trim().length() != 0) {
                    ok.setEnabled(true);
                    delete.setEnabled(true);
                } else {
                    ok.setEnabled(false);
                    delete.setEnabled(false);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                adapter.getSerials_data().set(position,serial.getText().toString().trim());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                interfaces.ScannStart(position);
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                serial.setText("");
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                checkData();
            }
        });

        serial.setHint(getString(R.string.hist_sn_imei_hint));
        dialog_title.setTypeface(calibri_bold);
        serial.setTypeface(calibri_bold);
        ok.setTypeface(calibri_bold);

        dialog.show();

    }

    void checkData(){
        boolean is_data_ok = false;
        for (int i = 0; i < adapter.getSerials_data().size() ; i++){
            if(adapter.getSerials_data().get(i) == null || adapter.getSerials_data().get(i).trim().isEmpty()){
                is_data_ok = false;
                break;
            } else {
                is_data_ok = true;
            }
        }
        if(is_data_ok){
            action_register_model_button.setEnabled(true);
        }

    }

    void goRegister(){
        Post_SN item = createItem();
        if(edit_mode){
            editPostSN_toBase(item, edited_serials);
        } else {
            savePostSN_toBase(item);
        }
        if(Connect.isOnline(getActivity())){

            pDialog = new ProgressDialog(getActivity());
            pDialog.show();
            Methods.post_SN(getActivity(), item, this);

        } else {
            Dialogs.ShowInternetDialog(getActivity(), getString(R.string.hist_reg_with_inet));
            ClearOrFinish();
            //adapter.getSerials_data().clear();
        }

    }

    private Post_SN createItem(){
        Post_SN item = new Post_SN();
        item.setAction_id(model.getModel_action());
        item.setAction_name(action.getAction_name());
        item.setModel_id(model.getModel_id());
        item.setModel_name(model.getModel_name());
        item.setAction_date_to(action.getAction_date_to());
        item.setAction_type_id(action.getAction_type_id());
        item.setModel_points(model.getModel_points());
        item.setSerials(adapter.getSerials_data());
        item.setReg_date(System.currentTimeMillis()/1000);
        item.setReg_status(Const.reg_status_await);
        item.setFail_reason("");
        return item;
    }

    private long savePostSN_toBase(Post_SN item){
        long position = db.addPostSN(item);
        return position;
    }

    private boolean editPostSN_toBase(Post_SN item, String old_serials){
        return db.setSerials_Post_SN_item(item, old_serials);
    }

    @Override
    public void delete_serial(int i) {
//        adapter.getSerials_data().remove(i);
//        //serials_data.remove(i);
//        adapter.notifyDataSetChanged();
    }

    public void updateScanCode(int id, String code){
        adapter.getSerials_data().set(id, code);
        //serials_data.get(id).setTitle(code);
        adapter.notifyDataSetChanged();
        ShowSerialDialog(id);
    }

    @Override
    public void http_result(int type, String result) {
        if( pDialog != null && pDialog.isShowing() )
            pDialog.dismiss();

        try {
            Log.v("", "SSS http_result = " + result);
            JSONObject data = new JSONObject(result);
            if(data.has(Const.JSON_Error)){
                switch (data.getJSONObject(Const.JSON_Error).getInt(Const.code)){
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
                        Dialogs.ShowRegDialog(getActivity(), false);
                        break;
                    case 4:
                        Dialogs.ShowRegDialog(getActivity(), false);
                        break;
                    case 5:
                        Dialogs.ShowRegDialog(getActivity(), false);
                        break;
                    case 6:
                        Dialogs.ShowRegDialog(getActivity(), false);
                        break;
                }
                Post_SN error_item = createItem();
                error_item.setReg_status(Const.reg_status_error);
                error_item.setFail_reason(data.getJSONObject(Const.JSON_Error).getString(Const.comment));
                db.setStatus_Post_SN_item(error_item);
                ClearOrFinish();

            } else {
                if(data.has(Const.ok) && data.getInt(Const.ok) == 1){
                    Post_SN received = new Post_SN();
                    received.setAction_id(data.getInt(Const.action_id));
                    received.setModel_id(data.getInt(Const.model_id));
                    received.setReg_date(Utils.getMillisFromDate(data.getString(Const.date)));
                    received.setReg_status(Const.reg_status_ok);
                    received.setSerials(StringTools.ListFromString(data.getString(Const.serials)));
                    if(db.setStatus_Post_SN_item(received)){
                        //model successfully registered
                        Dialogs.ShowRegDialog(getActivity(), true);
                        ClearOrFinish();
                        //adapter.getSerials_data().clear();
                    } else {
                        //something wrong with DB

                    }

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.v("", "2121 http_result= " + result);
    }

    void ClearOrFinish(){
        if (edit_mode){
            onBackPress.onBackPressed();
        } else {
            for (int i =0; i<adapter.getSerials_data().size();i++) {
                adapter.getSerials_data().set(i, "");
            }
            adapter.notifyDataSetChanged();
            action_register_model_button.setEnabled(false);
        }
    }

    @Override
    public void http_error(int type, String error) {
        if( pDialog != null && pDialog.isShowing() )
            pDialog.dismiss();
        Log.v("", "2121 http_error= " + error);
    }
}
