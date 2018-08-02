package phone.ktv.activitys.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import org.xutils.ex.DbException;

import java.util.List;

import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.FULL;

public class PlayerActivity extends  Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private AudioManager audioManager;
    int screenWidth;
    int screenHeight;
    int maxVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        init();
        initPlaylist();

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private List<MusicPlayBean> playlist;

    private void initPlaylist() {
        try {
            playlist = App.mDb.selector(MusicPlayBean.class).findAll();
            if (!playlist.isEmpty()) {
                player.setVideoURI(Uri.parse(playlist.get(0).path));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VideoView player;

    private void init() {
        player = findViewById(R.id.player);
        FULL.star(player);
        player.setMediaController(new MediaController(this));
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//把事件传递给手势识别器（注：对事件只进行了解析处理，没有拦截，解析成手势识别的单击、双击、长按）
//        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                startX = event.getX();
                downVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                handler.removeMessages(HIDE_CONTROL);
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float distanceY = startY - endY;
                if (startX > screenWidth / 2) {
//屏幕右半部分上滑，声音变大，下滑，声音变小
                    int touchRang = Math.min(screenWidth, screenHeight);
//int curvol= (int) (downVol+(distance/screenHeight)*maxVolume);
                    int curvol = (int) (downVol + (distanceY / touchRang) * maxVolume);//考虑到横竖屏切换的问题
                    int volume = Math.min(Math.max(0, curvol), maxVolume);

                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                    if (distanceY > FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        updateVolume(volume);
                    }
                    if (distanceY < FLING_MIN_DISTANCE
                            && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        updateVolume(volume);
                    }
                } else {
//屏幕左半部分上滑，亮度变大，下滑，亮度变小
                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                    if (distanceY > FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        setBrightness(20);
                    }
                    if (distanceY < FLING_MIN_DISTANCE
                            && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        setBrightness(-20);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
//                handler.sendEmptyMessageDelayed(HIDE_CONTROL, 5000);
                break;
        }

        return super.onTouchEvent(event);
    }

    private void updateVolume(int volume) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
    }

    private float startY;//记录手指按下时的Y坐标
    private float startX;//记录手指按下时的Y坐标
    private int downVol;//记录手指按下时的音量
    private Vibrator vibrator;//手机震动器

    //不要忘记震动权限<uses-permission android:name="android.permission.VIBRATE" />
    /*
     * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮
     */
    public void setBrightness(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
// if (lp.screenBrightness <= 0.1) {
// return;
// }
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
//            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            long[] pattern = {10, 200}; // OFF/ON/OFF/ON... 关闭10秒震动200毫秒，不停切换
//            vibrator.vibrate(pattern, -1);
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
//            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            long[] pattern = {10, 200}; // OFF/ON/OFF/ON...
//            vibrator.vibrate(pattern, -1);
        }
        getWindow().setAttributes(lp);
    }


}
