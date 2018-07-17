package phone.ktv.tootls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfoUtils {

    /**
     * @des 获取手机唯一标识
     */
    @SuppressLint("MissingPermission")
    public static String szImei(Context context){
        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        return telephonyMgr.getDeviceId();
    }
}
