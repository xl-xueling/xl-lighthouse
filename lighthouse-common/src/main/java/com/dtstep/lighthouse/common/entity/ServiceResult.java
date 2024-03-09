package com.dtstep.lighthouse.common.entity;

public class ServiceResult<T> {

    private T data;

    private ResultCode resultCode;

    private ServiceResult(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    private ServiceResult(ResultCode resultCode, T data){
        this.resultCode = resultCode;
        this.data = data;
    }

    public static <T> ServiceResult<T> result(ResultCode resultCode){
        return new ServiceResult<>(resultCode);
    }

    public static <T> ServiceResult<T> result(ResultCode resultCode, T data){
        return new ServiceResult<T>(resultCode,data);
    }

    public static <T> ServiceResult<T> success(T data){
        return new ServiceResult<>(ResultCode.success,data);
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


    public boolean isSuccess(){
        return this.resultCode == ResultCode.success;
    }
}
