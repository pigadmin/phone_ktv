package phone.ktv.bean;

import java.io.Serializable;

public class WelcomAd implements Serializable {
    private int id;

    private String name;

    private int aid;

    private String aName;

    private int position;

    private int playTime;

    private String beginTime;

    private String endTime;

    private int type;

    private boolean status;

    private String ptype;

    private String goal;

    private String ad;

    private String gName;

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

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getAid() {
        return this.aid;
    }

    public void setAName(String aName) {
        this.aName = aName;
    }

    public String getAName() {
        return this.aName;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getPlayTime() {
        return this.playTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getPtype() {
        return this.ptype;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getGoal() {
        return this.goal;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAd() {
        return this.ad;
    }

    public void setGName(String gName) {
        this.gName = gName;
    }

    public String getGName() {
        return this.gName;
    }

}
