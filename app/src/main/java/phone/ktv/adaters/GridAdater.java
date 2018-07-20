package phone.ktv.adaters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xutils.DbManager;

import java.util.List;

import phone.ktv.R;
import phone.ktv.bean.ListInfo;

/**
 *
 */
public class GridAdater extends BAdapter<ListInfo> {
    private static final String TAG = "GridAdater";

    Context mContext;

    public GridAdater(Context context, int layoutId, List<ListInfo> list) {
        super(context, layoutId, list);
        this.mContext = context;
    }

    @Override
    public void onInitView(View convertView, final int position) {
        try {
            ImageView music_icon = convertView.findViewById(R.id.music_icon);
            TextView music_name = convertView.findViewById(R.id.music_name);
            Picasso.with(mContext).load(getItem(position).getNgPath()).into(music_icon);
            music_name.setText(getItem(position).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
