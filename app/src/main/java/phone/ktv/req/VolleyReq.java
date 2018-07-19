package phone.ktv.req;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import okhttp3.Request;
import phone.ktv.app.App;
import phone.ktv.tootls.Logger;

public class VolleyReq {
    private Api api;
    private Context context;
    private String TAG = "VolleyReq";

    public VolleyReq(Context context, Api api) {
        this.context = context;
        this.api = api;
    }

    public interface Api {
        void finish(String tag, String json);

        void error(String tag, String json);
    }

    public StringRequest stringRequest;

    public StringRequest get(final String tag, String url) {
        final long start = System.currentTimeMillis();
        stringRequest = new StringRequest(url, new Response.Listener<String>() {
            //正确接收数据回调
            @Override
            public void onResponse(String json) {
                try {
                    api.finish(tag, json);
                    Logger.d(TAG, (System.currentTimeMillis() - start) + "");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                api.error(tag, "接口請求失敗或超時。");
            }
        });
        App.requestQueue.add(stringRequest);

        return stringRequest;
    }

}
