package phone.ktv.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import phone.ktv.R;
import phone.ktv.adaters.ProductRecyAdapter;
import phone.ktv.adaters.ProductRecyTypeAdapter;
import phone.ktv.bean.ProductRecyBean;
import phone.ktv.bean.ProductRecyTypeBean;
import phone.ktv.views.MyListView;

/**
 * 产品中心
 */
public class ProductRecyActivity extends AppCompatActivity {

    private static final String TAG = "ProductRecyActivity";

    Context mContext;

    private ProductRecyAdapter mProductAdapter;//产品记录adater
    private List<ProductRecyBean> mProductBeanList;//产品记录list
    private MyListView mProductListView;//产品记录listview

    private ProductRecyTypeAdapter mProductTypeAdapter;//产品分类adater
    private List<ProductRecyTypeBean> mProductTypeBeanList;//产品分类list
    private MyListView mProductTypeListView;//产品分类listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_recy_activity_view);

        mContext=ProductRecyActivity.this;
        initView();
        initLiter();
    }

    private void initView(){
        mProductListView=findViewById(R.id.product_list_lvw);
        mProductTypeListView=findViewById(R.id.class_list_lvw);

        mProductBeanList=new ArrayList<>();
        mProductTypeBeanList=new ArrayList<>();

        mProductBeanList.add(new ProductRecyBean("台湾","2018-03-28/2018-06-22"));
        mProductBeanList.add(new ProductRecyBean("台湾","2018-03-28/2018-06-22"));
        mProductBeanList.add(new ProductRecyBean("台湾","2018-03-28/2018-06-22"));
        mProductBeanList.add(new ProductRecyBean("台湾","2018-03-28/2018-06-22"));
        mProductBeanList.add(new ProductRecyBean("台湾","2018-03-28/2018-06-22"));

        mProductTypeBeanList.add(new ProductRecyTypeBean("台湾歌曲","立即开通"));
        mProductTypeBeanList.add(new ProductRecyTypeBean("台湾歌曲","立即开通"));
        mProductTypeBeanList.add(new ProductRecyTypeBean("台湾歌曲","立即开通"));
        mProductTypeBeanList.add(new ProductRecyTypeBean("台湾歌曲","立即开通"));
        mProductTypeBeanList.add(new ProductRecyTypeBean("台湾歌曲","立即开通"));

        mProductAdapter=new ProductRecyAdapter(mContext,R.layout.item_prord_list_view,mProductBeanList);
        mProductListView.setAdapter(mProductAdapter);

        mProductTypeAdapter=new ProductRecyTypeAdapter(mContext,R.layout.item_prord_type_list_view,mProductTypeBeanList);
        mProductTypeListView.setAdapter(mProductTypeAdapter);

    }

    private void initLiter(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
