package com.dtstep.lighthouse.insights.dto;

import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class ResultData<T> {

    private String code;

    private String message;

    private T data;

    private static MessageSource messageSource;

    public ResultData(){}

    public ResultData(ResultCode resultCode,T data){
        messageSource = SpringUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(resultCode.getI18nLabel(), null, LocaleContextHolder.getLocale());
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
    }

    public ResultData(String customMessage){

    }

    public static<T> ResultData<T> success(T data){
        return new ResultData<T>(ResultCode.success,data);
    }

    public static<T> ResultData<T> success(){
        return new ResultData<T>(ResultCode.success,null);
    }

    public static<T> ResultData<T> failed(ResultCode resultCode){
        return new ResultData<T>(resultCode,null);
    }

    public static<T> ResultData<T> failed(ResultCode resultCode,String ...params){
        messageSource = SpringUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(resultCode.getI18nLabel(),null,LocaleContextHolder.getLocale());
        message = String.format(message,params);
        ResultData resultData = new ResultData();
        resultData.setCode("-1");
        resultData.setMessage(message);
        return resultData;
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
