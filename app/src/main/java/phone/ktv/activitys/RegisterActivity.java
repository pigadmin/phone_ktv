package phone.ktv.activitys;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import phone.ktv.R;
import phone.ktv.tootls.IntentUtils;

/**
 * 注册页
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    Context mContext;

    private TextView mRegistProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);

        mContext=RegisterActivity.this;
        initView();
        initLiter();
    }

    private void initView(){
        mRegistProtocol=findViewById(R.id.regist_protocol_tvw);
        mRegistProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    }

    private void initLiter(){
        mRegistProtocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regist_protocol_tvw:
                IntentUtils.thisToOther(mContext,UserProtocolActivity.class);
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
