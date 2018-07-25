package phone.ktv.adaters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.ProductRecyTypeBean;

public class ProductRecyTypeAdapter extends BAdapter<ProductRecyTypeBean> {

    Context mContext;

    public ProductRecyTypeAdapter(Context context, int layoutId, List<ProductRecyTypeBean> list) {
        super(context, layoutId, list);
        this.mContext = context;
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView songName = get(convertView, R.id.song_name_tvw);//名称
        TextView openUp = get(convertView,R.id.open_up_tvw);//地址

        ProductRecyTypeBean item = getItem(position);
        songName.setText(item.address);
        openUp.setText(item.date);
    }
}
