package phone.ktv.adaters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.SPUtil;


public class AlreadySearchSongAdater extends BAdapter<MusicPlayBean> {
    private static final String TAG = "AlreadySearchSongAdater";

    public Context context;
    public SPUtil mSP;
    public Activity activity;

    public AlreadySearchSongAdater(Context context, int layoutId, List<MusicPlayBean> list, SPUtil mSP, Activity activity) {
        super(context, layoutId, list);
        this.context = context;
        this.mSP = mSP;
        this.activity = activity;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        TextView name = get(convertView, R.id.name_tvw12);//歌曲名称
        TextView songName = get(convertView, R.id.song_name12_tvw);//歌手名称
        TextView songType = get(convertView, R.id.song_type12_tvw);//标识HD or 演唱会

        final MusicPlayBean item = getItem(position);
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
