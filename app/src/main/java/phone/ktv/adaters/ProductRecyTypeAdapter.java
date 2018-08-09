package phone.ktv.adaters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.ProductRecyTypeBean;
import phone.ktv.tootls.ToastUtils;

/**
 * 查看现有产品adater
 */
public class ProductRecyTypeAdapter extends BAdapter<ProductRecyTypeBean> {

    Context context;

    public ProductRecyTypeAdapter(Context context, int layoutId, List<ProductRecyTypeBean> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView songName = get(convertView, R.id.song_name_tvw);//台湾歌曲
        TextView dayInfo = get(convertView, R.id.day_info_tvw);//(X天/X元)
        TextView openUp = get(convertView, R.id.open_up_tvw);//立即开通

        ProductRecyTypeBean item = getItem(position);
        if (item != null) {
            songName.setText(item.name);

            String s = "(" + item.days + "天/" + item.price + "元)";
            dayInfo.setText(s);
        }

        openUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast(context, "此功能暂未开放!");
            }
        });
    }
}
