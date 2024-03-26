package com.dtstep.lighthouse.insights.aspect;
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
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.insights.controller.annotation.RecordAnnotation;
import com.dtstep.lighthouse.common.enums.RecordTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Record;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.insights.service.BaseService;
import com.dtstep.lighthouse.insights.service.RecordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class RecordAspect {

    @Autowired
    private RecordService recordService;

    @Autowired
    private BaseService baseService;

    @Pointcut("@annotation(com.dtstep.lighthouse.insights.controller.annotation.RecordAnnotation)")
    public void serviceMethod() {}

    @AfterReturning(pointcut = "serviceMethod()", returning = "result")
    public void afterService(JoinPoint joinPoint, Object result) {
        int userId = baseService.getCurrentUserId();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        RecordAnnotation recordAnnotation = signature.getMethod().getAnnotation(RecordAnnotation.class);
        RecordTypeEnum recordTypeEnum = recordAnnotation.recordType();
        Record record = null;
        if(recordTypeEnum == RecordTypeEnum.UPDATE_STAT){
            Stat param = (Stat)args[0];
            if((Integer)result > 0){
                record = new Record();
                record.setUserId(userId);
                record.setRecordType(recordTypeEnum);
                record.setCreateTime(LocalDateTime.now());
                record.setExtend(JsonUtil.toJSONString(param));
                record.setResourceId(param.getId());
                record.setResourceType(ResourceTypeEnum.Stat);
            }
        }
        if(record != null){
            recordService.create(record);
        }
    }
}
