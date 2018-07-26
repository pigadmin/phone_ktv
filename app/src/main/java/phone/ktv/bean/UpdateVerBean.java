package phone.ktv.bean;

/**
 * 版本更新Bean
 */
public class UpdateVerBean {
    public String id;
    public String name;
    public String path;//apk地址
    public String beginTime;
    public String endTime;
    public String version;//版本号
    public String type;
    public String gid;
    public String gName;

    public UpdateVerBean() {

    }

    @Override
    public String toString() {
        return "UpdateVerBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", gid='" + gid + '\'' +
                ", gName='" + gName + '\'' +
                '}';
    }
}
