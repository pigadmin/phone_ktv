package phone.ktv.adaters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 已点歌曲adater
 */
public class AlreadyListAdater extends BAdapter<MusicPlayBean> {
    private static final String TAG = "AlreadyListAdater";

    Context context;
    OnCallBack mCallBack;

    private CheckBox deleCheckBox;
    private TextView name;
    private TextView songName;
    private TextView songType;
    private ImageView delete12;

    public boolean switchType;
    SPUtil spUtil;

    public AlreadyListAdater(Context context, int layoutId, List<MusicPlayBean> list, OnCallBack callBack) {
        super(context, layoutId, list);
        this.context = context;
        this.mCallBack = callBack;
        spUtil = new SPUtil(context);
    }

    public interface OnCallBack {
        void onSelectedListener(int pos);
    }

    @Override
    public void onInitView(final View convertView, final int position) {
        deleCheckBox = get(convertView, R.id.delete_all_cbx);//选中
        name = get(convertView, R.id.name_tvw19);//歌曲名称
        songName = get(convertView, R.id.song_name19_tvw);//歌手名称
        songType = get(convertView, R.id.song_type19_tvw);//标识HD or 演唱会
        delete12 = get(convertView, R.id.tianjia19_ivw);//删除

        final MusicPlayBean item = getItem(position);
        name.setText(item.name);
        songName.setText(item.singerName);

        if (switchType) {
            deleCheckBox.setVisibility(View.VISIBLE);
            songType.setVisibility(View.INVISIBLE);
            delete12.setVisibility(View.INVISIBLE);
        } else {
            deleCheckBox.setVisibility(View.INVISIBLE);
            songType.setVisibility(View.VISIBLE);
            delete12.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(item.label)) {
                songType.setVisibility(View.GONE);
            } else {
                songType.setVisibility(View.VISIBLE);
                songType.setText(item.label);
            }
        }

        deleCheckBox.setChecked(item.isState);
        deleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onSelectedListener(position);
                }
            }
        });

        /**
         * 删除
         */
        delete12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (position == getAllData().size() - 1) {
                        spUtil.putInt("play_index", 0);
                        context.sendBroadcast(new Intent(App.PLAY));
                    } else if (position == spUtil.getInt("play_index", 0)) {
                        context.sendBroadcast(new Intent(App.PLAY));
                    } else {
                        spUtil.putInt("play_index", position);
                    }
                    ToastUtils.showShortToast(context, "删除成功");
                    App.mDb.delete(item);//先删除DB数据
                    getAllData().remove(position);//再删本地列表
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Logger.i(TAG, "删除异常e.." + e.getMessage());
                }
            }
        });
    }

    /**
     * 多选
     */
    public void setUpdateType(boolean switchType) {
        this.switchType = switchType;
        notifyDataSetChanged();
    }
}
