package phone.ktv.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 解析歌曲列表Bean
 */
@Table(name = "MusicPlayBean")
public class MusicPlayBean implements Serializable {

    @Column(name = "id", isId = true, autoGen = true)
    public String id;//主键

    @Column(name = "songnumber")
    public String songnumber;//编号

    @Column(name = "singerid")
    public int singerid;//

    @Column(name = "name")
    public String name;//歌曲名称

    @Column(name = "nameVietnam")
    public String nameVietnam;//越南

    @Column(name = "namejapanese")
    public String namejapanese;//日本

    @Column(name = "namezhuyin")
    public String namezhuyin;//注音

    @Column(name = "namepinyin")
    public String namepinyin;//拼音

    @Column(name = "path")
    public String path;//歌曲地址

    @Column(name = "lanId")
    public int lanId;//

    @Column(name = "label")
    public String label;//

    @Column(name = "singerName")
    public String singerName;//歌手名称

    @Column(name = "lanName")
    public String lanName;//语言

    @Column(name = "fileName")
    public String fileName;

    @Column(name = "isTop")
    public boolean isTop;//是否添加

    @Column(name = "localTime")
    public long localTime;//本地时间

    public boolean isState;//选中状态

    public String sid;//使用于 收藏列表和最近播放列表的删除功能

    public MusicPlayBean() {

    }

    @Override
    public String toString() {
        return "MusicPlayBean{" +
                "id='" + id + '\'' +
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
                ", isTop=" + isTop +
                ", localTime=" + localTime +
                ", isState=" + isState +
                ", sid='" + sid + '\'' +
                '}';
    }
}
