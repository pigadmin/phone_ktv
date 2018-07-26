package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.io.IOException;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.tootls.AlertDialogHelper;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.BtmDialog;
import phone.ktv.views.CustomEditView;
import phone.ktv.views.CustomTopTitleView;

/**
 * 修改密码
 */
public class ModifyPsdActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ModifyPsdActivity";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件
    private CustomEditView customEditView1;//原密码
    private CustomEditView customEditView2;//新密码
    private CustomEditView customEditView3;//确认新密码

    private TextView mDetermine;//确定

    public static final int UpdateRequestSuccess = 100;//修改密码成功
    public static final int UpdateRequestError = 200;//修改密码失败

    private SVProgressHUD mSvProgressHUD;

    private SPUtil mSP;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UpdateRequestSuccess://提交成功
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext, "修改密码成功");
                    clearInput();
                    finish();
                    IntentUtils.thisToOther(mContext, LoginActivity.class);
                    mSP.clearSpData();
                    break;

                case UpdateRequestError://提交失败
                    mSvProgressHUD.dismiss();
                    ToastUtils.showLongToast(mContext, "修改密码失败:" + msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_psd_activity_layout);

        initView();
        initLiter();
    }

    private void initView() {
        mContext = ModifyPsdActivity.this;
        mSvProgressHUD = new SVProgressHUD(mContext);
        mSP = new SPUtil(mContext);

        mDetermine = findViewById(R.id.determine_tvw);
        mTopTitleView1 = findViewById(R.id.customTopTitleView1);
        customEditView1 = findViewById(R.id.customEditView1);
        customEditView2 = findViewById(R.id.customEditView2);
        customEditView3 = findViewById(R.id.customEditView3);
    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mDetermine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.determine_tvw://确定
                updateClick();
                break;
        }
    }

    /**
     * 登录事件
     */
    private void updateClick() {
        if (TextUtils.isEmpty(customEditView1.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("请输入您的原密码");
            return;
        }
        if (customEditView1.getInputTitle().length() < 6) {
            mSvProgressHUD.showInfoWithStatus("原密码不能小于6位");
            return;
        }
        if (TextUtils.isEmpty(customEditView2.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("请输入您的新密码");
            return;
        }
        if (customEditView2.getInputTitle().length() < 6) {
            mSvProgressHUD.showInfoWithStatus("新密码不能小于6位");
            return;
        }
        if (TextUtils.isEmpty(customEditView3.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("请确认您的新密码");
            return;
        }
        if (customEditView3.getInputTitle().length() < 6) {
            mSvProgressHUD.showInfoWithStatus("密码不能小于6位");
            return;
        }
        if (!customEditView2.getInputTitle().equals(customEditView3.getInputTitle())) {
            mSvProgressHUD.showInfoWithStatus("2次输入密码不一致,请认真输入");
            return;
        }

        submData();
    }

    /**
     * 提交登录数据
     */
    private void submData() {
        mSvProgressHUD.showWithStatus("请稍等,数据提交中...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();

        String tel = mSP.getString("telPhone", null);
        Logger.i(TAG, "tel.." + tel);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("oldpass", customEditView1.getInputTitle());//旧密码
        weakHashMap.put("newPass", customEditView2.getInputTitle());//新密码

        String url = App.getRqstUrl(App.headurl + "changePass", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(UpdateRequestError, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            mHandler.sendEmptyMessage(UpdateRequestSuccess);
                        } else {
                            mHandler.obtainMessage(UpdateRequestError, aJson.getMsg()).sendToTarget();
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            ToastUtils.showShortToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            isReturn();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isReturn();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void isReturn() {
        if (!TextUtils.isEmpty(customEditView1.getInputTitle())
                || !TextUtils.isEmpty(customEditView2.getInputTitle())
                || !TextUtils.isEmpty(customEditView3.getInputTitle())) {
            final BtmDialog dialog = new BtmDialog(this, "温馨提示", "确定放弃本次操作吗?");
            AlertDialogHelper.BtmDialogDerive1(dialog, false, true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    dialog.dismiss();
                }
            }, null);
        } else {
            finish();
        }
    }

    private void clearInput() {
        customEditView1.setInputTitle(null);
        customEditView2.setInputTitle(null);
        customEditView3.setInputTitle(null);
    }
}
