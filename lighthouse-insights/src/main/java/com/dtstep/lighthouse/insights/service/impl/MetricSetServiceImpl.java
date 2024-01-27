package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.MetricSetDao;
import com.dtstep.lighthouse.insights.dao.RelationDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto.MetricBindParam;
import com.dtstep.lighthouse.insights.dto.MetricBindRemoveParam;
import com.dtstep.lighthouse.insights.dto.MetricSetQueryParam;
import com.dtstep.lighthouse.insights.dto.PermissionGrantParam;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.*;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.util.TreeUtil;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
import com.dtstep.lighthouse.insights.vo.RelationVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetricSetServiceImpl implements MetricSetService {

    private static final Logger logger = LoggerFactory.getLogger(MetricSetServiceImpl.class);

    @Autowired
    private MetricSetDao metricSetDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StatService statService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private RelationDao relationDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private StatDao statDao;

    @Transactional
    @Override
    public int create(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setCreateTime(localDateTime);
        metricSet.setUpdateTime(localDateTime);
        metricSetDao.insert(metricSet);
        int id = metricSet.getId();
        Domain domain = domainService.queryDefault();
        RolePair rolePair = resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.MetricSet,id,ResourceTypeEnum.Domain,domain.getId()));
        Integer manageRoleId = rolePair.getManageRoleId();
        int currentUserId = baseService.getCurrentUserId();
        Permission adminPermission = new Permission(currentUserId,OwnerTypeEnum.USER,manageRoleId);
        permissionService.create(adminPermission);
        return id;
    }


    @Transactional
    @Override
    public ResultCode batchGrantPermissions(PermissionGrantParam grantParam) throws Exception{
        Integer resourceId = grantParam.getResourceId();
        MetricSet metricSet = metricSetDao.queryById(resourceId);
        RoleTypeEnum roleTypeEnum = grantParam.getRoleType();
        Validate.notNull(metricSet);
        Integer roleId;
        HashSet<Integer> adminsSet = new HashSet<>();
        if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,resourceId);
            roleId = role.getId();
            List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(roleId,5);
            adminsSet.addAll(adminIds);
        }else if(roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION){
            Role role = roleService.queryRole(RoleTypeEnum.METRIC_ACCESS_PERMISSION,resourceId);
            roleId = role.getId();
        }else {
            throw new Exception();
        }
        if(roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION && metricSet.getPrivateType() == PrivateTypeEnum.Public){
            return ResultCode.grantPermissionPublicLimit;
        }
        List<Integer> departmentIdList = grantParam.getDepartmentsPermissions();
        List<Integer> userIdList = grantParam.getUsersPermissions();
        if(CollectionUtils.isNotEmpty(departmentIdList)){
            for(int i=0;i<departmentIdList.size();i++){
                Integer tempDepartmentId = departmentIdList.get(i);
                Validate.isTrue(roleTypeEnum == RoleTypeEnum.METRIC_ACCESS_PERMISSION);
                permissionService.grantPermission(tempDepartmentId,OwnerTypeEnum.DEPARTMENT,roleId);
            }
        }
        if(CollectionUtils.isNotEmpty(userIdList)){
            if(roleTypeEnum == RoleTypeEnum.METRIC_MANAGE_PERMISSION){
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
    public int update(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setUpdateTime(localDateTime);
        return metricSetDao.update(metricSet);
    }

    private MetricSetVO translate(MetricSet metricSet){
        if(metricSet == null){
            return null;
        }
        MetricSetVO metricSetVO = new MetricSetVO(metricSet);
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,metricSet.getId());
        Role accessRole = roleService.queryRole(RoleTypeEnum.METRIC_ACCESS_PERMISSION,metricSet.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            metricSetVO.setAdmins(admins);
        }
        int currentUserId = baseService.getCurrentUserId();
        if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
            metricSetVO.addPermission(PermissionEnum.ManageAble);
            metricSetVO.addPermission(PermissionEnum.AccessAble);
        }else if(permissionService.checkUserPermission(currentUserId,accessRole.getId())){
            metricSetVO.addPermission(PermissionEnum.AccessAble);
        }
        List<Relation> relationList = relationDao.queryList(metricSetVO.getId(),RelationTypeEnum.MetricSetBindRelation);
        List<MetricBindElement> elements = new ArrayList<>();
        for(Relation relation : relationList){
            MetricBindElement bindElement = new MetricBindElement();
            bindElement.setResourceId(relation.getResourceId());
            bindElement.setResourceType(relation.getResourceType());
            elements.add(bindElement);
        }
        metricSetVO.setBindElements(elements);
        return metricSetVO;
    }

    @Override
    public MetricSetVO queryById(Integer id) {
        MetricSet metricSet = metricSetDao.queryById(id);
        return translate(metricSet);
    }

    @Override
    public int binded(MetricBindParam bindParam) {
        List<Integer> metricIds = bindParam.getMetricIds();
        List<MetricBindElement> bindElements = bindParam.getBindElements();
        List<Relation> relationList = new ArrayList<>();
        for(Integer metricId : metricIds){
            List<Integer> projectIds = bindElements.stream().filter(x -> x.getResourceType() == ResourceTypeEnum.Project).map(z -> z.getResourceId()).collect(Collectors.toList());
            for(Integer projectId : projectIds){
                Project project = projectService.queryById(projectId);
                if(project == null){
                    continue;
                }
                Relation relation = new Relation();
                String hash = Md5Util.getMD5(metricId + "_" + RelationTypeEnum.MetricSetBindRelation.getRelationType() + "_" + projectId + "_" + ResourceTypeEnum.Project.getResourceType());
                boolean isExist = relationService.isExist(hash);
                if(!isExist){
                    relation.setSubjectId(metricId);
                    relation.setRelationType(RelationTypeEnum.MetricSetBindRelation);
                    relation.setResourceId(projectId);
                    relation.setResourceType(ResourceTypeEnum.Project);
                    relation.setHash(hash);
                    relation.setCreateTime(LocalDateTime.now());
                    relationList.add(relation);
                }
            }
            List<Integer> statIds = bindElements.stream().filter(x -> x.getResourceType() == ResourceTypeEnum.Stat).map(z -> z.getResourceId()).collect(Collectors.toList());
            for(Integer statId : statIds){
                Stat stat = statService.queryById(statId);
                if(stat == null){
                    continue;
                }
                String hash = Md5Util.getMD5(metricId + "_" + RelationTypeEnum.MetricSetBindRelation.getRelationType() + "_" + statId + "_" + ResourceTypeEnum.Stat.getResourceType());
                boolean isExist = relationService.isExist(hash);
                if(!isExist){
                    Relation relation = new Relation();
                    relation.setSubjectId(metricId);
                    relation.setRelationType(RelationTypeEnum.MetricSetBindRelation);
                    relation.setResourceId(statId);
                    relation.setResourceType(ResourceTypeEnum.Stat);
                    relation.setHash(hash);
                    relation.setCreateTime(LocalDateTime.now());
                    relationList.add(relation);
                }
            }
        }
        int result = 0;
        if(CollectionUtils.isNotEmpty(relationList)){
            result = relationService.batchCreate(relationList);
        }
        return result;
    }

    @Override
    public ListData<MetricSetVO> queryList(MetricSetQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<MetricSet> pageInfo = null;
        try{
            List<MetricSet> metricSetList = metricSetDao.queryList(queryParam);
            pageInfo = new PageInfo<>(metricSetList);
        }finally {
            PageHelper.clearPage();
        }
        List<MetricSetVO> voList = new ArrayList<>();
        for(MetricSet metricSet : pageInfo.getList()){
            try{
                MetricSetVO metricSetVO = translate(metricSet);
                voList.add(metricSetVO);
            }catch (Exception ex){
                logger.error("translate item info error,id:{}",metricSet.getId(),ex);
            }
        }
        return ListData.newInstance(voList,pageInfo.getTotal(),pageNum,pageSize);
    }

    @Override
    public List<TreeNode> getStructure(MetricSet metricSet) throws Exception {
        Validate.notNull(metricSet);
        List<TreeNode> structure = metricSet.getStructure();
        if(CollectionUtils.isEmpty(structure)){
            structure = new ArrayList<>();
            structure.add(getDefaultStructure(metricSet));
        }else {
            List<String> keys = TreeUtil.getAllKeys(structure);
            TreeNode newElementNode = TreeUtil.findNodeByValue(structure,"-1");
            if(newElementNode == null){
                newElementNode = new TreeNode("",-1);
                structure.add(newElementNode);
            }
            TreeNode wasteNode = TreeUtil.findNodeByValue(structure,"-2");
            if(wasteNode == null){
                wasteNode = new TreeNode("",-2);
                structure.add(wasteNode);
            }
            List<RelationVO> relationList = relationService.queryList(metricSet.getId(),RelationTypeEnum.MetricSetBindRelation);
            for(RelationVO relation : relationList){
                if(relation.getResourceType() == ResourceTypeEnum.Project){
                    Project project = (Project) relation.getExtend();
                    if(project != null){
                        List<Stat> statList = statService.queryByProjectId(project.getId());
                        for(Stat stat:statList){
                            String tempKey = "stat" + "_" + stat.getId();
                            if(!keys.contains(tempKey)){
                                newElementNode.addChild(new TreeNode(stat.getTitle(),stat.getId(),"stat"));
                            }
                        }
                    }
                }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
                    Stat stat = (Stat) relation.getExtend();
                    if(stat != null){
                        String tempKey = "stat" + "_" + stat.getId();
                        if(!keys.contains(tempKey)){
                            newElementNode.addChild(new TreeNode(stat.getTitle(),stat.getId(),"stat"));
                        }
                    }
                }
            }
        }
        return structure;
    }

    private TreeNode getDefaultStructure(MetricSet metricSet){
        List<String> keyList = new ArrayList<>();
        TreeNode rootNode = new TreeNode(metricSet.getTitle(),metricSet.getId(),"metric");
        List<RelationVO> relationList = relationService.queryList(metricSet.getId(),RelationTypeEnum.MetricSetBindRelation);
        for(RelationVO relation : relationList){
            if(relation.getResourceType() == ResourceTypeEnum.Project){
                Project project = (Project) relation.getExtend();
                if(project != null){
                    TreeNode projectNode = new TreeNode(project.getTitle(),project.getId(),"project");
                    HashMap<String,TreeNode> nodeMap = new HashMap<>();
                    String key = "project_"+project.getId();
                    keyList.add(key);
                    nodeMap.put(key,projectNode);
                    List<Group> groupList = groupDao.queryByProjectId(project.getId());
                    for(Group group : groupList){
                        TreeNode groupNode = new TreeNode(group.getToken(),group.getId(),"group");
                        String groupKey = "group_"+group.getId();
                        keyList.add(groupKey);
                        nodeMap.put(groupKey,groupNode);
                        projectNode.addChild(groupNode);
                    }
                    List<Stat> statList = statDao.queryByProjectId(project.getId());
                    for(Stat stat : statList){
                        String statKey = "stat_"+stat.getId();
                        if(!keyList.contains(statKey)){
                            TreeNode statNode = new TreeNode(stat.getTitle(),stat.getId(),"stat");
                            TreeNode parentNode = nodeMap.get("group_"+stat.getGroupId());
                            keyList.add(statKey);
                            parentNode.addChild(statNode);
                        }
                    }
                    rootNode.addChild(projectNode);
                }
            }else if(relation.getResourceType() == ResourceTypeEnum.Stat){
                Stat stat = (Stat)relation.getExtend();
                if(stat != null){
                    String statKey = "stat_"+stat.getId();
                    if(!keyList.contains(statKey)){
                        TreeNode statNode = new TreeNode(stat.getTitle(), stat.getId(),"stat");
                        rootNode.addChild(statNode);
                        keyList.add(statKey);
                    }
                }
            }
        }
        return rootNode;
    }

    @Override
    public int bindRemove(MetricBindRemoveParam removeParam) {
        Integer relationId = removeParam.getRelationId();
        Relation relation = relationDao.queryById(relationId);
        Validate.isTrue(relation.getSubjectId().intValue() == removeParam.getId().intValue());
        return relationDao.deleteById(relationId);
    }

    @Override
    public int delete(MetricSet metricSet) {
        return metricSetDao.deleteById(metricSet.getId());
    }
}
