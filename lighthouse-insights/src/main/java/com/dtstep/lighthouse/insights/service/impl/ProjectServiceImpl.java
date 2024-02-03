package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.*;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dao.*;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
import com.dtstep.lighthouse.insights.vo.ProjectVO;
import com.dtstep.lighthouse.insights.vo.ServiceResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

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

    @Autowired
    private RelationService relationService;

    @Autowired
    private RelationDao relationDao;

    @Transactional
    @Override
    public ServiceResult<Integer> create(Project project){
        LocalDateTime localDateTime = LocalDateTime.now();
        project.setUpdateTime(localDateTime);
        project.setCreateTime(localDateTime);
        projectDao.insert(project);
        int id = project.getId();
        Integer departmentId = project.getDepartmentId();
        RolePair rolePair = resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Project,id,ResourceTypeEnum.Department,departmentId));
        Integer manageRoleId = rolePair.getManageRoleId();
        int currentUserId = baseService.getCurrentUserId();
        Permission adminPermission = new Permission(currentUserId,OwnerTypeEnum.USER,manageRoleId);
        permissionService.create(adminPermission);
        return ServiceResult.success(project.getId());
    }

    @Override
    public ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception{
        Integer resourceId = grantParam.getResourceId();
        Project project = projectDao.queryById(resourceId);
        RoleTypeEnum roleTypeEnum = grantParam.getRoleType();
        Validate.notNull(project);
        Integer roleId;
        HashSet<Integer> adminsSet = new HashSet<>();
        if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,resourceId);
            roleId = role.getId();
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(roleId,5);
            adminsSet.addAll(adminIds);
        }else if(roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION,resourceId);
            roleId = role.getId();
        }else {
            throw new Exception();
        }
        if(roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION && project.getPrivateType() == PrivateTypeEnum.Public){
            return ResultCode.grantPermissionPublicLimit;
        }
        List<Integer> departmentIdList = grantParam.getDepartmentsPermissions();
        List<Integer> userIdList = grantParam.getUsersPermissions();
        if(CollectionUtils.isNotEmpty(departmentIdList)){
            for(int i=0;i<departmentIdList.size();i++){
                Integer tempDepartmentId = departmentIdList.get(i);
                Validate.isTrue(roleTypeEnum == RoleTypeEnum.PROJECT_ACCESS_PERMISSION);
                permissionService.grantPermission(tempDepartmentId,OwnerTypeEnum.DEPARTMENT,roleId);
            }
        }
        if(CollectionUtils.isNotEmpty(userIdList)){
            if(roleTypeEnum == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
                adminsSet.addAll(userIdList);
            }
            if(adminsSet.size() > 3){
                return ResultCode.grantPermissionAdminExceedLimit;
            }
            for(int i=0;i<userIdList.size();i++){
                Integer userId = userIdList.get(i);
                permissionService.grantPermission(userId,OwnerTypeEnum.USER,roleId);
            }
        }
        return ResultCode.success;
    }

    @Override
    public ResultCode releasePermission(PermissionReleaseParam releaseParam) throws Exception {
        int currentUserId = baseService.getCurrentUserId();
        Integer resourceId = releaseParam.getResourceId();
        Integer permissionId = releaseParam.getPermissionId();
        Permission permission = permissionService.queryById(permissionId);
        Integer ownerId = permission.getOwnerId();
        Integer roleId = permission.getRoleId();
        if(releaseParam.getRoleType() == RoleTypeEnum.PROJECT_MANAGE_PERMISSION){
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(roleId,3);
            if(adminIds.size() <= 1){
                return ResultCode.releasePermissionAdminAtLeastOne;
            }
        }
        if(ownerId.intValue() == currentUserId){
            return ResultCode.releasePermissionCurrentNotAllowed;
        }
        Role role = roleService.queryById(roleId);
        Validate.isTrue(role.getResourceId().intValue() == resourceId.intValue());
        permissionService.releasePermission(permissionId);
        return ResultCode.success;
    }

    @Transactional
    @Override
    public int update(Project project) {
        resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Project,project.getId(),ResourceTypeEnum.Department,project.getDepartmentId()));
        return projectDao.update(project);
    }

    @Override
    public ProjectVO queryById(Integer id) {
        Project project = projectDao.queryById(id);
        if(project == null){
            return null;
        }
        return translate(project);
    }

    @Override
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public ProjectVO cacheQueryById(Integer id) {
        return queryById(id);
    }

    private ProjectVO translate(Project project){
        int currentUserId = baseService.getCurrentUserId();
        ProjectVO projectVO = new ProjectVO(project);
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION, project.getId());
        Role accessRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_ACCESS_PERMISSION, project.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            projectVO.setAdmins(admins);
        }
        if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
            projectVO.addPermission(PermissionEnum.ManageAble);
            projectVO.addPermission(PermissionEnum.AccessAble);
        }else if(project.getPrivateType() == PrivateTypeEnum.Public
                || permissionService.checkUserPermission(currentUserId, accessRole.getId())){
            projectVO.addPermission(PermissionEnum.AccessAble);
        }
        return projectVO;
    }

    @Override
    public List<ProjectVO> queryByIds(List<Integer> ids) {
        ProjectQueryParam queryParam = new ProjectQueryParam();
        queryParam.setIds(ids);
        List<Project> projectList = projectDao.queryList(queryParam);
        List<ProjectVO> voList = new ArrayList<>();
        for(Project project : projectList){
            ProjectVO projectVO = translate(project);
            voList.add(projectVO);
        }
        return voList;
    }

    @Override
    public ListData<ProjectVO> queryList(ProjectQueryParam queryParam, Integer pageNum, Integer pageSize) {
        Integer userId = baseService.getCurrentUserId();
        PageHelper.startPage(pageNum,pageSize);
        List<ProjectVO> dtoList = new ArrayList<>();
        PageInfo<Project> pageInfo = null;
        try{
            List<Project> projectList = projectDao.queryList(queryParam);
            pageInfo = new PageInfo<>(projectList);
        }finally {
            PageHelper.clearPage();
        }
        for(Project project : pageInfo.getList()){
            ProjectVO projectVO = null;
            try{
                projectVO = translate(project);
                dtoList.add(projectVO);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}!",project.getId(),ex);
            }
        }
        return ListData.newInstance(dtoList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public int count(ProjectQueryParam queryParam) {
        return projectDao.count(queryParam);
    }

    @Override
    public int deleteById(Integer id) {
        Project project = projectDao.queryById(id);
        int result = projectDao.deleteById(id);
        resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Project,project.getId(),ResourceTypeEnum.Department,project.getDepartmentId()));
        return result;
    }

    @Override
    public TreeNode getStructure(Project project) throws Exception{
        Integer id = project.getId();
        List<TreeNode> nodeList = new ArrayList<>();
        TreeNode rootNode = new TreeNode(project.getTitle(),project.getId(),"project");
        HashMap<String,TreeNode> nodeMap = new HashMap<>();
        nodeMap.put("project_"+project.getId(),rootNode);
        List<Group> groupList = groupDao.queryByProjectId(id);
        for(Group group : groupList){
            TreeNode groupNode = new TreeNode(group.getToken(),group.getId(),"group");
            nodeMap.put("group_"+group.getId(),groupNode);
            rootNode.addChild(groupNode);
        }
        List<Stat> statList = statDao.queryByProjectId(id);
        for(Stat stat : statList){
            TreeNode statNode = new TreeNode(stat.getTitle(),stat.getId(),"stat");
            TreeNode parentNode = nodeMap.get("group_"+stat.getGroupId());
            parentNode.addChild(statNode);
        }
        return rootNode;
    }

    @Override
    public ResultCode star(Project project) {
        int currentUserId = baseService.getCurrentUserId();
        RelationQueryParam countParam = new RelationQueryParam();
        countParam.setSubjectId(currentUserId);
        countParam.setRelationType(RelationTypeEnum.UserStarProjectRelation);
        int count = relationService.count(countParam);
        if(count > SystemConstant.USER_STAR_PROJECT_LIMIT){
            return ResultCode.userStarProjectLimitExceed;
        }
        Relation relation = new Relation();
        relation.setSubjectId(currentUserId);
        relation.setRelationType(RelationTypeEnum.UserStarProjectRelation);
        relation.setResourceId(project.getId());
        relation.setResourceType(ResourceTypeEnum.Project);
        return relationService.create(relation);
    }

    @Override
    public ResultCode unStar(Project project) {
        int currentUserId = baseService.getCurrentUserId();
        RelationDeleteParam relationDeleteParam = new RelationDeleteParam();
        relationDeleteParam.setSubjectId(currentUserId);
        relationDeleteParam.setRelationType(RelationTypeEnum.UserStarProjectRelation);
        relationDeleteParam.setResourceId(project.getId());
        relationDeleteParam.setResourceType(ResourceTypeEnum.Project);
        relationService.delete(relationDeleteParam);
        return ResultCode.success;
    }

    @Override
    public List<ProjectVO> queryStarList() {
        int currentUserId = baseService.getCurrentUserId();
        List<Relation> relationList = relationDao.queryList(currentUserId,RelationTypeEnum.UserStarProjectRelation);
        List<Integer> ids = relationList.stream().map(z -> z.getResourceId()).collect(Collectors.toList());
        List<ProjectVO> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ids)){
            ProjectQueryParam queryParam = new ProjectQueryParam();
            queryParam.setIds(ids);
            List<Project> projectList = projectDao.queryList(queryParam);
            for(Project project : projectList){
                try{
                    ProjectVO projectVO = translate(project);
                    voList.add(projectVO);
                }catch (Exception ex){
                    logger.error("translate item info error,id:{}",project.getId(),ex);
                }
            }
        }
        Collections.sort(voList, Comparator.comparingInt(e -> ids.indexOf(e.getId())));
        return voList;
    }


}
