package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Notice;

/**
 * Created by NickNb on 11.10.2016.
 */
public class Notif_ListView_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Notice> _notif_data;
    //ImageLoader imageLoader = AppContr.getInstance().getImageLoader();
    private LayoutInflater inflater;
    //private Typeface calibri, calibri_bold;

    public Notif_ListView_Adapter(Context context, ArrayList<Notice> data) {
        this.context = context;
        this._notif_data = data;
        //calibri = FontCache.get("fonts/calibri.ttf", context);
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
    }

    public ArrayList<Notice> getNotice_data(){
        return _notif_data;
    }

    public void setNotice_data(ArrayList<Notice> data){
        this._notif_data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return _notif_data.size();
    }

    @Override
    public Notice getItem(int position) { //Object
        // TODO Auto-generated method stub
        return _notif_data.get(position);
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

        LinearLayout notyLayout = (LinearLayout)convertView.findViewById(R.id.notif_listview_item_layout);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.notif_listview_item_icon);
        TextView title = (TextView)convertView.findViewById(R.id.notif_listview_item_title);
        TextView text = (TextView)convertView.findViewById(R.id.notif_listview_item_text);

        title.setTextColor(context.getResources().getColor(R.color.my_blue_color));

        Notice feed = _notif_data.get(position);
        if(feed.getNotice_type_id() == 1){
            imageView.setImageResource(R.drawable.f5_head_1);
            notyLayout.setBackgroundResource(R.drawable.message_green);
        } else if(feed.getNotice_type_id() == 2){
            imageView.setImageResource(R.drawable.f5_head_2);
            notyLayout.setBackgroundResource(R.drawable.message_grey);
        } else if(feed.getNotice_type_id() == 3){
            imageView.setImageResource(R.drawable.f5_head_3);
            notyLayout.setBackgroundResource(R.drawable.message_red);
        } else if(feed.getNotice_type_id() == 4){
            imageView.setImageResource(R.drawable.f5_head_4);
            notyLayout.setBackgroundResource(R.drawable.message_red);
            title.setTextColor(context.getResources().getColor(R.color.my_red_color));
        }
        title.setText(feed.getNotice_name());
        text.setText(feed.getNotice_text());
        title.setTypeface(AppContr.calibri_bold);
        text.setTypeface(AppContr.calibri);

        return convertView;
    }
}
