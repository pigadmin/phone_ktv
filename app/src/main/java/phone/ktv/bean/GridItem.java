package phone.ktv.bean;

import java.io.Serializable;

public class GridItem implements Serializable {
    private int id;

    private String name;

    private String icon;

    private int postion;

    private String ngPath;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public int getPostion() {
        return this.postion;
    }

    public void setNgPath(String ngPath) {
        this.ngPath = ngPath;
    }

    public String getNgPath() {
        return this.ngPath;
    }

}
