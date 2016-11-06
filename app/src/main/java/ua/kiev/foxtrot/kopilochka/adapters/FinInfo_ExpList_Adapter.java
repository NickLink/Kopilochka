package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.data.Charge;
import ua.kiev.foxtrot.kopilochka.data.FinInfo;
import ua.kiev.foxtrot.kopilochka.data.Payment;

/**
 * Created by NickNb on 26.10.2016.
 */
public class FinInfo_ExpList_Adapter extends BaseExpandableListAdapter {

    private FinInfo mFinInfo;
    private Context mContext;
    private LayoutInflater inflater;

    public FinInfo_ExpList_Adapter (Context context, FinInfo finInfo){
        mContext = context;
        mFinInfo = finInfo;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition == 0)
            return mFinInfo.getCharges().size();
        else
            return mFinInfo.getPayments().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(groupPosition == 0)
            return mFinInfo.getCharges();
        else
            return mFinInfo.getPayments();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition == 0)
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
        if (groupPosition == 0){
            if (isExpanded){
                //Изменяем что-нибудь, если текущая Group раскрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h1_exp, null);
            }
            else{
                //Изменяем что-нибудь, если текущая Group скрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h1, null);
            }
        }
        else {
            if (isExpanded){
                //Изменяем что-нибудь, если текущая Group раскрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h2_exp, null);
            }
            else{
                //Изменяем что-нибудь, если текущая Group скрыта
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_h2, null);
            }
        }



        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (groupPosition == 0) {
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_i1, null);
                TextView fininfo_charge_data = (TextView)convertView.findViewById(R.id.fininfo_charge_data);
                TextView fininfo_charge_actionname = (TextView)convertView.findViewById(R.id.fininfo_charge_actionname);
                TextView fininfo_charge_ammount = (TextView)convertView.findViewById(R.id.fininfo_charge_ammount);
                Charge charge = (Charge) getChild(groupPosition, childPosition);
                fininfo_charge_data.setText(charge.getDate_charge());
                fininfo_charge_actionname.setText(charge.getAction_charge());
                fininfo_charge_ammount.setText(String.valueOf(charge.getAmount_charges()));
            } else {
                convertView = inflater.inflate(R.layout.frag_data_p1_extra_i2, null);
                TextView fininfo_payment_data = (TextView)convertView.findViewById(R.id.fininfo_payment_data);
                TextView fininfo_payment_actionname = (TextView)convertView.findViewById(R.id.fininfo_payment_actionname);
                TextView fininfo_payment_ammount = (TextView)convertView.findViewById(R.id.fininfo_payment_ammount);
                TextView fininfo_payment_about = (TextView)convertView.findViewById(R.id.fininfo_payment_about);
                Payment payment = (Payment) getChild(groupPosition, childPosition);
                fininfo_payment_data.setText(payment.getDate_payment());
                fininfo_payment_actionname.setText(payment.getAction_payment());
                fininfo_payment_ammount.setText(String.valueOf(payment.getAmount_payment()));
                fininfo_payment_about.setText(payment.getComment_payment());
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}