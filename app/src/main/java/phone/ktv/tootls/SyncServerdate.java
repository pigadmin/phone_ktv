package phone.ktv.tootls;

import java.util.Calendar;

public class SyncServerdate {

    /**
     * 获取本地时间戳
     *
     * @return
     */
    public static long getLocalTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

}
