package phone.ktv.bean;

import java.util.List;

public class MusicNumBean {

    public int totalCount;
    public String pageSize;
    public String totalPage;
    public String currPage;
    public List<MusicPlayBean> list;

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
