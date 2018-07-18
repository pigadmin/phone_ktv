package phone.ktv.adaters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.ProductRecyBean;

public class ProductRecyAdapter extends BAdapter<ProductRecyBean> {

    Context mContext;

    public ProductRecyAdapter(Context context, int layoutId, List<ProductRecyBean> list) {
        super(context, layoutId, list);
        this.mContext = context;
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView name = get(convertView, R.id.name_tvw);//名称
        TextView address = get(convertView,R.id.address_tvw);//地址
        TextView nameDate = get(convertView,R.id.name_date_tvw);//时间
        TextView date = get(convertView,R.id.date_tvw);//时间内容

        ProductRecyBean item = getItem(position);
        address.setText(item.address);
        date.setText(item.date);
    }
}
