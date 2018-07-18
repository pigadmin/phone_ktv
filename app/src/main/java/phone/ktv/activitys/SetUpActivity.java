package phone.ktv.activitys;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import phone.ktv.R;
import phone.ktv.views.CustomTopTitleView;

/**
 * 设置
 */
public class SetUpActivity extends AppCompatActivity {

    private static final String TAG = "SetUpActivity";

    Context mContext;

    private CustomTopTitleView mTopTitleView1;//返回事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity_layout);

        initView();
        initLiter();
    }

    private void initView(){
        mContext=SetUpActivity.this;

        mTopTitleView1=findViewById(R.id.customTopTitleView1);
    }

    private void initLiter(){
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
