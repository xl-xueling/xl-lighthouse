package com.dtstep.lighthouse.common.entity;

public enum ApiResultCode {

    Success("0","success!"),

    AuthorizationError("100","Authorization Error!"),

    SystemError("101","System Error!"),

    ProcessError("102","Process Error!"),

    ParametersParseException("103","Parameters parsing exception!"),

    ApiNotSupported("104","The current call is not supported!"),

    MissingParams("105","Missing required parameters!"),

    MissingParam("106","Missing required parameter[%s]!"),

    IllegalParam("107","Parameter[%s] verification failed!"),

    ApiCallerNotExist("108","Caller[%s] does not exist!"),

    ApiCallerKeyIncorrect("109","Caller[%s] secret-key verification failed!"),

    UnavailableError("999","Remote service[%s] is unavailable!"),

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
