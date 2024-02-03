package com.dtstep.lighthouse.insights.controller.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthResourcePermission {

    String resourceId() default "";

    String resourceType() default "";
}
