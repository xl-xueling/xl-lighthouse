package com.dtstep.lighthouse.insights.controller;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
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
import com.dtstep.lighthouse.common.exception.AuthorizeException;
import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.insights.vo.ResultData;
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
        logger.error("globalErrorHandler",e);
        return ResultData.result(ResultCode.paramValidateFailed);
    }

    @ExceptionHandler(value = PermissionException.class)
    @ResponseBody
    public Object globalPermissionExceptionHandler(HttpServletRequest request, PermissionException e)
    {
        return ResultData.result(ResultCode.accessDenied);
    }

    @ExceptionHandler(value = AuthorizeException.class)
    @ResponseBody
    public Object globalAuthorizeExceptionHandler(HttpServletRequest request, AuthorizeException e)
    {
        logger.warn("System is not authorized!");
        return ResultData.result(ResultCode.systemUnauthorized);
    }

    @ExceptionHandler(value = UnexpectedTypeException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, UnexpectedTypeException e)
    {
        logger.error("globalErrorHandler",e);
        return ResultData.result(ResultCode.paramValidateFailed);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handleException(Exception ex) {
        logger.error("System Error!",ex);
        return ResultData.result(ResultCode.systemError);
    }
}
