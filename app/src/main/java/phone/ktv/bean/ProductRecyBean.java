package phone.ktv.bean;

import java.io.Serializable;

/**
 * 查看已购买产品
 */
public class ProductRecyBean implements Serializable {

    public String id;
    public String phonedeviceid;
    public String begintime;
    public String endtime;
    public String proid;
    public String pName;

    @Override
    public String toString() {
        return "ProductRecyBean{" +
                "id='" + id + '\'' +
                ", phonedeviceid='" + phonedeviceid + '\'' +
                ", begintime='" + begintime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", proid='" + proid + '\'' +
                ", pName='" + pName + '\'' +
                '}';
    }
}
