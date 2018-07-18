package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import phone.ktv.R;

/**
 * 忘记密码
 */
public class ForgetPsdActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ForgetPsdActivity";
    private Context mContext;

    private EditText mInputPhone;//手机号
    private EditText mInputPsd;//密码
    private CheckBox mJizhuPsd;//记住密码
    private TextView mWangjiPsd;//忘记密码
    private TextView mLogin;//登陆
    private TextView mReginster;//注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psd_activity_layout);
        initView();
        initLiter();
    }

    private void initView() {
        mContext = ForgetPsdActivity.this;

        mInputPhone=findViewById(R.id.phone_edt);
        mInputPsd=findViewById(R.id.password_edt);
        mJizhuPsd=findViewById(R.id.jizhu_ckb);
        mWangjiPsd=findViewById(R.id.wangji_tvw);
        mLogin=findViewById(R.id.login_tvw);
        mReginster=findViewById(R.id.register_tvw);
    }

    private void initLiter(){
        mWangjiPsd.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mReginster.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wangji_tvw:
                break;

            case R.id.login_tvw:
                break;

            case R.id.register_tvw:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
