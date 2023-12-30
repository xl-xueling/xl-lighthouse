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

    private MessageSource messageSource;

    public ResultData(){}

    public ResultData(ResultCode resultCode,T data){
        messageSource = SpringUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(resultCode.getI18nLabel(), null, LocaleContextHolder.getLocale());
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
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
