package com.dtstep.lighthouse.web.param;
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
import com.dtstep.lighthouse.common.entity.annotation.valid.*;
import com.dtstep.lighthouse.common.entity.user.UserEntity;
import com.dtstep.lighthouse.common.enums.result.RequestCodeEnum;
import com.dtstep.lighthouse.common.exception.LightViewIllegalException;
import com.dtstep.lighthouse.common.util.DateUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;


public final class ParamWrapper {

    private static final Logger logger = LoggerFactory.getLogger(ParamWrapper.class);

    private ParamWrapper(){}

    public static int getIntValueOrElse(HttpServletRequest request,String param,int defaultValue){
        String value = request.getParameter(param);
        if(StringUtil.isEmpty(value)){
           return defaultValue;
        }else if(!StringUtil.isNumber(value) || "-1".equals(value)){
            return defaultValue;
        }else{
            return Integer.parseInt(value);
        }
    }

    public static long getLongValueOrElse(HttpServletRequest request,String param,long defaultValue){
        String value = request.getParameter(param);
        if(StringUtil.isEmpty(value)){
            return defaultValue;
        }else if(!StringUtil.isNumber(value) || "-1".equals(value)){
            return defaultValue;
        }else{
            return Long.parseLong(value);
        }
    }

    public static int getIntValue(HttpServletRequest request,String param){
        String value = request.getParameter(param);
        if(StringUtil.isEmpty(value) || !StringUtil.isNumber(value)){
            logger.info("param valid,param:{},value:{},valid result:{}",param,value, RequestCodeEnum.PARAM_FORMAT_ERROR);
            throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
        }
        return Integer.parseInt(value);
    }

    public static Date getDateValue(HttpServletRequest request,String param,String format){
        String value = request.getParameter(param);
        if(StringUtil.isEmpty(value)){
            return null;
        }
        return new Date(DateUtil.parseDate(value,format));
    }

    public static String getValue(HttpServletRequest request,String param){
        String value = request.getParameter(param);
        return StringUtil.isEmptyOrNullStr(value) ? null : value;
    }

    public static <T> RequestCodeEnum valid(Class<T> clazz, String fieldName, Object value) throws Exception{
        Field field = ReflectionUtils.findField(clazz,fieldName);
        Annotation[] annotations = field.getAnnotations();
        if(annotations == null){
            return RequestCodeEnum.SUCCESS;
        }
        for(Annotation annotation : annotations){
            if(annotation.annotationType() == S_NotNull.class){
                if(value == null || StringUtil.isEmpty(value.toString()) || "-1".equals(value.toString())){
                    logger.info("param valid,param:{},value:{},valid result:{}",fieldName,value, RequestCodeEnum.REQUEST_PARAM_MISSING);
                    throw new LightViewIllegalException(RequestCodeEnum.REQUEST_PARAM_MISSING);
                }
            }else if(annotation.annotationType() == S_Integer.class){
                if(value != null && !StringUtil.isInt(value.toString())){
                    logger.info("param valid,param:{},value:{},valid result:{}",fieldName,value, RequestCodeEnum.PARAM_FORMAT_ERROR);
                    throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                }
            }else if(annotation.annotationType() == S_Numeric.class){
                if(value != null && !StringUtil.isNumber(value.toString())){
                    logger.info("param valid,param:{},value:{},valid result:{}",fieldName,value, RequestCodeEnum.PARAM_FORMAT_ERROR);
                    throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                }
            }else if(annotation.annotationType() == S_Phone.class){
                if(value != null && !StringUtil.isPhone(value.toString())){
                    logger.info("param valid,param:{},value:{},valid result:{}",fieldName,value, RequestCodeEnum.PARAM_FORMAT_ERROR);
                    throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                }
            }else if(annotation.annotationType() == S_Email.class){
                if(value != null && !StringUtil.isEmail(value.toString())){
                    logger.info("param valid,param:{},value:{},valid result:{}",fieldName,value, RequestCodeEnum.PARAM_FORMAT_ERROR);
                    throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                }
            }else if(annotation.annotationType() == S_Pattern.class){
                if(value != null){
                    S_Pattern an = (S_Pattern)annotation;
                    if(!StringUtil.isEmpty(an.pattern()) && !value.toString().matches(an.pattern())){
                        logger.info("param valid,param:{},value:{},valid result:{}",fieldName,value, RequestCodeEnum.PARAM_FORMAT_ERROR);
                        throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                    }
                }
            }else if(annotation.annotationType() == S_Length.class){
                if(value != null){
                    S_Length an = (S_Length)annotation;
                    int max = an.max();
                    int min = an.min();
                    if(min != -1 && value.toString().length() < min){
                        logger.info("param valid,param:{},value:{}",fieldName,value);
                        throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                    }
                    if(max != -1 && value.toString().length() > max){
                        logger.info("param valid,param:{},value:{}",fieldName,value);
                        throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                    }
                }
            }else if(annotation.annotationType() == S_Illegal.class){
                if(value != null && StringUtil.isExistSpecialChar(value.toString())){
                    logger.info("param valid,param:{},value:{}",fieldName,value);
                    throw new LightViewIllegalException(RequestCodeEnum.PARAM_FORMAT_ERROR);
                }
            }
        }
        return RequestCodeEnum.SUCCESS;
    }


    public static UserEntity getCurrentUser(HttpServletRequest request){
        return (UserEntity)request.getSession().getAttribute("user");
    }

    public static int getCurrentUserId(HttpServletRequest request){
        UserEntity userEntity = (UserEntity)request.getSession().getAttribute("user");
        return userEntity != null ? userEntity.getId() : -1;
    }

    public static String getBaseLink(String url, Map<String,Object> paramMap) throws Exception {
        StringBuilder sbr = new StringBuilder(url);
        int index = 0;
        if(MapUtils.isNotEmpty(paramMap)){
            for(String param : paramMap.keySet()){
                Object value = paramMap.get(param);
                if(value == null || StringUtil.isEmpty(value.toString()) || "-1".equals(value.toString())){
                    continue;
                }
                String valueStr = value.toString();
                if(index == 0){
                    sbr.append("?");
                }else{
                    sbr.append("&");
                }
                if(StringUtil.isLetterNumOrUnderLine(valueStr)){
                    sbr.append(param).append("=").append(valueStr);
                }else{
                    sbr.append(param).append("=").append(URLEncoder.encode(URLEncoder.encode(valueStr, "UTF-8"), "UTF-8"));
                }
                index++;
            }
        }
        if(index > 0){
            sbr.append("&page=#page");
        }else {
            sbr.append("?page=#page");
        }
        return sbr.toString();
    }
}
