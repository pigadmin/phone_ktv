package phone.ktv.adaters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.ProductRecyBean;

/**
 * 查看已购买产品adater
 */
public class ProductRecyAdapter extends BAdapter<ProductRecyBean> {

    Context mContext;

    public ProductRecyAdapter(Context context, int layoutId, List<ProductRecyBean> list) {
        super(context, layoutId, list);
        this.mContext = context;
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView address = get(convertView, R.id.address_tvw);//地址
        TextView startdate = get(convertView, R.id.date_star_tvw);//开始日期
        TextView enddate = get(convertView, R.id.date_end_tvw);//结束日期

        ProductRecyBean item = getItem(position);
        if (item != null) {
            address.setText(item.pName);
            startdate.setText(item.begintime);
            enddate.setText(item.endtime);
        }
    }
}
