package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.interfaces.Delete_Serial;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 07.10.2016.
 */
public class Serials_ListView_Adapter extends BaseAdapter {

    private Context context;
    private List<String> serials_data;
    private LayoutInflater inflater;
    private Delete_Serial delete_click = null;
    private Interfaces interfaces;
    //private int sn_count;
    TextView scan_result;
    Button scan_button;
    Button delete_button;
    //private Typeface calibri_bold;

    public Serials_ListView_Adapter(Context context, int sn_count,
                                         Delete_Serial delete_click, Interfaces interfaces) {
        this.context = context;
        this.delete_click = delete_click;
        this.interfaces = interfaces;
        this.serials_data = new ArrayList<String>(sn_count);
        while(serials_data.size() < sn_count) serials_data.add("");
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
    }

    public Serials_ListView_Adapter(Context context, List<String> arrayList,
                                    Delete_Serial delete_click, Interfaces interfaces) {
        this.context = context;
        this.delete_click = delete_click;
        this.interfaces = interfaces;
        this.serials_data = arrayList;
    }

    public List<String> getSerials_data(){
        return serials_data;
    }

    @Override
    public int getCount() {
        return serials_data.size();
    }

    @Override
    public String getItem(int position) { //Object
        return serials_data.get(position);
    }

    public void setItem(int position, String data){
        serials_data.set(position, data);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.frag_action_p3_list_item, viewGroup, false);

        scan_result = (TextView)convertView.findViewById(R.id.scan_result);
//        scan_button = (Button)convertView.findViewById(R.id.scan_button);
//        delete_button = (Button)convertView.findViewById(R.id.delete_button);


//        scan_result.setFocusable(true);
//
//        scan_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (!hasFocus) {
//                    // code to execute when EditText loses focus
//                    getItem(position).setTitle(scan_result.getText().toString());
//                }
//            }
//        });
//
//        scan_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                scan_result.setFocusable(false);
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "SCAN position=" + position, Toast.LENGTH_SHORT).show();
//                        interfaces.ScannStart(position);
//                        scan_result.setFocusableInTouchMode(true);
//                    }
//                }, 200);
//            }
//        });
//
//        delete_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "Delete position=" + position, Toast.LENGTH_SHORT).show();
//                        delete_click.delete_serial(position);
//                    }
//                }, 200);
//            }
//        });
//
        if(!Utils.notNull_orEmpty(getItem(position))) {
            setItem(position, ""); //context.getString(R.string.hist_sn_imei_hint)
        }
        scan_result.setText(getItem(position));
        scan_result.setTypeface(AppContr.calibri_bold);
        return convertView;
    }
}
