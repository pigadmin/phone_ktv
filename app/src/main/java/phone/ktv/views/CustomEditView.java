package phone.ktv.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
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
    public TextView mVerdCode;//发送验证码

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

        String digits = typedArray.getString(R.styleable.CustomEditView_input_digits);//EditText的输入字符设定 (内容)
        int inputType = typedArray.getInteger(R.styleable.CustomEditView_input_type, InputType.TYPE_CLASS_TEXT);//EditText的输入法 (内容),默认为text文本
        int maxLength = typedArray.getInteger(R.styleable.CustomEditView_maxLength, 15);//EditText的maxLeng (内容)

        typedArray.recycle();
        setDefinedView(srclog, titleType,firstState,digits,inputType,maxLength);
    }
//
    private void setDefinedView(int srclog,String titleType, boolean firstState,String digits,int inputType,int maxLength) {
        mSrcLogo.setImageResource(srclog);
        mInputTitle.setHint(titleType);
        mVerdCode.setVisibility(firstState?View.VISIBLE:View.GONE);
        setCutomInputDigits(digits);//指定字符输入
        mInputTitle.setInputType(inputType);//输入类型
        mInputTitle.setFilters(new InputFilter[]{setInputLength(maxLength)});//输入长度
    }

    /**
     * set输入框值
     * @param s
     */
    public void setInputTitle(String s){
         mInputTitle.setText(s);
    }

    /**
     * get输入框值
     * @return
     */
    public String getInputTitle(){
        return mInputTitle.getText().toString().trim();
    }
    /**
     * set验证码按钮文本
     * @param s
     */
    public void setVerdCode(String s){
        mVerdCode.setText(s);
    }

    /**
     * get验证码按钮文本
     * @return
     */
    public String getVerdCode(){
        return mVerdCode.getText().toString().trim();
    }

    /**
     * 发送验证码
     * @param listener
     */
    public void sendOnClick(OnClickListener listener){
        mVerdCode.setOnClickListener(listener);
    }

    /**
     * 设置 "指定字符才可输入"
     *
     * @param digits
     */
    public void setCutomInputDigits(String digits) {
        if (!TextUtils.isEmpty(digits)) {
            mInputTitle.setKeyListener(DigitsKeyListener.getInstance(digits));
        }
    }

    public InputFilter setInputLength(int quantity) {
        return new InputFilter.LengthFilter(quantity);
    }
}