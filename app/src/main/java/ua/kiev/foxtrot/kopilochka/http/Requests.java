package ua.kiev.foxtrot.kopilochka.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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

        JSONObject jsonBody = new JSONObject();
        try {
            for (Map.Entry<String, String> entry: params.entrySet()) {
                jsonBody.put(entry.getKey().toString(),entry.getValue().toString());
            }
            final String mRequestBody = jsonBody.toString();
            Log.v(TAG, "SSS json = " + mRequestBody);
            StringRequest stringObjReq = new StringRequest(Request.Method.POST,
                    Const.API_PATH, new Response.Listener<String>() { //jsonBody
                @Override
                public void onResponse(String response) {
                    Log.v(TAG, "SSS String responce=" + response);
                    request.http_result(req_type, response.toString());
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Log.v(TAG, "SSS Error = " + error.getMessage().toString());
                            request.http_error(req_type, error.getMessage());
                        }

                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json"; //"application/json; charset=utf-8"
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
                    return Response.success(response.data.toString(), HttpHeaderParser.parseCacheHeaders(response)); //responseString
                }
            };
            AppController.getInstance().addToRequestQueue(stringObjReq, Const.TAG_JSON);
            
        } catch (JSONException e) {
            Log.v(TAG, "SSS JSONException = " + e.toString());
            request.http_error(req_type, e.toString());
            e.printStackTrace();
        }

    }

//    String user = params.get(Const.user);
//    String password = params.get(Const.password);
//    String get_params = "?" + Const.user + "=" + user + "&" + Const.password + "=" + password;
//    StringRequest stringObjReq = new StringRequest(Request.Method.GET,
//            Const.BBS_NEWS_API_PATH + Const.BBS_NEWS_API_KEY,new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            Log.v(TAG, "SSS String responce=" + response.toString());
//            request.http_result(req_type, response.toString());
//        }
//    },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                    request.http_error(req_type, error.getMessage());
//                }
//
//            });
//    AppController.getInstance().addToRequestQueue(stringObjReq, Const.TAG_JSON);


}
