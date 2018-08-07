package phone.ktv.bean;

public class UserBean {
    public String id;//用户ID

    public String telPhone;//用户手机号

    public String username;//用户名称

    public String password;//用户密码

    public String status;//状态

    public UserBean() {

    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", telPhone='" + telPhone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
