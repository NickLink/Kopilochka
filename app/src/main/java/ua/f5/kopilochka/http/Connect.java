package ua.f5.kopilochka.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by NickNb on 31.10.2016.
 */
public class Connect {
    public static boolean isOnline(Context mContext){
        final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nInfo = connMgr.getActiveNetworkInfo();
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if( (wifi.isAvailable() || mobile.isAvailable()) && nInfo != null && nInfo.isConnected() )
            return true;
        else
            return false;
    }
}
