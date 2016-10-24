package ua.kiev.foxtrot.kopilochka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.data.ProductGroup;

/**
 * Created by NickNb on 24.10.2016.
 */
public class Product_ListView_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductGroup> arrayList;
    //ImageLoader imageLoader = AppContr.getInstance().getImageLoader();
    private LayoutInflater inflater;

    public Product_ListView_Adapter(Context context, ArrayList<ProductGroup> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public ArrayList<ProductGroup> getProduct_data(){
        return arrayList;
    }

    public void setNotice_data(ArrayList<ProductGroup> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();
    }

    @Override
    public ProductGroup getItem(int position) { //Object
        // TODO Auto-generated method stub
        return arrayList.get(position);
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
            convertView = inflater.inflate(R.layout.frag_start_p1_list_item, viewGroup, false);

        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView count = (TextView)convertView.findViewById(R.id.count);

        ProductGroup feed = arrayList.get(position);

        title.setText(feed.getGroup_name().toString());
        count.setText(String.valueOf(feed.getModels_count()));

        return convertView;
    }
}
