package phone.ktv.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import phone.ktv.R;
import phone.ktv.tootls.Logger;

/**
 * 自定义 PopupWindow
 */
public class CustomPopuWindw extends PopupWindow {

    private static final String TAG = "CustomPopuWindw";

    private View strView;

    public static int postion = 1;
    public Context context;

    public LinearLayout mPlayStyle1;
    public LinearLayout mPlayStyle2;
    public LinearLayout mPlayStyle3;

    public ImageView mSrc1;
    public TextView mText1;
    public ImageView mSrc1Go;

    public ImageView mSrc2;
    public TextView mText2;
    public ImageView mSrc2Go;

    public ImageView mSrc3;
    public TextView mText3;
    public ImageView mSrc3Go;

    public CustomPopuWindw(final Context context) {

        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        strView = inflater.inflate(R.layout.powindos_play_style_layout, null, false);

        // 设置SelectPicPopupWindow的View
        this.setContentView(strView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);//设置点击窗口外边窗口消失
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        setEnText();
    }

    public void setEnText() {
        mPlayStyle1 = strView.findViewById(R.id.play_style_1_rbn);
        mPlayStyle2 = strView.findViewById(R.id.play_style_2_rbn);
        mPlayStyle3 = strView.findViewById(R.id.play_style_3_rbn);

        mSrc1 = strView.findViewById(R.id.src1_ivw);
        mText1 = strView.findViewById(R.id.text1_tvw);
        mSrc1Go = strView.findViewById(R.id.src1_go_ivw);

        mSrc2 = strView.findViewById(R.id.src2_ivw);
        mText2 = strView.findViewById(R.id.text2_tvw);
        mSrc2Go = strView.findViewById(R.id.src2_go_ivw);

        mSrc3 = strView.findViewById(R.id.src3_ivw);
        mText3 = strView.findViewById(R.id.text3_tvw);
        mSrc3Go = strView.findViewById(R.id.src3_go_ivw);

        initialState(mSrc1, mText1, mSrc1Go, mSrc2, mText2, mSrc2Go, mSrc3, mText3, mSrc3Go);
    }

    private void initialState(ImageView src1, TextView text1, ImageView src1go, ImageView src2, TextView text2, ImageView src2go, ImageView src3, TextView text3, ImageView src3go) {
        Logger.d(TAG,"postion1..."+postion);
        switch (postion) {
            case 1:
                src1.setImageResource(R.mipmap.popovers_x_2);
                text1.setTextColor(context.getResources().getColor(R.color.bule));
                src1go.setImageResource(R.mipmap.popovers_y_2);

                src2.setImageResource(R.mipmap.popovers_s_1);
                text2.setTextColor(context.getResources().getColor(R.color.grgray));
                src2go.setImageResource(R.mipmap.popovers_y_1);

                src3.setImageResource(R.mipmap.popovers_n_1);
                text3.setTextColor(context.getResources().getColor(R.color.grgray));
                src3go.setImageResource(R.mipmap.popovers_y_1);
                break;

            case 2:
                src2.setImageResource(R.mipmap.popovers_s_2);
                text2.setTextColor(context.getResources().getColor(R.color.bule));
                src2go.setImageResource(R.mipmap.popovers_y_2);

                src1.setImageResource(R.mipmap.popovers_x_1);
                text1.setTextColor(context.getResources().getColor(R.color.grgray));
                src1go.setImageResource(R.mipmap.popovers_y_1);

                src3.setImageResource(R.mipmap.popovers_n_1);
                text3.setTextColor(context.getResources().getColor(R.color.grgray));
                src3go.setImageResource(R.mipmap.popovers_y_1);

                break;

            case 3:
                src3.setImageResource(R.mipmap.popovers_n_2);
                text3.setTextColor(context.getResources().getColor(R.color.bule));
                src3go.setImageResource(R.mipmap.popovers_y_2);

                src1.setImageResource(R.mipmap.popovers_x_1);
                text1.setTextColor(context.getResources().getColor(R.color.grgray));
                src1go.setImageResource(R.mipmap.popovers_y_1);

                src2.setImageResource(R.mipmap.popovers_s_1);
                text2.setTextColor(context.getResources().getColor(R.color.grgray));
                src2go.setImageResource(R.mipmap.popovers_y_1);
                break;
        }
    }

    public void setState(ImageView playStyle1, TextView playStyle2, ImageView playStyle3, int index) {
        switch (index) {
            case 1:
                playStyle1.setImageResource(R.mipmap.popovers_x_2);
                playStyle2.setTextColor(context.getResources().getColor(R.color.bule));
                playStyle3.setImageResource(R.mipmap.popovers_y_2);
                postion = 1;
                break;

            case 2:
                playStyle1.setImageResource(R.mipmap.popovers_s_2);
                playStyle2.setTextColor(context.getResources().getColor(R.color.bule));
                playStyle3.setImageResource(R.mipmap.popovers_y_2);
                postion = 2;
                break;

            case 3:
                playStyle1.setImageResource(R.mipmap.popovers_n_2);
                playStyle2.setTextColor(context.getResources().getColor(R.color.bule));
                playStyle3.setImageResource(R.mipmap.popovers_y_2);
                postion = 3;
                break;
        }
        Logger.d(TAG,"postion2..."+postion);
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
