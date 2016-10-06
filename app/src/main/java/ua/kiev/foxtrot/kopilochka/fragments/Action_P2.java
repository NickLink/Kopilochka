package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;
import ua.kiev.foxtrot.kopilochka.app.AppController;
import ua.kiev.foxtrot.kopilochka.data.BBS_News;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.database.Tables;
import ua.kiev.foxtrot.kopilochka.interfaces.OnBackPress;

/**
 * Created by NickNb on 06.10.2016.
 */
public class Action_P2 extends Fragment {
    Interfaces interfaces;
    OnBackPress onBackPress;
    private int action_id;
    private String title;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public static Action_P2 newInstance(int action_id) { //
        Action_P2 fragment = new Action_P2();
        Bundle args = new Bundle();
        args.putInt(Const.action_id, action_id);
        //args.putString(Const.action_id, title); //--------no need----
        fragment.setArguments(args);
        return fragment;
    }

    public Action_P2() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interfaces = (Interfaces) activity;
            onBackPress = (OnBackPress) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Interfaces");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_action_p2, container,
                false);
        action_id = getArguments().getInt(Const.action_id, 0);
//        if(action_id == 0){
//            //Error getting data
//            Utils.ShowInputErrorDialog(getActivity(), "Error", "Action number error", "OK");
//            return null;
//        }
        //All ok

        //title = getArguments().getString(Const.action_id);

        TextView action_name = (TextView)rootView.findViewById(R.id.action_name);
        TextView textView_text = (TextView)rootView.findViewById(R.id.textView_text);
        NetworkImageView imageView_image = (NetworkImageView)rootView.findViewById(R.id.imageView_image);

        BBS_News item = getItem_fromBase(action_id);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        action_name.setText(item.getTitle());
        textView_text.setText(item.getDescription());
        imageView_image.setImageUrl(item.getUrlToImage(), imageLoader);


        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // interfaces.OpenClose();
                Toast.makeText(getActivity(), "Pressed in Fragment", Toast.LENGTH_SHORT).show();
                onBackPress.onBackPressed();
            }
        });
        menu_item_title.setText(getString(R.string.menu_action));
        return rootView;
    }

    private BBS_News getItem_fromBase(int id){
        DB db = new DB(getActivity());
        db.open();
        Cursor myCursor = db.getData_forId(id);
        if (myCursor.moveToFirst()){
            BBS_News item = new BBS_News();
            item.setAuthor(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_author)));
            item.setTitle(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_title)));
            item.setDescription(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_description)));
            item.setUrl(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_url)));
            item.setUrlToImage(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_urlToImage)));
            item.setPublishedAt(myCursor.getString(myCursor.getColumnIndex(Tables.bbs_publishedAt)));
            db.close();
            return item;
        } else
            return null;
    }
}
