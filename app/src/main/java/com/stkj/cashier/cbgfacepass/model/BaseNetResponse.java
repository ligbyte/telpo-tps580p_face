package com.stkj.cashier.cbgfacepass.model;

import android.text.TextUtils;

public class BaseNetResponse<T> {
    private String Code;
    private String Message;
    private T Data;

    public BaseNetResponse() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public boolean isSuccess() {
        return TextUtils.equals("200", Code) || TextUtils.equals("10000", Code);
    }

    public boolean isTokenInvalid() {
        return "401".equals(Code);
    }
}
