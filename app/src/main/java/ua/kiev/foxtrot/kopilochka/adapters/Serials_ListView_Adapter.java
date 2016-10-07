package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.interfaces.Delete_Serial;

/**
 * Created by NickNb on 07.10.2016.
 */
public class Serials_ListView_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<BBS_News> serials_data;
    private LayoutInflater inflater;
    private Delete_Serial delete_click = null;
    private Interfaces interfaces;

    public Serials_ListView_Adapter(Context context, ArrayList<BBS_News> serials_data,
                                    Delete_Serial delete_click, Interfaces interfaces) {
        this.context = context;
        this.serials_data = serials_data;
        this.delete_click = delete_click;
        this.interfaces = interfaces;
    }

    @Override
    public int getCount() {
        return serials_data.size();
    }

    @Override
    public BBS_News getItem(int position) { //Object
        // TODO Auto-generated method stub
        return serials_data.get(position);
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

        final BBS_News item = getItem(position);

        final EditText scan_result = (EditText)convertView.findViewById(R.id.scan_result);
        Button scan_button = (Button)convertView.findViewById(R.id.scan_button);
        Button delete_button = (Button)convertView.findViewById(R.id.delete_button);

        scan_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    item.setTitle(scan_result.getText().toString());
                }
            }
        });

        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "SCAN position=" + position, Toast.LENGTH_SHORT).show();
                        interfaces.ScannStart();
                    }
                }, 200);
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Delete position=" + position, Toast.LENGTH_SHORT).show();
                        delete_click.delete_serial(position);
                    }
                }, 200);
            }
        });

        BBS_News feed = serials_data.get(position);
        scan_result.setText(feed.getTitle());

        return convertView;
    }
}
