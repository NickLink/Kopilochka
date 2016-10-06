package ua.kiev.foxtrot.kopilochka.http;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

import ua.kiev.foxtrot.kopilochka.Const;
import ua.kiev.foxtrot.kopilochka.app.AppController;
import ua.kiev.foxtrot.kopilochka.interfaces.HttpRequest;

/**
 * Created by NickNb on 05.10.2016.
 */
public class Requests {
    private static String TAG = "Requests";
    //private Context context;
    private HttpRequest request;
    int req_type;

    public Requests(int req_type, HttpRequest request){ //, Context context
        //this.context = context;
        this.request = request;
        this.req_type = req_type;
    }

    public void getNewsData(){
        StringRequest stringObjReq = new StringRequest(Request.Method.GET,
                Const.BBS_NEWS_API_PATH + Const.BBS_NEWS_API_KEY,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "SSS String responce=" + response.toString());
                request.http_result(req_type, response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        request.http_error(req_type, error.getMessage());
                    }

                });
        AppController.getInstance().addToRequestQueue(stringObjReq, Const.TAG_JSON);
    }

    public void getToken(HashMap<String, String> params){
        String user = params.get(Const.user);
        String password = params.get(Const.password);
        String get_params = "?" + Const.user + "=" + user + "&" + Const.password + "=" + password;
        StringRequest stringObjReq = new StringRequest(Request.Method.GET,
                Const.BBS_NEWS_API_PATH + Const.BBS_NEWS_API_KEY,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "SSS String responce=" + response.toString());
                request.http_result(req_type, response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        request.http_error(req_type, error.getMessage());
                    }

                });
        AppController.getInstance().addToRequestQueue(stringObjReq, Const.TAG_JSON);
    }


}
