package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.stat.StatStateEnum;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.commonv2.insights.ListData;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.core.formula.TemplateUtil;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto.StatQueryParamExtend;
import com.dtstep.lighthouse.insights.dto_bak.PermissionEnum;
import com.dtstep.lighthouse.insights.vo.ResultWrapper;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.insights.dto_bak.TreeNode;
import com.dtstep.lighthouse.insights.enums.ComponentTypeEnum;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.modal.*;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.insights.template.TemplateContext;
import com.dtstep.lighthouse.insights.template.TemplateParser;
import com.dtstep.lighthouse.insights.util.TreeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class StatServiceImpl implements StatService {

    private static final Logger logger = LoggerFactory.getLogger(StatServiceImpl.class);

    @Autowired
    private StatDao statDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupService groupService;

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
    public ResultCode create(Stat stat) {
        int groupId = stat.getGroupId();
        Group group = groupDao.queryById(groupId);
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        ResultWrapper<TemplateEntity> resultWrapper = TemplateParser.parseConfig(new TemplateContext(template,timeParam, group.getColumns()));
        ResultCode resultCode = resultWrapper.getResultCode();
        if(resultCode != ResultCode.success){
            return resultCode;
        }
        TemplateEntity templateEntity = resultWrapper.getData();
        stat.setTitle(templateEntity.getTitle());
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setUpdateTime(localDateTime);
        stat.setCreateTime(localDateTime);
        stat.setState(StatStateEnum.RUNNING);
        stat.setRandomId(UUID.randomUUID().toString());
        statDao.insert(stat);
        int id = stat.getId();
        resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Stat,id, ResourceTypeEnum.Group,stat.getGroupId()));
        return ResultCode.success;
    }

    @Transactional
    @Override
//    @RecordAnnotation(recordType = RecordTypeEnum.UPDATE_STAT)
    public ResultCode update(Stat stat) {
        int groupId = stat.getGroupId();
        Group group = groupDao.queryById(groupId);
        String template = stat.getTemplate();
        Document document = Jsoup.parse(template, "", Parser.xmlParser());
        Element node = document.select("stat-item").first();
        node.attr("title", stat.getTitle());
        String newTemplate = document.toString();
        stat.setTemplate(newTemplate);
        String timeParam = stat.getTimeparam();
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setUpdateTime(localDateTime);
        int result = statDao.update(stat);
        resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Stat,stat.getId(),ResourceTypeEnum.Group,stat.getGroupId()));
        return ResultCode.success;
    }

    @Transactional
    @Override
    public int delete(Stat stat) {
        Integer id = stat.getId();
        resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Stat,id,ResourceTypeEnum.Group,stat.getGroupId()));
        return statDao.deleteById(id);
    }

    private StatVO translate(Stat stat){
        int userId = baseService.getCurrentUserId();
        StatVO statVO = new StatVO(stat);
        Role manageRole = roleService.cacheQueryRole(RoleTypeEnum.STAT_MANAGE_PERMISSION,stat.getId());
        Role accessRole = roleService.cacheQueryRole(RoleTypeEnum.STAT_ACCESS_PERMISSION,stat.getId());
        if(permissionService.checkUserPermission(userId, manageRole.getId())){
            statVO.addPermission(PermissionEnum.ManageAble);
            statVO.addPermission(PermissionEnum.AccessAble);
        }else if(permissionService.checkUserPermission(userId,accessRole.getId())){
            statVO.addPermission(PermissionEnum.AccessAble);
        }
//        List<User> admins = projectService.cacheQueryAdmins(project.getId());
//        statVO.setAdmins(admins);
        return statVO;
    }

    @Override
    public List<StatVO> queryByIds(List<Integer> ids) {
        StatQueryParamExtend queryParam = new StatQueryParamExtend();
        queryParam.setIds(ids);
        List<Stat> statList = statDao.queryJoinList(queryParam);
        List<StatVO> voList = new ArrayList<>();
        for(Stat stat : statList){
            StatVO statVO = translate(stat);
            voList.add(statVO);
        }
        return voList;
    }

    @Override
    public ListData<StatVO> queryList(StatQueryParam queryParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<StatVO> dtoList = new ArrayList<>();
        PageInfo<Stat> pageInfo = null;
        try{
            List<Stat> list = statDao.queryList(queryParam);
            pageInfo = new PageInfo<>(list);
        }finally {
            PageHelper.clearPage();
        }
        for(Stat stat : pageInfo.getList()){
            try{
                StatVO statVo = translate(stat);
                dtoList.add(statVo);
            }catch (Exception ex){
                logger.error("translate item info error,itemId:{}!",stat.getId(),ex);
            }
        }
        return ListData.newInstance(dtoList,pageInfo.getTotal(),pageNum,pageSize);
    }


    @Override
    public StatVO queryById(Integer id) {
        Stat stat = statDao.queryById(id);
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        Group group = groupDao.queryById(stat.getGroupId());
        List<Column> columnList = group.getColumns();
        ResultWrapper<TemplateEntity> resultWrapper = TemplateParser.parseConfig(new TemplateContext(template,timeParam, group.getColumns()));
        TemplateEntity templateEntity = resultWrapper.getData();
        stat.setTemplateEntity(templateEntity);
        stat.setTitle(templateEntity.getTitle());
        return translate(stat);
    }

    @Override
    public List<Stat> queryByProjectId(Integer projectId) {
        return statDao.queryByProjectId(projectId);
    }

    private List<String> queryDimensValueList(String dimens) {
        List<String> dimensValueList = new ArrayList<>();
        for(int i=0;i<3;i++){
            dimensValueList.add("dimens_" + i);
        }
        return dimensValueList;
    }

    @Override
    public RenderConfig getStatRenderConfig(Stat stat) {
        RenderConfig renderConfig = stat.getRenderConfig();
        List<String> defaultDimensList = new ArrayList<>();
        HashMap<String,RenderFilterConfig> filtersConfigMap = new HashMap<>();
        if(CollectionUtils.isEmpty(renderConfig.getFilters())){
            TemplateEntity templateEntity = stat.getTemplateEntity();
            String[] dimensArray = templateEntity.getDimensArray();
            for(String dimens:dimensArray){
                List<String> dimensValueList = queryDimensValueList(dimens);
                List<TreeNode> treeNodes = dimensValueList.stream().map(z -> new TreeNode(z,z)).collect(toList());
                RenderFilterConfig renderFilterConfig = new RenderFilterConfig();
                renderFilterConfig.setComponentType(ComponentTypeEnum.FILTER_SELECT);
                renderFilterConfig.setComponentId(0);
                renderFilterConfig.setLabel(dimens);
                renderFilterConfig.setDimens(dimens);
                renderFilterConfig.setConfigData(treeNodes);
                filtersConfigMap.put(dimens,renderFilterConfig);
            }
        }else{
            List<RenderFilterConfig> filterConfigs = renderConfig.getFilters();
            for(RenderFilterConfig filterConfig : filterConfigs){
                Integer componentId = filterConfig.getComponentId();
                ComponentTypeEnum componentTypeEnum = filterConfig.getComponentType();
                filterConfig.setComponentId(componentId);
                filterConfig.setComponentType(componentTypeEnum);
                filterConfig.setDimens(filterConfig.getDimens());
                filterConfig.setLabel(filterConfig.getLabel());
                if(componentId != 0){
                    Component component = componentService.queryById(componentId);
                    filterConfig.setTitle(component.getTitle());
                    filterConfig.setConfigData(component.getConfiguration());
                    filtersConfigMap.put(filterConfig.getDimens(),filterConfig);
                }else{
                    List<String> dimensValueList = queryDimensValueList(filterConfig.getDimens());
                    List<TreeNode> treeNodes = dimensValueList.stream().map(z -> new TreeNode(z,z)).collect(toList());
                    filterConfig.setConfigData(treeNodes);
                    filtersConfigMap.put(filterConfig.getDimens(),filterConfig);
                }
            }
        }
        List<RenderFilterConfig> configList = new ArrayList<>();
        for(String key : filtersConfigMap.keySet()){
            configList.add(filtersConfigMap.get(key));
        }
        renderConfig.setFilters(configList);
        return renderConfig;
    }

    @Override
    public ResultCode filterConfig(Stat stat, List<RenderFilterConfig> filterConfigs) {
        String[] dimensArray = stat.getTemplateEntity().getDimensArray();
        Validate.notNull(dimensArray);
        if(CollectionUtils.isEmpty(filterConfigs)){
            return ResultCode.filterConfigConfigCannotBeEmpty;
        }
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
        RenderConfig renderConfig = stat.getRenderConfig();
        renderConfig.setFilters(filterConfigs);
        statDao.update(stat);
        return ResultCode.success;
    }

    @Override
    public int count(StatQueryParam queryParam) {
        return statDao.count(queryParam);
    }
}
