package com.dtstep.lighthouse.insights.service.impl;

import com.dtstep.lighthouse.insights.dao.GroupDao;
import com.dtstep.lighthouse.insights.dto.GroupDto;
import com.dtstep.lighthouse.insights.modal.Group;
import com.dtstep.lighthouse.insights.service.GroupService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Override
    public int create(Group group) {
        LocalDateTime localDateTime = LocalDateTime.now();
        group.setCreateTime(localDateTime);
        group.setUpdateTime(localDateTime);
        group.setRefreshTime(localDateTime);
        return groupDao.insert(group);
    }

    @Override
    public Group queryById(Integer id) {
        return groupDao.queryById(id);
    }

    @Override
    public List<Group> queryByProjectId(Integer projectId) {
        return groupDao.queryByProjectId(projectId);
    }
}
