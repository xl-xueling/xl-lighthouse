package com.dtstep.lighthouse.common.entity;

import java.io.Serializable;

public class ApiResultData implements Serializable {

    private String code;

    private Object data;

    private String message;

    public ApiResultData(String code,String message){
        this.code = code;
        this.message = message;
    }

    public ApiResultData(String code,String message,Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
