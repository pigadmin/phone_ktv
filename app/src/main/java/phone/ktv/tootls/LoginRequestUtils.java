package phone.ktv.tootls;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.UserBean;

/**
 * 默认请求登录接口
 */
public class LoginRequestUtils {

    private static final String TAG = "LoginRequestUtils";

    private static int RequestIndex = 10;

    private SPUtil mSP;
    private Context context;


    public LoginRequestUtils(final SPUtil mSP, Context context) {
        this.mSP = mSP;
        this.context = context;
    }

    public int getRequestIndex() {
        return RequestIndex;
    }

    public void requestLoginData() {
        String tel = mSP.getString("telPhone", null);//tel
        String psd = mSP.getString("password", null);
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("pass", psd);//密码
        String url = App.getRqstUrl(App.headurl + "login", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(context)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    RequestIndex = 20;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "登录s.." + s);
                    AJson aJson = App.jsonToObject(s, new TypeToken<AJson<UserBean>>() {
                    });
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            String token = aJson.getToken();
                            mSP.putString("token", token);
                            RequestIndex = 10;
                        } else {
                            RequestIndex = 20;
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            ToastUtils.showShortToast(context, "网络连接异常,请检查网络配置");
        }
    }
}
