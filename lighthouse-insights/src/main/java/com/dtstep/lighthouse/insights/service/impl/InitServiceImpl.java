package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.Department;
import com.dtstep.lighthouse.insights.modal.Domain;
import com.dtstep.lighthouse.insights.modal.Role;
import com.dtstep.lighthouse.insights.modal.User;
import com.dtstep.lighthouse.insights.service.*;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InitServiceImpl implements InitService {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DomainService domainService;

    @Transactional
    @Override
    public void initRole() {
        if(!roleService.isRoleExist(RoleTypeEnum.FULL_MANAGE_PERMISSION,0)){
            Role fullManageRole = new Role(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
            int result = roleService.create(fullManageRole);
            Validate.isTrue(result > 0);
        }
        if(!roleService.isRoleExist(RoleTypeEnum.FULL_ACCESS_PERMISSION,0)){
            Role fullAccessRole = new Role(RoleTypeEnum.FULL_ACCESS_PERMISSION,0);
            int result = roleService.create(fullAccessRole);
            Validate.isTrue(result > 0);
        }
        if(!roleService.isRoleExist(RoleTypeEnum.OPT_MANAGE_PERMISSION,0)){
            Role parentRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_MANAGE_PERMISSION,0);
            Role optManageRole = new Role(RoleTypeEnum.OPT_MANAGE_PERMISSION,0, parentRole.getId());
            int result = roleService.create(optManageRole);
            Validate.isTrue(result > 0);
        }
        if(!roleService.isRoleExist(RoleTypeEnum.OPT_ACCESS_PERMISSION,0)){
            Role parentRole = roleService.cacheQueryRole(RoleTypeEnum.FULL_ACCESS_PERMISSION,0);
            Role optAccessRole = new Role(RoleTypeEnum.OPT_ACCESS_PERMISSION,0, parentRole.getId());
            int result = roleService.create(optAccessRole);
            Validate.isTrue(result > 0);
        }
    }

    @Transactional
    @Override
    public void initDepartment() {
        int level = departmentService.getChildLevel(0);
        if(level == 0){
            Department department = new Department();
            department.setName("First Department");
            department.setPid(0);
            int result = departmentService.create(department);
            Validate.isTrue(result > 0);
        }
    }

    @Transactional
    @Override
    public void initAdmin() throws Exception{
        if(!userService.isUserNameExist(SystemConstant.DEFAULT_ADMIN_USER)){
            List<Department> departmentList = departmentService.queryAll();
            Validate.notNull(departmentList);
            int adminId;
            User user = new User();
            user.setUsername(SystemConstant.DEFAULT_ADMIN_USER);
            user.setDepartmentId(departmentList.get(0).getId());
            user.setPassword(Md5Util.getMD5(SystemConstant.DEFAULT_PASSWORD));
            userService.create(user,false);
            adminId = user.getId();
            Validate.isTrue(adminId != 0);
            int result = resourceService.grantPermission(adminId, OwnerTypeEnum.USER,0, RoleTypeEnum.OPT_MANAGE_PERMISSION);
            Validate.isTrue(result > 0);
        }else{
            User user = userService.queryAllInfoByUserName(SystemConstant.DEFAULT_ADMIN_USER);
            Role role = roleService.cacheQueryRole(RoleTypeEnum.OPT_MANAGE_PERMISSION,0);
            Validate.notNull(role);
            if(!permissionService.existPermission(user.getId(),OwnerTypeEnum.USER,role.getId())){
                int result = permissionService.grantPermission(user.getId(),OwnerTypeEnum.USER,role.getId());
                Validate.isTrue(result > 0);
            }
        }
    }


    @Transactional
    @Override
    public void initDefaultDomain() throws Exception {
        Domain defaultDomain = domainService.queryDefault();
        if(defaultDomain == null){
            Domain domain = new Domain();
            LocalDateTime localDateTime = LocalDateTime.now();
            domain.setCreateTime(localDateTime);
            domain.setUpdateTime(localDateTime);
            domain.setName("Default-Domain-" + System.currentTimeMillis());
            domain.setDefaultTokenPrefix(RandomID.id(3));
            int result = domainService.create(domain);
            Validate.isTrue(result > 0);
        }
    }
}
