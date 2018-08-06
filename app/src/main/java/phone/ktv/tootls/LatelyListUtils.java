package phone.ktv.tootls;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.MusicPlayBean;

/**
 * 添加播放记录
 */
public class LatelyListUtils {

    private static final String TAG = "LatelyListUtils";

    private SPUtil mSP;
    private Activity context;
    private MusicPlayBean bean;

    public LatelyListUtils(final SPUtil mSP, Activity context, MusicPlayBean bean) {
        this.mSP = mSP;
        this.context = context;
        this.bean = bean;
    }

    public void getLatelyList() {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token
        weakHashMap.put("sid", bean.id);//token

        String url = App.getRqstUrl(App.headurl + "song/record/add", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(context)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    getResult(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    AJson<List<MusicPlayBean>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<MusicPlayBean>>>() {
                    });
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            Logger.i(TAG, "aJson..." + aJson.toString());
                        } else if (aJson.getCode() == 500) {
                            getResult(aJson.getMsg());
                        } else {
                            getResult(aJson.getMsg());
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            ToastUtils.showLongToast(context, "网络连接异常,请检查网络配置");
        }
    }

    private void getResult(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongToast(context, msg);
            }
        });
    }
}
