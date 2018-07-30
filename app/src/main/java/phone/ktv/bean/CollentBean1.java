package phone.ktv.bean;

import java.io.Serializable;
import java.util.List;

public class CollentBean1 implements Serializable {

    public String totalCount;
    public String pageSize;
    public String totalPage;
    public String currPage;
    public List<CollentBean2> list;

    @Override
    public String toString() {
        return "MusicNumBean{" +
                "totalCount='" + totalCount + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", totalPage='" + totalPage + '\'' +
                ", currPage='" + currPage + '\'' +
                ", list=" + list +
                '}';
    }
}
