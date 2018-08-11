package phone.ktv.bean;

public class NoticeBean {

    public int id;
    public String name;
    public String path;
    public String url;

    @Override
    public String toString() {
        return "NoticeBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
