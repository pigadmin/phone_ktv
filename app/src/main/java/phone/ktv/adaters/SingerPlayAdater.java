package phone.ktv.adaters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.SingerNumBean;

/**
 * 歌手Adater
 */
public class SingerPlayAdater extends BAdapter<SingerNumBean.SingerBean> {

    Context mContext;

    public SingerPlayAdater(Context context, int layoutId, List<SingerNumBean.SingerBean> list) {
        super(context, layoutId, list);
        this.mContext = context;
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView singertitle = get(convertView, R.id.singertitle12_tvw);//歌手名称
        TextView singerTypeName = get(convertView, R.id.singerTypeName12_tvw);//大陆男演员

        SingerNumBean.SingerBean playBean= getItem(position);
        singertitle.setText(playBean.name);
        singerTypeName.setText(playBean.singerTypeName);
    }
}
