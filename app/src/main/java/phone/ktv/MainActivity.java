package phone.ktv;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStripExtends;

import phone.ktv.activitys.LoginActivity;
import phone.ktv.activitys.ProductRecyActivity;
import phone.ktv.activitys.SetUpActivity;
import phone.ktv.adaters.TabAdater;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.views.CoordinatorMenu;
import phone.ktv.views.CustomTextView;

/**
 * 主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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

    private LinearLayout mSetup;//设置
    private LinearLayout mSignOut;//退出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;

        initView();
        initMenuView();
        initListener();
    }

    private void initMenuView(){
        mLoging= findViewById(R.id.loging_llt);

        mCustomTextView1= findViewById(R.id.custom_menu_1);
        mCustomTextView2= findViewById(R.id.custom_menu_2);
        mCustomTextView3= findViewById(R.id.custom_menu_3);
        mCustomTextView4= findViewById(R.id.custom_menu_4);

        mSetup= findViewById(R.id.setup_llt);
        mSignOut= findViewById(R.id.sign_out_llt);
    }

    private void initView(){
        mPagerChange = new onPageChangeListener();

        mCoordinatorMenu = findViewById(R.id.menu);
        mNewsTabs = findViewById(R.id.news_tabs);
        mVpTab = findViewById(R.id.news_vp_tab);

        mHeadIv = findViewById(R.id.main_btn_menu);
    }

    private void initListener() {
        TabAdater tabAdapter = new TabAdater(getSupportFragmentManager(),getResources().getStringArray(R.array.title_menu_array));
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

        mSetup.setOnClickListener(this);
        mSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_menu://侧滑
                if (mCoordinatorMenu.isOpened()) {
                    mCoordinatorMenu.closeMenu();
                } else {
                    mCoordinatorMenu.openMenu();
                }
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

                break;

            case R.id.setup_llt://设置
                IntentUtils.thisToOther(mContext, SetUpActivity.class);
                break;

            case R.id.sign_out_llt://退出

                break;
        }
    }

    /**
     * 登录
     */
    private void isStateLogin(){
        IntentUtils.thisToOther(mContext, LoginActivity.class);
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
            super.onBackPressed();
        }
    }
}
