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
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.SPUtil;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private String TAG = "MusicService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private App app;
    private SPUtil spUtil;
    private int index = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        app = (App) getApplication();
        setMediaListene();

        spUtil = new SPUtil(this);
        getindex();

        getList();

        IntentFilter filter = new IntentFilter();
        filter.addAction(App.PLAY);
        filter.addAction(App.LAST);
        filter.addAction(App.NEXT);
        registerReceiver(receiver, filter);


    }

    private int getindex() {
        return index = spUtil.getInt("play_index", 0);
    }


    private List<MusicPlayBean> playlist = new ArrayList<>();

    private List<MusicPlayBean> getList() {
        try {
            playlist = App.mDb.selector(MusicPlayBean.class).findAll();
//            System.out.println(playlist.size() + "@@@@@@@@@");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playlist;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getindex();//刷新下标
            if (intent.getAction().equals(App.PLAY)) {
                System.out.println("播放");
                try {
                    playerSong();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        app.setMediaPlayer(player);
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
        mediaPlayer.start();
        sendBroadcast(new Intent(App.STARTPLAY));
    }


    //上一曲
    private void last() {
        try {
            Log.e(TAG, index + "@@@@" + (playlist.size() - 1) + "@@@@" + (index < playlist.size() - 1));
            getList();
            getindex();
            if (index > 0) {
                index--;
            } else {
                index = playlist.size() - 1;
            }
            playerSong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下一曲
    private void next() {
        try {
            getList();
            getindex();
//            Log.e(TAG, index + "@@@@" + (playlist.size() - 1) + "@@@@" + (index < playlist.size() - 1));
//            if (index < playlist.size() - 1) {
//                index++;
//            } else {
//                index = 0;
//            }
//            playerSong();

            try {
                int playmodel = app.getPlaymodel();
                if (playmodel == 0) {
                    //顺序
                    if (++index < playlist.size()) {
                        try {
                            playerSong();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        index = 0;
                        playerSong();
                    }
                    Logger.d(TAG, "顺序" + index);
                } else if (playmodel == 1) {
                    //随机
                    index = getRandom();
                    playerSong();
                    Logger.d(TAG, "随机" + index);
                } else {
                    //循坏
                    playerSong();
                    Logger.d(TAG, "单曲循坏" + index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRandom() {
        try {
            if (playlist.size() == 1) {
                return 0;
            } else {
                Random random = new Random();
                int s = random.nextInt(playlist.size() - 1) % (playlist.size() - 1 - 0 + 1) + 0;
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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


    // 播放哪一首歌
    private void playerSong() throws IllegalStateException,
            IOException {
        try {
            spUtil.putInt("play_index", index);
            sendBroadcast(new Intent(App.SWITCHPLAY));
            Log.e(TAG, playlist.get(index).name + "---" + playlist.get(index).path);
            app.getMediaPlayer().stop();
            app.getMediaPlayer().reset();
            app.getMediaPlayer().setDataSource(this,
                    Uri.parse(playlist.get(index).path));
            app.getMediaPlayer().prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
