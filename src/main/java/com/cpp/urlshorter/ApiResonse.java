package com.cpp.urlshorter;

public class ApiResonse<T> {
    private static final int SUCCESS = 0;
    private static final String SUCCESS_MSG = "OK";
    private int code;
    private T data;
    private String msg;

    public ApiResonse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public ApiResonse(T data) {
        this.code = SUCCESS;
        this.msg = SUCCESS_MSG;
        this.data = data;
    }

    public ApiResonse() {
    }

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
}
