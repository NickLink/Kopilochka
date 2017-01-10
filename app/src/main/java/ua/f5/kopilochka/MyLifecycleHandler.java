package ua.f5.kopilochka;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by NickNb on 11.11.2016.
 */
public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        android.util.Log.w("test", "application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        android.util.Log.w("test", "application is visible: " + (started > stopped));
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

    public static boolean isApplicationInBackground() {
        if (started>0 & resumed == paused)
            return true; else return false;
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:
    /*
    // Replace the four variables above with these four
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }
    */
}