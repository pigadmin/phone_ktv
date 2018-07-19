package phone.ktv.tootls;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkhttpUtils {

    public static void doStart(String url, Callback callback) {
        Request  request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}
