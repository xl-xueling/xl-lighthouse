package com.dtstep.lighthouse.web.controller.base;
/*
 * Copyright (C) 2022-2023 XueLing.雪灵
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
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.exception.LightViewIllegalException;
import com.dtstep.lighthouse.common.exception.PermissionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;


@RestController
@ControllerAdvice
public class BaseController {

    @ExceptionHandler(value = PermissionException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, PermissionException e)
    {
        int isSub = (int)request.getAttribute("isSub");
        if(isSub == 1){
            return new ModelAndView("/common/sub_forbidden");
        }else{
            return new ModelAndView("/common/forbidden");
        }
    }

    @ExceptionHandler(value = LightViewIllegalException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, LightViewIllegalException e)
    {
        e.printStackTrace();
        int isView = (int)request.getAttribute("isView");
        int isSub = (int)request.getAttribute("isSub");
        if(isSub == 1){
            return new ModelAndView("/common/sub_404");
        }else if(isView == 1){
            return new ModelAndView("/common/404");
        }else{
            return RequestCodeEnum.toJSON(e.getRequestCodeEnum());
        }
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request,IllegalArgumentException e)
    {
        e.printStackTrace();
        int isView = (int)request.getAttribute("isView");
        int isSub = (int)request.getAttribute("isSub");
        if(isSub == 1){
            return new ModelAndView("/common/sub_404");
        }else if(isView == 1){
            return new ModelAndView("/common/404");
        }else{
            return RequestCodeEnum.toJSON(RequestCodeEnum.PARAM_FORMAT_ERROR);
        }
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request,Exception e)
    {
        e.printStackTrace();
        int isView = (int)request.getAttribute("isView");
        int isSub = (int)request.getAttribute("isSub");
        if(isSub == 1){
            return new ModelAndView("/common/sub_404");
        }else if(isView == 1){
            return new ModelAndView("/common/404");
        }else{
            return RequestCodeEnum.toJSON(RequestCodeEnum.SYSTEM_ERROR);
        }
    }
}
