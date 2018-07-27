package phone.ktv;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStripExtends;
import com.bigkoo.svprogresshud.SVProgressHUD;

import phone.ktv.activitys.LoginActivity;
import phone.ktv.activitys.ModifyPsdActivity;
import phone.ktv.activitys.ProductRecyActivity;
import phone.ktv.activitys.SetUpActivity;
import phone.ktv.activitys.already_activitys.AlreadySearchListActivity;
import phone.ktv.adaters.TabAdater;
import phone.ktv.tootls.Contants;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.PermissionRequestUtil;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.UpdateVersionUtils;
import phone.ktv.views.CoordinatorMenu;
import phone.ktv.views.CustomTextView;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, PermissionRequestUtil.PermissionRequestListener {

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

    private TextView mUserName;//用户名
    private TextView mTelPhone;//手机号

    private SPUtil mSP;

    private SVProgressHUD mSvProgressHUD;

    private long firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        //动态申请权限(动态申请的权限需要在AndroidManifest.xml中声明)
        PermissionRequestUtil.judgePermissionOver23(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                Contants.PermissRequest);

        initView();
        initMenuView();
        initListener();
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

        mUserName = findViewById(R.id.user_name_tvw);
        mTelPhone = findViewById(R.id.tel_phone_tvw);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSP != null) {
            String username = mSP.getString("username", null);
            String telPhone = mSP.getString("telPhone", null);

            mUserName.setText(TextUtils.isEmpty(username) ? "立即登录" : username);
            mTelPhone.setText(TextUtils.isEmpty(telPhone) ? null : telPhone);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

                break;

            case R.id.custom_menu_2://最近播放

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
        IntentUtils.intIntent(mContext, LoginActivity.class,"index",1);
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
}
