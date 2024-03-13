package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.entity.ServiceResult;
import com.dtstep.lighthouse.common.entity.group.GroupExtEntity;
import com.dtstep.lighthouse.common.entity.stat.TemplateEntity;
import com.dtstep.lighthouse.common.enums.GroupStateEnum;
import com.dtstep.lighthouse.common.random.RandomID;
import com.dtstep.lighthouse.common.modal.Column;
import com.dtstep.lighthouse.common.modal.Stat;
import com.dtstep.lighthouse.common.util.StringUtil;
import com.dtstep.lighthouse.core.builtin.BuiltinLoader;
import com.dtstep.lighthouse.core.formula.FormulaTranslate;
import com.dtstep.lighthouse.core.template.TemplateContext;
import com.dtstep.lighthouse.core.template.TemplateParser;
import com.dtstep.lighthouse.core.wrapper.GroupDBWrapper;
import com.dtstep.lighthouse.core.wrapper.StatDBWrapper;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.common.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.common.modal.Group;
import com.dtstep.lighthouse.common.modal.ResourceDto;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.ResourceService;
import com.dtstep.lighthouse.insights.vo.GroupVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ResourceService resourceService;

    @Transactional
    @Override
    public int create(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setSecretKey(RandomID.id(40));
        group.setCreateTime(localDateTime);
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        group.setRandomId(RandomID.id(32));
        group.setState(GroupStateEnum.RUNNING);
        group.setDebugMode(0);
        groupDao.insert(group);
        resourceService.addResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return group.getId();
    }

    @Transactional
    @Override
    public int update(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        int result = groupDao.update(group);
        resourceService.updateResourcePidCallback(ResourceDto.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return result;
    }

    @Transactional
    @Override
    public int delete(Group group) {
        Validate.notNull(group);
        Integer id = group.getId();
        resourceService.deleteResourceCallback(ResourceDto.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return groupDao.deleteById(id);
    }

    @Override
    public GroupVO queryById(Integer id) throws Exception {
        GroupExtEntity groupExtEntity;
        if(BuiltinLoader.isBuiltinGroup(id)){
            groupExtEntity = BuiltinLoader.getBuiltinGroup(id);
        }else{
            Group group = groupDao.queryById(id);
            if(group == null){
                return null;
            }
            groupExtEntity = GroupDBWrapper.combineExtInfo(group);
        }
        GroupVO groupVO = new GroupVO(groupExtEntity);
        Set<String> relatedColumns = getRelatedColumns(groupExtEntity);
        groupVO.setRelatedColumns(relatedColumns);
        return groupVO;
    }

    public Set<String> getRelatedColumns(Group group) throws Exception {
        Set<String> relatedColumnSet = new HashSet<>();
        List<Column> columnList = group.getColumns();
        List<Stat> statList = StatDBWrapper.queryStatByGroupIDFromDB(group.getId());
        if (CollectionUtils.isNotEmpty(statList)) {
            for (Stat stat : statList) {
                String template = stat.getTemplate();
                ServiceResult<TemplateEntity> serviceResult = TemplateParser.parseConfig(new TemplateContext(stat.getId(),template,stat.getTimeparam(),columnList));
                List<Column> statRelatedColumns = FormulaTranslate.queryRelatedColumns(columnList, template);
                if (CollectionUtils.isNotEmpty(statRelatedColumns)) {
                    for (Column column : statRelatedColumns) {
                        relatedColumnSet.add(column.getName());
                    }
                }
                String dimens = serviceResult.getData().getDimens();
                if(!StringUtil.isEmpty(dimens)){
                    List<Column> dimensRelatedColumns = FormulaTranslate.queryRelatedColumns(columnList,dimens);
                    if (CollectionUtils.isNotEmpty(dimensRelatedColumns)) {
                        for (Column column : dimensRelatedColumns) {
                            relatedColumnSet.add(column.getName());
                        }
                    }
                }
            }
        }
        return relatedColumnSet;
    }

    @Override
    @Cacheable(value = "LongPeriod",key = "#targetClass + '_' + 'queryById' + '_' + #id",cacheManager = "caffeineCacheManager",unless = "#result == null")
    public Group cacheQueryById(Integer id) {
        return groupDao.queryById(id);
    }

    @Override
    public List<Group> queryByProjectId(Integer projectId) {
        return groupDao.queryByProjectId(projectId);
    }

    @Override
    public int count(GroupQueryParam queryParam) {
        return groupDao.count(queryParam);
    }

    @Override
    public String getSecretKey(Integer id) {
        return groupDao.getSecretKey(id);
    }
}
