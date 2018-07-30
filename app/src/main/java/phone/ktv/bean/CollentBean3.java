package phone.ktv.bean;

import java.io.Serializable;

public class CollentBean3 implements Serializable {

    public int id;
    public String songnumber;
    public int singerid;
    public String name;
    public String nameVietnam;
    public String namejapanese;
    public String namezhuyin;
    public String namepinyin;
    public String path;
    public int lanId;
    public String label;
    public String singerName;
    public String lanName;
    public String fileName;

    @Override
    public String toString() {
        return "PengBean{" +
                "id=" + id +
                ", songnumber='" + songnumber + '\'' +
                ", singerid=" + singerid +
                ", name='" + name + '\'' +
                ", nameVietnam='" + nameVietnam + '\'' +
                ", namejapanese='" + namejapanese + '\'' +
                ", namezhuyin='" + namezhuyin + '\'' +
                ", namepinyin='" + namepinyin + '\'' +
                ", path='" + path + '\'' +
                ", lanId=" + lanId +
                ", label='" + label + '\'' +
                ", singerName='" + singerName + '\'' +
                ", lanName='" + lanName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
