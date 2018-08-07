package phone.ktv.activitys.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import phone.ktv.MainActivity;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

public class MiniPlayer extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private String TAG = "MiniPlayer";


    private View view;
    private Context mContext;
    private App app;
    private SPUtil mSP;

    public MiniPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        app = (App) context.getApplicationContext();

        mSP = new SPUtil(context);
        view = LayoutInflater.from(context).inflate(R.layout.player_mini, this);

        initPlayer();

        getList();


        IntentFilter filter = new IntentFilter();
        filter.addAction(App.START);
        filter.addAction(App.UPDATEPLAYER);
        mContext.registerReceiver(receiver, filter);
    }

    private void ColseRece() {
        mContext.unregisterReceiver(receiver);
    }


    private void UpdatePlayer() {

        try {//                Picasso.with(this).load(playlist.get(0).n)
            player_name.setText(playlist.get(mSP.getInt("play_index", 0)).name);
            player_singer.setText(playlist.get(mSP.getInt("play_index", 0)).singerName);

            handler.sendEmptyMessage(updateprocess);
            PlayStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<MusicPlayBean> playlist = new ArrayList<>();

    private List<MusicPlayBean> getList() {

        try {
            playlist = App.mDb.selector(MusicPlayBean.class).findAll();
//            if (playlist != null && !playlist.isEmpty()) {
//
//                UpdatePlayer();
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlist;
    }

    private ImageView singer_icon;
    private SeekBar player_progress;
    private TextView player_name, player_singer;
    private ImageView player_last, player_play, player_next;
    private MediaPlayer player;
    private LinearLayout llt_115;

    private void initPlayer() {
        try {
            player = app.getMediaPlayer();

            llt_115 = view.findViewById(R.id.llt_115);
            llt_115.setOnClickListener(this);

            singer_icon = view.findViewById(R.id.singer_icon);
            singer_icon.setOnClickListener(this);

            player_progress = view.findViewById(R.id.player_progress);
            player_progress.setOnSeekBarChangeListener(this);

            player_progress.setMax(player.getDuration());

            player_name = view.findViewById(R.id.player_name);
            player_singer = view.findViewById(R.id.player_singer);

            player_last = view.findViewById(R.id.player_last);
            player_last.setOnClickListener(this);
            player_play = view.findViewById(R.id.player_play);
            player_play.setOnClickListener(this);
            player_next = view.findViewById(R.id.player_next);
            player_next.setOnClickListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.player_last://上一首
                    if (getList() == null || getList().isEmpty()) {
                        ToastUtils.showShortToast(mContext, "先添加");
                        break;
                    } else {
                        mContext.sendBroadcast(new Intent(App.LAST));
                    }
                    break;
                case R.id.player_play://播放暂停
                    if (getList() == null || getList().isEmpty()) {
                        ToastUtils.showShortToast(mContext, "先添加");
                    } else {
                        System.out.println("************************点击" + app.getPlaystatus());
                        switch (app.getPlaystatus()) {
                            case 0:
                                mContext.sendBroadcast(new Intent(App.PLAY));
                                app.setPlaystatus(1);
                                player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                                break;
                            case 1://播放可暂停
                                app.setPlaystatus(2);
                                player.pause();
                                player_play.setBackgroundResource(R.mipmap.bottom_icon_3);
                                break;
                            case 2://暂停可播放
                                app.setPlaystatus(1);
                                player.start();
                                player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                                break;
                        }

                    }
                    break;
                case R.id.player_next://下一首
                    if (getList() == null || getList().isEmpty()) {
                        ToastUtils.showShortToast(mContext, "先添加");
                    } else {
                        mContext.sendBroadcast(new Intent(App.NEXT));
                    }
                    break;
                case R.id.llt_115:
                case R.id.singer_icon://图标
                    if (getList() == null || getList().isEmpty()) {
                        ToastUtils.showShortToast(mContext, "先添加");
                        break;
                    } else {
                        mContext.startActivity(new Intent(mContext, PlayerActivity.class));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PlayStatus() {
        switch (app.getPlaystatus()) {
            case 1:
                player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                break;
            case 2:
                player_play.setBackgroundResource(R.mipmap.bottom_icon_3);
                break;
        }
    }


    private MusicPlayBean now;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(App.START)) {
                    now = (MusicPlayBean) intent.getSerializableExtra("key");
                    player_name.setText(now.name);
                    player_singer.setText(now.singerName);
                    handler.sendEmptyMessage(updateprocess);
                    player_progress.setMax(player.getDuration());
                } else if (intent.getAction().equals(App.UPDATEPLAYER)) {
                    UpdatePlayer();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeMessages(updateprocess);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        try {
            progress = seekBar.getProgress();
            handler.sendEmptyMessage(SEEKTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int progress = 0;//拖动进度
    private final int SEEKTO = 1;//跳转指定进度
    private final int updateprocess = 2;//更新进度条
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEEKTO:
                    player.seekTo(progress);
                    handler.sendEmptyMessage(updateprocess);
                    break;
                case updateprocess:
                    player_progress.setProgress(player.getCurrentPosition());
                    handler.sendEmptyMessageDelayed(updateprocess, 1 * 1000);
                    break;
            }
        }
    };


}
