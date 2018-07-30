package phone.ktv.bean;

import java.io.Serializable;

public class CollentBean2 implements Serializable {

    public int id;
    public int sid;
    public int did;
    public int type;
    public String createTime;
    public CollentBean3 song;

    @Override
    public String toString() {
        return "MusicPlayBean{" +
                "id=" + id +
                ", sid=" + sid +
                ", did=" + did +
                ", type=" + type +
                ", createTime='" + createTime + '\'' +
                ", song=" + song +
                '}';
    }
}
