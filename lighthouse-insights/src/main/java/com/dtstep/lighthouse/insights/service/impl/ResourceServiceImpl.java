package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.exception.RoleDefendException;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto.PermissionQueryParam;
import com.dtstep.lighthouse.insights.dto.RolePair;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private StatDao statDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MetricService metricService;


    @Transactional
    @Override
    public RolePair addResourceCallback(Resource resource) {
        List<Role> roleList = new ArrayList<>();
        Role manageRole = null;
        Role accessRole = null;
        String name = null;
        if(resource.getResourceType() == ResourceTypeEnum.Department){
            RoleTypeEnum parentManageRoleType = resource.getResourcePid().intValue() == 0 ? RoleTypeEnum.FULL_MANAGE_PERMISSION : RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION;
            Role parentManageRole = roleService.cacheQueryRole(parentManageRoleType,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            RoleTypeEnum parentAccessRoleType = resource.getResourcePid().intValue() == 0 ? RoleTypeEnum.FULL_ACCESS_PERMISSION : RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION;
            Role parentAccessRole = roleService.cacheQueryRole(parentAccessRoleType,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = departmentDao.queryById(resource.getResourceId()).getName();
        }else if(resource.getResourceType() == ResourceTypeEnum.Project){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = projectDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.Group){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = groupDao.queryById(resource.getResourceId()).getToken();
        }else if(resource.getResourceType() == ResourceTypeEnum.Stat){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.STAT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.STAT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = statDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.Metric){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
        }
        Validate.notNull(manageRole);
        Validate.notNull(accessRole);
        manageRole.setDesc(manageRole.getRoleType().name() + "(" + name + ")");
        accessRole.setDesc(accessRole.getRoleType().name() + "(" + name + ")");
        roleService.create(manageRole);
        roleService.create(accessRole);
        return new RolePair(manageRole.getId(),accessRole.getId());
    }

    @Transactional
    @Override
    public void updateResourcePidCallback(Resource resource) {
        List<Role> roleList = new ArrayList<>();
        Role manageRole = null;
        Role accessRole = null;
        String name = null;
        if(resource.getResourceType() == ResourceTypeEnum.Department){
            RoleTypeEnum parentManageRoleType = resource.getResourcePid().intValue() == 0 ? RoleTypeEnum.FULL_MANAGE_PERMISSION : RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION;
            Role parentManageRole = roleService.cacheQueryRole(parentManageRoleType,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            RoleTypeEnum parentAccessRoleType = resource.getResourcePid().intValue() == 0 ? RoleTypeEnum.FULL_ACCESS_PERMISSION : RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION;
            Role parentAccessRole = roleService.cacheQueryRole(parentAccessRoleType,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = departmentDao.queryById(resource.getResourceId()).getName();
        }else if(resource.getResourceType() == ResourceTypeEnum.Project){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = projectDao.queryById(resource.getResourceId()).getTitle();
        }else if(resource.getResourceType() == ResourceTypeEnum.Group){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
            name = groupDao.queryById(resource.getResourceId()).getToken();
        }else if(resource.getResourceType() == ResourceTypeEnum.Stat){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.STAT_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.STAT_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
        }else if(resource.getResourceType() == ResourceTypeEnum.Metric){
            Role parentManageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourcePid());
            Integer manageRolePid = parentManageRole.getId();
            Role parentAccessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourcePid());
            Integer accessRolePid = parentAccessRole.getId();
            manageRole = new Role(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resource.getResourceId(),manageRolePid);
            accessRole = new Role(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resource.getResourceId(),accessRolePid);
        }
        Validate.notNull(manageRole);
        Validate.notNull(accessRole);
        manageRole.setDesc(manageRole.getRoleType().name() + "(" + name + ")");
        accessRole.setDesc(accessRole.getRoleType().name() + "(" + name + ")");
        roleService.update(manageRole);
        roleService.update(accessRole);
    }

    @Transactional
    @Override
    public void deleteResourceCallback(Resource resource){
        List<Role> roleList = new ArrayList<>();
        Role manageRole = null;
        Role accessRole = null;
        if(resource.getResourceType() == ResourceTypeEnum.Department){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Project){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Group){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.GROUP_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Stat){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,resource.getResourceId());
        }else if(resource.getResourceType() == ResourceTypeEnum.Metric){
            manageRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resource.getResourceId());
            accessRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resource.getResourceId());
        }
        Validate.notNull(manageRole);
        Validate.notNull(accessRole);
        List<Role> childManageRoles = roleService.queryListByPid(manageRole.getId(),1,20);
        if(CollectionUtils.isNotEmpty(childManageRoles)){
            String childRoles = childManageRoles.stream().map(z -> String.valueOf(z.getId())).collect(Collectors.joining(","));
            throw new RoleDefendException("can't delete manage role [resourceId:" + resource.getResourceId() + ",roleType:"+resource.getResourceType().name()+"],child roles ["+childRoles+"] exists!" );
        }
        List<Role> childAccessRoles = roleService.queryListByPid(manageRole.getId(),1,20);
        if(CollectionUtils.isNotEmpty(childAccessRoles)){
            String childRoles = childAccessRoles.stream().map(z -> String.valueOf(z.getId())).collect(Collectors.joining(","));
            throw new RoleDefendException("can't delete access role [resourceId:" + resource.getResourceId() + ",roleType:"+resource.getResourceType().name()+"],child roles ["+childRoles+"] exists!" );
        }
        PermissionQueryParam manageQueryParam = new PermissionQueryParam();
        manageQueryParam.setRoleId(manageRole.getId());
        permissionService.delete(manageQueryParam);
        PermissionQueryParam accessQueryParam = new PermissionQueryParam();
        accessQueryParam.setRoleId(accessRole.getId());
        permissionService.delete(accessQueryParam);
        roleService.deleteById(manageRole.getId());
        roleService.deleteById(accessRole.getId());
    }

    @Override
    public int grantPermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId, RoleTypeEnum roleTypeEnum) {
        Role role = roleService.cacheQueryRole(roleTypeEnum,resourceId);
        Validate.notNull(role);
        return permissionService.grantPermission(ownerId,ownerTypeEnum,role.getId());
    }

    @Override
    public int releasePermission(Integer ownerId, OwnerTypeEnum ownerTypeEnum, Integer resourceId, RoleTypeEnum roleTypeEnum) {
        Role role = roleService.cacheQueryRole(roleTypeEnum,resourceId);
        return permissionService.releasePermission(ownerId,ownerTypeEnum,role.getId());
    }

    @Override
    public Resource queryByRoleId(Integer roleId) {
        Role role = roleService.queryById(roleId);
        if(role == null){
            return null;
        }
        Resource resource = null;
        RoleTypeEnum roleTypeEnum = role.getRoleType();
        Integer resourceId = role.getResourceId();
        if(roleTypeEnum == RoleTypeEnum.DEPARTMENT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.DEPARTMENT_ACCESS_PERMISSION){
            Department department = departmentDao.queryById(resourceId);
            resource = new Resource(ResourceTypeEnum.Department,resourceId,department.getPid(),department);
        }else if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION){
            Project project = projectDao.queryById(resourceId);
            resource = new Resource(ResourceTypeEnum.Project,resourceId,project.getDepartmentId(),project);
        }else if(roleTypeEnum == RoleTypeEnum.GROUP_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.GROUP_ACCESS_PERMISSION){
            Group group = groupDao.queryById(resourceId);
            resource = new Resource(ResourceTypeEnum.Group,resourceId,group.getProjectId(),group);
        }else if(roleTypeEnum == RoleTypeEnum.STAT_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.STAT_ACCESS_PERMISSION){
            Stat stat = statDao.queryById(resourceId);
            resource = new Resource(ResourceTypeEnum.Group,resourceId,stat.getGroupId(),stat);
        }else if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION){

        }else if(roleTypeEnum == RoleTypeEnum.FULL_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.FULL_ACCESS_PERMISSION){
            resource = new Resource(ResourceTypeEnum.System,0);
        }else if (roleTypeEnum == RoleTypeEnum.FULL_MANAGE_PERMISSION || roleTypeEnum == RoleTypeEnum.FULL_ACCESS_PERMISSION){
            resource = new Resource(ResourceTypeEnum.System,0);
        }
        return resource;
    }
}
