package phone.ktv.bean;

import java.io.Serializable;

public class LatelyBean2 implements Serializable {

    public int id;
    public int sid;
    public int did;
    public int type;
    public String playTime;
    public MusicPlayBean song;

    @Override
    public String toString() {
        return "LatelyBean2{" +
                "id=" + id +
                ", sid=" + sid +
                ", did=" + did +
                ", type=" + type +
                ", playTime='" + playTime + '\'' +
                ", song=" + song +
                '}';
    }
}
