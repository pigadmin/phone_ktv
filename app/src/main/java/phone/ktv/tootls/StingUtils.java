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
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.find() ? true : false;
    }

    /**
     * 判断字符串是否包含-中文
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
