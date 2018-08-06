package phone.ktv.tootls;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求
 */
public class CallBackUtils {

    private static final String TAG = "CallBackUtils";
    private static CallBackUtils callBackUtils;

    public static CallBackUtils getInstance() {
        if (callBackUtils == null) {
            synchronized (CallBackUtils.class) {
                if (callBackUtils == null) {
                    callBackUtils = new CallBackUtils();
                }
            }
        }
        return callBackUtils;
    }

    private CallBackUtils() {

    }

    public void init(String url, final CallBackUtils.CommonCallback callback) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFinish(null, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                callback.onFinish(s, null);
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }

    public interface CommonCallback {
        void onFinish(String result, String msg);
    }
}
