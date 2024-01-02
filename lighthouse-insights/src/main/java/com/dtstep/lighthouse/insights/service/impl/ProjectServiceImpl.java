package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.constant.SysConst;
import com.dtstep.lighthouse.common.enums.role.PermissionsEnum;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dao.RoleDao;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ResourceService resourceService;

    @Transactional
    @Override
    public int create(ProjectCreateParam project){
        LocalDateTime localDateTime = LocalDateTime.now();
        project.setUpdateTime(localDateTime);
        project.setCreateTime(localDateTime);
        projectDao.insert(project);
        int projectId = project.getId();
        Integer departmentId = project.getDepartmentId();
        RolePair rolePair = resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Project,projectId,departmentId));
        Integer manageRoleId = rolePair.getManageRoleId();
        Integer accessRoleId = rolePair.getAccessRoleId();
        Permission permission = new Permission();
        permission.setRoleId(manageRoleId);
        permission.setOwnerType(OwnerTypeEnum.USER);
        Integer currentUserId = baseService.getCurrentUserId();
        permission.setOwnerId(currentUserId);
        permissionService.create(permission);
        List<Integer> departmentIdList = project.getDepartmentsPermission();
        List<Integer> userIdList = project.getUsersPermission();
        List<Permission> permissionList = new ArrayList<>();
        if(project.getPrivateType() == PrivateTypeEnum.Private && CollectionUtils.isNotEmpty(departmentIdList)){
            for(int i=0;i<departmentIdList.size();i++){
                Integer tempDepartmentId = departmentIdList.get(i);
                Permission tempPermission = new Permission(tempDepartmentId,OwnerTypeEnum.DEPARTMENT,accessRoleId);
                permissionList.add(tempPermission);
            }
        }
        if(project.getPrivateType() == PrivateTypeEnum.Private && CollectionUtils.isNotEmpty(userIdList)){
            for(int i=0;i<userIdList.size();i++){
                Integer userId = userIdList.get(i);
                Permission tempPermission = new Permission(userId,OwnerTypeEnum.USER,accessRoleId);
                permissionList.add(tempPermission);
            }
        }
        permissionService.batchCreate(permissionList);
        return projectId;
    }

    @Override
    public int update(Project project) {
        return projectDao.update(project);
    }

    @Override
    public ProjectDto queryById(Integer id) {
        Project project = projectDao.queryById(id);
        return translate(project);
    }

    private ProjectDto translate(Project project){
        ProjectDto projectDto = new ProjectDto(project);
        List<Group> dtoList = groupDao.queryByProjectId(project.getId());
        CommonTreeNode treeNode = new CommonTreeNode(String.valueOf(project.getId()),project.getTitle(), "0","1");
        if(CollectionUtils.isNotEmpty(dtoList)){
            List<CommonTreeNode> children = new ArrayList<>();
            treeNode.setChildren(children);
            for(Group group : dtoList){
                CommonTreeNode groupNode = new CommonTreeNode(String.valueOf(group.getId()),group.getToken(),String.valueOf(project.getId()),"2");
                children.add(groupNode);
            }
        }
        projectDto.setStructure(List.of(treeNode));
        return projectDto;
    }

    @Override
    public ListData<ProjectDto> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize) {
        Integer userId = baseService.getCurrentUserId();
        List<Project> projectList = projectDao.queryList(queryParam,pageNum,pageSize);
        List<ProjectDto> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(projectList)){
            for(Project project : projectList){
                dtoList.add(translate(project));
            }
        }
        ListData<ProjectDto> listData = new ListData<>();
        listData.setList(dtoList);
        return listData;
    }
}
