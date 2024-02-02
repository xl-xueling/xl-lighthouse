package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.common.util.Md5Util;
import com.dtstep.lighthouse.commonv2.constant.SystemConstant;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dao.*;
import com.dtstep.lighthouse.insights.dto.*;
import com.dtstep.lighthouse.insights.dto_bak.*;
import com.dtstep.lighthouse.insights.enums.*;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.vo.MetricSetVO;
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
import java.util.*;
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

    @Autowired
    private ProjectDao projectDao;

    @Transactional
    @Override
    public int create(MetricSet metricSet) {
        LocalDateTime localDateTime = LocalDateTime.now();
        metricSet.setCreateTime(localDateTime);
        metricSet.setUpdateTime(localDateTime);
        metricSetDao.insert(metricSet);
        int id = metricSet.getId();
        Domain domain = domainService.queryDefault();
        RolePair rolePair = resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.MetricSet,id,ResourceTypeEnum.Domain,domain.getId()));
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
        int currentUserId = baseService.getCurrentUserId();
        if(permissionService.checkUserPermission(currentUserId, manageRole.getId())){
            metricSetVO.addPermission(PermissionEnum.ManageAble);
            metricSetVO.addPermission(PermissionEnum.AccessAble);
        }else if(permissionService.checkUserPermission(currentUserId,accessRole.getId())){
            metricSetVO.addPermission(PermissionEnum.AccessAble);
        }
        return metricSetVO;
    }

    @Override
    public MetricSetVO queryById(Integer id) {
        MetricSet metricSet = metricSetDao.queryById(id);
        MetricSetVO metricSetVO = translate(metricSet);
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.METRIC_MANAGE_PERMISSION,metricSet.getId());
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(manageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            metricSetVO.setAdmins(admins);
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
    public TreeNode getStructure(MetricSet metricSet) throws Exception {
        Validate.notNull(metricSet);
        TreeNode structure = metricSet.getStructure();
        if(structure == null || CollectionUtils.isEmpty(structure.getChildren())){
            structure = combineDefaultStructure(metricSet);
        }
        return structure;
    }

    private TreeNode combineDefaultStructure(MetricSet metricSet){
        List<String> keyList = new ArrayList<>();
        String rootKey = RandomID.id(10,keyList);
        TreeNode rootNode = new TreeNode(rootKey,metricSet.getTitle(),metricSet.getId(),"metric");
        RelationQueryParam queryParam = new RelationQueryParam();
        queryParam.setSubjectId(metricSet.getId());
        queryParam.setRelationType(RelationTypeEnum.MetricSetBindRelation);
        List<Relation> relationList = relationDao.queryJoinList(queryParam);
        List<Integer> projectIdList = relationList.stream().filter(x -> x.getResourceType() == ResourceTypeEnum.Project).map(Relation::getResourceId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(projectIdList)){
            HashMap<String,TreeNode> nodesMap = new HashMap<>();
            List<FlatTreeNode> flatTreeNodes = projectDao.queryNodeList(projectIdList);
            for(FlatTreeNode flatNode:flatTreeNodes){
                String key = RandomID.id(10,keyList);
                TreeNode treeNode = new TreeNode(key,flatNode.getTitle(), flatNode.getId(),flatNode.getType());
                nodesMap.put(flatNode.getType() + "_" + flatNode.getId(),treeNode);
            }
            for (FlatTreeNode flatNode:flatTreeNodes) {
                TreeNode currentNode = nodesMap.get(flatNode.getType() + "_" + flatNode.getId());
                String parentType = null;
                if(flatNode.getType().equals("group")){
                    TreeNode parentNode = nodesMap.get("project" + "_" + flatNode.getPid());
                    parentNode.addChild(currentNode);
                }else if(flatNode.getType().equals("stat")){
                    TreeNode parentNode = nodesMap.get("group" + "_" + flatNode.getPid());
                    parentNode.addChild(currentNode);
                }
            }
            for(TreeNode treeNode : nodesMap.values()){
                if(treeNode.getType().equals("project")){
                    rootNode.addChild(treeNode);
                }
            }
        }
        for(Relation relation : relationList){
            if(relation.getResourceType() == ResourceTypeEnum.Stat){
                String key = RandomID.id(10,keyList);
                TreeNode treeNode = new TreeNode(key,relation.getResourceTitle(),relation.getResourceId(),"stat");
                rootNode.addChild(treeNode);
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

    @Override
    public ResultCode star(MetricSet metricSet) {
        int currentUserId = baseService.getCurrentUserId();
        RelationQueryParam countParam = new RelationQueryParam();
        countParam.setSubjectId(currentUserId);
        countParam.setRelationType(RelationTypeEnum.UserStarMetricSetRelation);
        int count = relationService.count(countParam);
        if(count > SystemConstant.USER_STAR_METRICSET_LIMIT){
            return ResultCode.userStarMetricLimitExceed;
        }
        Relation relation = new Relation();
        relation.setSubjectId(currentUserId);
        relation.setRelationType(RelationTypeEnum.UserStarMetricSetRelation);
        relation.setResourceId(metricSet.getId());
        relation.setResourceType(ResourceTypeEnum.MetricSet);
        return relationService.create(relation);
    }

    @Override
    public ResultCode unStar(MetricSet metricSet) {
        int currentUserId = baseService.getCurrentUserId();
        RelationDeleteParam relationDeleteParam = new RelationDeleteParam();
        relationDeleteParam.setSubjectId(currentUserId);
        relationDeleteParam.setRelationType(RelationTypeEnum.UserStarMetricSetRelation);
        relationDeleteParam.setResourceId(metricSet.getId());
        relationDeleteParam.setResourceType(ResourceTypeEnum.MetricSet);
        relationService.delete(relationDeleteParam);
        return ResultCode.success;
    }

    @Override
    public void updateStructure(MetricUpdateStructureParam updateStructureParam){
        TreeNode treeNode = updateStructureParam.getStructure();
        modifyStructure(List.of(treeNode));
        MetricSet metricSet = new MetricSet();
        metricSet.setId(updateStructureParam.getId());
        metricSet.setStructure(treeNode);
        metricSetDao.update(metricSet);
    }

    private void modifyStructure(List<TreeNode> nodeList){
        for(TreeNode treeNode : nodeList){
            if(treeNode.getType() == null || treeNode.getType().equals("project") || treeNode.getType().equals("group")){
                treeNode.setType("dir");
            }
            if(CollectionUtils.isNotEmpty(treeNode.getChildren())){
                modifyStructure(treeNode.getChildren());
            }
        }
    }

    @Override
    public ListData<Indicator> queryIndicatorList(MetricPendQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<Indicator> pageInfo = null;
        try{
            List<Indicator> ids = metricSetDao.queryIndicatorList(queryParam.getId());
            pageInfo = new PageInfo(ids);
        }finally {
            PageHelper.clearPage();
        }
        return ListData.newInstance(pageInfo.getList(),pageInfo.getTotal(),pageNum,pageSize);
    }

    private static List<Integer> getCurrentStatIds(List<TreeNode> nodes) {
        List<Integer> values = new ArrayList<>();
        for (TreeNode node : nodes) {
            if(node.getType().equals("stat")){
                values.add((Integer) node.getValue());
            }
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                values.addAll(getCurrentStatIds(node.getChildren()));
            }
        }
        return values;
    }

    @Override
    public List<MetricSetVO> queryStarList() {
        int currentUserId = baseService.getCurrentUserId();
        List<Relation> relationList = relationDao.queryList(currentUserId,RelationTypeEnum.UserStarMetricSetRelation);
        List<Integer> ids = relationList.stream().map(z -> z.getResourceId()).collect(Collectors.toList());
        List<MetricSetVO> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ids)){
            MetricSetQueryParam queryParam = new MetricSetQueryParam();
            queryParam.setIds(ids);
            List<MetricSet> metricSetList = metricSetDao.queryList(queryParam);
            for(MetricSet metricSet : metricSetList){
                try{
                    MetricSetVO metricSetVO = translate(metricSet);
                    voList.add(metricSetVO);
                }catch (Exception ex){
                    logger.error("translate item info error,id:{}",metricSet.getId(),ex);
                }
            }
        }
        Collections.sort(voList, Comparator.comparingInt(e -> ids.indexOf(e.getId())));
        return voList;
    }
}
