package phone.ktv.bean;

/**
 * 开机广告Bean
 */
public class AdverOpenOne {
    public int id;
    public String name;
    public int aid;
    public String aName;
    public int position;
    public int playTime;
    public String beginTime;
    public String endTime;
    public int type;
    public boolean status;
    public String ptype;
    public String goal;
    public AdverOpenTwo ad;

    @Override
    public String toString() {
        return "AdverOpenOne{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", aid=" + aid +
                ", aName='" + aName + '\'' +
                ", position=" + position +
                ", playTime=" + playTime +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", ptype='" + ptype + '\'' +
                ", goal='" + goal + '\'' +
                ", ad=" + ad +
                '}';
    }

    public static class AdverOpenTwo {
        public int id;
        public String name;
        public int type; //资源类型  1.视频 2。图片
        public String path; //广告文件路径
        public String createtime;
        public String backFile; //背景音乐

        @Override
        public String toString() {
            return "AdverOpenTwo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    ", path='" + path + '\'' +
                    ", createtime='" + createtime + '\'' +
                    ", backFile='" + backFile + '\'' +
                    '}';
        }
    }
}