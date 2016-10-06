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
import ua.kiev.foxtrot.kopilochka.app.AppController;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;

/**
 * Created by NickNb on 06.10.2016.
 */
public class Action_ListView_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<BBS_News> action_data;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;

    public Action_ListView_Adapter(Context context, ArrayList<BBS_News> action_data) {
        this.context = context;
        this.action_data = action_data;
    }

    @Override
    public int getCount() {
        return action_data.size();
    }

    @Override
    public BBS_News getItem(int position) { //Object
        // TODO Auto-generated method stub
        return action_data.get(position);
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
            imageLoader = AppController.getInstance().getImageLoader();

        //NetworkImageView imageView = (NetworkImageView)convertView.findViewById(R.id.imageView);
        TextView action_name = (TextView)convertView.findViewById(R.id.action_name);
        TextView action_count = (TextView)convertView.findViewById(R.id.action_count);

        BBS_News feed = action_data.get(position);

        //imageView.setImageUrl(feed.getUrlToImage(), imageLoader);
        action_name.setText(feed.getTitle());
        action_count.setText(String.valueOf(position));


        return convertView;
    }
}
