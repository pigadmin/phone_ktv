package phone.ktv.bean;

import java.io.Serializable;

/**
 * 排行榜Bean
 */
public class GridItem implements Serializable {
    public String id;//id
    public String name;//名称
    public String icon;//图片地址
    public String postion;//
    public String ngPath;//

    public GridItem(String id, String name, String icon, String postion, String ngPath) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.postion = postion;
        this.ngPath = ngPath;
    }

    public GridItem() {
    }

    @Override
    public String toString() {
        return "GridItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", postion='" + postion + '\'' +
                ", ngPath='" + ngPath + '\'' +
                '}';
    }
}
