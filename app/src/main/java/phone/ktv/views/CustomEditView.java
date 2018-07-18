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

public class CustomEditView extends LinearLayout {
    private ImageView mSrcLogo;//图标log
    private EditText mInputTitle;//输入框
    private TextView mVerdCode;//发送验证码

    public CustomEditView(Context context) {
        super(context);
    }

    public CustomEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public CustomEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        View textView = LayoutInflater.from(context).inflate(R.layout.custom_edit_view, this);

        mSrcLogo = textView.findViewById(R.id.src_logo_ivw1);
        mInputTitle= textView.findViewById(R.id.input_edt);
        mVerdCode= textView.findViewById(R.id.verd_code_tvw);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditView);

        int srclog = typedArray.getResourceId(R.styleable.CustomEditView_src_log_type1, 0);//图标显示类型
        String titleType = typedArray.getString(R.styleable.CustomEditView_title_hint_type);//标题hint
        boolean firstState = typedArray.getBoolean(R.styleable.CustomEditView_verd_code_isState, true);//多少首是否隐藏

        typedArray.recycle();
        setDefinedView(srclog, titleType,firstState);
    }

    private void setDefinedView(int srclog,String titleType, boolean firstState) {
        mSrcLogo.setImageResource(srclog);
        mInputTitle.setHint(titleType);
        mVerdCode.setVisibility(firstState?View.VISIBLE:View.GONE);
    }

    public void setInputTitle(String s){
         mInputTitle.setText(s);
    }

    public String getInputTitle(){
        return mInputTitle.getText().toString().trim();
    }
}