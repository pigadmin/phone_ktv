package phone.ktv.tootls;

public class Contants {
    public static final int PermissRequest = 0x11;//6.0权限


    public static class RequestInterfaceClass {
        public static final int RequestSuccess = 100;//请求成功
        public static final int RequestError = 200;//请求失败
        public static final int RequestExpiredToken = 300;//token失效
        public static final int ServerException = 400;//服务器异常
        public static final int DataParsingException = 500;//数据解析异常
        public static final int NoNetworkPresent = 600;//当前无网络
        public static final int OtherError = 700;//其他错误
    }
}
