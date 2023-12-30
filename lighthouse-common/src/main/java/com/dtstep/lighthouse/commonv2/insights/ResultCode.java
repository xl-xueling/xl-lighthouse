package com.dtstep.lighthouse.commonv2.insights;

public enum ResultCode {

    success("0","success"),
    systemError("1","systemError"),
    paramValidateFailed("2","paramValidateFailed"),

    accessDenied("3","accessDenied"),
    unauthorized("4","unauthorized"),

    loginCheckFailed("1001","loginCheckFailed"),

    authRenewalFailed("1002","authRenewalFailed"),
    ;

    ResultCode(String code , String message){
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
