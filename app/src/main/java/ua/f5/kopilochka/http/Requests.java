package ua.f5.kopilochka.http;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ua.f5.kopilochka.Const;
import ua.f5.kopilochka.MyLifecycleHandler;
import ua.f5.kopilochka.app.AppContr;
import ua.f5.kopilochka.interfaces.HttpRequest;
import ua.f5.kopilochka.utils.Dialogs;
import ua.f5.kopilochka.utils.Utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

/**
 * Created by NickNb on 05.10.2016.
 */
public class Requests {
    private static String TAG = "Requests";
    private Context context;
    private HttpRequest request;
    int req_type;
//    HashMap<String, Object> gl_params;

    public Requests(Context context, int req_type, HttpRequest request){ //, Context context
        this.context = context;
        this.request = request;
        this.req_type = req_type;
    }

    public void getHTTP_Responce(HashMap<String, Object> params){

//        gl_params = params;

        JSONObject jsonBody = new JSONObject();
        JSONArray serials = new JSONArray((List)(params.get(Const.serials)));
        try {
            for (Map.Entry<String, Object> entry: params.entrySet()) {
                if(entry.getKey().toString().equals(Const.serials)){
                    jsonBody.put(entry.getKey().toString(), serials);
                    //Log.v("", "2121 mRequestBody Array =" + serials.toString());
                } else
                    jsonBody.put(entry.getKey().toString(),entry.getValue().toString());
            }
            final String mRequestBody = jsonBody.toString();
            Log.v(TAG, "2121 Const.API_PATH =" + Const.API_PATH);
            Log.v(TAG, "2121 mRequestBody =" + mRequestBody);
            StringRequest stringObjReq = new StringRequest(Request.Method.POST,
                    Const.API_PATH, new Response.Listener<String>() { //jsonBody
                @Override
                public void onResponse(String response) {
                    Log.v(TAG, "2121 String responce =" + response);
                    //Log.v(TAG, "SSS String CheckError_1_2=" + Utils.CheckError_1_2(context, response));
                    request.http_result(req_type, Utils.CheckError_1_2(context, response));
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String message = null;
                            if (error instanceof NetworkError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof ServerError) {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (error instanceof AuthFailureError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (error instanceof NoConnectionError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (error instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }
                            try{
                                if(MyLifecycleHandler.isApplicationInForeground()){
                                    Dialogs.ShowInternetDialog(context, "Перевірте наявність Інтернету.");
                                }
                            } catch (Exception e){

                            }
                            request.http_error(req_type, message);

                        }

                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json"; //"application/json; charset=utf-8"
                }

//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> headers = new HashMap<>();
//                    headers.put("Content-Type", "application/json");
//                    headers.putAll(getAuthHeaders((String)gl_params.get("login"), (String)gl_params.get("password")));
//                    return headers;
//                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        //VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };
            AllCertificatesAndHostsTruster.apply();
            AppContr.getInstance().addToRequestQueue(stringObjReq, Const.TAG_JSON);
            
        } catch (JSONException e) {
            //Log.v(TAG, "SSS JSONException = " + e.toString());
            request.http_error(req_type, e.toString());
            e.printStackTrace();
        }

    }

//    public Map<String, String> getAuthHeaders(String loginName, String password) {
//        HashMap<String, String> params = new HashMap<String, String>();
//        String creds = String.format("%s:%s", loginName, password);
//        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//        params.put("Authorization", auth);
//        return params;
//    }

}
