package com.dtstep.lighthouse.common.entity;

public enum ApiResultCode {

    Success("0","success!"),

    SystemError("101","System Error!"),

    ProcessError("102","Process Error!"),

    ApiNotSupported("103","The api is not supported!"),

    MissingParams("104","Missing required parameters!"),

    MissingParam("105","Missing required parameter[%s]!"),

    IllegalParam("106","Parameter[%s] verification failed!"),

    ;

    private String code;

    private String message;

    ApiResultCode(String code,String message){
        this.code = code;
        this.message = message;
    }

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

    public String formatMessage(String... args) {
        return String.format(this.message, (Object[]) args);
    }
}
