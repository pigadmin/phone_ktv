package phone.ktv.activitys.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import phone.ktv.MainActivity;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

public class MiniPlayer extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

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

        getList();
        initPlayer();

        IntentFilter filter = new IntentFilter();
        filter.addAction(App.START);
        mContext.registerReceiver(receiver, filter);
    }

    private List<MusicPlayBean> playlist = new ArrayList<>();

    private List<MusicPlayBean> getList() {

        try {
            playlist = App.mDb.selector(MusicPlayBean.class).findAll();
            if (playlist != null && !playlist.isEmpty()) {
//                Picasso.with(this).load(playlist.get(0).n)
                if (!player.isPlaying()) {
                    player_name.setText(playlist.get(mSP.getInt("play_index", 0)).name);
                    player_singer.setText(playlist.get(mSP.getInt("play_index", 0)).singerName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlist;
    }

    private ImageView singer_icon;
    private SeekBar player_progress;
    private TextView player_name, player_singer;
    private ImageView player_last, player_play, player_next;
    private VideoView player;
    private LinearLayout llt_115;

    private void initPlayer() {
        llt_115 = view.findViewById(R.id.llt_115);
        llt_115.setOnClickListener(this);

        singer_icon = view.findViewById(R.id.singer_icon);
        singer_icon.setOnClickListener(this);

        player_progress = view.findViewById(R.id.player_progress);
        player_progress.setOnSeekBarChangeListener(this);
        player_name = view.findViewById(R.id.player_name);
        player_singer = view.findViewById(R.id.player_singer);

        player_last = view.findViewById(R.id.player_last);
        player_last.setOnClickListener(this);
        player_play = view.findViewById(R.id.player_play);
        player_play.setOnClickListener(this);
        player_next = view.findViewById(R.id.player_next);
        player_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
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
                    if (app.getMediaPlayer() == null) {
                        ToastUtils.showShortToast(mContext, "第一次播放");
                        mContext.sendBroadcast(new Intent(App.PLAY));
                        player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                    } else {
                        if (app.getMediaPlayer().isPlaying()) {
                            ToastUtils.showShortToast(mContext, "暂停");
                            app.getMediaPlayer().pause();
                            player_play.setBackgroundResource(R.mipmap.bottom_icon_3);
                        } else {
                            ToastUtils.showShortToast(mContext, "播放");
                            app.getMediaPlayer().start();
                            player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                        }
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
//                    if (app.getMediaPlayer() == null)
//                        return;
//                    if (app.getMediaPlayer().isPlaying()) {
//                        app.getMediaPlayer().pause();
//                        player_play.setBackgroundResource(R.mipmap.bottom_icon_3);
//                    }
                    mContext.startActivity(new Intent(mContext, PlayerActivity.class));
                }
                break;
        }
    }

    private MusicPlayBean now;
    private CountDownTimer timer = null;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(App.START)) {
                    now = (MusicPlayBean) intent.getSerializableExtra("key");
                    player_name.setText(now.name);
                    player_singer.setText(now.singerName);

                    player_progress.setMax(app.getMediaPlayer().getDuration());

                    if (timer != null)
                        timer.cancel();
                    timer = new CountDownTimer(app.getMediaPlayer().getDuration(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            player_progress.setProgress(app.getMediaPlayer().getCurrentPosition());
                        }

                        @Override
                        public void onFinish() {
                            timer.cancel();
                        }
                    }.start();

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

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
