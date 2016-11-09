package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.data.Model;

/**
 * Created by NickNb on 07.10.2016.
 */
public class Models_ListView_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<Model> models_data;
//    ImageLoader imageLoader = AppContr.getInstance().getImageLoader();
    private LayoutInflater inflater;

    public Models_ListView_Adapter(Context context, ArrayList<Model> models_data) {
        this.context = context;
        this.models_data = models_data;
    }

    @Override
    public int getCount() {
        return models_data.size();
    }

    @Override
    public Model getItem(int position) { //Object
        // TODO Auto-generated method stub
        return models_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.frag_action_p2_list_item, viewGroup, false);

        TextView action_list_item_name = (TextView)convertView.findViewById(R.id.action_list_item_name);
        //TextView action_list_item_days = (TextView)convertView.findViewById(R.id.action_list_item_days);
        TextView action_list_item_points = (TextView)convertView.findViewById(R.id.action_list_item_points);

        Model feed = models_data.get(position);

        action_list_item_name.setText(feed.getModel_name());
//        if(feed.getModel_days_left() != 0)
//        action_list_item_days.setText(String.valueOf(feed.getModel_days_left()));
        action_list_item_points.setText(String.valueOf(feed.getModel_points()));


        return convertView;
    }
}
