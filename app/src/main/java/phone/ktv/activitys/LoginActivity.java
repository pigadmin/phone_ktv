package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import phone.ktv.R;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomTopTitleView;

/**
 * 登录页
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private Context mContext;

//    private EditText mInputPhone;//手机号
//    private EditText mInputPsd;//密码
    private CheckBox mJizhuPsd;//记住密码
    private TextView mWangjiPsd;//忘记密码
    private TextView mLogin;//登陆
    private TextView mReginster;//注册

    private CustomTopTitleView mTopTitleView1;//返回事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        initView();
        initLiter();
    }

    private void initView() {
        mContext = LoginActivity.this;

        mTopTitleView1=findViewById(R.id.customTopTitleView1);

//        mInputPhone=findViewById(R.id.phone_edt);
//        mInputPsd=findViewById(R.id.password_edt);
        mJizhuPsd=findViewById(R.id.jizhu_ckb);
        mWangjiPsd=findViewById(R.id.wangji_tvw);
        mLogin=findViewById(R.id.login_tvw);
        mReginster=findViewById(R.id.register_tvw);
    }

    private void initLiter(){
        mWangjiPsd.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mReginster.setOnClickListener(this);
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wangji_tvw:
                IntentUtils.thisToOther(mContext,ForgetPsdActivity.class);
                break;

            case R.id.login_tvw:
                loginClick();
                break;

            case R.id.register_tvw:
                IntentUtils.thisToOther(mContext,RegisterActivity.class);
                break;
        }
    }

    /**
     * 登录事件
     */
    private void loginClick(){
//        if (TextUtils.isEmpty(mInputPhone.getText().toString().trim())){
//            ToastUtils.showLongToast(mContext,"请输入手机号码");
//            return;
//        }
//        if (mInputPhone.getText().toString().trim().length()!=11){
//            ToastUtils.showLongToast(mContext,"请输入正确的手机号码");
//            return;
//        }
//        if (TextUtils.isEmpty(mInputPsd.getText().toString().trim())){
//            ToastUtils.showLongToast(mContext,"请输入密码");
//            return;
//        }
        submLoginData();
    }

    /**
     * 提交登录数据
     */
    private void submLoginData(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
