package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.core.formula.TemplateUtil;
import com.dtstep.lighthouse.insights.controller.annotation.RecordAnnotation;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto_bak.PermissionEnum;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.dto_bak.TreeNode;
import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;
import com.dtstep.lighthouse.insights.enums.RecordTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.template.TemplateContext;
import com.dtstep.lighthouse.insights.template.TemplateParser;
import com.dtstep.lighthouse.insights.util.TreeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ComponentService componentService;

    @Transactional
    @Override
    public int create(Stat stat) {
        int groupId = stat.getGroupId();
        Group group = groupDao.queryById(groupId);
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        try{
            TemplateEntity templateEntity = TemplateParser.parse(new TemplateContext(template,timeParam, group.getColumns()));
            stat.setTitle(templateEntity.getTitle());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setUpdateTime(localDateTime);
        stat.setCreateTime(localDateTime);
        stat.setState(StatStateEnum.RUNNING);
        stat.setRandomId(UUID.randomUUID().toString());
        statDao.insert(stat);
        int id = stat.getId();
        resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Stat,id, ResourceTypeEnum.Group,stat.getGroupId()));
        return id;
    }

    @Transactional
    @Override
    @RecordAnnotation(recordType = RecordTypeEnum.UPDATE_STAT)
    public int update(Stat stat) {
        int groupId = stat.getGroupId();
        Group group = groupDao.queryById(groupId);
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        try{
            TemplateEntity templateEntity = TemplateParser.parse(new TemplateContext(template,timeParam, group.getColumns()));
            stat.setTitle(templateEntity.getTitle());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setUpdateTime(localDateTime);
        int result = statDao.update(stat);
        resourceService.updateResourcePidCallback(Resource.newResource(ResourceTypeEnum.Stat,stat.getId(),ResourceTypeEnum.Group,stat.getGroupId()));
        return result;
    }


    @Override
    public int delete(Stat stat) {
        Integer id = stat.getId();
        return statDao.deleteById(id);
    }

    private StatVO translate(Stat stat){
        int userId = baseService.getCurrentUserId();
        StatVO statVO = new StatVO(stat);
        Group group = groupDao.queryById(stat.getGroupId());
        Project project = projectDao.queryById(stat.getProjectId());
        Validate.notNull(project);
        Role manageRole = roleService.queryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,stat.getId());
        Role accessRole = roleService.queryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,stat.getId());
        if(permissionService.checkUserPermission(userId, manageRole.getId())){
            statVO.addPermission(PermissionEnum.ManageAble);
        }else if(permissionService.checkUserPermission(userId,accessRole.getId())){
            statVO.addPermission(PermissionEnum.AccessAble);
        }
        List<User> admins = projectService.cacheQueryAdmins(project.getId());
        statVO.setAdmins(admins);
        statVO.setGroup(group);
        statVO.setProject(project);
        return statVO;
    }

    @Override
    public ListData<StatVO> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize) {
        List<Stat> list = statDao.queryList(queryParam,pageNum,pageSize);
        List<StatVO> dtoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(Stat stat : list){
                StatVO statVO = translate(stat);
                dtoList.add(statVO);
            }
        }
        ListData<StatVO> listData = new ListData<>();
        listData.setList(dtoList);
        return listData;
    }


    @Override
    public StatVO queryById(Integer id) {
        Stat stat = statDao.queryById(id);
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        Group group = groupDao.queryById(stat.getGroupId());
        List<Column> columnList = group.getColumns();
        try{
            TemplateEntity templateEntity = TemplateParser.parse(new TemplateContext(template,timeParam, columnList));
            stat.setTemplateEntity(templateEntity);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return translate(stat);
    }

    @Override
    public List<Stat> queryByProjectId(Integer projectId) {
        return statDao.queryByProjectId(projectId);
    }

    @Override
    public RenderConfig getStatRenderConfig(Stat stat) {
        RenderConfig renderConfig = stat.getRenderConfig();
        List<String> defaultDimensList = new ArrayList<>();
        HashMap<String,RenderFilterConfig> filtersConfigMap = new HashMap<>();
        if(renderConfig == null || CollectionUtils.isEmpty(renderConfig.getFilters())){
            TemplateEntity templateEntity = stat.getTemplateEntity();
            String[] dimensArray = templateEntity.getDimensArray();
            if(dimensArray != null){
                defaultDimensList = Arrays.asList(dimensArray);
            }
        }else{
            List<RenderFilterConfig> filterConfigs = renderConfig.getFilters();
            for(RenderFilterConfig filterConfig : filterConfigs){
                Integer componentId = filterConfig.getComponentId();
                ComponentTypeEnum componentTypeEnum = filterConfig.getComponentType();
                if(componentId != 0){
                    Component component = componentService.queryById(componentId);
                    RenderFilterConfig renderFilterConfig = new RenderFilterConfig();
                    renderFilterConfig.setComponentType(ComponentTypeEnum.FILTER_SELECT);
                    renderFilterConfig.setComponentId(componentId);
                    renderFilterConfig.setDimens(filterConfig.getDimens());
                    renderFilterConfig.setTitle(component.getTitle());
                    renderFilterConfig.setLabel(filterConfig.getLabel());
                    renderFilterConfig.setConfigData(component.getConfiguration());
                    filtersConfigMap.put(filterConfig.getDimens(),renderFilterConfig);
                }else{
                    defaultDimensList.add(filterConfig.getDimens());
                }
            }
        }
        for(String dimens : defaultDimensList){
            List<TreeNode> dimensValueList = new ArrayList<>();
            for(int i=0;i<3;i++){
//                dimensValueList.add(new TreeNode("dimens_" + i,"dimens_" + i));
            }
            RenderFilterConfig renderFilterConfig = new RenderFilterConfig();
            renderFilterConfig.setComponentType(ComponentTypeEnum.FILTER_SELECT);
            renderFilterConfig.setComponentId(0);
            renderFilterConfig.setLabel(dimens);
            renderFilterConfig.setDimens(dimens);
            renderFilterConfig.setConfigData(dimensValueList);
            filtersConfigMap.put(dimens,renderFilterConfig);
        }
        List<RenderFilterConfig> configList = new ArrayList<>();
        for(String key : filtersConfigMap.keySet()){
            configList.add(filtersConfigMap.get(key));
        }
        if(renderConfig == null){
            renderConfig = new RenderConfig();
        }
        renderConfig.setFilters(configList);
        System.out.println("renderConfig is:" + JsonUtil.toJSONString(renderConfig));
        return renderConfig;
    }

    @Override
    public ResultCode filterConfig(Stat stat, List<RenderFilterConfig> filterConfigs) {
        String[] dimensArray = stat.getTemplateEntity().getDimensArray();
        Validate.notNull(dimensArray);
        Validate.isTrue(CollectionUtils.isNotEmpty(filterConfigs));
        List<String> list = Arrays.asList(dimensArray);
        List<String> configList = new ArrayList<>();
        for(RenderFilterConfig filterConfig : filterConfigs){
            String dimens = filterConfig.getDimens();
            String label = filterConfig.getLabel();
            if(StringUtil.isEmpty(dimens)){
                return ResultCode.getExtendResultCode(ResultCode.filterConfigDimensCannotBeEmpty,"dimens");
            }
            if(StringUtil.isEmpty(label)){
                return ResultCode.getExtendResultCode(ResultCode.filterConfigLabelCannotBeEmpty,"label");
            }
            String [] dimensArrayUnit = TemplateUtil.split(dimens);
            for(String temp:dimensArrayUnit){
                if(!list.contains(temp)){
                    return ResultCode.getExtendResultCode(ResultCode.filterConfigDimensNotExist,temp);
                }else if(configList.contains(temp)){
                    return ResultCode.getExtendResultCode(ResultCode.filterConfigDimensDuplicate,temp);
                }else{
                    configList.add(temp);
                }
            }
            int componentId = filterConfig.getComponentId();
            if(componentId != 0 && filterConfig.getComponentType() == ComponentTypeEnum.FILTER_SELECT){
                Component component = componentService.queryById(componentId);
                int level = TreeUtil.getMaxLevel(component.getConfiguration());
                if(dimensArrayUnit.length != level){
                    return ResultCode.getExtendResultCode(ResultCode.filterConfigLevelNotMatch,dimens);
                }
            }else if(componentId == 0 && dimensArrayUnit.length != 1){
                return ResultCode.getExtendResultCode(ResultCode.filterConfigLevelNotMatch,dimens);
            }
        }
        List<String> missList = list.stream().filter(item -> !configList.contains(item)).collect(toList());
        if(CollectionUtils.isNotEmpty(missList)){
            return ResultCode.getExtendResultCode(ResultCode.filterConfigDimensMissing,JsonUtil.toJSONString(missList));
        }
        return ResultCode.success;
    }
}
