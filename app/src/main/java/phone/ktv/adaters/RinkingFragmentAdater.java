package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.GridItem;
import phone.ktv.tootls.PicassoUtil;

/**
 * 排行榜adater 备注图片返回字段 icon
 */
public class RinkingFragmentAdater extends BAdapter<GridItem> {
    private static final String TAG = "Rinking1FragmentAdater";

    Context context;

    public RinkingFragmentAdater(Context context, int layoutId, List<GridItem> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        ImageView icon = get(convertView, R.id.music_icon);//图片
        TextView name = get(convertView, R.id.music_name);//名称

        GridItem item = getItem(position);

        name.setText(item.name);
        if (TextUtils.isEmpty(item.icon)) {
            icon.setImageResource(R.mipmap.logo);
        } else {
            String srcPath = PicassoUtil.utf8Togb2312(item.icon);
            PicassoUtil.picassoAdvanced(context, srcPath, R.mipmap.station_src, R.mipmap.logo, icon);
        }
    }
}
