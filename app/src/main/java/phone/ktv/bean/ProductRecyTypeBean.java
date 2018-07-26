package phone.ktv.bean;

import java.io.Serializable;

/**
 * 查看现有产品
 */
public class ProductRecyTypeBean implements Serializable {

    public String id;
    public String name;
    public String type;
    public String cid;
    public String days;
    public int price;
    public String cName;

    @Override
    public String toString() {
        return "ProductRecyTypeBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", cid='" + cid + '\'' +
                ", days='" + days + '\'' +
                ", price='" + price + '\'' +
                ", cName='" + cName + '\'' +
                '}';
    }
}
