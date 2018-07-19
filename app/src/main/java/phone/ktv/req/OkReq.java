package phone.ktv.req;

import android.content.Context;

import okhttp3.Request;
import okhttp3.Response;
import phone.ktv.app.App;
import phone.ktv.tootls.Logger;

public class OkReq {
    private Api api;
    private Context context;
    private String TAG = "OkReq";

    public OkReq(Context context, Api api) {
        this.context = context;
        this.api = api;
    }

    public interface Api {
        void finish(String tag, String json);

        void error(String tag, String json);
    }


    public void Get(final String tag, final String url) {
        final long start = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = null;
                Request request = null;
                Response response = null;
                try {
                    request = new Request.Builder().url(url).build();
                    response = App.client.newCall(request).execute();
                    if (response.code() == 200) {
                        json = response.body().string();
                        if (api == null)
                            return;
                        api.finish(tag, json);
                        Logger.d(TAG, (System.currentTimeMillis() - start) + "");
                    } else {
                        if (api == null)
                            return;
                        api.error(tag, response.code() + "");
                    }
                } catch (Exception e) {
                    if (api == null)
                        return;
                    api.error(tag, response.code() + "");
                    // e.printStackTrace();
                }
            }
        }).start();
    }
}
