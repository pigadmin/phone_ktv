package phone.ktv.bean;

import java.io.Serializable;

public class ListInfo implements Serializable {
    private int id;

    private String name;

    private String path;

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

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setNgPath(String ngPath) {
        this.ngPath = ngPath;
    }

    public String getNgPath() {
        return this.ngPath;
    }

}
