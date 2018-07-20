package phone.ktv.bean;

public class UserBean {
    public String id;//用户ID

    public String telPhone;//用户手机号

    public String username;//用户名称

    public String password;//用户密码

    public String status;//状态

    public UserBean() {

    }

    public UserBean(String id, String telPhone, String username, String password, String status) {
        this.id = id;
        this.telPhone = telPhone;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", telPhone='" + telPhone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}
