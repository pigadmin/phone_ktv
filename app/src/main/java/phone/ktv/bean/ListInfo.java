package phone.ktv.bean;

import java.io.Serializable;

public class ListInfo implements Serializable {
    private String id;

    private String name;

    private String path;

    private String ngPath;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
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
