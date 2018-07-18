package phone.ktv.bean;

import java.io.Serializable;

public class ProductRecyTypeBean implements Serializable {

    public String address;
    public String date;

    public ProductRecyTypeBean(String address, String date) {
        this.address = address;
        this.date = date;
    }
}
