package phone.ktv.bean;

public class ColleResultBean {
    public int code;

    public CollentBean1 data;

    public String msg;

    @Override
    public String toString() {
        return "CollectionResultBean{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
