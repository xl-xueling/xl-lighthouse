package com.dtstep.lighthouse.commonv2.insights;

import com.dtstep.lighthouse.common.util.JsonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResultData<T> {

    private String code;

    private String message;

    private T data;

    public ResultData(){}

    public ResultData(String code ,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultData(ResultCode resultCode ,String message,T data){
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
    }

    public ResultData(ResultCode resultCode,T data){
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static<T> ResultData<T> success(T data){
        return new ResultData<T>(ResultCode.SUCCESS,data);
    }

    public static<T> ResultData<T> success(){
        return new ResultData<T>(ResultCode.SUCCESS,null);
    }

    public static<T> ResultData<T> failed(ResultCode resultCode){
        return new ResultData<T>(resultCode,null);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
