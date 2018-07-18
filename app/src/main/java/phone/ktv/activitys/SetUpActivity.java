package phone.ktv.activitys;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import phone.ktv.R;

/**
 * 设置
 */
public class SetUpActivity extends AppCompatActivity {

    private static final String TAG = "SetUpActivity";

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity_layout);

        mContext=SetUpActivity.this;
        initView();
        initLiter();
    }

    private void initView(){

    }

    private void initLiter(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
