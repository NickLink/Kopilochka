package ua.f5.kopilochka.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.MyLifecycleHandler;
import ua.f5.kopilochka.data.UserData;
import ua.f5.kopilochka.database.DB;
import ua.f5.kopilochka.ui.FontCache;
import ua.f5.kopilochka.utils.LruBitmapCache;

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
    public static DB db;

    public static Typeface calibri;
    public static Typeface calibri_bold;

    private static AppContr mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
        userData = new UserData();
        sPref = context.getSharedPreferences(Const.myAppPrefs, Context.MODE_PRIVATE);
        db = new DB(context);
        db.open();
        // Initialize the singletons so their instances
        // are bound to the application process.
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        calibri = FontCache.get("fonts/opensansregular.ttf", context);
        calibri_bold = FontCache.get("fonts/opensansbold.ttf", context);
    }

    public static synchronized AppContr getInstance() {
        return mInstance;
    }

    public static SharedPreferences getSharPref() {
        return sPref;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(
                    context.getApplicationContext(), new HttpClientStack(client()));

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


    DefaultHttpClient client() {
        HttpParams params = new BasicHttpParams();
        HttpClientParams.setRedirecting(params, true);

        HttpConnectionParams.setConnectionTimeout(params, 5000);

        HttpConnectionParams.setSoTimeout(params, 10000);

        ConnManagerParams.setMaxTotalConnections(params, 15);
        ConnPerRoute cpr = new ConnPerRoute() {
            @Override
            public int getMaxForRoute(HttpRoute httpRoute) {
                return 5;
            }
        };

        ConnManagerParams.setMaxConnectionsPerRoute(params, cpr);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        SSLSocketFactory sslSocketFactory = null;
        try {
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string)
                        throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] xcs, String string)
                        throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);

            sslSocketFactory = new TrustAllSSLSocketFactory(ctx);
            if (sslSocketFactory != null)
                sslSocketFactory.setHostnameVerifier(
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            sslSocketFactory = null;
        }

        if (sslSocketFactory == null) {
            sslSocketFactory = SSLSocketFactory.getSocketFactory();
            sslSocketFactory.setHostnameVerifier(
                    SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        }
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        DefaultHttpClient client = new DefaultHttpClient(cm, params);
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount,
                                        HttpContext context) {
                // retry a max of 5 times
                if (executionCount >= 5) {
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    return true;
                } else if (exception instanceof ClientProtocolException) {
                    return true;
                }
                return false;
            }
        };
        client.setHttpRequestRetryHandler(retryHandler);
        return client;
    }

    static final
    private class TrustAllSSLSocketFactory extends SSLSocketFactory {

        private SSLContext sslContext = SSLContext.getInstance("TLS");

        public TrustAllSSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException,
                KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        public TrustAllSSLSocketFactory(SSLContext context)
                throws KeyManagementException,
                NoSuchAlgorithmException, KeyStoreException,
                UnrecoverableKeyException {
            super(null);
            sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory()
                    .createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }


}
