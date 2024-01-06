package com.dtstep.lighthouse.insights.config;

import com.dtstep.lighthouse.common.exception.PermissionException;
import com.dtstep.lighthouse.insights.controller.annotation.AuthPermission;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.service.PermissionService;
import com.dtstep.lighthouse.insights.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Request URI:" + request.getRequestURI());
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(AuthPermission.class)) {
                AuthPermission[] authPermissions = method.getDeclaredAnnotationsByType(AuthPermission.class);
                for(AuthPermission authPermission : authPermissions){
                    if(!hasPermission(authPermission)){
                        throw new PermissionException();
                    }
                }
            }
        }
        return true;
    }

    private boolean hasPermission(AuthPermission authPermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RoleTypeEnum roleTypeEnum = authPermission.roleTypeEnum();
        String relatedParam = authPermission.relationParam();
        if(authentication.getClass() == SeedAuthenticationToken.class){
            Integer userId = ((SeedAuthenticationToken) authentication).getUserId();
            Role role = null;
            System.out.println("roleService:" + roleService);
            if(roleTypeEnum == RoleTypeEnum.OPT_MANAGE_PERMISSION){
                role = roleService.cacheQueryRole(roleTypeEnum,0);
            }else{
                return false;
            }
            return permissionService.checkUserPermission(userId,role.getId());
        }
        return false;
    }
}