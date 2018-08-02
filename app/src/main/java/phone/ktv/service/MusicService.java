package phone.ktv.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.VideoView;

import java.util.List;

import phone.ktv.activitys.songdesk_activitys.SongDeskActivity4;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.ToastUtils;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = (App) getApplication();
        setMediaListene();

        IntentFilter filter = new IntentFilter();
        filter.addAction(App.PLAY);
        filter.addAction(App.LAST);
        filter.addAction(App.NEXT);
        registerReceiver(receiver, filter);
        initPlaylist();
    }

    private List<MusicPlayBean> playlist;

    private void initPlaylist() {
        try {
//            playlist = App.mDb.selector(MusicPlayBean.class).findAll();
            playlist = app.getTestlist();
            System.out.println(playlist.size() + "@@@@@@@@@");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(App.PLAY)) {
                System.out.println("播放");
                playerSong();
            } else if (intent.getAction().equals(App.LAST)) {
                System.out.println("上一首");
                last();
            } else if (intent.getAction().equals(App.NEXT)) {
                System.out.println("下一首");
                next();
            }


        }
    };

    private MediaPlayer player;

    private void setMediaListene() {
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        System.out.println("下一首");
        next();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        System.out.println("准备播放。。。。");
        app.setMediaPlayer(mediaPlayer);
        mediaPlayer.start();
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", app.getTestlist().get(index));
        sendBroadcast(new Intent(App.START).putExtras(bundle));

    }

    //上一曲
    private void last() {
        if (index > 0) {
            index--;
        } else {
            index = app.getTestlist().size() - 1;
        }
        playerSong();
    }

    //下一曲
    private void next() {
        if (index < app.getTestlist().size() - 1) {
            index++;
        } else {
            index = 0;
        }
        playerSong();
    }


    // 停止播放
    private void stopMusic() {
        if (player.isPlaying()) {
            player.stop();
        }
    }


    // 继续暂停播放
    private void restMusic() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
    }

    private int index = 0;

    // 播放歌曲
    private void playerSong() {
        try {
            if (app.getTestlist() == null || app.getTestlist().isEmpty())
                return;
            player.stop();
            player.reset();
            System.out.println(app.getTestlist().get(index).name);
            player.setDataSource(this,
                    Uri.parse(app.getTestlist().get(index).path));
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
