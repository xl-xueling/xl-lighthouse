package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

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
        int currentUserId = baseService.getCurrentUserId();
        Permission adminPermission = new Permission(currentUserId,OwnerTypeEnum.USER,manageRoleId);
        permissionList.add(adminPermission);
        permissionService.batchCreate(permissionList);
        return projectId;
    }

    @Transactional
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
        int currentUserId = baseService.getCurrentUserId();
        ProjectDto projectDto = new ProjectDto(project);
        Role manageRole = roleService.queryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION, project.getId());
        Role accessRole = roleService.queryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION, project.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            projectDto.setAdmins(admins);
        }
        if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
            projectDto.addPermission(PermissionInfo.PermissionEnum.ManageAble);
        }else if(permissionService.checkUserPermission(currentUserId, accessRole.getId())){
            projectDto.addPermission(PermissionInfo.PermissionEnum.AccessAble);
        }
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
        PageHelper.startPage(pageNum,pageSize);
        List<ProjectDto> dtoList = new ArrayList<>();
        List<Project> projectList;
        try{
            projectList = projectDao.queryList(queryParam,pageNum,pageSize);
        }finally {
            PageHelper.clearPage();
        }
        if(CollectionUtils.isNotEmpty(projectList)){
            for(Project project : projectList){
                ProjectDto projectDto = null;
                try{
                    projectDto = translate(project);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                dtoList.add(projectDto);
            }
        }
        return baseService.translateToListData(dtoList);
    }

    @Override
    public int countByDepartmentId(Integer departmentId) {
        return projectDao.countByDepartmentId(departmentId);
    }
}
