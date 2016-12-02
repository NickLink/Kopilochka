package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.MenuItem;
import ua.kiev.foxtrot.kopilochka.utils.Utils;

/**
 * Created by NickNb on 29.09.2016.
 */
public class SliderMenuAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    //private Typeface calibri_bold;
    private List<MenuItem> menuItemList;

    public SliderMenuAdapter(Context context) {
        this.context = context;
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
        refreshData();
    }

    public void refreshData(){
        menuItemList = new ArrayList<MenuItem>();
        menuItemList.add(new MenuItem(context.getString(R.string.menu_start), //Start
                AppContr.getSharPref().getBoolean(Const.new_in_group, true),
                Utils.getResourceId(context, "logo_with_stroke"))); // f5_head
        menuItemList.add(new MenuItem(context.getString(R.string.menu_notification), //Notices
                AppContr.getSharPref().getBoolean(Const.new_in_notice, true),
                Utils.getResourceId(context, "letter")));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_action), //Actions
                AppContr.getSharPref().getBoolean(Const.new_in_action, true),
                Utils.getResourceId(context, "promos")));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_history), //Serials
                false,
                Utils.getResourceId(context, "clock")));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_data), //Login
                false,
                Utils.getResourceId(context, "id_card")));
        menuItemList.add(new MenuItem(context.getString(R.string.menu_wtf), //Question
                false,
                Utils.getResourceId(context, "help")));
        notifyDataSetChanged();
    }

    public void setViewed(int id) {
        menuItemList.get(id).setViewed(false);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuItemList.size();
    }

    @Override
    public MenuItem getItem(int i) {
        return menuItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.menu_list_item, viewGroup, false);

        ImageView menu_item_icon = (ImageView) convertView.findViewById(R.id.menu_item_icon);
        //ImageView menu_item_special = (ImageView)convertView.findViewById(R.id.menu_item_special);
        TextView menu_item_title = (TextView) convertView.findViewById(R.id.menu_item_title);
        View red_dot = (View) convertView.findViewById(R.id.red_dot);

        menu_item_icon.setImageResource(getItem(i).getResource());
        menu_item_title.setText(getItem(i).getName());
        if (getItem(i).getViewed())
            red_dot.setVisibility(View.VISIBLE);
        else
            red_dot.setVisibility(View.GONE);

        menu_item_title.setTypeface(AppContr.calibri_bold);

/*        switch (i){
            case 0:
                menu_item_icon.setBackgroundResource(R.drawable.f5_head);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_start);
                if(AppContr.getSharPref().getBoolean(Const.new_in_group, true)){
                    red_dot.setVisibility(View.VISIBLE);
                } else {
                    red_dot.setVisibility(View.GONE);
                }
                break;

            case 1:
                menu_item_icon.setBackgroundResource(R.drawable.letter);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_notification);
                if(AppContr.getSharPref().getBoolean(Const.new_in_notice, true)){
                    red_dot.setVisibility(View.VISIBLE);
                } else {
                    red_dot.setVisibility(View.GONE);
                }
                break;

            case 2:
                menu_item_icon.setBackgroundResource(R.drawable.promos);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_action);
                if(AppContr.getSharPref().getBoolean(Const.new_in_action, true)){
                    red_dot.setVisibility(View.VISIBLE);
                } else {
                    red_dot.setVisibility(View.GONE);
                }
                break;

            case 3:
                menu_item_icon.setBackgroundResource(R.drawable.clock);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_history);
                break;

            case 4:
                menu_item_icon.setBackgroundResource(R.drawable.id_card);
                menu_item_special.setBackgroundResource(0);
                menu_item_title.setText(R.string.menu_data);
                break;

            case 5:
                menu_item_icon.setBackgroundResource(R.drawable.help);
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


        }*/


        return convertView;
    }
}