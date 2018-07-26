package phone.ktv.activitys;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import phone.ktv.R;
import phone.ktv.views.CustomTopTitleView;

/**
 * 用户协议
 */
public class UserProtocolActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserProtocolActivity";

    private static final String url = "http://192.168.2.25:8060/xieyi/xieyi.html";

    Context mContext;

    private WebView mWebView;

    private CustomTopTitleView mTopTitleView1;//返回事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_protocol_activity_layout);

        initView();
        initLiter();
    }

    private void initView() {
        mContext = UserProtocolActivity.this;

//        mSvProgressHUD = new SVProgressHUD(mContext);

        mTopTitleView1 = findViewById(R.id.customTopTitleView1);

        mWebView = findViewById(R.id.user_proto_wvw);

        initWebView();

        mWebView.setWebChromeClient(new WebChromeClient() {
            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                mSvProgressHUD.showWithStatus("加载中...");
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
//                mTitleText.setText(title);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                L.i(TAG, "加载结束.." + url);
//                mSvProgressHUD.dismiss();
            }

            //出现错误
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
//                L.i(TAG, "出现错误.." + error);
//                mSvProgressHUD.dismiss();
            }
        });
        mWebView.loadUrl(url);
    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件

    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许与Javascript交互
        webSettings.setJavaScriptEnabled(true);//允许执行动画操作
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.back_ivw:
//                finish();
//                break;
        }
    }

    /**
     * 返回事件
     */
    public class MyOnClickBackReturn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
