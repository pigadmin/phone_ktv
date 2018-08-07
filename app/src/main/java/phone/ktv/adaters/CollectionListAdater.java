package phone.ktv.adaters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.WeakHashMap;

import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.CallBackUtils;
import phone.ktv.tootls.GsonJsonUtils;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 收藏列表、最近播放adater
 */
public class CollectionListAdater extends BAdapter<MusicPlayBean> {
    private static final String TAG = "CollectionListAdater";

    Activity context;
    private SPUtil mSP;
    private boolean deleteType;

    public CollectionListAdater(Activity context, int layoutId, List<MusicPlayBean> list, boolean deleteType, SPUtil mSP) {
        super(context, layoutId, list);
        this.context = context;
        this.mSP = mSP;
        this.deleteType = deleteType;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        TextView name = get(convertView, R.id.name_tvw13);//歌曲名称
        TextView songName = get(convertView, R.id.song_name13_tvw);//歌手名称
        TextView songType = get(convertView, R.id.song_type13_tvw);//标识HD or 演唱会
        ImageView delete = get(convertView, R.id.delete13_ivw);//删除

        final MusicPlayBean item = getItem(position);

        name.setText(item.name);
        songName.setText(item.singerName);

        if (TextUtils.isEmpty(item.label)) {
            songType.setVisibility(View.GONE);
        } else {
            songType.setVisibility(View.VISIBLE);
            songType.setText(item.label);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submDelete(item, position);
            }
        });
    }

    /**
     * 获取播放记录列表
     */
    private void submDelete(MusicPlayBean item, final int position) {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        weakHashMap.clear();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token
        String url;
        if (deleteType) {
            //收藏
            weakHashMap.put("cid", item.sid);
            url = App.getRqstUrl(App.headurl + "song/collect/del", weakHashMap);
        } else {
            //播放记录
            weakHashMap.put("id", item.sid);
            url = App.getRqstUrl(App.headurl + "song/record/del", weakHashMap);
        }
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(context)) {
            CallBackUtils.getInstance().init(url, new CallBackUtils.CommonCallback() {
                @Override
                public void onFinish(String result, String msg) {
                    if (TextUtils.isEmpty(result)) {
                        ToastUtils.showLongToast(context, result);
                    } else {
                        Logger.i(TAG, "s.." + result);
                        AJson aJson = GsonJsonUtils.parseJson2Obj(result, AJson.class);
                        if (aJson != null) {
                            if (aJson.getCode() == 0) {
                                getResult("删除成功");
                                getAllData().remove(position);//再删本地列表
                                if (deleteType) {
                                    int collectListSize = mSP.getInt("collectListSize", 0);
                                    mSP.putInt("collectListSize", collectListSize - 1);
                                } else {
                                    int latelyListSize = mSP.getInt("latelyListSize", 0);
                                    mSP.putInt("latelyListSize", latelyListSize - 1);
                                }

                            } else {
                                getResult("删除失败" + aJson.getMsg());
                            }
                        }
                    }
                }
            });
        } else {
            ToastUtils.showLongToast(context, "网络连接异常,请检查网络配置");
        }
    }

    private void getResult(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLongToast(context, msg);
                notifyDataSetChanged();
            }
        });
    }
}
