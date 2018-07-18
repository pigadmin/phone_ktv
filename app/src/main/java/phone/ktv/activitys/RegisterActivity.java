package phone.ktv.activitys;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import phone.ktv.R;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomEditView;
import phone.ktv.views.CustomTopTitleView;

/**
 * 注册页
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    Context mContext;

    private TextView mRegistProtocol;

    private CustomTopTitleView mTopTitleView1;//返回事件
    private CustomEditView customEditView1;
    private CustomEditView customEditView2;
    private CustomEditView customEditView3;
    private CustomEditView customEditView4;

    private TextView mRegister;//注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);

        initView();
        initLiter();
    }

    private void initView(){
        mContext=RegisterActivity.this;

        mTopTitleView1=findViewById(R.id.customTopTitleView1);

        mRegister=findViewById(R.id.register_tvw);

        mRegistProtocol=findViewById(R.id.regist_protocol_tvw);
        mRegistProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

        customEditView1=findViewById(R.id.customEditView1);
        customEditView2=findViewById(R.id.customEditView2);
        customEditView3=findViewById(R.id.customEditView3);
        customEditView4=findViewById(R.id.customEditView4);
    }

    private void initLiter(){
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件

        mRegistProtocol.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regist_protocol_tvw:
                IntentUtils.thisToOther(mContext,UserProtocolActivity.class);
                break;

            case R.id.register_tvw://注册
                registerClick();
                break;
        }
    }

    /**
     * 注册
     */
    private void registerClick(){
        if (TextUtils.isEmpty(customEditView1.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(customEditView2.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入手机号");
            return;
        }
        if (customEditView2.getInputTitle().length()!=11){
            ToastUtils.showLongToast(mContext,"请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(customEditView3.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(customEditView4.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入密码");
            return;
        }
        submLoginData();
    }

    /**
     * 提交注册数据
     */
    private void submLoginData(){

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
