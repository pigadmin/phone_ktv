package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.MusicPlayBean;

/**
 * 收藏列表、最近播放adater
 */
public class CollectionListAdater extends BAdapter<MusicPlayBean> {
    private static final String TAG = "CollectionListAdater";

    Context context;

    public CollectionListAdater(Context context, int layoutId, List<MusicPlayBean> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        TextView name = get(convertView, R.id.name_tvw13);//歌曲名称
        TextView songName = get(convertView, R.id.song_name13_tvw);//歌手名称
        TextView songType = get(convertView, R.id.song_type13_tvw);//标识HD or 演唱会

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
