package phone.ktv.tootls;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONObject;

import java.util.List;

/**
 * 讯飞语音识别
 */
public class SpeechRecognitionUtils implements RecognizerDialogListener, InitListener {
    private static final String TAG = "SpeechRecognitionUtils";

    StringBuilder builder = new StringBuilder();

    public Context context;
    public String result;
    public EditText editText;

    public SpeechRecognitionUtils(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        printResult(recognizerResult);
    }

    @Override
    public void onError(SpeechError speechError) {

    }

    @Override
    public void onInit(int code) {
        if (code != ErrorCode.SUCCESS) {
            Toast.makeText(context, "初始化失败，错误码：" + code,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void startDialog() {
        RecognizerDialog dialog = new RecognizerDialog(context, this);
        dialog.setListener(this);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//语种，这里可以有zh_cn和en_us
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");//设置口音，这里设置的是汉语普通话 具体支持口音请查看讯飞文档
        dialog.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");//设置编码类型
        dialog.show();
        TextView txt = dialog.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText("");
    }

    //打印结果
    private void printResult(RecognizerResult results) {
        String json = results.getResultString().toString();
        Log.i(TAG, "json.." + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            String ws = jsonObject.getString("ws");
            boolean ls = jsonObject.getBoolean("ls");
            if (!ls){
                List<bean2> collentBean = GsonJsonUtils.parseJson2Obj(ws, new TypeToken<List<bean2>>() {
                });

                for (bean2 a : collentBean) {
                    for (bean3 x : a.cw) {
                        result = x.w;
                        if (!TextUtils.isEmpty(result)) {
                            builder.append(result);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "e.." + e.getMessage());
        }

        Log.d(TAG, "build.." + builder.toString());
        editText.setText(builder.toString());
        editText.setSelection(editText.length());
    }

    public class bean2 {

        public int bg;
        public List<bean3> cw;

        @Override
        public String toString() {
            return "bean2{" +
                    "bg=" + bg +
                    ", cw=" + cw +
                    '}';
        }
    }

    public class bean3 {

        public int sc;
        public String w;

        @Override
        public String toString() {
            return "bean3{" +
                    "sc=" + sc +
                    ", w='" + w + '\'' +
                    '}';
        }
    }

}
