package phone.ktv.adaters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.MusicPlayBean;
import phone.ktv.bean.SingerNumBean;
import phone.ktv.tootls.Logger;


public class AlreadySearchAdater extends BaseAdapter {
    private static final String TAG = "AlreadySearchAdater";

    public List<MusicPlayBean> list1;
    public List<SingerNumBean.SingerBean> list2;

    public Context context;
    public LayoutInflater layoutInflater;
    public boolean isState;

    ViewHolder1 holder1;
    ViewHolder2 holder2;

    public AlreadySearchAdater(Context context, List<MusicPlayBean> list1, List<SingerNumBean.SingerBean> list2, boolean isState) {
        this.context = context;
        this.list1 = list1;
        this.list2 = list2;
        this.isState = isState;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return isState?list1.size():list2.size();
    }

    @Override
    public Object getItem(int position) {
        return isState?list1.get(position):list2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (isState) {
                Logger.d(TAG,".1.isState.."+isState);
                holder1=new ViewHolder1();
                convertView = layoutInflater.inflate(R.layout.item_ringlist_layout, parent, false);
                holder1.text1 = convertView.findViewById(R.id.name_tvw12);
                holder1.text2 = convertView.findViewById(R.id.song_name12_tvw);
                holder1.text3 = convertView.findViewById(R.id.song_type12_tvw);
                holder1.src1 = convertView.findViewById(R.id.shoucang12_ivw);
                holder1.src2 = convertView.findViewById(R.id.tianjia12_ivw);
                convertView.setTag(holder1);
            } else {
                holder2=new ViewHolder2();
                convertView = layoutInflater.inflate(R.layout.singer_play_item, parent, false);
                holder2.text4 = convertView.findViewById(R.id.singertitle12_tvw);
                holder2.text5 = convertView.findViewById(R.id.singerTypeName12_tvw);
                convertView.setTag(holder2);
            }
        } else {
            Logger.d(TAG,"..2..isState...."+isState);
            if (isState) {
                holder1 = (ViewHolder1) convertView.getTag();
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }
        }
        Logger.d(TAG,"..3....isState......"+isState);
        if (isState) {
            final MusicPlayBean bean = list1.get(position);
            holder1.text1.setText(bean.name);
            holder1.text2.setText(bean.singerName);

            if (TextUtils.isEmpty(bean.label)){
                holder1.text3.setVisibility(View.GONE);
            } else {
                holder1.text3.setVisibility(View.VISIBLE);
                holder1.text3.setText(bean.label);
            }
        } else {
            SingerNumBean.SingerBean playBean= list2.get(position);
            holder2.text4.setText(playBean.name);
            holder2.text5.setText(playBean.singerTypeName);
        }
        return convertView;
    }

    public class ViewHolder1 {
        public TextView text1, text2, text3;
        public ImageView src1, src2;
    }

    public class ViewHolder2 {
        public TextView text4, text5;
    }

    public void updaState(boolean state){
        isState=state;
        notifyDataSetChanged();
    }
}
