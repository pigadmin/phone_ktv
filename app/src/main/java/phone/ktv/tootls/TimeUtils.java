package phone.ktv.tootls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author quan zi
 * @time 2016/8/25 11:33
 * @des 1.时间的工具类
 */
public class TimeUtils {

    /**
     * 获取本地时间
     * @return 时:分
     */
    public static String getLocalDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(new Date(calendar.getTimeInMillis()));
    }

    /**
     * 获取当前时间
     * @param formatType
     *            yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurTime(String formatType) {
        Calendar calendar = new GregorianCalendar();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        return format.format(date);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

}
