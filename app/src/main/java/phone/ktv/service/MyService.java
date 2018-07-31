package phone.ktv.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import phone.ktv.R;
import phone.ktv.tootls.DensityUtil;

public class MyService extends Service {
    String TAG = "MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createFloatView();

    }

    private LayoutParams wmParams;
    private WindowManager mWindowManager;
    private int screenWidth;
    private int screenHeight;
    private LinearLayout mFloatLayout;

    private void createFloatView() {
        try {

            wmParams = new WindowManager.LayoutParams();

            // 获取WindowManagerImpl.CompatModeWrapper
            mWindowManager = (WindowManager) getApplication().getSystemService(
                    getApplication().WINDOW_SERVICE);


            DisplayMetrics dm = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;//宽度height = dm.heightPixels ;//高度
            screenHeight = dm.heightPixels;
            Log.i(TAG, screenWidth + "-->" + screenHeight);

            // 设置window type
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            // 设置图片格式，效果为背景透明
            wmParams.format = PixelFormat.RGBA_8888;
            // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            wmParams.flags =
                    // LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            // LayoutParams.FLAG_NOT_TOUCHABLE


            // 调整悬浮窗显示的停靠位置为左侧置顶
            wmParams.gravity = Gravity.LEFT | Gravity.TOP;
            // 以屏幕左上角为原点，设置x、y初始值
            wmParams.x = DensityUtil.px2dip(this, 111);
            wmParams.y = DensityUtil.px2dip(this, 111);
//		wmParams.x = DensityUtil.px2dip(this, 1200 -10);
//		wmParams.y = DensityUtil.px2dip(this, 80);wmParams

            // 设置悬浮窗口长宽数据
            wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            // 获取浮动窗口视图所在布局
            mFloatLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.player_mini,
                    null);

            // 添加mFloatLayout
            mWindowManager.addView(mFloatLayout, wmParams);

            Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
            Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
            Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
            Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());


        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

}
