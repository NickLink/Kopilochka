package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.data.Post_SN;
import ua.kiev.foxtrot.kopilochka.utils.StringTools;

/**
 * Created by NickNb on 31.10.2016.
 */
public class History_ExpList_Adapter extends BaseExpandableListAdapter {

    private Context mContext;
//    List<Post_SN> success_reg;
//    List<Post_SN> error_reg;
//    List<Post_SN> await_reg;
    List<List<Post_SN>> fullArray = new ArrayList<List<Post_SN>>();
    LayoutInflater inflater;
    //private Typeface calibri, calibri_bold;

    public History_ExpList_Adapter(Context context, List<List<Post_SN>> fullArray){
//                                   List<Post_SN> success_reg,
//                                   List<Post_SN> error_reg,
//                                   List<Post_SN> await_reg) {
        this.mContext = context;
//        this.success_reg = success_reg;
//        this.error_reg = error_reg;
//        this.await_reg = await_reg;
        this.fullArray = fullArray;
        //calibri = FontCache.get("fonts/calibri.ttf", context);
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
    }

    private static class ItemHolder
    {
        TextView model_name;
        TextView model_serial;
        LinearLayout models_serial;
    }

    public void setExpListData(List<List<Post_SN>> setdata){
        this.fullArray = setdata;
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).size();
    }

    @Override
    public List<Post_SN> getGroup(int groupPosition) {
        return fullArray.get(groupPosition);
//        switch (groupPosition) {
//            case 0:
//                return success_reg;
//            case 1:
//                return error_reg;
//            case 2:
//                return await_reg;
//            default:
//                return null;
//        }
    }

    @Override
    public Post_SN getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
            convertView = inflater.inflate(R.layout.frag_history_p1_list_group_exp, null);
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
            convertView = inflater.inflate(R.layout.frag_history_p1_list_group, null);
        }

        TextView group_count = (TextView)convertView.findViewById(R.id.group_count);
        TextView group_title = (TextView)convertView.findViewById(R.id.group_title);

            switch (groupPosition) {
                case 0:
                    group_count.setText(String.valueOf(getChildrenCount(groupPosition))); //success_reg.size()
                    group_title.setText(mContext.getString(R.string.hist_title_1));
                    break;
                case 1:
                    group_count.setText(String.valueOf(getChildrenCount(groupPosition))); //error_reg.size()
                    group_title.setText(mContext.getString(R.string.hist_title_2));
                    break;
                case 2:
                    group_count.setText(String.valueOf(getChildrenCount(groupPosition))); //await_reg.size()
                    group_title.setText(mContext.getString(R.string.hist_title_3));
                    break;
            }
        group_count.setTypeface(AppContr.calibri_bold);
        group_title.setTypeface(AppContr.calibri_bold);
            return convertView;
        }

        @Override
        public View getChildView ( int groupPosition, int childPosition, boolean isLastChild,
        View convertView, ViewGroup parent){
            Post_SN post_sn = getChild(groupPosition, childPosition);
            Log.v("TAG", "AAA post_sn = " + StringTools.StringFromList(post_sn.getSerials()));
            ItemHolder itemHolder;
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.frag_history_p1_list_item, parent, false);
                itemHolder = new ItemHolder();
                itemHolder.model_name = (TextView) row.findViewById(R.id.model_name);
                itemHolder.model_serial = (TextView) row.findViewById(R.id.model_serial);
                //itemHolder.models_serial = (LinearLayout)row.findViewById(R.id.serial_numbers_list_layout);
                row.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) row.getTag();
            }

            itemHolder.model_name.setText(post_sn.getModel_name());
            String serials_string = "";
            StringBuilder sb = new StringBuilder();
            for (int i = 0 ; i < post_sn.getSerials().size(); i++){
                sb.append(post_sn.getSerials().get(i));
                if(post_sn.getSerials().size() - i > 1) sb.append(System.getProperty ("line.separator"));
            }

            itemHolder.model_serial.setText(sb.toString());
            itemHolder.model_serial.setSingleLine(false);
            itemHolder.model_serial.setLines(post_sn.getSerials().size());
//            for (int i = 0 ; i < post_sn.getSerials().size(); i++){
//                TextView temp = new TextView(mContext);
//                itemHolder.models_serial.addView(temp);
//                temp.setText(post_sn.getSerials().get(i));
//            }
            itemHolder.model_name.setTypeface(AppContr.calibri_bold);
            itemHolder.model_serial.setTypeface(AppContr.calibri);

            return row;
        }

        @Override
        public boolean isChildSelectable ( int groupPosition, int childPosition){
            return true;
        }

}