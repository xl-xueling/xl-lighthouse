package com.dtstep.lighthouse.insights.controller;

import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dto.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.UnexpectedTypeException;

@ControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, MethodArgumentNotValidException e)
    {
        logger.info(e.getMessage());
        return ResultData.failed(ResultCode.paramValidateFailed);
    }

    @ExceptionHandler(value = PermissionException.class)
    @ResponseBody
    public Object globalPermissionExceptionHandler(HttpServletRequest request, PermissionException e)
    {
        logger.info(e.getMessage());
        return ResultData.failed(ResultCode.accessDenied);
    }

    @ExceptionHandler(value = UnexpectedTypeException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, UnexpectedTypeException e)
    {
        logger.info(e.getMessage());
        return ResultData.failed(ResultCode.paramValidateFailed);
    }


}
