package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.MusicPlayBean;

/**
 * 排行榜歌曲adater
 */
public class RinkingListAdater extends BAdapter<MusicPlayBean> {
    private static final String TAG = "RinkingListAdater";

    Context context;

    public RinkingListAdater(Context context, int layoutId, List<MusicPlayBean> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        TextView name = get(convertView, R.id.name_tvw12);//歌曲名称
        TextView songName = get(convertView, R.id.song_name12_tvw);//歌手名称
        TextView songType = get(convertView, R.id.song_type12_tvw);//标识HD or 演唱会

        ImageView shoucang12 = get(convertView, R.id.shoucang12_ivw);//收藏
        ImageView tianjia12 = get(convertView, R.id.tianjia12_ivw);//添加

        MusicPlayBean item = getItem(position);
        name.setText(item.name);
        songName.setText(item.singerName);

        if (TextUtils.isEmpty(item.label)) {
            songType.setVisibility(View.GONE);
        } else {
            songType.setVisibility(View.VISIBLE);
            songType.setText(item.label);
        }
    }
}
