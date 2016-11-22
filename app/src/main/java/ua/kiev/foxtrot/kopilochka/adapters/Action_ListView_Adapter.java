package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.Const;
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
    //ImageLoader imageLoader = AppContr.getInstance().getImageLoader();
    private LayoutInflater inflater;
    //private Typeface calibri_bold;

    public Action_ListView_Adapter(Context context, ArrayList<Action> action_data) {
        this.context = context;
        this._action_data = action_data;
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
    }

    public void setActionViewed(int position){
        this._action_data.get(position).setViewed(Const.viewed_yes);
        notifyDataSetChanged();
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
            convertView = inflater.inflate(R.layout.frag_start_p1_list_item, viewGroup, false); //frag_action_p1_list_item

//        if (imageLoader == null)
//            imageLoader = AppContr.getInstance().getImageLoader();

        //NetworkImageView imageView = (NetworkImageView)convertView.findViewById(R.id.imageView);
        TextView action_name = (TextView)convertView.findViewById(R.id.title);
        TextView action_count = (TextView)convertView.findViewById(R.id.count);
        View red_dot  = (View)convertView.findViewById(R.id.red_dot);
        //BBS_News feed = action_data.get(position);

        //imageView.setImageUrl(feed.getUrlToImage(), imageLoader);
        action_name.setText(getItem(position).getAction_name());
        action_count.setText(String.valueOf(getItem(position).getModels().size()));
        action_name.setTypeface(AppContr.calibri_bold);
        action_count.setTypeface(AppContr.calibri_bold);

        if(getItem(position).getViewed() == Const.viewed_no){
            red_dot.setVisibility(View.VISIBLE);
        } else red_dot.setVisibility(View.GONE);

        return convertView;
    }
}
