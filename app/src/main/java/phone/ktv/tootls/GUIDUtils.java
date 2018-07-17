package phone.ktv.tootls;

import java.util.UUID;

/**
 * @des 生成GUID的工具类
 */
public class GUIDUtils {
    /**
     * @des 生成GUID
     * 格式 : "4255C27C-A444-4D65-8FFB-B04379321B35"
     */
    public static String createGUID(){
        UUID uuid = UUID.randomUUID();
        String a    = uuid.toString();
        a=a.toUpperCase();
        return a;
    }
}
