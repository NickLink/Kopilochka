package ua.f5.kopilochka;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import ua.f5.kopilochka.adapters.SliderMenuAdapter;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.data.Post_SN;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.fragments.Action_P1;
import ua.f5.kopilochka.fragments.Action_P2;
import ua.f5.kopilochka.fragments.Action_P3;
import ua.f5.kopilochka.fragments.Data_P1;
import ua.f5.kopilochka.fragments.Data_P1_Extra;
import ua.f5.kopilochka.fragments.Data_P1_Logged;
import ua.f5.kopilochka.fragments.History_P1;
import ua.f5.kopilochka.fragments.Notif_P1;
import ua.f5.kopilochka.fragments.ScanFragment;
import ua.f5.kopilochka.fragments.Start_P1;
import ua.f5.kopilochka.fragments.Start_P2;
import ua.f5.kopilochka.fragments.WTF_P1;
import ua.f5.kopilochka.http.Methods;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.interfaces.OnBackPress;
import ua.f5.kopilochka.receivers.BackgroundService;
import ua.f5.kopilochka.receivers.NetworkChangeReceiver;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.Encryption;
import ua.f5.kopilochka.utils.Utils;

public class MainActivity extends AppCompatActivity implements Interfaces, OnBackPress, HttpRequest {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    RelativeLayout drawerView;
    RelativeLayout mainView;
    ListView list_slidermenu;
    SliderMenuAdapter adapter;

    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    FrameLayout fragment_place;
    DB db = AppContr.db;

    private Encryption encrypt;

    private String TAG = "MainActivity";

    private List<Post_SN> arrayList;
    private Post_SN register_item;
    ProgressDialog pDialog;

    NetworkChangeReceiver broadcastReceiver = new NetworkChangeReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CallSync();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db.create();

//        PstatProvider item = new PstatProvider();
//        item.init(getBaseContext());
//        item.addEvent("Kopilka", null);

        registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_AWAKE"));

        encrypt = Encryption.getDefault("Key", "Disabled", new byte[16]);
        //calibri = FontCache.get("fonts/calibri.ttf", getBaseContext());
        //calibri_bold = FontCache.get("fonts/calibri_bold.ttf", getBaseContext());

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
        adapter = new SliderMenuAdapter(MainActivity.this);
        list_slidermenu.setAdapter(adapter);

        list_slidermenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setViewed(i);
                switch (i) {
                    case 0:
                        Utils.setGroupViewed();
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        Start_P1.newInstance(), Const.Fr_StP1).commit();

                        break;
                    case 1:
                        Utils.setNoticeViewed();
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.fragment_place,
                                        Notif_P1.newInstance(), Const.Fr_NtP1).commit();

                        break;
                    case 2:
                        Utils.setActionViewed();
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
                        checkForLoggedStatus();

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
        if (fragmentList != null && fragmentList.get(fragmentList.size() - 1) != null) {
            //Fragments exist
        } else {
            if (AppContr.getSharPref().getString(Const.SAVED_SES, null) != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_place,
                                Start_P1.newInstance(), Const.Fr_StP1).commit();
                list_slidermenu.setItemChecked(0, true);
            } else {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_place,
                                Data_P1.newInstance(), Const.Fr_DtP1).commit();
                list_slidermenu.setItemChecked(4, true);
            }
        }


    }

    private void checkForLoggedStatus() {
        //Log.v(TAG, "SSS TIME Start=" + System.currentTimeMillis());
        Utils.Restore_User(this, encrypt);
        //Log.v(TAG, "SSS TIME Finish=" + System.currentTimeMillis());
        if (Utils.Correct_User()) {
            //All data exist we can move
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_place,
                            Data_P1_Logged.newInstance(), Const.Fr_DtPL).commit();
        } else {
            //Something broken - full relogin & clear database
            Utils.Clear_User();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_place,
                            Data_P1.newInstance(), Const.Fr_DtP1).commit();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        //Refresh data adapter
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.v(Const.TAG, "SSS MainActivity: onStart() Stop service");
        //if(isMyServiceRunning(BackgroundService.class)){
        //    service_running = false;
        Intent stopServiceIntent = new Intent(getApplicationContext(), BackgroundService.class);
        stopService(stopServiceIntent);
        //}
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.v(Const.TAG, "SSS MainActivity: onStop() Start service");
        //if(!isMyServiceRunning(BackgroundService.class)) {
        //   service_running = true;
        Intent startServiceIntent = new Intent(getApplicationContext(), BackgroundService.class);
        startService(startServiceIntent);
        //}
    }

    @Override
    public void OpenClose() {

        if (mDrawerLayout.isDrawerOpen(drawerView)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

    }

    @Override
    public void ScannStart(int id) {
        //scanner = new ScanFragment();
        TransactionActionStack(ScanFragment.newInstance(id), Const.Fr_Scan, true); //action_id
    }

    @Override
    public void ScannResult(int id, String result) {

//        TransactionAction(fragmentManager.findFragmentByTag(Const.Fr_AcP1),
//                scanner, Const.Fr_AcP1, Const.Fr_Scan, false);
        Action_P3 frag = (Action_P3)
                getSupportFragmentManager().findFragmentByTag(Const.Fr_AcP3);
        frag.updateScanCode(id, result);
//        ((EditText)getSupportFragmentManager()
//                .findFragmentByTag(Const.Fr_AcP3).getView().findViewById(R.id.scan_result)).setText(result);
        onBackPressed();
//        fragmentManager
//                .beginTransaction()
//                .remove(scanner).commit();

    }

    @Override
    public void SaveUser() {
        //Encrypt & Save token
        Utils.Save_User(this, encrypt);
    }

    @Override
    public void CallSync() {
        arrayList = db.getPost_SN_List(Const.reg_status_await);
        if(arrayList != null && arrayList.size()>0){
            Dialogs.ShowCallSyncDialog(this, this);
        }

    }

    @Override
    public void DoSync() {
        pDialog = new ProgressDialog(this);
        pDialog.show();
        SyncRepeat();
    }

    private void SyncRepeat(){
        register_item = arrayList.get(arrayList.size()-1);
        Methods.post_SN(this, register_item, this);
    }

    @Override
    public void http_result(int type, String result) {
        switch (type){
            case Const.postSN:
                if(Methods.RegisterReceive(this, result, register_item)){
                    //succes_count++;
                } else {
                    //error_count++;
                }
                arrayList.remove(arrayList.size()-1);
                if(arrayList !=null && arrayList.size() > 0) {
                    SyncRepeat();
                } else {
                    //Dialog for completeing SYNC
                    if(pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                    Dialogs.ShowSyncDialog(this);
                    History_P1 history = (History_P1) getSupportFragmentManager().findFragmentByTag(Const.Fr_HsP1);
                    if(history != null) {
                        history.NotifyAdapter();
                    }
//                    if(arrayList !=null && arrayList.size() == 0 && (succes_count > 0 || error_count > 0)){
//                        CreateNotification("Повідомлення", "Вдало зареєстровано - " + String.valueOf(succes_count)
//                                + System.getProperty("line.separator") + " невдалих реєстрацій - " + String.valueOf(error_count) , "");
//                        succes_count = 0;
//                        error_count = 0;
//                    }
                }
                break;
        }
    }

    @Override
    public void http_error(int type, String error) {

    }

    @Override
    public void LoginSuccess() {
        //Go next page of login
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.flip_in, R.anim.flip_out); //, R.anim.slide_left_in, R.anim.slide_left_out
        transaction.add(R.id.fragment_place, Data_P1_Logged.newInstance(), Const.Fr_DtPL);
        transaction.remove(fragmentManager.findFragmentByTag(Const.Fr_DtP1));
        transaction.commit();


        //    Encryption encrypt = Encryption.getDefault("Key", "Salt", new byte[16]);
        //    String encrypted = encrypt.encryptOrNull("top secret string");
        //    String decrypted = encrypt.decryptOrNull(encrypted);
    }

    @Override
    public void ShowDataExtra() {
        TransactionActionStack(Data_P1_Extra.newInstance(), Const.Fr_DtPE, true);
    }

    @Override
    public void LogOut() {
        //Clear user data
        Utils.Clear_User();
        AppContr.userData = null;
        db.erase();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.flip_in, R.anim.flip_out); //, R.anim.slide_left_in, R.anim.slide_left_out
        transaction.add(R.id.fragment_place, Data_P1.newInstance(), Const.Fr_DtP1);
        transaction.remove(fragmentManager.findFragmentByTag(Const.Fr_DtPL));
        transaction.commit();
        adapter.refreshData();
    }

    @Override
    public void ActionSelected(int action_id) { //

        TransactionActionStack(Action_P2.newInstance(action_id), Const.Fr_AcP2, true); //action_id

//        TransactionAction(Action_P2.newInstance(action_id),
//                fragmentManager.findFragmentByTag(Const.Fr_AcP1),
//                Const.Fr_AcP2, Const.Fr_AcP1, true);
    }

    @Override
    public void ModelSelected(int action_id, int model_id) {
        TransactionActionStack(Action_P3.newInstance(action_id, model_id), Const.Fr_AcP3, true);
    }

    @Override
    public void ProductGroupSelected(int group_id, String group_name, int action_type_id) {
        TransactionActionStack(Start_P2.newInstance(group_id, group_name, action_type_id), Const.Fr_StP2, true);
    }

    @Override
    public void EditExistPostSN(int action_id, int model_id, String serials) {
        TransactionActionStack(Action_P3.newInstance(action_id, model_id, serials), Const.Fr_AcP3, true);
    }

    @Override
    public void DeleteExistPostSN() {
        History_P1 frag = (History_P1) fragmentManager.findFragmentByTag(Const.Fr_HsP1);
        frag.getAllData();
        frag.NotifyAdapter();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    void TransactionActionStack(Fragment fragment_in, String tag_in, boolean forward) {
        transaction = fragmentManager.beginTransaction();
        ////Log.v(TAG, "TransactionAction tag_in=" + tag_in + " tag_out=" + tag_out);
        if (fragment_in != null) {
            if (forward) {
                transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out, R.anim.slide_left_in, R.anim.slide_left_out);
                transaction.add(R.id.fragment_place, fragment_in, tag_in);
                transaction.addToBackStack(null);
                //transaction.hide(fragment_out);
            } else {
                //transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_left_out);
                fragmentManager.popBackStack();
                //transaction.remove(fragment_out);
                //transaction.show(fragment_in);
            }
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0)
            //Back to stack
            fragmentManager.popBackStack();
        else
            //Finnish activity dialog
            Dialogs.ShowExitDialog(this, getString(R.string.menu_exit_title), getString(R.string.menu_exit_text),
                    getString(R.string.menu_exit_no), getString(R.string.menu_exit_yes));

    }

}
