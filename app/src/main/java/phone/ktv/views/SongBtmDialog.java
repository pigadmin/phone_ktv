package phone.ktv.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import phone.ktv.R;

/**
 * 自定义提示框
 */
public class SongBtmDialog extends Dialog {

    public TextView mSongName;//歌名
    public TextView mSingName;//歌手名
    public TextView mStartPaly;//开始播放
    public TextView mAdd;//添加收藏
    public TextView mPoint;//添加已点
    public TextView mCanel;//取消

    public SongBtmDialog(Context context, String title, String message) {
        super(context, R.style.CustomDialog);
        setContentView(R.layout.song_alert_diaog);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        initView(title, message);
    }

    private void initView(String title, String message) {
        mSongName = (TextView) findViewById(R.id.pow_tvw_1);
        mSingName = (TextView) findViewById(R.id.pow_tvw_2);
        mStartPaly = (TextView) findViewById(R.id.pow_tvw_3);
        mAdd = (TextView) findViewById(R.id.pow_tvw_4);
        mPoint = (TextView) findViewById(R.id.pow_tvw_5);
        mCanel = (TextView) findViewById(R.id.pow_tvw_6);
        mSongName.setText(title);
        mSingName.setText(message);
    }

    public SongBtmDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SongBtmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
