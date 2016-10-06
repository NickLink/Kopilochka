package ua.kiev.foxtrot.kopilochka;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.kiev.foxtrot.kopilochka.adapters.SliderMenuAdapter;
import ua.kiev.foxtrot.kopilochka.database.DB;
import ua.kiev.foxtrot.kopilochka.fragments.Action_P1;
import ua.kiev.foxtrot.kopilochka.fragments.Data_P1;
import ua.kiev.foxtrot.kopilochka.fragments.History_P1;
import ua.kiev.foxtrot.kopilochka.fragments.Notif_P1;
import ua.kiev.foxtrot.kopilochka.fragments.ScanFragment;
import ua.kiev.foxtrot.kopilochka.fragments.Start_P1;
import ua.kiev.foxtrot.kopilochka.fragments.WTF_P1;
import ua.kiev.foxtrot.kopilochka.receivers.BackgroundService;

public class MainActivity extends AppCompatActivity implements Interfaces{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    RelativeLayout drawerView;
    RelativeLayout mainView;
    ListView list_slidermenu;
    ArrayList<String> menu_items;
    TextView main_text, menu_text;
    Button open_close;

    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    FrameLayout fragment_place;
    boolean service_running;
    DB db;

    ScanFragment scanner;
    private String scan_code;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        db.create();
        db.close();

        fragmentManager = getSupportFragmentManager();

        drawerView = (RelativeLayout) findViewById(R.id.drawerView);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        list_slidermenu = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //main_text = (TextView) findViewById(R.id.textView);
//        open_close = (Button) findViewById(R.id.menu_button);
//        menu_text = (TextView) findViewById(R.id.menu_text);
        fragment_place = (FrameLayout) findViewById(R.id.fragment_place);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.icon_menu, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        list_slidermenu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        SliderMenuAdapter adapter = new SliderMenuAdapter(MainActivity.this);
        list_slidermenu.setAdapter(adapter);

        list_slidermenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        Start_P1.newInstance(), Const.Fr_StP1).commit();

                        break;
                    case 1:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        Notif_P1.newInstance(), Const.Fr_NtP1).commit();

                        break;
                    case 2:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        Action_P1.newInstance(), Const.Fr_AcP1).commit();

                        break;
                    case 3:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        History_P1.newInstance(), Const.Fr_HsP1).commit();
                        break;
                    case 4:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        Data_P1.newInstance(), Const.Fr_DtP1).commit();
                        break;
                    case 5:
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        WTF_P1.newInstance(), Const.Fr_WtP1).commit();
                        break;
                }


                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if(fragmentList !=null && fragmentList.get(fragmentList.size()-1)!=null){
            //Fragments exist
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_place,
                            Start_P1.newInstance(), Const.Fr_StP1).commit();
            list_slidermenu.setItemChecked(0, true);
        }





    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(Const.TAG, "SSS MainActivity: onStart() Stop service");
        if(isMyServiceRunning(BackgroundService.class)){
            service_running = false;
            Intent stopServiceIntent = new Intent(getApplicationContext(), BackgroundService.class);
            stopService(stopServiceIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(Const.TAG, "SSS MainActivity: onStop() Start service");
        if(!isMyServiceRunning(BackgroundService.class)) {
            service_running = true;
            Intent startServiceIntent = new Intent(getApplicationContext(), BackgroundService.class);
            startService(startServiceIntent);
        }
    }

    @Override
    public void OpenClose() {

        if(mDrawerLayout.isDrawerOpen(drawerView)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

    }

    @Override
    public void ScannStart() {
        scanner = new ScanFragment();

        TransactionAction(scanner,
                getSupportFragmentManager().findFragmentByTag(Const.Fr_AcP1),
                Const.Fr_Scan, Const.Fr_AcP1, true);

//        fragmentManager
//                .beginTransaction()
//                .addToBackStack("action")
//                .add(R.id.fragment_place,
//                        scanner, "scanner").commit();
    }

    @Override
    public void ScannResult(String result) {
        TransactionAction(getSupportFragmentManager().findFragmentByTag(Const.Fr_AcP1),
                scanner, Const.Fr_AcP1, Const.Fr_Scan, false);

        ((TextView)getSupportFragmentManager()
                .findFragmentByTag(Const.Fr_AcP1).getView().findViewById(R.id.result_test)).setText(result);
//        fragmentManager
//                .beginTransaction()
//                .remove(scanner).commit();

    }

    @Override
    public void LoginSuccess(String token) {

    }

//    public String getScan_code(){
//        return scan_code;
//    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    void TransactionAction(Fragment fragment_in, Fragment fragment_out,
                           String tag_in, String tag_out, boolean forward) {
        transaction = getSupportFragmentManager().beginTransaction();
        Log.v(TAG, "TransactionAction tag_in=" + tag_in + " tag_out=" + tag_out);
        if(fragment_out != null && fragment_in != null){
            if(forward) {
                //transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
                transaction.add(R.id.fragment_place, fragment_in, tag_in);
                transaction.hide(fragment_out);
            } else {
                //transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
                transaction.remove(fragment_out);
                transaction.show(fragment_in);
            }
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {

//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }

    }
}
