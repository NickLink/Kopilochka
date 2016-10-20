package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Action;

/**
 * Created by NickNb on 06.10.2016.
 */
public class Action_ListView_Adapter extends BaseAdapter {

    private Context context;
    //private ArrayList<BBS_News> action_data;
    private ArrayList<Action> _action_data;
    ImageLoader imageLoader = AppContr.getInstance().getImageLoader();
    private LayoutInflater inflater;

    public Action_ListView_Adapter(Context context, ArrayList<Action> action_data) {
        this.context = context;
        this._action_data = action_data;
    }

    public ArrayList<Action> getAction_data(){
        return _action_data;
    }

    public void setAction_data(ArrayList<Action> data){
        this._action_data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _action_data.size();
    }

    @Override
    public Action getItem(int position) { //Object
        // TODO Auto-generated method stub
        return _action_data.get(position);
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
            convertView = inflater.inflate(R.layout.frag_action_p1_list_item, viewGroup, false);

        if (imageLoader == null)
            imageLoader = AppContr.getInstance().getImageLoader();

        //NetworkImageView imageView = (NetworkImageView)convertView.findViewById(R.id.imageView);
        TextView action_name = (TextView)convertView.findViewById(R.id.action_name);
        TextView action_count = (TextView)convertView.findViewById(R.id.action_count);

        //BBS_News feed = action_data.get(position);

        //imageView.setImageUrl(feed.getUrlToImage(), imageLoader);
        action_name.setText(getItem(position).getAction_name());
        action_count.setText(String.valueOf(getItem(position).getModels().size()));


        return convertView;
    }
}
