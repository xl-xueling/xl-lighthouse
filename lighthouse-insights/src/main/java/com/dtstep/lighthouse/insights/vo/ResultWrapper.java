package com.dtstep.lighthouse.insights.vo;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;

public class ResultWrapper<T> {

    private T data;

    private ResultCode resultCode;

    private ResultWrapper(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    private ResultWrapper(ResultCode resultCode,T data){
        this.resultCode = resultCode;
        this.data = data;
    }

    public static <T> ResultWrapper result(ResultCode resultCode){
        return new ResultWrapper(resultCode);
    }

    public static <T> ResultWrapper result(ResultCode resultCode,T data){
        return new ResultWrapper(resultCode,data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
