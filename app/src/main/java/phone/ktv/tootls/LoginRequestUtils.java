package phone.ktv.tootls;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.util.WeakHashMap;

import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.UserBean;

/**
 * 默认请求登录接口
 */
public class LoginRequestUtils {

    private static final String TAG = "LoginRequestUtils";

    private SPUtil mSP;
    private Context context;
    private DefaultLoginInterface loginInterface;

    public LoginRequestUtils(final SPUtil mSP, Context context, DefaultLoginInterface loginInterface) {
        this.mSP = mSP;
        this.context = context;
        this.loginInterface = loginInterface;
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
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    if (TextUtils.isEmpty(result)) {
                        loginInterface.resuleMessage(30);
                    } else {
                        AJson aJson = App.jsonToObject(result, new TypeToken<AJson<UserBean>>() {
                        });
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                String token = aJson.getToken();
                                mSP.putString("token", token);
                                loginInterface.resuleMessage(10);
                            } else {
                                loginInterface.resuleMessage(20);
                            }
                        }
                    }
                }
            });
        } else {
            ToastUtils.showLongToast(context, "网络连接异常,请检查网络配置");
        }
    }

    public interface DefaultLoginInterface {
        void resuleMessage(int code);
    }

    //用法:
//                  case RankingExpiredToken:
//            ToastUtils.showLongToast(mContext, (String) msg.obj);
//    LoginRequestUtils utils = new LoginRequestUtils(mSP, mContext, loginInterface);
//                    utils.requestLoginData();
//                    break;
//}
//            mSvProgressHUD.dismiss();
//                    mPullToRefresh.onRefreshComplete();
//                    }
//                    };
//
//                    LoginRequestUtils.DefaultLoginInterface loginInterface = new LoginRequestUtils.DefaultLoginInterface() {
//@Override
//public void resuleMessage(int code) {
//        Logger.i(TAG, "code.." + code);
//        switch (code) {
//        case 10:
//        break;
//        case 20:
//        break;
//        case 30:
//        break;
//        }
//        }
//        };
}
