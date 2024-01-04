package com.dtstep.lighthouse.insights.controller.annotation;

import com.dtstep.lighthouse.common.enums.role.PrivilegeTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AuthPermissions.class)
public @interface AuthPermission {

    String relationParam() default "";

    RoleTypeEnum roleTypeEnum();
}
