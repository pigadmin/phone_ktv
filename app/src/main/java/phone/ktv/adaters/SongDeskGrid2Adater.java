package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.SingerNumBean;
import phone.ktv.tootls.PicassoUtil;

/**
 * 点歌台adater 备注图片返回字段 ngPath
 */
public class SongDeskGrid2Adater extends BAdapter<SingerNumBean.SingerBean> {
    private static final String TAG = "SongDeskGrid2Adater";

    Context context;

    public SongDeskGrid2Adater(Context context, int layoutId, List<SingerNumBean.SingerBean> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        ImageView icon = get(convertView, R.id.music_icon);//图片
        TextView name = get(convertView, R.id.music_name);//名称

        SingerNumBean.SingerBean item = getItem(position);

        name.setText(item.name);
        if (TextUtils.isEmpty(item.ngPath)) {
            icon.setImageResource(R.mipmap.logo);
        } else {
            String srcPath = PicassoUtil.utf8Togb2312(item.ngPath);
            PicassoUtil.picassoAdvanced(context, srcPath, R.mipmap.station_src, R.mipmap.logo, icon);
        }
    }
}
