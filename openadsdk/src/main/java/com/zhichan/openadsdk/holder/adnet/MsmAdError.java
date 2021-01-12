package com.zhichan.openadsdk.holder.adnet;

/**
 * author : frankylee
 * date : 2021/1/12 3:46 PM
 * description :
 */
public class MsmAdError {
    private int code;
    private String message;

    public MsmAdError() {
    }

    public MsmAdError(int errorCode, String errorMsg) {
        this.code = errorCode;
        this.message = errorMsg;
    }

    public int getErrorCode() {
        return this.code;
    }

    public String getErrorMsg() {
        return this.message;
    }
}
