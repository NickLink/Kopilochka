package ua.kiev.foxtrot.kopilochka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.R;

/**
 * Created by NickNb on 29.09.2016.
 */
public class Action_P1 extends Fragment {
    Interfaces interfaces;
    TextView result_test;
    Button button2;


    public static Action_P1 newInstance() {
        Action_P1 fragment = new Action_P1();
        return fragment;
    }

    public Action_P1() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interfaces = (Interfaces) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Interfaces");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_action_p1, container,
                false);

        button2 = (Button)rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.ScannStart();
            }
        });
        result_test = (TextView)rootView.findViewById(R.id.result_test);



        ImageButton menu_item_icon = (ImageButton)rootView.findViewById(R.id.menu_item_icon);
        TextView menu_item_title = (TextView)rootView.findViewById(R.id.menu_item_title);
        menu_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaces.OpenClose();
            }
        });
        menu_item_title.setText(getString(R.string.menu_action));
        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        result_test.setText(getScanCode());
//    }
//
//    private String getScanCode(){
//        MainActivity activity = (MainActivity) getActivity();
//        String code = activity.getScan_code();
//        return code;
//    }
}
