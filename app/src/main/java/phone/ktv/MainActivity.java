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
import phone.ktv.adaters.TabAdater;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.views.CoordinatorMenu;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_menu:
                if (mCoordinatorMenu.isOpened()) {
                    mCoordinatorMenu.closeMenu();
                } else {
                    mCoordinatorMenu.openMenu();
                }
                break;

            case R.id.loging_llt:
                isStateLogin();
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
