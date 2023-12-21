package com.dtstep.lighthouse.commonv2.insights;

public enum ResultCode {

    SUCCESS("0","success"),
    ERROR("1","System Error!"),
    VALIDATE_FAILED("2","Param Validate failed!"),
    FORBIDDEN("3","Access Denied!"),
    UNAUTHORIZED("4","Unauthorized!"),
    AUTH_RENEWAL_FAILED("5","Authorization Renewal Failed"),
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
