package ua.kiev.foxtrot.kopilochka.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.Interfaces;
import ua.kiev.foxtrot.kopilochka.MyLifecycleHandler;
import ua.kiev.foxtrot.kopilochka.app.AppContr;
import ua.kiev.foxtrot.kopilochka.http.Connect;

/**
 * Created by NickNb on 16.11.2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "CheckNetworkStatus";
    private boolean isConnected = false;
    Interfaces interfaces;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.v(LOG_TAG, "Receieved notification about network status");
        isNetworkAvailable(context);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if(!isConnected){
                            Log.v(LOG_TAG, "Now you are connected to Internet!");
                            isConnected = true;
                            //do your processing here ---
                            //if you need to post any data to the server or get status
                            //update from the server
                            if(AppContr.getSharPref().getString(Const.SAVED_SES, null) != null
                                    && Connect.isOnline(context)
                                    && !MyLifecycleHandler.isApplicationInForeground()){
                                Intent startServiceIntent = new Intent(context, BackgroundService.class);
                                context.startService(startServiceIntent);
                            } else
                            if(AppContr.getSharPref().getString(Const.SAVED_SES, null) != null
                                    && Connect.isOnline(context)
                                    && MyLifecycleHandler.isApplicationInForeground()) {
                                context.sendBroadcast(new Intent("INTERNET_AWAKE"));
                                //interfaces.CallSync();
                            }

                        }
                        return true;
                    }
                }
            }
        }
        Log.v(LOG_TAG, "You are not connected to Internet!");
        isConnected = false;
        return false;
    }
}