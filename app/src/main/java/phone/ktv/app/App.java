package phone.ktv.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.service.MusicService;
import phone.ktv.service.SocketService;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.SyncServerdate;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.tootls.xDBUtils;

public class App extends Application {
    public static final String PLAY = "PLAY";//去播放
    public static final String LAST = "LAST";//上一曲
    public static final String NEXT = "NEXT";//下一曲
//    public static final String DESTROY = "DESTROY";//更新播放列表

    public static final String SWITCHPLAY = "SWITCHPLAY";//歌曲切換
    public static final String STARTPLAY = "STARTPLAY";//开始播放
    public static final String ENDPLAY = "ENDPLAY";//播放完毕
    public static final String SEEKTO = "SEEKTO";//跳转
    public static final String PAUSE = "PAUSE";//暂停播放
    public static final String UPDATEPROCESS = "UPDATEPROCESS";//更新进度
    public static final String ENDPLAYER = "ENDPLAYER";//退出全屏播放
    public int getPlaymodel() {
        return playmodel;
    }

    public void setPlaymodel(int playmodel) {
        this.playmodel = playmodel;
    }

    private int playmodel = 0;


    public static Context context;

    public static Gson gson;
    public static OkHttpClient client;
    private static SharedPreferences config;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    private View view;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            context = this;
            client = new OkHttpClient();
            gson = new GsonBuilder().setDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss").create();
            config = getSharedPreferences("config", Context.MODE_PRIVATE);
            initSpeech();
            config();
            getip();
            initDBUtils();
            startService(new Intent(this, MusicService.class));
            startService(new Intent(this, SocketService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//            headurl = "http://" + tmp + ":8080/ktv/api/phone/";

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

    public static DbManager mDb;
    public xDBUtils DBUtils;
    public DbManager.DaoConfig daoConfig;

    public void initDBUtils() {
        x.Ext.init(this);
        x.Ext.setDebug(false);// 是否输出debug日志, 开启debug会影响性能
        DBUtils = new xDBUtils();

        daoConfig = new DbManager.DaoConfig();
        mDb = x.getDb(daoConfig);

    }

    public static int Maxlimit = 10;
    public static int MaxlimitSrc = 15;

//    public List<MusicPlayBean> getTestlist() {
//        return testlist;
//    }
//
//    public void setTestlist(List<MusicPlayBean> testlist) {
//        this.testlist = testlist;
//    }
//
//    private List<MusicPlayBean> testlist;


    private MediaPlayer mediaPlayer;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private int playstatus;

    public int getPlaystatus() {
        return playstatus;
    }

    public void setPlaystatus(int playstatus) {
        this.playstatus = playstatus;
    }


    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * 保存数据到DB
     *
     * @param item
     * @param context
     * @param TAG
     * @param s
     */
    public static void saveData(MusicPlayBean item, Context context, String TAG, boolean s) {
        try {
            item.localTime = SyncServerdate.getLocalTime();
            App.mDb.save(item);
            if (s) {
                ToastUtils.showLongToast(context, "歌曲添加成功");
            }
        } catch (Exception e) {
            Logger.d(TAG, "保存数据异常.." + e.getMessage());
            if (s) {
                ToastUtils.showLongToast(context, "此歌曲已被添加");
            }
            UpdateSaveData(item, TAG);
        }
    }

    public static void UpdateSaveData(MusicPlayBean item, String TAG) {
        try {
            item.localTime = SyncServerdate.getLocalTime();
            App.mDb.update(item);
        } catch (Exception e) {
            Logger.d(TAG, "修改数据异常.." + e.getMessage());
        }
    }

    public static List<MusicPlayBean> getSelectData() {
        try {
            List<MusicPlayBean> playBeans = App.mDb.selector(MusicPlayBean.class).orderBy("localTime", true).findAll();//数据库查询
            return playBeans;
        } catch (Exception e) {
            ToastUtils.showLongToast(context, "查询失败" + e.getMessage());
        }
        return null;
    }
}
