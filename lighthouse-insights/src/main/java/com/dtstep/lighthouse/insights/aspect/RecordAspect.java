package com.dtstep.lighthouse.insights.aspect;

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
                record.setRecordTime(LocalDateTime.now());
                record.setDesc(JsonUtil.toJSONString(param));
                record.setResourceId(param.getId());
                record.setResourceType(ResourceTypeEnum.Stat);
            }
        }
        if(record != null){
            recordService.create(record);
        }
        System.out.println("args is:" + JsonUtil.toJSONString(args));
        System.out.println("result is:" + JsonUtil.toJSONString(result));
        System.out.println("recordTypeEnum is:" + recordTypeEnum);
    }


}
