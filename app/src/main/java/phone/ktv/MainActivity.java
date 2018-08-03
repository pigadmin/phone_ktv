package phone.ktv;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.astuetz.PagerSlidingTabStripExtends;
import com.bigkoo.svprogresshud.SVProgressHUD;

import java.util.List;

import phone.ktv.activitys.CollectionListActivity;
import phone.ktv.activitys.LatelyListActivity;
import phone.ktv.activitys.LoginActivity;
import phone.ktv.activitys.ModifyPsdActivity;
import phone.ktv.activitys.ProductRecyActivity;
import phone.ktv.activitys.SetUpActivity;
import phone.ktv.activitys.already_activitys.AlreadySearchListActivity;
import phone.ktv.activitys.player.PlayerActivity;
import phone.ktv.adaters.TabAdater;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.AlertDialogHelper;
import phone.ktv.tootls.Contants;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.PermissionRequestUtil;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.tootls.UpdateVersionUtils;
import phone.ktv.views.BtmDialog;
import phone.ktv.views.CoordinatorMenu;
import phone.ktv.views.CustomTextView;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        PermissionRequestUtil.PermissionRequestListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "MainActivity";
    private PagerSlidingTabStripExtends mNewsTabs;
    private ViewPager mVpTab;
    private onPageChangeListener mPagerChange;

    private CoordinatorMenu mCoordinatorMenu;
    private ImageView mHeadIv;//侧滑

    private LinearLayout mLoging;//登录

    private Context mContext;

    private CustomTextView mCustomTextView1;//收藏列表
    private CustomTextView mCustomTextView2;//最近播放
    private CustomTextView mCustomTextView3;//产品中心
    private CustomTextView mCustomTextView4;//修改密码
    private CustomTextView mCustomTextView5;//版本更新

    private LinearLayout mSetup;//设置
    private LinearLayout mSignOut;//退出

    private ImageView mSearch;//搜索

    private ImageView mUserSrc;//用户头像
    private TextView mUserName;//用户名
    private TextView mTelPhone;//手机号

    private SPUtil mSP;

    private SVProgressHUD mSvProgressHUD;

    private long firstTime;

    private TextView mSintUser;//退出账户
    private App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App) getApplication();
        mContext = MainActivity.this;

        //动态申请权限(动态申请的权限需要在AndroidManifest.xml中声明)
        PermissionRequestUtil.judgePermissionOver23(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CONTACTS},
                Contants.PermissRequest);

//        PermissionRequestUtil.showSuspeWindow(MainActivity.this);
//        startService(new Intent(this, MyService.class));

        initView();
        initMenuView();
        initPlayer();
        initListener();

        initPlaylist();

        IntentFilter filter = new IntentFilter();
        filter.addAction(App.START);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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

    private ImageView singer_icon;
    private SeekBar player_progress;
    private TextView player_name, player_singer;
    private ImageView player_last, player_play, player_next;
    private VideoView player;

    private void initPlayer() {
        singer_icon = findViewById(R.id.singer_icon);
        singer_icon.setOnClickListener(this);

        player_progress = findViewById(R.id.player_progress);
        player_progress.setOnSeekBarChangeListener(this);
        player_name = findViewById(R.id.player_name);
        player_singer = findViewById(R.id.player_singer);

        player_last = findViewById(R.id.player_last);
        player_last.setOnClickListener(this);
        player_play = findViewById(R.id.player_play);
        player_play.setOnClickListener(this);
        player_next = findViewById(R.id.player_next);
        player_next.setOnClickListener(this);

//        player = new VideoView(this);
        player = findViewById(R.id.player);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    private List<MusicPlayBean> playlist;

    private void initPlaylist() {

        try {
            playlist = App.mDb.selector(MusicPlayBean.class).findAll();
            if (playlist != null && !playlist.isEmpty()) {
//                Picasso.with(this).load(playlist.get(0).n)
                if (!player.isPlaying()) {
                    player_name.setText(playlist.get(0).name);
                    player_singer.setText(playlist.get(0).singerName);
//                    player.setVideoURI(Uri.parse(playlist.get(0).path));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initMenuView() {
        mLoging = findViewById(R.id.loging_llt);

        mCustomTextView1 = findViewById(R.id.custom_menu_1);
        mCustomTextView2 = findViewById(R.id.custom_menu_2);
        mCustomTextView3 = findViewById(R.id.custom_menu_3);
        mCustomTextView4 = findViewById(R.id.custom_menu_4);
        mCustomTextView5 = findViewById(R.id.custom_menu_5);

        mSetup = findViewById(R.id.setup_llt);
        mSignOut = findViewById(R.id.sign_out_llt);
        mSearch = findViewById(R.id.main_btn_search);

        mUserSrc = findViewById(R.id.user_src_ivw);
        mUserName = findViewById(R.id.user_name_tvw);
        mTelPhone = findViewById(R.id.tel_phone_tvw);

        mSintUser = findViewById(R.id.sint_user_tvw);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateState();
    }

    private void updateState() {
        if (mSP != null) {
            String username = mSP.getString("username", null);
            String telPhone = mSP.getString("telPhone", null);

            mUserName.setText(TextUtils.isEmpty(username) ? "立即登录" : username);
            mTelPhone.setText(TextUtils.isEmpty(telPhone) ? null : telPhone);
            mUserSrc.setImageResource(TextUtils.isEmpty(telPhone) ? R.mipmap.wd_nrq_tx : R.mipmap.wd_nrq_tx_1);
            mTelPhone.setVisibility(TextUtils.isEmpty(telPhone) ? View.GONE : View.VISIBLE);
            mCustomTextView4.setVisibility(TextUtils.isEmpty(telPhone) ? View.GONE : View.VISIBLE);
        }
    }


    private void initView() {
        mSP = new SPUtil(mContext);

        mSvProgressHUD = new SVProgressHUD(mContext);
        mPagerChange = new onPageChangeListener();

        mCoordinatorMenu = findViewById(R.id.menu);
        mNewsTabs = findViewById(R.id.news_tabs);
        mVpTab = findViewById(R.id.news_vp_tab);

        mHeadIv = findViewById(R.id.main_btn_menu);


    }

    private void initListener() {
        TabAdater tabAdapter = new TabAdater(getSupportFragmentManager(), getResources().getStringArray(R.array.title_menu_array));
        mVpTab.setAdapter(tabAdapter);
        mNewsTabs.setViewPager(mVpTab);
        mNewsTabs.setOnPageChangeListener(mPagerChange);
        mNewsTabs.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPagerChange.onPageSelected(0);
                mNewsTabs.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        mHeadIv.setOnClickListener(this);
        mLoging.setOnClickListener(this);

        mCustomTextView1.setOnClickListener(this);
        mCustomTextView2.setOnClickListener(this);
        mCustomTextView3.setOnClickListener(this);
        mCustomTextView4.setOnClickListener(this);
        mCustomTextView5.setOnClickListener(this);

        mSetup.setOnClickListener(this);
        mSignOut.setOnClickListener(this);
        mSearch.setOnClickListener(this);

        mSintUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_last://上一首
                if (app.getTestlist() == null || app.getTestlist().isEmpty()) {
                    ToastUtils.showShortToast(MainActivity.this, "先添加");
                    break;
                } else {
                    sendBroadcast(new Intent(App.LAST));
                }
                break;
            case R.id.player_play://播放暂停
                if (app.getTestlist() == null || app.getTestlist().isEmpty()) {
                    ToastUtils.showShortToast(MainActivity.this, "先添加");
                } else {
                    if (app.getMediaPlayer() == null) {
                        sendBroadcast(new Intent(App.PLAY));
                        player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                    } else {
                        if (app.getMediaPlayer().isPlaying()) {
                            app.getMediaPlayer().pause();
                            player_play.setBackgroundResource(R.mipmap.bottom_icon_3);
                        } else {
                            app.getMediaPlayer().start();
                            player_play.setBackgroundResource(R.mipmap.bottom_icon_4);
                        }
                    }
                }
                break;
            case R.id.player_next://下一首
                if (app.getTestlist() == null || app.getTestlist().isEmpty()) {
                    ToastUtils.showShortToast(MainActivity.this, "先添加");
                } else {
                    sendBroadcast(new Intent(App.NEXT));
                }
                break;
            case R.id.singer_icon://图标
                startActivity(new Intent(mContext, PlayerActivity.class));
                break;
            case R.id.main_btn_menu://侧滑
                if (mCoordinatorMenu.isOpened()) {
                    mCoordinatorMenu.closeMenu();
                } else {
                    mCoordinatorMenu.openMenu();
                }
                break;

            case R.id.main_btn_search://搜索
                IntentUtils.thisToOther(mContext, AlreadySearchListActivity.class);
                break;

            case R.id.loging_llt://立即登录
                isStateLogin();
                break;

            case R.id.custom_menu_1://收藏列表
                IntentUtils.thisToOther(mContext, CollectionListActivity.class);
                break;

            case R.id.custom_menu_2://最近播放
                IntentUtils.thisToOther(mContext, LatelyListActivity.class);
                break;

            case R.id.custom_menu_3://产品中心
                IntentUtils.thisToOther(mContext, ProductRecyActivity.class);
                break;

            case R.id.custom_menu_4://修改密码
                IntentUtils.thisToOther(mContext, ModifyPsdActivity.class);
                break;

            case R.id.custom_menu_5://版本更新
                updateApk();
                break;

            case R.id.setup_llt://设置
                IntentUtils.thisToOther(mContext, SetUpActivity.class);
                break;

            case R.id.sign_out_llt://退出
                toExit();
                break;

            case R.id.sint_user_tvw://退出账户
                exitOnClick();
                break;
        }
    }

    private void updateApk() {
        UpdateVersionUtils utils = new UpdateVersionUtils(mSvProgressHUD, mContext);
        utils.submLoginData();
    }

    /**
     * 登录
     */
    private void isStateLogin() {
        IntentUtils.intIntent(mContext, LoginActivity.class, "index", 1);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        System.out.println("onProgressChanged" + i);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        System.out.println("onStartTrackingTouch" + seekBar.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        System.out.println("onStopTrackingTouch" + seekBar.getProgress());
        if (app.getMediaPlayer() == null)
            return;
        app.getMediaPlayer().seekTo(seekBar.getProgress());

    }


    class onPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    public void onBackPressed() {
        if (mCoordinatorMenu.isOpened()) {
            mCoordinatorMenu.closeMenu();
        } else {
            toReturn();
        }
    }

    private void toReturn() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;//更新firstTime
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onPermissionReqResult(int reqCode, boolean isAllow) {
        if (reqCode != Contants.PermissRequest) {
            return;
        }
        if (isAllow) {
            //被授权
            Toast.makeText(this, "已获取所有权限", Toast.LENGTH_SHORT).show();
        } else {
            //App申请的权限已被拒绝,为了能正常使用,请进入设置--权限管理打开相关权限
            PermissionRequestUtil.openAppDetails(this);
        }
    }

    /**
     * 重写这个系统方法
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 请求结果数据集
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //调用封装好的方法
        PermissionRequestUtil.solvePermissionRequest(this, requestCode, grantResults);
    }

    private void exitOnClick() {
        if (mSP != null) {
            String token = mSP.getString("token", null);//token
            if (TextUtils.isEmpty(token)) {
                toLoginInfo();
            } else {
                exitAccount();
            }
        }
    }

    /**
     * 提示框
     */
    private void toLoginInfo() {
        final BtmDialog dialog = new BtmDialog(this, "温馨提示", "当前无账户,请先登录");
        AlertDialogHelper.BtmDialogDerive2(dialog, false, true, null);
    }

    /**
     * 退出当前账户提示框
     */
    private void exitAccount() {
        final BtmDialog dialog = new BtmDialog(this, "温馨提示", "确定退出当前账户吗?");
        AlertDialogHelper.BtmDialogDerive1(dialog, false, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSP.clearSpData();
                updateState();
                dialog.dismiss();
            }
        }, null);
    }


    private void toExit() {
        final BtmDialog dialog = new BtmDialog(this, "温馨提示", "是否退出程序?");
        AlertDialogHelper.BtmDialogDerive1(dialog, false, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                dialog.dismiss();
            }
        }, null);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playnext();
    }

    private void playnext() {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        System.out.println("8888888888888888888888a");
        return true;
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        System.out.println("2222222222222222222222");
        mediaPlayer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Contants.PermissRequest) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    ToastUtils.showLongToast(mContext, "未允许");
                    PermissionRequestUtil.showSuspeWindow(MainActivity.this);
                } else {
                    ToastUtils.showLongToast(mContext, "允许");
                }
            }
        }
    }
}
