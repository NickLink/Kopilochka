package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kiev.foxtrot.kopilochka.R;

/**
 * Created by NickNb on 29.09.2016.
 */
public class SliderMenuAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    public SliderMenuAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.menu_list_item, viewGroup, false);

        ImageView menu_item_icon = (ImageView)convertView.findViewById(R.id.menu_item_icon);
        ImageView menu_item_special = (ImageView)convertView.findViewById(R.id.menu_item_special);
        TextView menu_item_title = (TextView)convertView.findViewById(R.id.menu_item_title);

        switch (i){
            case 0:
                menu_item_icon.setBackgroundResource(R.drawable.icon_menu_start);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_start);
                break;

            case 1:
                menu_item_icon.setBackgroundResource(R.drawable.icon_menu_notif);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_notification);
                break;

            case 2:
                menu_item_icon.setBackgroundResource(R.drawable.icon_menu_action);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_action);
                break;

            case 3:
                menu_item_icon.setBackgroundResource(R.drawable.icon_menu_history);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_history);
                break;

            case 4:
                menu_item_icon.setBackgroundResource(R.drawable.icon_menu_data);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_data);
                break;

            case 5:
                menu_item_icon.setBackgroundResource(R.drawable.icon_menu_wtf);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_wtf);
                break;

//            case 6:
//                menu_item_icon.setBackgroundResource(R.drawable.menu_icon_profile);
//                menu_item_special.setBackgroundResource(0);
//                menu_item_title.setText(R.string.menu_profile);
//                break;
//
//            case 7:
//                menu_item_icon.setBackgroundResource(R.drawable.menu_icon_terms);
//                menu_item_special.setBackgroundResource(R.drawable.menu_icon_new_terms);
//                menu_item_title.setText(R.string.menu_terms);
//                break;




        }


        return convertView;
    }
}