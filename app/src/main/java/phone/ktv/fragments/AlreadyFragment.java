package phone.ktv.fragments;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import phone.ktv.R;
import phone.ktv.tootls.ScreenUtils;

/**
 * 已点歌曲 1级
 */
public class AlreadyFragment extends Fragment {
    View mNewsView;
    public Context mContext;

    public ImageView mTitle_1_ivw;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNewsView = inflater.inflate(R.layout.already_fragment_layout, null);
        mContext = getActivity();
        initView();
        return mNewsView;
    }

    private void initView(){
        mTitle_1_ivw=mNewsView.findViewById(R.id.title_1_ivw);
        mTitle_1_ivw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoWindo();
            }
        });
    }


    private void showPoWindo() {
        int height = ScreenUtils.getScreenHeight(mContext);
        View strView = getLayoutInflater().inflate(R.layout.powindos_play_style_layout, null, false);
        final PopupWindow window = new PopupWindow(strView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setAnimationStyle(R.style.AnimationFade);
        window.setBackgroundDrawable(new BitmapDrawable());// 需要设置一下此参数，点击外边可消失
        window.setFocusable(true);// 设置此参数获得焦点，否则无法点击
        window.setOutsideTouchable(false);//设置点击窗口外边窗口消失
        window.showAtLocation(mNewsView.findViewById(R.id.title_1_ivw), Gravity.CENTER, 0, -height / 10);

        final TextView playStyle1 = strView.findViewById(R.id.play_style_1_rbn);
        final TextView playStyle2 = strView.findViewById(R.id.play_style_2_rbn);
        final TextView playStyle3 = strView.findViewById(R.id.play_style_3_rbn);

        playStyle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1(playStyle1,playStyle2,playStyle3,1);
                window.dismiss();
            }
        });

        playStyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                window.dismiss();
            }
        });

        playStyle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                window.dismiss();
            }
        });
    }

    private void s1(TextView playStyle1,TextView playStyle2,TextView playStyle3,int index){
        switch (index){
            case 1:


                break;

            case 2:

                break;

            case 3:

                break;
        }
    }
}
