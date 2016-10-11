package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppController;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;

/**
 * Created by NickNb on 11.10.2016.
 */
public class Notif_ListView_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<BBS_News> notif_data;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;

    public Notif_ListView_Adapter(Context context, ArrayList<BBS_News> _news_data) {
        this.context = context;
        this.notif_data = _news_data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return notif_data.size();
    }

    @Override
    public BBS_News getItem(int position) { //Object
        // TODO Auto-generated method stub
        return notif_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.frag_notif_p1_list_item, viewGroup, false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView imageView = (NetworkImageView)convertView.findViewById(R.id.notif_listview_item_image);
        TextView title = (TextView)convertView.findViewById(R.id.notif_listview_item_title);
        TextView text = (TextView)convertView.findViewById(R.id.notif_listview_item_text);
        //TextView author = (TextView)convertView.findViewById(R.id.textView3);

        BBS_News feed = notif_data.get(position);

        imageView.setImageUrl(feed.getUrlToImage(), imageLoader);
        title.setText(feed.getTitle());
        text.setText(feed.getDescription());
        //author.setText(feed.getAuthor());

        return convertView;
    }
}
