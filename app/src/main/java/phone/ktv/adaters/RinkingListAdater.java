package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import phone.ktv.R;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.tootls.Logger;
import phone.ktv.tootls.NetUtils;
import phone.ktv.tootls.OkhttpUtils;
import phone.ktv.tootls.SPUtil;
import phone.ktv.tootls.ToastUtils;

/**
 * 排行榜歌曲adater
 */
public class RinkingListAdater extends BAdapter<MusicPlayBean> {
    private static final String TAG = "RinkingListAdater";

    Context context;
    SPUtil mSP;

    public RinkingListAdater(Context context, int layoutId, List<MusicPlayBean> list, SPUtil mSP) {
        super(context, layoutId, list);
        this.context = context;
        this.mSP = mSP;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        TextView name = get(convertView, R.id.name_tvw12);//歌曲名称
        TextView songName = get(convertView, R.id.song_name12_tvw);//歌手名称
        TextView songType = get(convertView, R.id.song_type12_tvw);//标识HD or 演唱会

        ImageView shoucang12 = get(convertView, R.id.shoucang12_ivw);//收藏
        ImageView tianjia12 = get(convertView, R.id.tianjia12_ivw);//添加

        final MusicPlayBean item = getItem(position);
        name.setText(item.name);
        songName.setText(item.singerName);

        if (TextUtils.isEmpty(item.label)) {
            songType.setVisibility(View.GONE);
        } else {
            songType.setVisibility(View.VISIBLE);
            songType.setText(item.label);
        }

        shoucang12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRankingListData(item);
            }
        });

        tianjia12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    App.mDb.save(item);
                    ToastUtils.showLongToast(context, "歌曲保存成功");
                } catch (Exception e) {
                    Logger.d(TAG, "e.." + e.getMessage());
                    ToastUtils.showLongToast(context, "歌曲已保存");
                }
            }
        });
    }


    private void getRankingListData(MusicPlayBean playBean) {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String tel = mSP.getString("telPhone", null);//tel
        String token = mSP.getString("token", null);//token
        Logger.i(TAG, "tel.." + tel);
        weakHashMap.put("telPhone", tel);//手机号
        weakHashMap.put("token", token);//token
        weakHashMap.put("sid", playBean.id);//token

        String url = App.getRqstUrl(App.headurl + "song/collect/add", weakHashMap);
        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(context)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "s.." + s);
                    AJson<List<MusicPlayBean>> aJson = App.jsonToObject(s, new TypeToken<AJson<List<MusicPlayBean>>>() {
                    });
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            Logger.i(TAG, "aJson..." + aJson.toString());
                        } else if (aJson.getCode() == 500) {

                        } else {

                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            ToastUtils.showLongToast(context, "网络连接异常,请检查网络配置");
        }
    }
}
