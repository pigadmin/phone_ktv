package phone.ktv.bean;

import java.io.Serializable;

public class AJson<T> implements Serializable{
    private int code;

    private T data;

    private String msg;

    private String errorInfo;

    private String token;//令牌

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AJson{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
