package com.dtstep.lighthouse.insights.service;

import com.dtstep.lighthouse.insights.dto.GroupQueryParam;
import com.dtstep.lighthouse.insights.modal.Group;

import java.util.List;

public interface GroupService {

    int create(Group group);

    int update(Group group);

    int delete(Group group);

    Group queryById(Integer id);

    int count(GroupQueryParam queryParam);

    String getSecretKey(Integer id);

    List<Group> queryByProjectId(Integer projectId);
}
