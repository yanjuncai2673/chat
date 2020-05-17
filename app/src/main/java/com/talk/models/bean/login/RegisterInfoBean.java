package com.talk.models.bean.login;

public class RegisterInfoBean {

    /**
     * err : 200
     * errmsg :
     * data : 注册成功
     * code : 10000
     */

    private int err;
    private String errmsg;
    private String data;
    private int code;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
