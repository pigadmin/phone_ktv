package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.ListInfo;
import phone.ktv.tootls.PicassoUtil;

/**
 * 点歌台adater 备注图片返回字段 ngPath
 */
public class SongDeskGridAdater extends BAdapter<ListInfo> {
    private static final String TAG = "GridAdater";

    Context context;

    public SongDeskGridAdater(Context context, int layoutId, List<ListInfo> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        ImageView icon = get(convertView, R.id.music_icon);//图片
        TextView name = get(convertView,R.id.music_name);//名称

        ListInfo item = getItem(position);

        name.setText(item.getName());
        if (TextUtils.isEmpty(item.getNgPath())){
            icon.setImageResource(R.mipmap.logo);
        } else {
            String srcPath= PicassoUtil.utf8Togb2312(item.getNgPath());
            PicassoUtil.picassoAdvanced(context,srcPath,R.mipmap.station_src,R.mipmap.logo,icon);
        }
    }
}
