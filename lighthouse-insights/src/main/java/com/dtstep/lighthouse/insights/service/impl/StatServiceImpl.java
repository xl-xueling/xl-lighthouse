package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.constant.StatConst;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.StatStateEnum;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.modal.*;
import com.dtstep.lighthouse.common.util.JsonUtil;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.common.entity.ListData;
import com.dtstep.lighthouse.common.entity.ResultCode;
import com.dtstep.lighthouse.core.formula.TemplateUtil;
import com.dtstep.lighthouse.core.storage.dimens.DimensStorageSelector;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.StatDao;
import com.dtstep.lighthouse.insights.dto.StatQueryParam;
import com.dtstep.lighthouse.common.modal.PermissionEnum;
import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.insights.vo.StatVO;
import com.dtstep.lighthouse.common.modal.TreeNode;
import com.dtstep.lighthouse.common.enums.ComponentTypeEnum;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.enums.RoleTypeEnum;
import com.dtstep.lighthouse.insights.service.*;
import com.dtstep.lighthouse.core.template2.TemplateContext;
import com.dtstep.lighthouse.core.template2.TemplateParser;
import com.dtstep.lighthouse.insights.util.TreeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class StatServiceImpl implements StatService {

    private static final Logger logger = LoggerFactory.getLogger(StatServiceImpl.class);

    @Autowired
    private StatDao statDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private UserService userService;

    @Autowired
    private MetaTableService metaTableService;

    @Transactional
    @Override
    public ResultCode create(Stat stat) throws Exception{
        int groupId = stat.getGroupId();
        Group group = groupDao.queryById(groupId);
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(template,timeParam, group.getColumns()));
        ResultCode resultCode = serviceResult.getResultCode();
        if(resultCode != ResultCode.success){
            return resultCode;
        }
        TemplateEntity templateEntity = serviceResult.getData();
        stat.setTitle(templateEntity.getTitle());
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setUpdateTime(localDateTime);
        stat.setCreateTime(localDateTime);
        stat.setState(StatStateEnum.RUNNING);
        stat.setRandomId(RandomID.id(32));
        int metaId = metaTableService.getCurrentStatResultTable();
        stat.setMetaId(metaId);
        statDao.insert(stat);
        int id = stat.getId();
        resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Stat,id, ResourceTypeEnum.Group,stat.getGroupId()));
        return ResultCode.success;
    }

    @Transactional
    @Override
    public ResultCode update(Stat stat) {
        String template = stat.getTemplate();
        Document document = Jsoup.parse(template, "", Parser.xmlParser());
        Element node = document.select("stat-item").first();
        node.attr("title", stat.getTitle());
        String newTemplate = document.toString();
        stat.setTemplate(newTemplate);
        LocalDateTime localDateTime = LocalDateTime.now();
        stat.setUpdateTime(localDateTime);
        statDao.update(stat);
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
        Role projectManageRole = roleService.cacheQueryRole(RoleTypeEnum.PROJECT_MANAGE_PERMISSION,statVO.getProjectId());
        Validate.notNull(manageRole);
        List<Integer> adminIds = permissionService.queryUserPermissionsByRoleId(projectManageRole.getId(),3);
        if(CollectionUtils.isNotEmpty(adminIds)){
            List<User> admins = adminIds.stream().map(z -> userService.cacheQueryById(z)).collect(Collectors.toList());
            statVO.setAdmins(admins);
        }
        String template = stat.getTemplate();
        String timeParam = stat.getTimeparam();
        Group group = groupDao.queryById(stat.getGroupId());
        ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(template,timeParam, group.getColumns()));
        TemplateEntity templateEntity = serviceResult.getData();
        statVO.setTemplateEntity(templateEntity);
        statVO.setTitle(templateEntity.getTitle());
        return statVO;
    }

    @Override
    public List<StatVO> queryByIds(List<Integer> ids) {
        StatQueryParam queryParam = new StatQueryParam();
        queryParam.setIds(ids);
        List<Stat> statList = statDao.queryList(queryParam);
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
        return translate(stat);
    }

    @Override
    public List<Stat> queryByProjectId(Integer projectId) {
        return statDao.queryByProjectId(projectId);
    }

    @Override
    public RenderConfig getStatRenderConfig(StatVO stat) throws Exception{
        RenderConfig renderConfig = stat.getRenderConfig();
        Group group = groupDao.queryById(stat.getGroupId());
        HashMap<String, RenderFilterConfig> filtersConfigMap = new HashMap<>();
        if(CollectionUtils.isEmpty(renderConfig.getFilters())){
            TemplateEntity templateEntity = stat.getTemplateEntity();
            String[] dimensArray = templateEntity.getDimensArray();
            for(String dimens:dimensArray){
                List<String> dimensValueList = DimensStorageSelector.query(group,dimens, null,StatConst.DIMENS_THRESHOLD_LIMIT_COUNT);
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
                    List<String> dimensValueList = DimensStorageSelector.query(group,filterConfig.getDimens(), null,StatConst.DIMENS_THRESHOLD_LIMIT_COUNT);
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
    public ResultCode filterConfig(StatVO stat, List<RenderFilterConfig> filterConfigs) {
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
            String commaSeparatedString = String.join(",", missList);
            return ResultCode.getExtendResultCode(ResultCode.filterConfigDimensMissing,commaSeparatedString);
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
