package phone.ktv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import phone.ktv.R;
import phone.ktv.adaters.GridAdater;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.GridList;
import phone.ktv.bean.ListInfo;
import phone.ktv.bean.WelcomAd;
import phone.ktv.bgabanner.BGABanner;
import phone.ktv.req.OkReq;
import phone.ktv.req.VolleyReq;

/**
 * 点歌台
 */
public class SongDeskFragment extends Fragment implements VolleyReq.Api, AdapterView.OnItemClickListener {

    private static final String TAG = "SongDeskFragment";
    View mNewsView;

    private Context mContext;
    private BGABanner mBanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.songdesk_fragment_layout, null);
        mContext = getActivity();
        req = new VolleyReq(mContext, this);
        initView();
        initLiter();

        initGrid();
        return mNewsView;
    }

    private VolleyReq req;
    private String updategrid;

    private void initGrid() {
        updategrid = "http://192.168.2.25:8109/ktv/api/phone/song/getSongType?telPhone=18689200036&token=1c21b72f-7ecd-40f2-bd4d-f2c4a2dd3a2e";
        req.get(updategrid);
    }

    private GridView songgrid;
    private GridAdater playAdater;

    private void initView() {
        mBanner = mNewsView.findViewById(R.id.banner_main_accordion);
        mBanner.measure(0, 0);
        songgrid = mNewsView.findViewById(R.id.songgrid);
        songgrid.setOnItemClickListener(this);

        playAdater = new GridAdater(mContext, R.layout.grids, list);
        songgrid.setAdapter(playAdater);
    }

    private void initLiter() {
        List<Integer> int1 = new ArrayList<>();
        int1.add(R.mipmap.lu_1);
        int1.add(R.mipmap.lu_2);
        int1.add(R.mipmap.lu_3);

        List<String> int2 = new ArrayList<>();
        int2.add("图1");
        int2.add("图2");
        int2.add("图3");

        mBanner.setData(int1, int2);
        mBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {

            }
        });
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                Glide.with(mContext)
                        .load(model)
                        .placeholder(R.mipmap.lu_1)
                        .error(R.mipmap.lu_1)
                        .fitCenter()
                        .thumbnail(0.7f)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into((ImageView) view);
            }
        });
    }

    List<ListInfo> list=new ArrayList<>();

    @Override
    public void finish(String tag, String json) {
        try {
            System.out.println(json);
            if (tag.equals(updategrid)) {
                GridList gridList = App.jsonToObject(json, new TypeToken<AJson<GridList>>() {
                }).getData();
//                list = gridList.getList();
                list.addAll(gridList.getList());
                System.out.println(list.size());
                playAdater.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void error(String tag, String json) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
