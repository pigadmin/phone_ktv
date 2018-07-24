package phone.ktv.bean;

public class ResultBean{
    public int code;

    public MusicNumBean data;

    public String msg;

    @Override
    public String toString() {
        return "ResultBean{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
