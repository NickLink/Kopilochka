package ua.f5.kopilochka.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import ua.f5.kopilochka.R;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Charge;
import ua.f5.kopilochka.data.FinInfo;
import ua.f5.kopilochka.data.Payment;
import ua.f5.kopilochka.utils.Utils;

/**
 * Created by NickNb on 26.10.2016.
 */
public class FinInfo_ExpList_Adapter extends BaseExpandableListAdapter {

    private FinInfo mFinInfo;
    private Context mContext;
    private LayoutInflater inflater;
    //private Typeface calibri, calibri_bold;

    public FinInfo_ExpList_Adapter(Context context, FinInfo finInfo) {
        mContext = context;
        mFinInfo = finInfo;
        //calibri = FontCache.get("fonts/calibri.ttf", context);
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", context);
    }

    private static class ItemHolder {
        TextView date;
        TextView action;
        TextView sum;
        TextView about;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0)
            return mFinInfo.getCharges().size();
        else
            return mFinInfo.getPayments().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition == 0)
            return mFinInfo.getCharges();
        else
            return mFinInfo.getPayments();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0)
            return mFinInfo.getCharges().get(childPosition);
        else
            return mFinInfo.getPayments().get(childPosition);
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
        if (groupPosition == 0) {
            if (isExpanded) {
                //Изменяем что-нибудь, если текущая Group раскрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h1_exp, null);
                TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
                TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
                TextView textView4 = (TextView) convertView.findViewById(R.id.textView4);
                textView2.setTypeface(AppContr.calibri);
                textView3.setTypeface(AppContr.calibri);
                textView4.setTypeface(AppContr.calibri);
            } else {
                //Изменяем что-нибудь, если текущая Group скрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h1, null);
            }
        } else {
            if (isExpanded) {
                //Изменяем что-нибудь, если текущая Group раскрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h2_exp, null);
                TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
                TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
                TextView textView4 = (TextView) convertView.findViewById(R.id.textView4);
                TextView textView5 = (TextView) convertView.findViewById(R.id.textView5);
                textView2.setTypeface(AppContr.calibri);
                textView3.setTypeface(AppContr.calibri);
                textView4.setTypeface(AppContr.calibri);
                textView5.setTypeface(AppContr.calibri);
            } else {
                //Изменяем что-нибудь, если текущая Group скрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h2, null);
            }
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setTypeface(AppContr.calibri_bold);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ItemHolder itemHolder = new ItemHolder();
        View row = convertView;
        if (inflater == null)
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (row == null) {
        //itemHolder = new ItemHolder();
        if (groupPosition == 0) {
            //Log.v("TAG", "!!! groupPosition == 0 = " + groupPosition);
            row = inflater.inflate(R.layout.frag_data_p1_extra_i1, parent, false);
            itemHolder.date = (TextView) row.findViewById(R.id.fininfo_charge_data);
            itemHolder.action = (TextView) row.findViewById(R.id.fininfo_charge_actionname);
            itemHolder.sum = (TextView) row.findViewById(R.id.fininfo_charge_ammount);
        } else {
            //Log.v("TAG", "!!! groupPosition == 1 = " + groupPosition);
            row = inflater.inflate(R.layout.frag_data_p1_extra_i2, parent, false);
            itemHolder.date = (TextView) row.findViewById(R.id.fininfo_payment_data);
            itemHolder.action = (TextView) row.findViewById(R.id.fininfo_payment_actionname);
            itemHolder.sum = (TextView) row.findViewById(R.id.fininfo_payment_ammount);
            itemHolder.about = (TextView) row.findViewById(R.id.fininfo_payment_about);
            itemHolder.about.setTypeface(AppContr.calibri);
        }
        itemHolder.date.setTypeface(AppContr.calibri);
        itemHolder.action.setTypeface(AppContr.calibri);
        itemHolder.sum.setTypeface(AppContr.calibri);
        //row.setTag(itemHolder);
//        }
//        else {
//            itemHolder = (ItemHolder) row.getTag();
//        }

        if (groupPosition == 0) {
            Charge charge = (Charge) getChild(groupPosition, childPosition);
            itemHolder.date.setText(charge.getDate_charge());
            itemHolder.action.setText(charge.getAction_charge());
            itemHolder.sum.setText(String.valueOf(charge.getAmount_charges()));
        } else {
            Payment payment = (Payment) getChild(groupPosition, childPosition);
            itemHolder.date.setText(payment.getDate_payment());
            itemHolder.action.setText(payment.getAction_payment());
            itemHolder.sum.setText(String.valueOf(payment.getAmount_payment()));
            if (Utils.notNull_orEmpty(payment.getComment_payment()))
                itemHolder.about.setText(payment.getComment_payment());
            else itemHolder.about.setText("");
        }

        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}