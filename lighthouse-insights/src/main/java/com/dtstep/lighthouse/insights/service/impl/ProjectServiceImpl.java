package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.insights.dao.DepartmentDao;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.OwnerTypeEnum;
import com.dtstep.lighthouse.insights.enums.PrivateTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private StatDao statDao;

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
        resourceService.updateResourcePidCallback(Resource.newResource(ResourceTypeEnum.Project,project.getId(),project.getDepartmentId()));
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
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION, project.getId());
        Role accessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION, project.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            projectDto.setAdmins(admins);
        }
        if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
            projectDto.addPermission(PermissionInfo.PermissionEnum.ManageAble);
        }else if(project.getPrivateType() == PrivateTypeEnum.Public
                || permissionService.checkUserPermission(currentUserId, accessRole.getId())){
            projectDto.addPermission(PermissionInfo.PermissionEnum.AccessAble);
        }
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

    @Override
    public int delete(Project project) {
        int result = projectDao.deleteById(project.getId());
        resourceService.deleteResourceCallback(Resource.newResource(ResourceTypeEnum.Project,project.getId(),project.getDepartmentId()));
        return result;
    }

    @Override
    public TreeNode getStructure(Project project) throws Exception{
        Integer id = project.getId();
        List<TreeNode> nodeList = new ArrayList<>();
        TreeNode rootNode = new TreeNode(project.getTitle(),project.getId(),"project");
        HashMap<String,TreeNode> nodeMap = new HashMap<>();
        nodeMap.put(String.valueOf(project.getId()),rootNode);
        List<Group> groupList = groupDao.queryByProjectId(id);
        for(Group group : groupList){
            TreeNode groupNode = new TreeNode(group.getToken(),group.getId(),"group");
            nodeMap.put(String.valueOf(group.getId()),groupNode);
            rootNode.addChild(groupNode);
        }
        List<Stat> statList = statDao.queryByProjectId(id);
        for(Stat stat : statList){
            TreeNode statNode = new TreeNode(stat.getTitle(),stat.getId(),"stat");
            TreeNode parentNode = nodeMap.get(String.valueOf(stat.getGroupId()));
            parentNode.addChild(statNode);
        }
        return rootNode;
    }


    @Override
    public List<User> cacheQueryAdmins(Integer id) {
        Role manageRole = roleService.queryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,id);
        Validate.notNull(manageRole);
        List<Integer> userIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(), 3);
        if(CollectionUtils.isEmpty(userIds)){
            return null;
        }
        List<User> users = new ArrayList<>();
        for(Integer userId:userIds){
            User user = userService.queryBasicInfoById(userId);
            users.add(user);
        }
        return users;
    }
}
