package phone.ktv.tootls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 */
public class StingUtils {
    /**
     * 禁止输入特殊字符
     */
    public static boolean specialString(String s) {
        String regEx = "[`~!@#$%^&*().+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.find() ? true : false;
    }
//            Toast.makeText(InputRegisterInfoActivity.this, "姓名不允许输入特殊符号！", Toast.LENGTH_LONG).show();
}
