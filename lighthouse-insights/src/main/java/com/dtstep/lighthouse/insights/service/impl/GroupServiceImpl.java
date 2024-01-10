package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.GroupDto;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.insights.dto.RolePair;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Project;
import com.dtstep.lighthouse.insights.modal.Resource;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.ResourceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ResourceService resourceService;

    @Override
    public int create(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setCreateTime(localDateTime);
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        groupDao.insert(group);
        resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Group,group.getId(),group.getProjectId()));
        return group.getId();
    }

    @Override
    public int update(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        int result = groupDao.update(group);
        resourceService.updateResourcePidCallback(Resource.newResource(ResourceTypeEnum.Group,group.getId(),group.getProjectId()));
        return result;
    }

    @Override
    public int delete(Group group) {
        Validate.notNull(group);
        Integer id = group.getId();
        return groupDao.deleteById(id);
    }

    @Override
    public Group queryById(Integer id) {
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
