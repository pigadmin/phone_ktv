package phone.ktv.activitys.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        filter.addAction(App.STARTPLAY);
        filter.addAction(App.SWITCHPLAY);
        mContext.registerReceiver(receiver, filter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                getList();
                if (intent.getAction().equals(App.SWITCHPLAY)) {
                    handler.sendEmptyMessage(SWITCHPLAY);
                } else if (intent.getAction().equals(App.STARTPLAY)) {
                    handler.sendEmptyMessage(STARTPLAY);
                }
//                else if (intent.getAction().equals(App.DESTROY)) {
//                    context.unregisterReceiver(receiver);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private List<MusicPlayBean> playlist = new ArrayList<>();

    private List<MusicPlayBean> getList() {
        try {
            playlist = App.mDb.selector(MusicPlayBean.class).findAll();
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
    private LinearLayout llt_11501;

    private void initPlayer() {
        try {
            player = app.getMediaPlayer();

            llt_11501 = view.findViewById(R.id.llt_11501);
            llt_11501.setOnClickListener(this);

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
            List<MusicPlayBean> list = getList();
            switch (v.getId()) {
                case R.id.player_last://上一首
                    if (list == null || list.isEmpty()) {
                        ToastUtils.showShortToast(mContext, "去添加");
                        break;
                    } else {
                        app.setPlaystatus(1);
                        mContext.sendBroadcast(new Intent(App.LAST));
                    }
                    break;
                case R.id.player_play://播放暂停
                    if (list == null || list.isEmpty()) {
                        ToastUtils.showShortToast(mContext, "去添加");
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
                    if (list == null || list.isEmpty()) {
                        ToastUtils.showShortToast(mContext, "去添加");
                    } else {
                        app.setPlaystatus(1);
                        mContext.sendBroadcast(new Intent(App.NEXT));
                    }
                    break;
                case R.id.llt_11501:
                case R.id.singer_icon://图标
                    if (list == null || list.isEmpty()) {
                        ToastUtils.showShortToast(mContext, "去添加");
                        break;
                    } else if (!app.getMediaPlayer().isPlaying()) {
                        ToastUtils.showShortToast(mContext, "去播放");
                    } else {
                        mContext.startActivity(new Intent(mContext, PlayerActivity.class));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int play_index = 0;
    private int progress = 0;//拖动进度
    private final int SEEKTO = 1;//跳转指定进度
    private final int UPDATEPROCESS = 2;//更新进度条
    private final int SWITCHPLAY = 3;//更新播放信息
    private final int STARTPLAY = 4;//更新播放信息
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case SEEKTO://拖动
                        player.seekTo(progress);
                        handler.sendEmptyMessage(UPDATEPROCESS);
                        break;
                    case UPDATEPROCESS://实时更新进度
                        player_progress.setProgress(player.getCurrentPosition());
                        handler.sendEmptyMessageDelayed(UPDATEPROCESS, 1 * 1000);
                        break;
                    case SWITCHPLAY://更新播放信息
                        play_index = mSP.getInt("play_index", 0);
                        player_name.setText(playlist.get(play_index).name);
                        player_singer.setText(playlist.get(play_index).singerName);
                        switch (app.getPlaystatus()) {
                            case 1:
                                player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                                break;
                            case 2:
                                player_play.setBackgroundResource(R.mipmap.bottom_icon_3);
                                break;
                        }
                        break;
                    case STARTPLAY://去更新进度
                        player_progress.setMax(player.getDuration());
                        handler.sendEmptyMessage(UPDATEPROCESS);
                        break;
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
        handler.removeMessages(UPDATEPROCESS);
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
}
