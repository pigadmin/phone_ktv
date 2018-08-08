package phone.ktv.tootls;

import android.os.Environment;

import java.io.File;
import java.util.Calendar;

public class Utils {

    /**
     * 文件路径
     *
     * @return
     */
    public static String getPath() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "HaitunYing" + File.separator;
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    /**
     * 判断sdcard是否被挂载
     *
     * @return
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 文件名字
     *
     * @return
     */
    public static String getCameraPath(String s) {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("Video");
        sb.append(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1; // 0~11
        sb.append(month < 10 ? "0" + month : month);
        int day = calendar.get(Calendar.DATE);
        sb.append(day < 10 ? "0" + day : day);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        sb.append(hour < 10 ? "0" + hour : hour);
        int minute = calendar.get(Calendar.MINUTE);
        sb.append(minute < 10 ? "0" + minute : minute);
        int second = calendar.get(Calendar.SECOND);
        sb.append(second < 10 ? "0" + second : second);
        if (!new File(sb.toString() + "." + s).exists()) {
            return sb.toString() + "." + s;
        }

        StringBuilder tmpSb = new StringBuilder(sb);
        int indexStart = sb.length();
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            tmpSb.append('(');
            tmpSb.append(i);
            tmpSb.append(')');
            tmpSb.append("." + s);
            if (!new File(tmpSb.toString()).exists()) {
                break;
            }

            tmpSb.delete(indexStart, tmpSb.length());
        }
        return tmpSb.toString();
    }

}
