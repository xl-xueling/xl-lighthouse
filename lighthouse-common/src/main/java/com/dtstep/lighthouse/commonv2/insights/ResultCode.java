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

    ResultCode(String code , String i18nLabel){
        this.code = code;
        this.i18nLabel = i18nLabel;
    }

    private String code;

    private String i18nLabel;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getI18nLabel() {
        return i18nLabel;
    }

    public void setI18nLabel(String i18nLabel) {
        this.i18nLabel = i18nLabel;
    }
}
