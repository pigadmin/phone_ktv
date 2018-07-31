package phone.ktv.tootls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 */
public class StringUtils {
    /**
     * 禁止输入特殊字符
     */
    public static boolean specialString(String s) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
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

    /**
     * 去除空格回车符等
     *
     * @param s
     */
    public static String removeSpace(String s) {
        String regex = "\\s";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("");
    }
}
