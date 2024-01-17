package com.dtstep.lighthouse.insights.controller.annotation;

import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordAnnotation {

    RecordTypeEnum recordType();
}
