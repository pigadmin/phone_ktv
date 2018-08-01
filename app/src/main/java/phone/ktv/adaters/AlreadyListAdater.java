package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.ToastUtils;

/**
 * 已点歌曲adater
 */
public class AlreadyListAdater extends BAdapter<MusicPlayBean> {
    private static final String TAG = "AlreadyListAdater";

    Context context;

    public AlreadyListAdater(Context context, int layoutId, List<MusicPlayBean> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {

        CheckBox deleCheckBox = get(convertView, R.id.delete_all_cbx);//选中
//        deleCheckBox.setVisibility(View.INVISIBLE);
        TextView name = get(convertView, R.id.name_tvw19);//歌曲名称
        TextView songName = get(convertView, R.id.song_name19_tvw);//歌手名称
        TextView songType = get(convertView, R.id.song_type19_tvw);//标识HD or 演唱会

        ImageView top12 = get(convertView, R.id.shoucang19_ivw);//置顶
        ImageView delete12 = get(convertView, R.id.tianjia19_ivw);//删除

        MusicPlayBean item = getItem(position);
        name.setText(item.name);
        songName.setText(item.singerName);

        if (TextUtils.isEmpty(item.label)) {
            songType.setVisibility(View.GONE);
        } else {
            songType.setVisibility(View.VISIBLE);
            songType.setText(item.label);
        }

        top12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLongToast(context, "top12");
            }
        });

        delete12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLongToast(context, "delete12");
            }
        });
    }
}
