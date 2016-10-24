package ua.kiev.foxtrot.kopilochka.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.data.UserData;
import ua.kiev.foxtrot.kopilochka.receivers.BackgroundService;
import ua.kiev.foxtrot.kopilochka.utils.LruBitmapCache;

/**
 * Created by NickNb on 29.09.2016.
 */
public class AppContr extends Application {

    public static final String TAG = AppContr.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static SharedPreferences sPref;
    Context context;
    public static UserData userData;

    private static AppContr mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
        userData = new UserData();
        sPref = context.getSharedPreferences(Const.myAppPrefs, Context.MODE_PRIVATE);
        // Initialize the singletons so their instances
        // are bound to the application process.
        Intent startServiceIntent = new Intent(context, BackgroundService.class);
        startService(startServiceIntent);
    }

    public static synchronized AppContr getInstance() {
        return mInstance;
    }

    public static SharedPreferences getSharPref() {
        return sPref;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().getCache().clear();
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().getCache().clear();
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelAllRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }
}
