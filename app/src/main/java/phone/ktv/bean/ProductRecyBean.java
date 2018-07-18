package phone.ktv.bean;

import java.io.Serializable;

public class ProductRecyBean implements Serializable {

    public String address;
    public String date;

    public ProductRecyBean(String address, String date) {
        this.address = address;
        this.date = date;
    }
}
