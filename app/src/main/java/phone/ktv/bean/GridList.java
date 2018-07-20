package phone.ktv.bean;

import java.util.List;

public class GridList {
    private int totalCount;

    private int pageSize;

    private int totalPage;

    private int currPage;

    private List<ListInfo> list;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getCurrPage() {
        return this.currPage;
    }

    public void setList(List<ListInfo> list) {
        this.list = list;
    }

    public List<ListInfo> getList() {
        return this.list;
    }
}
