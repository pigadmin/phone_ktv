package phone.ktv.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import phone.ktv.R;

public class CustomSetupView extends LinearLayout {

    private TextView mTextTitle1;//名称
    private TextView mTextTitle2;
    private SwitchButton mSwitch;

    public CustomSetupView(Context context) {
        super(context);
    }

    public CustomSetupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public CustomSetupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        View textView = LayoutInflater.from(context).inflate(R.layout.custom_setup_view, this);

        mTextTitle1 = textView.findViewById(R.id.text_title1_tvw);
        mTextTitle2 = textView.findViewById(R.id.text_title2_tvw);
        mSwitch = textView.findViewById(R.id.switch_btn);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSetupView);

        String titleType = typedArray.getString(R.styleable.CustomSetupView_setup_text);
        boolean oPenText = typedArray.getBoolean(R.styleable.CustomSetupView_no_open_isState, true);
        boolean oPenSwitchState = typedArray.getBoolean(R.styleable.CustomSetupView_no_switch_btn_isState, true);
        boolean switchState = typedArray.getBoolean(R.styleable.CustomSetupView_switch_btn_isState, true);


        typedArray.recycle();
        setDefinedView(titleType, oPenText, oPenSwitchState, switchState);
    }

    private void setDefinedView(String titleType, boolean oPenText, boolean oPenSwitchState, boolean switchState) {
        mTextTitle1.setText(titleType);
        mTextTitle2.setVisibility(oPenText ? View.VISIBLE : View.GONE);
        mSwitch.setVisibility(oPenSwitchState ? View.VISIBLE : View.GONE);
        mSwitch.setChecked(switchState ? true : false);
    }

    /**
     * 开关事件
     *
     * @param listener
     */
    private void OnCheckSwitchLiter(SwitchButton.OnCheckedChangeListener listener) {
        mSwitch.setOnCheckedChangeListener(listener);
    }
}