package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private CheckBox deleCheckBox;
    private TextView name;
    private TextView songName;
    private TextView songType;
    private List<Boolean> booleanList;
    private ImageView top12;
    private ImageView delete12;

    public boolean switchType;

    public AlreadyListAdater(Context context, int layoutId, List<MusicPlayBean> list, List<Boolean> booleanList) {
        super(context, layoutId, list);
        this.booleanList = booleanList;
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {

        deleCheckBox = get(convertView, R.id.delete_all_cbx);//选中
        name = get(convertView, R.id.name_tvw19);//歌曲名称
        songName = get(convertView, R.id.song_name19_tvw);//歌手名称
        songType = get(convertView, R.id.song_type19_tvw);//标识HD or 演唱会

        top12 = get(convertView, R.id.shoucang19_ivw);//置顶
        delete12 = get(convertView, R.id.tianjia19_ivw);//删除

        if (switchType) {
            deleCheckBox.setVisibility(View.VISIBLE);
            songType.setVisibility(View.INVISIBLE);
            top12.setVisibility(View.INVISIBLE);
            delete12.setVisibility(View.INVISIBLE);
        } else {
            deleCheckBox.setVisibility(View.INVISIBLE);
            songType.setVisibility(View.VISIBLE);
            top12.setVisibility(View.VISIBLE);
            delete12.setVisibility(View.VISIBLE);
        }

        MusicPlayBean item = getItem(position);
        name.setText(item.name);
        songName.setText(item.singerName);

        if (TextUtils.isEmpty(item.label)) {
            songType.setVisibility(View.GONE);
        } else {
            songType.setVisibility(View.VISIBLE);
            songType.setText(item.label);
        }

        deleCheckBox.setTag(position);
        deleCheckBox.setChecked(booleanList.get(position));
        deleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                booleanList.set((Integer) buttonView.getTag(), b);
            }
        });

        /**
         * 置顶
         */
        top12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLongToast(context, "top12");
            }
        });

        /**
         * 删除
         */
        delete12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLongToast(context, "delete12");
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

    /**
     * 更新复选框
     *
     * @param booleanList
     * @param updateType
     */
    public void setUpdateState(List<Boolean> booleanList, boolean updateType) {
        for (int i = 0; i < booleanList.size(); i++) {
            booleanList.set(i, updateType ? true : false);
        }
        this.booleanList = booleanList;
        notifyDataSetChanged();
    }
}
