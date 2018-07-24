package phone.ktv.activitys.already_activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import phone.ktv.R;
import phone.ktv.adaters.RinkingListAdater;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.ScreenUtils;

/**
 * 已点搜索
 */
public class AlreadySearchActivity extends AppCompatActivity {

    private static final String TAG = "AlreadySearchActivity";

    Context mContext;

    private ImageView mSrcBack11;//返回
    private ImageView mVoice;//语音功能
    private TextView mSongType;//歌名/歌星
    private EditText mSearchContent;//搜索内容=
    private TextView mSearch;//搜索按钮

    private ListView mListView;
    private RinkingListAdater mRinkingAdater;

    private List<MusicPlayBean> musicPlayBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.already_search_layout);

        initView();
        initLiter();
    }

    private void initView() {
        musicPlayBeans=new ArrayList<>();

        mContext = AlreadySearchActivity.this;

        mSrcBack11 = findViewById(R.id.src_back11_ivw);
        mSongType = findViewById(R.id.songType_tvw11);
        mVoice= findViewById(R.id.voice12_ivw);
        mSearchContent= findViewById(R.id.search_content_edt);
        mSearch= findViewById(R.id.text_search11_tvw);
        mListView = findViewById(R.id.list112_view);

        musicPlayBeans.add(new MusicPlayBean());


        mRinkingAdater=new RinkingListAdater(mContext,R.layout.item_ringlist_layout,musicPlayBeans);
        mListView.setAdapter(mRinkingAdater);
    }

    private void initLiter() {
        mSrcBack11.setOnClickListener(new MyOnClickBackReturn());//返回事件
        mSongType.setOnClickListener(new MyOnClickSongType());
        mVoice.setOnClickListener(new MyOnClickListenerVoice());
        mSearch.setOnClickListener(new MyOnClickListenerSearch());
    }

    /**
     * 搜索事件
     */
    private class MyOnClickListenerSearch implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    /**
     * 语音
     */
    private class MyOnClickListenerVoice implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    /**
     * 显示选择框
     */
    public class MyOnClickSongType implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showPoWindo();
        }
    }

    private void showPoWindo() {
        int height= ScreenUtils.getScreenHeight(mContext);
        View strView = getLayoutInflater().inflate(R.layout.pow_already_btn, null, false);
        final PopupWindow window = new PopupWindow(strView,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        window.setAnimationStyle(R.style.AnimationFade);
        window.setFocusable(false);
        window.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, -height/10);

        final TextView song1=strView.findViewById(R.id.song_btn_1);
        final TextView song2=strView.findViewById(R.id.song_btn_2);
        song1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongType.setText(song1.getText().toString().trim());
                window.dismiss();
            }
        });

        song2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongType.setText(song2.getText().toString().trim());
                window.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
