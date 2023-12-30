package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.commonv2.insights.ResultData;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.UnexpectedTypeException;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, MethodArgumentNotValidException e)
    {
        return ResultData.failed(ResultCode.paramValidateFailed);
    }


    @ExceptionHandler(value = UnexpectedTypeException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, UnexpectedTypeException e)
    {
        return ResultData.failed(ResultCode.paramValidateFailed);
    }
}
