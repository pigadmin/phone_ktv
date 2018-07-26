package phone.ktv.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import phone.ktv.R;

public class CustomTopTitleView extends RelativeLayout {
    private ImageView mSrcLogo;//图标log
    private TextView mTextTitle;//标题

    public CustomTopTitleView(Context context) {
        super(context);
    }

    public CustomTopTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public CustomTopTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        View textView = LayoutInflater.from(context).inflate(R.layout.custom_top_title_layout, this);

        mSrcLogo = textView.findViewById(R.id.src_back_ivw);
        mTextTitle = textView.findViewById(R.id.text_back_tvw);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTopTitleView);

        String titleType = typedArray.getString(R.styleable.CustomTopTitleView_back_text_type);//标题显示类型

        typedArray.recycle();
        setDefinedView(titleType);
    }

    private void setDefinedView(String titleType) {
        mTextTitle.setText(titleType);
    }

    /**
     * 返回事件
     *
     * @param listener
     */
    public void toBackReturn(OnClickListener listener) {
        mSrcLogo.setOnClickListener(listener);
    }

    public void setTopText(String text) {
        mTextTitle.setText(text);
    }
}