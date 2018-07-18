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
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomEditView;
import phone.ktv.views.CustomTopTitleView;

/**
 * 忘记密码
 */
public class ForgetPsdActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ForgetPsdActivity";
    private Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    private CustomEditView customEditView1;//手机号
    private CustomEditView customEditView2;//验证码
    private CustomEditView customEditView3;//新密码
    private CustomEditView customEditView4;//确认新密码

    private TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psd_activity_layout);
        initView();
        initLiter();
    }

    private void initView() {
        mContext = ForgetPsdActivity.this;

        mTopTitleView1=findViewById(R.id.customTopTitleView1);
        customEditView1=findViewById(R.id.customEditView1);
        customEditView2=findViewById(R.id.customEditView2);
        customEditView3=findViewById(R.id.customEditView3);
        customEditView4=findViewById(R.id.customEditView4);

        mRegister=findViewById(R.id.register_tvw);
    }

    private void initLiter(){
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_tvw://注册
                registerClick();
                break;
        }
    }

    private void registerClick(){
        if (TextUtils.isEmpty(customEditView1.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入手机号码");
            return;
        }
        if (customEditView1.getInputTitle().length()!=11){
            ToastUtils.showLongToast(mContext,"请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(customEditView2.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入验证码");
            return;
        }
        if (customEditView2.getInputTitle().length()!=6){
            ToastUtils.showLongToast(mContext,"请输入正确的验证码");
            return;
        }
        if (TextUtils.isEmpty(customEditView3.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(customEditView4.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请确认新密码");
            return;
        }
        submRegisterData();
    }

    /**
     * 提交注册数据
     */
    private void submRegisterData(){

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
