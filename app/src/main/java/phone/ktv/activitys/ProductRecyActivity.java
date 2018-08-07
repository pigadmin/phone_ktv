package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.adaters.ProductRecyAdapter;
import phone.ktv.adaters.ProductRecyTypeAdapter;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.ProductRecyBean;
import phone.ktv.bean.ProductRecyTypeBean;
import phone.ktv.bean.SingerNumBean;
import phone.ktv.bean.UserBean;
import phone.ktv.tootls.IntentUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;
import phone.ktv.views.CustomTopTitleView;
import phone.ktv.views.MyListView;

/**
 * 产品中心
 */
public class ProductRecyActivity extends phone.ktv.BaseActivity {

    private static final String TAG = "ProductRecyActivity";

    Context mContext;

    private ProductRecyAdapter mProductAdapter;//产品记录adater
    private List<ProductRecyBean> mProductBeanList;//产品记录list
    private MyListView mProductListView;//产品记录listview

    private ProductRecyTypeAdapter mProductTypeAdapter;//产品分类adater
    private List<ProductRecyTypeBean> mProductTypeBeanList;//产品分类list
    private MyListView mProductTypeListView;//产品分类listview

    private CustomTopTitleView mTopTitleView1;//返回事件

    private SPUtil mSP;

    private SVProgressHUD mSvProgressHUD;

    public static final int HasBuyProSuccess = 100;//已购买产品获取成功
    public static final int HasBuyProError = 200;//已购买产品获取失败
    public static final int GetProSuccess = 300;//现有产品获取成功
    public static final int GetProError = 400;//现有产品获取失败
    public static final int XpiredToken = 500;//Token过期

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HasBuyProSuccess:
                    mProductAdapter.notifyDataSetChanged();
                    getProductData(false);
                    break;

                case HasBuyProError:
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;

                case GetProSuccess:
                    mProductTypeAdapter.notifyDataSetChanged();
                    break;

                case GetProError:
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;

                case XpiredToken:
                    ToastUtils.showLongToast(mContext, (String) msg.obj);
                    break;
            }
            mSvProgressHUD.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_recy_activity_view);

        initView();
        initLiter();
        mSvProgressHUD.showWithStatus("正在搜索中,请稍后...");
        getProductData(true);
    }

    private void initView() {
        mContext = ProductRecyActivity.this;
        mSP = new SPUtil(mContext);
        mSvProgressHUD = new SVProgressHUD(mContext);

        mProductBeanList = new ArrayList<>();
        mProductTypeBeanList = new ArrayList<>();

        mTopTitleView1 = findViewById(R.id.customTopTitleView1);

        mProductListView = findViewById(R.id.product_list_lvw);
        mProductTypeListView = findViewById(R.id.class_list_lvw);

        mProductAdapter = new ProductRecyAdapter(mContext, R.layout.item_prord_list_view, mProductBeanList);
        mProductListView.setAdapter(mProductAdapter);

        mProductTypeAdapter = new ProductRecyTypeAdapter(mContext, R.layout.item_prord_type_list_view, mProductTypeBeanList);
        mProductTypeListView.setAdapter(mProductTypeAdapter);

    }

    private void initLiter() {
        mTopTitleView1.toBackReturn(new MyOnClickBackReturn());//返回事件
    }

    /**
     * 获取已购买产品
     */
    private void getProductData(final boolean isState) {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel + "..token.." + token);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token

        String url;
        if (isState) {
            url = App.getRqstUrl(App.headurl + "hasBuyPro", weakHashMap);
        } else {
            url = App.getRqstUrl(App.headurl + "getPro", weakHashMap);
        }
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(mContext)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    if (isState) {
                        mHandler.obtainMessage(HasBuyProError, e.getMessage()).sendToTarget();
                    } else {
                        mHandler.obtainMessage(GetProError, e.getMessage()).sendToTarget();
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);

                    if (isState) {
                        AJson<List<ProductRecyBean>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<ProductRecyBean>>>() {
                        });
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                mHandler.sendEmptyMessage(HasBuyProSuccess);
                                Logger.i(TAG, "aJson1..." + aJson.toString());
                                GetProDataState(aJson.getData());
                            } else if (aJson.getCode() == 500) {
                                mHandler.obtainMessage(XpiredToken, aJson.getMsg()).sendToTarget();
                            } else {
                                mHandler.obtainMessage(HasBuyProError, aJson.getMsg()).sendToTarget();
                            }
                        }
                    } else {
                        AJson<List<ProductRecyTypeBean>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<ProductRecyTypeBean>>>() {
                        });
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                mHandler.sendEmptyMessage(GetProSuccess);
                                Logger.i(TAG, "aJson2..." + aJson.toString());
                                HasBuyProDataState(aJson.getData());
                            } else if (aJson.getCode() == 500) {
                                mHandler.obtainMessage(XpiredToken, aJson.getMsg()).sendToTarget();
                            } else {
                                mHandler.obtainMessage(GetProError, aJson.getMsg()).sendToTarget();
                            }
                        }
                    }

                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            ToastUtils.showLongToast(mContext, "网络连接异常,请检查网络配置");
        }
    }

    private void GetProDataState(List<ProductRecyBean> beanList) {
        if (beanList != null && !beanList.isEmpty()) {
            mProductBeanList.addAll(beanList);
        }
    }

    private void HasBuyProDataState(List<ProductRecyTypeBean> typeBeanList) {
        if (typeBeanList != null && !typeBeanList.isEmpty()) {
            mProductTypeBeanList.addAll(typeBeanList);
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
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
