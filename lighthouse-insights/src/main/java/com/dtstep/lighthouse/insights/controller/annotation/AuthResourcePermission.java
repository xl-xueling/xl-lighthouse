package com.dtstep.lighthouse.insights.controller.annotation;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthResourcePermission {

    String resourceId() default "";

    String resourceType() default "";
}
