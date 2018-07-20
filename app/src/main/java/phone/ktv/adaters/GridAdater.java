package phone.ktv.adaters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
            System.out.println(music_icon.getWidth());

//            music_name.setText(getItem(position).getName());
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) music_icon.getLayoutParams();
//            params.height = music_icon.getWidth();
//            music_icon.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
