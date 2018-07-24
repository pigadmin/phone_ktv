package phone.ktv.bean;

import java.io.Serializable;

public class SingerNumBean implements Serializable {

    public String id;//id
    public String singertypeid;//歌星分类ID
    public String name;//歌手名称
    public String sex;//性别: 男:1   女:2
    public String nameen;//英文
    public String namejapanese;//日本
    public String namezhuyin;//注音
    public String nameVietnam;//越南
    public String iconPath;//地址
    public String singerTypeName;//歌星分类名称
    public String ngPath;//

    @Override
    public String toString() {
        return "SingerBean{" +
                "id='" + id + '\'' +
                ", singertypeid='" + singertypeid + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nameen='" + nameen + '\'' +
                ", namejapanese='" + namejapanese + '\'' +
                ", namezhuyin='" + namezhuyin + '\'' +
                ", nameVietnam='" + nameVietnam + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", singerTypeName='" + singerTypeName + '\'' +
                ", ngPath='" + ngPath + '\'' +
                '}';
    }
}
