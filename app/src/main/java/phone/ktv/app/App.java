package phone.ktv.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.xutils.x;

import java.util.Map;

import okhttp3.OkHttpClient;
import phone.ktv.tootls.Logger;

public class App extends Application {

    public static Context context;

    public static Gson gson;
    public static OkHttpClient client;
    public static RequestQueue requestQueue;
    private static SharedPreferences config;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        requestQueue = Volley.newRequestQueue(this);
        client = new OkHttpClient();
        gson = new GsonBuilder().setDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss").create();
        config = getSharedPreferences("config", Context.MODE_PRIVATE);
        initSpeech();
        config();
        getip();
        //    initDBUtils();
    }

    public static <T> T jsonToObject(String json, TypeToken<T> typeToken) {
        //  new TypeToken<AJson<Object>>() {}.getType()   对象参数
        // new TypeToken<AJson<List<Object>>>() {}.getType() 集合参数

        if (TextUtils.isEmpty(json) || json.equals("null"))
            return null;
        try {
            return gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    private boolean fstart;
    private static String ip = "192.168.2.25";
    public static String version;

    private void config() {
        try {
            fstart = config.getBoolean("fstart", false);
            if (!fstart) {
                SharedPreferences.Editor editor = config.edit();
                editor.putBoolean("fstart", true);
                Log.d("app", "fstart");
                editor.putString("ip", ip);
//                editor.putString("socketip", socketip);
                Log.d("ip", "---ip---\n" + ip);
                editor.commit();
            }
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String headurl;
    public static String socketurl;

    private void getip() {
        String tmp = config.getString("ip", "");
        if (!tmp.equals("")) {
            headurl = "http://" + tmp + ":8109/ktv/api/phone/";
//            headurl = "http://" + tmp + ":8080/ktv/api/";
            Log.d("host", "---headurl---\n" + headurl);
            socketurl = "http://" + tmp + ":8000/tv";
            Log.d("host", "---socketurl---\n" + socketurl);
        }
    }

    /**
     * 拼接get请求的url请求地址
     */
    public static String getRqstUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        return builder.toString();
    }

    /**
     * 讯飞语音初始化
     * APPID : 5b5c45cf;
     */
    private void initSpeech() {
        //初始化即创建语音配置对象，只有初始化后才可以使用MSC的各项服务，
        //将XXXXX改成自己应用的AppID,如果不知道AppId在哪，请看第1步中第③小步
        SpeechUtility.createUtility(context, SpeechConstant.APPID
                + "=5b5c45cf");
    }

    public void initDBUtils() {
        x.Ext.init(this);
        x.Ext.setDebug(false);// 是否输出debug日志, 开启debug会影响性能
//        DBUtils = new xDBUtils();
    }

    public static int Maxlimit = 10;
}
