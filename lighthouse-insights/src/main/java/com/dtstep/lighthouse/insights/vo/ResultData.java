package com.dtstep.lighthouse.insights.vo;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.insights.util.SpringUtil;
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

    public ResultData(String customMessage){}

    public static<T> ResultData<T> success(T data){
        return new ResultData<T>(ResultCode.success,data);
    }

    public static<T> ResultData<T> success(){
        return new ResultData<T>(ResultCode.success,null);
    }

    public static<T> ResultData<T> result(ResultCode resultCode){
        messageSource = SpringUtil.getBean(MessageSource.class);
        Object[] params = resultCode.getParams();
        String message = messageSource.getMessage(resultCode.getI18nLabel(),null,LocaleContextHolder.getLocale());
        if(params != null){
            message = String.format(message,params);
        }
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(resultCode.getCode());
        resultData.setMessage(message);
        resultData.setData(null);
        return resultData;
    }

    public static<T> ResultData<T> result(ServiceResult<T> serviceResult){
        ResultCode resultCode = serviceResult.getResultCode();
        messageSource = SpringUtil.getBean(MessageSource.class);
        Object[] params = resultCode.getParams();
        String message = messageSource.getMessage(resultCode.getI18nLabel(),null,LocaleContextHolder.getLocale());
        if(params != null){
            message = String.format(message,params);
        }
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(resultCode.getCode());
        resultData.setMessage(message);
        resultData.setData(serviceResult.getData());
        return resultData;
    }


    public static<T> ResultData<T> result(ResultCode resultCode, Object ...params){
        messageSource = SpringUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(resultCode.getI18nLabel(),null,LocaleContextHolder.getLocale());
        message = String.format(message,params);
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(resultCode.getCode());
        resultData.setMessage(message);
        return resultData;
    }

    public static String getMessage(ResultCode resultCode,Object ...params){
        messageSource = SpringUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(resultCode.getI18nLabel(),null,LocaleContextHolder.getLocale());
        message = String.format(message,params);
        return message;
    }

    public static String getMessage(ResultCode resultCode){
        messageSource = SpringUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(resultCode.getI18nLabel(),null,LocaleContextHolder.getLocale());
        if(resultCode.getParams() != null){
            message = String.format(message, (Object[]) resultCode.getParams());
        }
        return message;
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
