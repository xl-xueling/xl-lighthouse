package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.common.key.RandomID;
import com.dtstep.lighthouse.commonv2.insights.ResultCode;
import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dao.ProjectDao;
import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.insights.enums.ResourceTypeEnum;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.modal.Resource;
import com.dtstep.lighthouse.insights.service.GroupService;
import com.dtstep.lighthouse.insights.service.ResourceService;
import com.dtstep.lighthouse.insights.vo.ResultWrapper;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        group.setSecretKey(RandomID.id(40));
        group.setCreateTime(localDateTime);
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        groupDao.insert(group);
        resourceService.addResourceCallback(Resource.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return group.getId();
    }

    @Override
    public int update(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        int result = groupDao.update(group);
        resourceService.updateResourcePidCallback(Resource.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        return result;
    }

    @Override
    public int delete(Group group) {
        Validate.notNull(group);
        Integer id = group.getId();
        resourceService.deleteResourceCallback(Resource.newResource(ResourceTypeEnum.Group,group.getId(),ResourceTypeEnum.Project,group.getProjectId()));
        int result = groupDao.deleteById(id);
        return result;
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
