package phone.ktv.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import phone.ktv.R;

public class CustomPrordView extends LinearLayout {

    private TextView mName;//名称
    private TextView mAddress;//地址
    private TextView mNameDate;//时间
    private TextView mDate;//时间内容

    public CustomPrordView(Context context) {
        super(context);
    }

    public CustomPrordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public CustomPrordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        View textView = LayoutInflater.from(context).inflate(R.layout.custom_prord_view, this);

        mName = textView.findViewById(R.id.name_tvw);
        mAddress= textView.findViewById(R.id.address_tvw);
        mNameDate= textView.findViewById(R.id.name_date_tvw);
        mDate= textView.findViewById(R.id.date_tvw);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditView);

        String titleType = typedArray.getString(R.styleable.CustomEditView_title_hint_type);
        boolean firstState = typedArray.getBoolean(R.styleable.CustomEditView_verd_code_isState, true);

        typedArray.recycle();
//        setDefinedView(srclog, titleType,firstState);
    }

    private void setDefinedView(String titleType, boolean firstState) {
//        mSrcLogo.setImageResource(srclog);
//        mInputTitle.setHint(titleType);
    }

//    2.地址值
//    3.时间内容
}