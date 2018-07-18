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
import phone.ktv.views.CustomTopTitleView;

/**
 * 忘记密码
 */
public class ForgetPsdActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ForgetPsdActivity";
    private Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件


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
    }

    private void initLiter(){
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.wangji_tvw:
//                break;

        }
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
