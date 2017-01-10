package ua.f5.kopilochka.receivers;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.app.AppContr;

/**
 * Created by NickNb on 29.09.2016.
 */
public class BackgroundService extends Service {

    private static final String TAG = "BackgroundService";

    PeriodicTaskReceiver mPeriodicTaskReceiver = new PeriodicTaskReceiver();
    ExecutorService es;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.v("", "SSS onStartCommand Service Started");
        AppContr appContr = (AppContr) getApplicationContext();
        SharedPreferences sharedPreferences = appContr.getSharPref();
        IntentFilter batteryStatusIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = registerReceiver(null, batteryStatusIntentFilter);

        if (batteryStatusIntent != null) {
            int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPercentage = level / (float) scale;
            float lowBatteryPercentageLevel = 0.14f;

            try {
                int lowBatteryLevel = Resources.getSystem().getInteger(Resources.getSystem().getIdentifier("config_lowBatteryWarningLevel", "integer", "android"));
                lowBatteryPercentageLevel = lowBatteryLevel / (float) scale;
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Missing low battery threshold resource");
            }

            sharedPreferences.edit().putBoolean(Const.BACKGROUND_SERVICE_BATTERY_CONTROL, batteryPercentage >= lowBatteryPercentageLevel).apply();
        } else {
            sharedPreferences.edit().putBoolean(Const.BACKGROUND_SERVICE_BATTERY_CONTROL, true).apply();
        }

        mPeriodicTaskReceiver.restartPeriodicTaskHeartBeat(BackgroundService.this, appContr);
        return START_STICKY;
    }

    public void onCreate() {
        super.onCreate();
        //Log.v("", "SSS onCreate Service Started");
        es = Executors.newFixedThreadPool(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.v("", "SSS Service stopped");
        mPeriodicTaskReceiver.stopPeriodicTaskHeartBeat(BackgroundService.this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}