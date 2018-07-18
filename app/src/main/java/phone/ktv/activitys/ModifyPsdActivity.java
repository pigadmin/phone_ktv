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
 * 修改密码
 */
public class ModifyPsdActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ModifyPsdActivity";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件
    private CustomEditView customEditView1;
    private CustomEditView customEditView2;
    private CustomEditView customEditView3;

    private TextView mDetermine;//确定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_psd_activity_layout);

        initView();
        initLiter();
    }

    private void initView(){
        mContext=ModifyPsdActivity.this;

        mDetermine=findViewById(R.id.determine_tvw);
        mTopTitleView1=findViewById(R.id.customTopTitleView1);
        customEditView1=findViewById(R.id.customEditView1);
        customEditView2=findViewById(R.id.customEditView2);
        customEditView3=findViewById(R.id.customEditView3);
    }

    private void initLiter(){
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
        mDetermine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.determine_tvw://确定
                updateClick();
                break;
        }
    }

    /**
     * 登录事件
     */
    private void updateClick(){
        if (TextUtils.isEmpty(customEditView1.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入您的原密码");
            return;
        }
        if (TextUtils.isEmpty(customEditView2.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请输入您的新密码");
            return;
        }
        if (TextUtils.isEmpty(customEditView3.getInputTitle())){
            ToastUtils.showLongToast(mContext,"请确认您的新密码");
            return;
        }
        submData();
    }

    /**
     * 提交登录数据
     */
    private void submData(){

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
