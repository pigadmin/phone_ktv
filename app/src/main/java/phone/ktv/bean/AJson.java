package phone.ktv.bean;

import java.io.Serializable;

public class AJson<T> implements Serializable {
    private int code;

    private T data;

    private String msg;

    private String errorInfo;

    private String token;//令牌

    private int collect_count;//收藏列表数量

    private int record_count;//最近播放数量

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

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public int getRecord_count() {
        return record_count;
    }

    public void setRecord_count(int record_count) {
        this.record_count = record_count;
    }

    @Override
    public String toString() {
        return "AJson{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                ", token='" + token + '\'' +
                ", collect_count=" + collect_count +
                ", record_count=" + record_count +
                '}';
    }
}
