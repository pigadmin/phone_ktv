package phone.ktv.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import phone.ktv.R;

public class CustomTextView extends LinearLayout {
    private ImageView mSrcLogo;//图标log
    private TextView mTextTitle;//标题
    private TextView mNumFirst;//多少首

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        View textView = LayoutInflater.from(context).inflate(R.layout.custom_textview, this);

        mSrcLogo = textView.findViewById(R.id.src_logo_ivw);
        mTextTitle = textView.findViewById(R.id.text_title_tvw);
        mNumFirst = textView.findViewById(R.id.num_first_tvw);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        int srclog = typedArray.getResourceId(R.styleable.CustomTextView_src_log_type, 0);//图标显示类型
        String titleType = typedArray.getString(R.styleable.CustomTextView_title_type);//标题显示类型
        boolean firstState = typedArray.getBoolean(R.styleable.CustomTextView_num_first_isState, true);//多少首是否隐藏
        String firstString = typedArray.getString(R.styleable.CustomTextView_num_first_string);//多少首动态设置文字

        typedArray.recycle();
        setDefinedView(srclog, titleType, firstState, firstString);
    }

    private void setDefinedView(int srclog, String titleType, boolean firstState, String firstString) {
        mSrcLogo.setImageResource(srclog);
        mTextTitle.setText(titleType);
        mNumFirst.setVisibility(firstState ? View.VISIBLE : View.GONE);
        mNumFirst.setText(firstString);
    }

    public void setNumFirst(String s) {
        mNumFirst.setText(s);
    }
}