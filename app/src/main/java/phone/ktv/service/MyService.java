package phone.ktv.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import phone.ktv.R;
import phone.ktv.tootls.DensityUtil;

public class MyService extends Service {
    private static final int POPWINDOW_HEIGHT = 550;
    private static final int POPWINDOW_WIDTH = 370;
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;

    ImageButton mFloatView;

    private static final String TAG = "FxService";
    private float upx = 0;
    private float upy = 0;

    private PopupWindow mPopupWindow;


    private LinearLayout linearLayout;
    private Handler handler;
    private Handler handler1;
    private int screenWidth;
    private int screenHeight;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(TAG, "oncreat");


        try {
//            createFloatView();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        return null;
    }

    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(
                getApplication().WINDOW_SERVICE);


        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;//瀹藉害height = dm.heightPixels ;//楂樺害
        screenHeight = dm.heightPixels;


        // 璁剧疆window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        // 璁剧疆鍥剧墖鏍煎紡锛屾晥鏋滀负鑳屾櫙閫忔槑
        wmParams.format = PixelFormat.RGBA_8888;
        // 璁剧疆娴姩绐楀彛涓嶅彲鑱氱劍锛堝疄鐜版搷浣滈櫎娴姩绐楀彛澶栫殑鍏朵粬鍙绐楀彛鐨勬搷浣滐級
        wmParams.flags =
                // LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_NOT_FOCUSABLE;
        // LayoutParams.FLAG_NOT_TOUCHABLE


        // 璋冩暣鎮诞绐楁樉绀虹殑鍋滈潬浣嶇疆涓哄乏渚х疆椤?
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 浠ュ睆骞曞乏涓婅涓哄師鐐癸紝璁剧疆x銆亂鍒濆鍊?
        wmParams.x = DensityUtil.px2dip(this, 1200 - 10);
        wmParams.y = DensityUtil.px2dip(this, 80);


        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout,
                null);
        mWindowManager.addView(mFloatLayout, wmParams);

        Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
        Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
        Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
        Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());

        mFloatView = (ImageButton) mFloatLayout.findViewById(R.id.float_id);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);

        mFloatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        upx = event.getRawX() - wmParams.x;
                        upy = event.getRawY() - wmParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        wmParams.x = (int) (event.getRawX() - upx);
                        wmParams.y = (int) (event.getRawY() - upy);
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        float upx2 = event.getRawX() - wmParams.x;
                        float upy2 = event.getRawY() - wmParams.y;
                        if ((Math.abs((upx - upx2)) < 10) && (Math.abs((upy - upy2)) < 10)) {
                        }
                        break;
                }
                return false;
            }
        });


    }

    private void initPopuptWindow() {
        int[] location = new int[2];
        mFloatLayout.getLocationOnScreen(location);
        mPopupWindow.showAsDropDown(mFloatLayout, screenWidth - location[0] - DensityUtil.dip2px(this, POPWINDOW_WIDTH) - 30, screenHeight - location[1] - DensityUtil.dip2px(this, POPWINDOW_HEIGHT));
    }

    private synchronized void closeMenu() {
        mPopupWindow.dismiss();
        mPopupWindow = null;
    }


    private void getPopupWindowInstance() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        } else {
            initPopuptWindow();
        }
    }


}
