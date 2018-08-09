package phone.ktv.adaters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.SingerNumBean;
import phone.ktv.tootls.SPUtil;


public class AlreadySearchSingAdater extends BAdapter<SingerNumBean.SingerBean> {
    private static final String TAG = "AlreadySearchSingAdater";

    public Context context;
    public SPUtil mSP;
    public Activity activity;

    public AlreadySearchSingAdater(Context context, int layoutId, List<SingerNumBean.SingerBean> list, SPUtil mSP, Activity activity) {
        super(context, layoutId, list);
        this.context = context;
        this.mSP = mSP;
        this.activity = activity;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        TextView text4 = get(convertView, R.id.singertitle12_tvw);//歌曲名称
        TextView text5 = get(convertView, R.id.singerTypeName12_tvw);//歌手名称

        final SingerNumBean.SingerBean item = getItem(position);
        text4.setText(item.name);
        text5.setText(item.singerTypeName);
    }
}
